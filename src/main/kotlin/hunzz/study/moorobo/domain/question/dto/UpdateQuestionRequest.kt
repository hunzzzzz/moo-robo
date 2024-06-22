package hunzz.study.moorobo.domain.question.dto

import hunzz.study.moorobo.global.utility.BannedWordsFilter
import hunzz.study.moorobo.global.exception.case.BannedWordInQuestionException
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.context.annotation.Description

data class UpdateQuestionRequest(
    @field:NotBlank(message = "질문의 제목은 필수 입력 항목입니다.")
    @field:Size(max = 128, message = "질문의 제목은 128자 이하여야 합니다.")
    val title: String,

    @field:NotBlank(message = "질문의 내용은 필수 입력 항목입니다.")
    val content: String
) {
    @Description("비속어 포함 여부 검증")
    fun validate(bannedWordsFilter: BannedWordsFilter): UpdateQuestionRequest {
        if (bannedWordsFilter.check(this.title))
            throw BannedWordInQuestionException("title")
        else if (bannedWordsFilter.check(this.content))
            throw BannedWordInQuestionException("content")
        else return this
    }
}