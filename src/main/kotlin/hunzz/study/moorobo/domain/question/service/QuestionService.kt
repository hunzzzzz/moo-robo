package hunzz.study.moorobo.domain.question.service

import hunzz.study.moorobo.domain.question.dto.AddQuestionRequest
import hunzz.study.moorobo.domain.question.dto.QuestionResponse
import hunzz.study.moorobo.domain.question.repository.QuestionRepository
import org.springframework.context.annotation.Description
import org.springframework.stereotype.Service

@Service
class QuestionService(
    private val questionRepository: QuestionRepository
) {
    @Description("질문 등록")
    fun addQuestion(request: AddQuestionRequest) =
        request.to()
            .let { questionRepository.save(it) }
            .let { QuestionResponse.from(it) }
}