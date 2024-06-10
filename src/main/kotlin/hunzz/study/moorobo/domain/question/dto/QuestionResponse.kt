package hunzz.study.moorobo.domain.question.dto

import hunzz.study.moorobo.domain.question.model.Question

data class QuestionResponse(
    val id: Long,
    val title: String,
    val content: String
) {
    companion object {
        // Entity -> Response
        fun from(question: Question) = QuestionResponse(
            id = question.id!!,
            title = question.title,
            content = question.content
        )
    }
}