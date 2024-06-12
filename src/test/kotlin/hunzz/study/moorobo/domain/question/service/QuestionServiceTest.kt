package hunzz.study.moorobo.domain.question.service

import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.repository.QuestionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class QuestionServiceTest {
    @Autowired
    lateinit var questionService: QuestionService

    @Autowired
    lateinit var questionRepository: QuestionRepository

    @BeforeEach
    fun clean() {
        questionRepository.deleteAll()
    }

    @Test
    fun addQuestion() {
        // given
        val request = AddQuestionRequest(title = "테스트 제목", content = "테스트 내용")

        // when
        questionService.addQuestion(request)

        // then
        assertEquals(1, questionRepository.count())
        questionRepository.findByIdOrNull(1L).let {
            assertEquals("테스트 제목", it!!.title)
            assertEquals("테스트 내용", it.content)
        }
    }
}