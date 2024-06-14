package hunzz.study.moorobo.domain.question.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QuestionRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : QuestionRepositoryCustom