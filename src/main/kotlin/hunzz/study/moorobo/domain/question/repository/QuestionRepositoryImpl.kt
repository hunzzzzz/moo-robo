package hunzz.study.moorobo.domain.question.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import hunzz.study.moorobo.domain.question.model.QQuestion
import hunzz.study.moorobo.global.config.QueryDslConfig
import org.springframework.stereotype.Repository

@Repository
class QuestionRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : QueryDslConfig(), QuestionRepositoryCustom {
    private val question = QQuestion.question
}