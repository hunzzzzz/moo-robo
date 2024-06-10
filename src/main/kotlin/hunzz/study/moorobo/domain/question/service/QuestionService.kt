package hunzz.study.moorobo.domain.question.service

import hunzz.study.moorobo.domain.question.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class QuestionService(
    private val questionRepository: QuestionRepository
) {
}