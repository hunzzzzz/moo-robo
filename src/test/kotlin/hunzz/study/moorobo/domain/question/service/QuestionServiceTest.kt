package hunzz.study.moorobo.domain.question.service

import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.dto.UpdateQuestionRequest
import hunzz.study.moorobo.domain.question.model.Question
import hunzz.study.moorobo.domain.question.model.QuestionStatus
import hunzz.study.moorobo.domain.question.repository.QuestionRepository
import hunzz.study.moorobo.global.exception.case.BannedWordInQuestionException
import hunzz.study.moorobo.global.exception.case.ModelNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
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
    @DisplayName("질문 등록 시, 제목이나 내용에 비속어가 포함된 경우 BannedWordInQuestionException")
    fun addQuestionException1() {
        // given
        val request1 = AddQuestionRequest(title = "욕설이 포함된 테스트 제목", content = "테스트 내용")
        val request2 = AddQuestionRequest(title = "테스트 제목", content = "테스트 내용 내 욕설 포함")

        // expected
        assertThrows(BannedWordInQuestionException::class.java) {
            questionService.addQuestion(request1)
        }.let { assertEquals("질문 제목에 비속어가 포함되어 있습니다.", it.message) }
        assertThrows(BannedWordInQuestionException::class.java) {
            questionService.addQuestion(request2)
        }.let { assertEquals("질문 내용에 비속어가 포함되어 있습니다.", it.message) }
    }

    @Test
    @DisplayName("질문 등록 시, 제목이나 내용에 구분자를 활용한 비속어가 포함된 경우 BannedWordInQuestionException")
    fun addQuestionException2() {
        // given
        val request1 = AddQuestionRequest(title = "욕1설이 포함된 테스트 제목", content = "테스트 내용")
        val request2 = AddQuestionRequest(title = "테스트 제목", content = "테스트 내용 내 욕@설 포함")

        // expected
        assertThrows(BannedWordInQuestionException::class.java) {
            questionService.addQuestion(request1)
        }.let { assertEquals("질문 제목에 비속어가 포함되어 있습니다.", it.message) }
        assertThrows(BannedWordInQuestionException::class.java) {
            questionService.addQuestion(request2)
        }.let { assertEquals("질문 내용에 비속어가 포함되어 있습니다.", it.message) }
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
    @DisplayName("질문 단건 조회 시, 존재하지 않는 id를 대입한 경우 ModelNotFoundException")
    fun findQuestionException() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }

        // expected
        assertThrows(ModelNotFoundException::class.java) {
            questionService.findQuestion(existingQuestion.id!! + 1)
        }.let { assertEquals("존재하지 않는 질문입니다.", it.message) }
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

    @Test
    @DisplayName("질문 삭제")
    fun deleteQuestion() {
        // given
        val existingQuestion =
            Question(status = QuestionStatus.NORMAL, title = "테스트 제목", content = "테스트 내용")
                .let { questionRepository.save(it) }

        // when
        questionService.deleteQuestion(existingQuestion.id!!)
        val question = questionRepository.findByIdOrNull(existingQuestion.id)!!

        // then
        assertEquals(1, questionRepository.count())
        assertEquals(QuestionStatus.DELETED, question.status)
        assertEquals(existingQuestion.title, question.title)
        assertEquals(existingQuestion.content, question.content)
    }

    companion object {
        const val QUESTION_PAGE_SIZE = 10 // 한 페이지의 크기
        const val AMOUNT_OF_QUESTIONS = 50 // 페이징 테스트를 위해 만든 더미 질문의 개수
    }
}