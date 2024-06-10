package hunzz.study.moorobo.domain.question.controller

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("정상적으로 질문이 등록되는 경우")
    fun addQuestion() {
        mockMvc.perform(
            post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                          "title": "테스트 제목",
                          "content": "테스트 내용"
                        }    
                    """.trimIndent()
                )
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.title").value("테스트 제목"))
            .andExpect(jsonPath("$.content").value("테스트 내용"))
            .andDo(print())
    }

    @Test
    @DisplayName("질문의 제목과 내용을 미입력한 경우")
    fun addQuestionException1() {
        mockMvc.perform(
            post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                          "title": "",
                          "content": ""
                        }    
                    """.trimIndent()
                )
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors").isArray)
            .andExpect(jsonPath("$.errors.size()").value(2))
            .andExpect(jsonPath("$.errors[0].field").value("title"))
            .andExpect(jsonPath("$.errors[0].message").value("질문의 제목은 필수 입력 항목입니다."))
            .andExpect(jsonPath("$.errors[1].field").value("content"))
            .andExpect(jsonPath("$.errors[1].message").value("질문의 내용은 필수 입력 항목입니다."))
            .andDo(print())
    }

    @Test
    @DisplayName("128자를 초과하는 질문 제목이 등록된 경우")
    fun addQuestionException2() {
        mockMvc.perform(
            post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                          "title": "${"테스트".repeat(43)}",
                          "content": "테스트 내용"
                        }    
                    """.trimIndent()
                )
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.errors").isArray)
            .andExpect(jsonPath("$.errors.size()").value(1))
            .andExpect(jsonPath("$.errors[0].field").value("title"))
            .andExpect(jsonPath("$.errors[0].message").value("질문의 제목은 128자 이하여야 합니다."))
            .andDo(print())
    }
}