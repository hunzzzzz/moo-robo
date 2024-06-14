package hunzz.study.moorobo.domain.question.repository

import hunzz.study.moorobo.domain.question.model.Question
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepositoryCustom {
    fun findQuestions(pageable: Pageable): Page<Question>
}