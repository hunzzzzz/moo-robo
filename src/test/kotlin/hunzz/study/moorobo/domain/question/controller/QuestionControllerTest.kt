package hunzz.study.moorobo.domain.question.controller

import com.fasterxml.jackson.databind.ObjectMapper
import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.dto.UpdateQuestionRequest
import hunzz.study.moorobo.domain.question.model.Question
import hunzz.study.moorobo.domain.question.model.QuestionStatus
import hunzz.study.moorobo.domain.question.repository.QuestionRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var questionRepository: QuestionRepository

    @BeforeEach
    fun clean() {
        questionRepository.deleteAll()
    }

    @Test
    @DisplayName("정상적으로 질문이 등록되는 경우")
    fun addQuestion() {
        // given
        val json = objectMapper.writeValueAsString(
            AddQuestionRequest(title = "테스트 제목", content = "테스트 내용")
        )

        // expected
        mockMvc.perform(
            post("/questions")
                .contentType(APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.title").value("테스트 제목"))
            .andExpect(jsonPath("$.content").value("테스트 내용"))
            .andDo(print())
    }

    @Test
    @DisplayName("질문 등록 시, 질문의 내용을 미입력한 경우")
    fun addQuestionException1() {
        // given
        val json = objectMapper.writeValueAsString(
            AddQuestionRequest(title = "테스트 제목", content = "")
        )

        // expected
        mockMvc.perform(
            post("/questions")
                .contentType(APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors").isArray)
            .andExpect(jsonPath("$.errors.size()").value(1))
            .andExpect(jsonPath("$.errors[0].field").value("content"))
            .andExpect(jsonPath("$.errors[0].message").value("질문의 내용은 필수 입력 항목입니다."))
    }

    @Test
    @DisplayName("질문 등록 시, 128자를 초과하는 질문 제목이 등록된 경우")
    fun addQuestionException2() {
        // given
        val json = objectMapper.writeValueAsString(
            AddQuestionRequest(title = "테스트".repeat(43), content = "테스트 내용")
        )

        // expected
        mockMvc.perform(
            post("/questions")
                .contentType(APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors").isArray)
            .andExpect(jsonPath("$.errors.size()").value(1))
            .andExpect(jsonPath("$.errors[0].field").value("title"))
            .andExpect(jsonPath("$.errors[0].message").value("질문의 제목은 128자 이하여야 합니다."))
            .andDo(print())
    }

    @Test
    @DisplayName("질문 등록 시, 질문 내용에 비속어가 포함된 경우")
    fun addQuestionException3() {
        // given
        val json = objectMapper.writeValueAsString(
            AddQuestionRequest(title = "테스트 제목", content = "욕설이 포함된 테스트 내용")
        )

        // expected
        mockMvc.perform(
            post("/questions")
                .contentType(APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors").isArray)
            .andExpect(jsonPath("$.errors.size()").value(1))
            .andExpect(jsonPath("$.errors[0].field").value("content"))
            .andExpect(jsonPath("$.errors[0].message").value("질문 내용에 비속어가 포함되어 있습니다."))
            .andDo(print())
    }

    @Test
    @DisplayName("질문 등록 시, 질문 내용에 구분자를 활용한 비속어가 포함된 경우")
    fun addQuestionException4() {
        // given
        val json = objectMapper.writeValueAsString(
            AddQuestionRequest(title = "테스트 제목", content = "욕1설이 포함된 테스트 내용")
        )

        // expected
        mockMvc.perform(
            post("/questions")
                .contentType(APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors").isArray)
            .andExpect(jsonPath("$.errors.size()").value(1))
            .andExpect(jsonPath("$.errors[0].field").value("content"))
            .andExpect(jsonPath("$.errors[0].message").value("질문 내용에 비속어가 포함되어 있습니다."))
            .andDo(print())
    }

    @Test
    @DisplayName("정상적으로 질문이 조회된 경우")
    fun findQuestion() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }

        // expected
        mockMvc.perform(
            get("/questions/{questionId}", existingQuestion.id)
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(existingQuestion.id))
            .andExpect(jsonPath("$.title").value("테스트 제목"))
            .andExpect(jsonPath("$.content").value("테스트 내용"))
            .andDo(print())
    }

    @Test
    @DisplayName("질문 조회 시, id에 해당하는 질문이 존재하지 않는 경우")
    fun findQuestionException() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }

        // expected
        mockMvc.perform(
            get("/questions/{questionId}", existingQuestion.id!! + 1)
        ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.errors").isArray)
            .andExpect(jsonPath("$.errors.size()").value(1))
            .andExpect(jsonPath("$.errors[0].field").value(null))
            .andExpect(jsonPath("$.errors[0].message").value("존재하지 않는 질문입니다."))
            .andDo(print())
    }

    @Test
    @DisplayName("정상적으로 질문 목록이 페이징이 적용된 채 조회된 경우")
    fun findQuestions() {
        // given
        (1..AMOUNT_OF_QUESTIONS).forEach {
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목${it}", content = "테스트 내용${it}")
                .let { q -> questionRepository.save(q) }
        }

        // expected
        mockMvc.perform(
            get("/questions?page=1")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.content.size()").value(QUESTION_PAGE_SIZE))
            .andExpect(jsonPath("$.content[0].title").value("테스트 제목${AMOUNT_OF_QUESTIONS}"))
            .andExpect(jsonPath("$.content[0].content").value("테스트 내용${AMOUNT_OF_QUESTIONS}"))
            .andDo(print())
    }

    @Test
    @DisplayName("정상적으로 질문 목록이 페이징이 적용된 채 조회된 경우 (page에 값을 대입 X)")
    fun findQuestions2() {
        // given
        (1..AMOUNT_OF_QUESTIONS).forEach {
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목${it}", content = "테스트 내용${it}")
                .let { q -> questionRepository.save(q) }
        }

        // expected
        mockMvc.perform(
            get("/questions")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.content.size()").value(QUESTION_PAGE_SIZE))
            .andExpect(jsonPath("$.content[0].title").value("테스트 제목${AMOUNT_OF_QUESTIONS}"))
            .andExpect(jsonPath("$.content[0].content").value("테스트 내용${AMOUNT_OF_QUESTIONS}"))
            .andDo(print())
    }

    @Test
    @DisplayName("정상적으로 질문이 수정되는 경우")
    fun updateQuestion() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }
        val json = objectMapper.writeValueAsString(
            UpdateQuestionRequest(title = "수정된 테스트 제목", content = "수정된 테스트 내용")
        )

        // expected
        mockMvc.perform(
            put("/questions/{questionId}", existingQuestion.id)
                .contentType(APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(existingQuestion.id))
            .andExpect(jsonPath("$.title").value("수정된 테스트 제목"))
            .andExpect(jsonPath("$.content").value("수정된 테스트 내용"))
            .andDo(print())
    }

    @Test
    @DisplayName("정상적으로 질문이 삭제되는 경우")
    fun deleteQuestion() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }

        // expected
        mockMvc.perform(
            delete("/questions/{questionId}", existingQuestion.id)
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andDo(print())
    }

    companion object {
        const val QUESTION_PAGE_SIZE = 10 // 한 페이지의 크기
        const val AMOUNT_OF_QUESTIONS = 50 // 페이징 테스트를 위해 만든 더미 질문의 개수
    }
}