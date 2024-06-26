package hunzz.study.moorobo.domain.question.service

import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.dto.QuestionPageResponse
import hunzz.study.moorobo.domain.question.dto.QuestionResponse
import hunzz.study.moorobo.domain.question.dto.UpdateQuestionRequest
import hunzz.study.moorobo.domain.question.model.Question
import hunzz.study.moorobo.domain.question.repository.QuestionRepository
import hunzz.study.moorobo.global.utility.BannedWordsFilter
import hunzz.study.moorobo.global.exception.case.ModelNotFoundException
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Description
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class QuestionService(
    private val questionRepository: QuestionRepository,
    private val bannedWordsFilter: BannedWordsFilter
) {
    @Description("질문 id로 질문 엔티티를 가져오는 내부 메서드")
    private fun getQuestionById(questionId: Long) =
        questionRepository.findByIdOrNull(questionId)
            ?: throw ModelNotFoundException(Question::class.simpleName.toString())

    @Description("질문 등록")
    fun addQuestion(request: AddQuestionRequest) =
        request.validate(bannedWordsFilter).to()
            .let { questionRepository.save(it) }
            .let { QuestionResponse.from(it) }

    @Description("질문 단건 조회")
    fun findQuestion(questionId: Long) =
        getQuestionById(questionId)
            .let { QuestionResponse.from(it) }

//    @Description("질문 목록 조회 (v1: QueryDSL 미사용)")
//    fun findQuestions(page: Int) =
//        PageRequest.of(page - 1, QUESTION_PAGE_SIZE, Sort.by(DESC, "id"))
//            .let { questionRepository.findAll(it).map { q -> QuestionResponse.from(q) } }

    @Description("질문 목록 조회 (v2: QueryDSL 사용)")
    fun findQuestions(page: Int) =
        PageRequest.of(page - 1, QUESTION_PAGE_SIZE)
            .let { questionRepository.findQuestions(it).map { q -> QuestionResponse.from(q) } }
            .let { QuestionPageResponse.from(it) }

    @Transactional
    @Description("질문 수정")
    fun updateQuestion(questionId: Long, request: UpdateQuestionRequest) =
        request.validate(bannedWordsFilter).let {
            getQuestionById(questionId).update(title = request.title, content = it.content)
        }.let { QuestionResponse.from(it) }

    @Transactional
    @Description("질문 삭제")
    fun deleteQuestion(questionId: Long) =
        getQuestionById(questionId).delete()

    companion object {
        private const val QUESTION_PAGE_SIZE = 10
    }
}