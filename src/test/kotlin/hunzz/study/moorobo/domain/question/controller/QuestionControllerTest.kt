package hunzz.study.moorobo.domain.question.controller

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class QuestionControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("")
    fun addQuestion() {
        mockMvc.perform(
            post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "John")
        ).andExpect(status().isCreated)
            .andExpect(content().string(""))
            .andDo(print())


    }
}