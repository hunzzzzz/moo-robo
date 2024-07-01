package hunzz.study.moorobo.domain.question.dto

import org.springframework.data.domain.Page

data class QuestionPageResponse(
    val totalElements: Long,
    val totalPages: Int,
    val size: Int,
    val content: List<QuestionResponse>
) {
    companion object {
        fun from(page: Page<QuestionResponse>) = QuestionPageResponse(
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            size = page.size,
            content = page.content
        )
    }
}