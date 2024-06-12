package hunzz.study.moorobo.domain.question.service

import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.model.Question
import hunzz.study.moorobo.domain.question.model.QuestionStatus
import hunzz.study.moorobo.domain.question.repository.QuestionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

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
    @DisplayName("질문 등록")
    fun addQuestion() {
        // given
        val request = AddQuestionRequest(title = "테스트 제목", content = "테스트 내용")

        // when
        val question = questionService.addQuestion(request)

        // then
        assertEquals(1, questionRepository.count())
        assertEquals("테스트 제목", question.title)
        assertEquals("테스트 내용", question.content)
    }

    @Test
    @DisplayName("질문 단건 조회")
    fun findQuestion() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }

        // when
        val question = questionService.findQuestion(existingQuestion.id!!)

        // then
        assertEquals("테스트 제목", question.title)
        assertEquals("테스트 내용", question.content)
    }
}