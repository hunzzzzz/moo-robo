package hunzz.study.moorobo.domain.question.repository

import hunzz.study.moorobo.domain.question.model.Question
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : JpaRepository<Question, Long>