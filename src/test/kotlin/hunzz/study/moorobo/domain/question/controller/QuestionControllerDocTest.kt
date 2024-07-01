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
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.JsonFieldType.NUMBER
import org.springframework.restdocs.payload.JsonFieldType.STRING
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "moo-robo.api", uriPort = 443)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension::class)
class QuestionControllerDocTest {
    @Autowired
    lateinit var questionRepository: QuestionRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun clean() {
        questionRepository.deleteAll()
    }

    @Test
    @DisplayName("질문 등록")
    fun addQuestion() {
        // given
        val json = objectMapper.writeValueAsString(
            AddQuestionRequest(title = "테스트 제목", content = "테스트 내용")
        )

        // expected
        mockMvc.perform(
            post("/questions")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isCreated)
            .andDo(print())
            .andDo(
                document(
                    "question-create",
                    requestFields(
                        fieldWithPath("title").type(STRING).description("질문 제목"),
                        fieldWithPath("content").type(STRING).description("질문 내용")
                    ),
                    responseFields(
                        fieldWithPath("id").type(NUMBER).description("질문 ID"),
                        fieldWithPath("title").type(STRING).description("질문 제목"),
                        fieldWithPath("content").type(STRING).description("질문 내용")
                    )
                )
            )
    }

    @Test
    @DisplayName("질문 단건 조회")
    fun findQuestion() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }

        // expected
        this.mockMvc.perform(
            get("/questions/{questionId}", existingQuestion.id)
                .accept(APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "question-read",
                    pathParameters(
                        parameterWithName("questionId").description("질문 ID")
                    ),
                    responseFields(
                        fieldWithPath("id").type(NUMBER).description("질문 ID"),
                        fieldWithPath("title").type(STRING).description("질문 제목"),
                        fieldWithPath("content").type(STRING).description("질문 내용")
                    )
                )
            )
    }

    @Test
    @DisplayName("질문 목록 조회")
    fun findQuestions() {
        // given
        (1..AMOUNT_OF_QUESTIONS).forEach {
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목${it}", content = "테스트 내용${it}")
                .let { q -> questionRepository.save(q) }
        }

        // expected
        mockMvc.perform(
            get("/questions?page=1")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "question-read-all",
                    queryParameters(
                        parameterWithName("page").description("페이지 번호").optional()
                    ),
                    responseFields(
                        fieldWithPath("totalElements").type(NUMBER).description("전체 질문 수"),
                        fieldWithPath("totalPages").type(NUMBER).description("전체 페이지 수"),
                        fieldWithPath("size").type(NUMBER).description("한 페이지에 존재하는 질문의 수"),
                        fieldWithPath("content").description("질문 데이터"),

                        fieldWithPath("content[].id").type(NUMBER).description("질문 ID"),
                        fieldWithPath("content[].title").type(STRING).description("질문 제목"),
                        fieldWithPath("content[].content").type(STRING).description("질문 내용")
                    )
                )
            )
    }

    @Test
    @DisplayName("질문 수정")
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
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "question-update",
                    pathParameters(
                        parameterWithName("questionId").description("질문 ID")
                    ),
                    requestFields(
                        fieldWithPath("title").type(STRING).description("질문 제목"),
                        fieldWithPath("content").type(STRING).description("질문 내용")
                    ),
                    responseFields(
                        fieldWithPath("id").type(NUMBER).description("수정된 질문 ID"),
                        fieldWithPath("title").type(STRING).description("수정된 질문 제목"),
                        fieldWithPath("content").type(STRING).description("수정된 질문 내용")
                    )
                )
            )
    }

    @Test
    @DisplayName("질문 삭제")
    fun deleteQuestion() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }

        // expected
        mockMvc.perform(
            delete("/questions/{questionId}", existingQuestion.id)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "question-delete",
                    pathParameters(
                        parameterWithName("questionId").description("질문 ID")
                    )
                )
            )

    }

    companion object {
        const val QUESTION_PAGE_SIZE = 10 // 한 페이지의 크기
        const val AMOUNT_OF_QUESTIONS = 50 // 페이징 테스트를 위해 만든 더미 질문의 개수
    }
}