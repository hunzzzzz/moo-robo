package hunzz.study.moorobo.domain.question.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import hunzz.study.moorobo.domain.question.model.QQuestion
import org.springframework.context.annotation.Description
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class QuestionRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : QuestionRepositoryCustom {
    private val question = QQuestion.question

    @Description("질문 목록을 조회하는 메서드")
    override fun findQuestions(pageable: Pageable) =
        PageImpl(getContents(pageable), pageable, getTotalElements())

    @Description("페이지에 포함될 질문 데이터를 가져오는 내부 메서드")
    private fun getContents(pageable: Pageable) =
        jpaQueryFactory.selectFrom(question)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(question.id.desc())
            .fetch()

    @Description("전체 질문 개수를 가져오는 내부 메서드")
    private fun getTotalElements() =
        jpaQueryFactory.select(question.count()).from(question).fetchOne() ?: 0L
}