package hunzz.study.moorobo.domain.question.service

import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.dto.QuestionResponse
import hunzz.study.moorobo.domain.question.repository.QuestionRepository
import org.springframework.context.annotation.Description
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class QuestionService(
    private val questionRepository: QuestionRepository
) {
    @Description("질문 id로 질문 엔티티를 가져오는 내부 메서드")
    private fun getQuestionById(questionId: Long) =
        questionRepository.findByIdOrNull(questionId) ?: throw Exception("") // TODO : 추후 별도의 Exception 생성

    @Description("질문 등록")
    fun addQuestion(request: AddQuestionRequest) =
        request.to()
            .let { questionRepository.save(it) }
            .let { QuestionResponse.from(it) }

    @Description("질문 단건 조회")
    fun findQuestion(questionId: Long) =
        getQuestionById(questionId)
            .let { QuestionResponse.from(it) }
}