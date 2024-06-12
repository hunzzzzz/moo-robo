package hunzz.study.moorobo.domain.question.controller

import com.fasterxml.jackson.databind.ObjectMapper
import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.model.Question
import hunzz.study.moorobo.domain.question.model.QuestionStatus
import hunzz.study.moorobo.domain.question.repository.QuestionRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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
    @DisplayName("질문의 내용을 미입력한 경우")
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
    @DisplayName("128자를 초과하는 질문 제목이 등록된 경우")
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
    }
}