package hunzz.study.moorobo.domain.question.service

import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.dto.UpdateQuestionRequest
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
        assertEquals(existingQuestion.id, question.id)
        assertEquals("테스트 제목", question.title)
        assertEquals("테스트 내용", question.content)
    }

    @Test
    @DisplayName("질문 목록 조회")
    fun findQuestions() {
        // given
        (1..AMOUNT_OF_QUESTIONS).forEach {
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목${it}", content = "테스트 내용${it}")
                .let { q -> questionRepository.save(q) }
        }

        // when
        val firstPage = questionService.findQuestions(1)
        val lastPage = questionService.findQuestions(AMOUNT_OF_QUESTIONS / QUESTION_PAGE_SIZE)

        // then
        assertEquals(QUESTION_PAGE_SIZE, firstPage.size)
        assertEquals("테스트 제목${AMOUNT_OF_QUESTIONS}", firstPage.first().title)
        assertEquals("테스트 내용${AMOUNT_OF_QUESTIONS}", firstPage.first().content)
        assertEquals("테스트 제목1", lastPage.last().title)
        assertEquals("테스트 내용1", lastPage.last().content)
    }

    @Test
    @DisplayName("질문 수정")
    fun updateQuestion() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }
        val request = UpdateQuestionRequest(title = "수정된 테스트 제목", content = "수정된 테스트 내용")

        // when
        val question = questionService.updateQuestion(existingQuestion.id!!, request)

        // then
        assertEquals(true, existingQuestion.id == question.id)
        assertEquals("수정된 테스트 제목", question.title)
        assertEquals("수정된 테스트 내용", question.content)
    }

    companion object {
        const val QUESTION_PAGE_SIZE = 10 // 한 페이지의 크기
        const val AMOUNT_OF_QUESTIONS = 50 // 페이징 테스트를 위해 만든 더미 질문의 개수
    }
}