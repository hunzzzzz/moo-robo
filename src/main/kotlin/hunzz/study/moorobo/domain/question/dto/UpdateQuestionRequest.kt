package hunzz.study.moorobo.domain.question.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateQuestionRequest(
    @field:NotBlank(message = "질문의 제목은 필수 입력 항목입니다.")
    @field:Size(max = 128, message = "질문의 제목은 128자 이하여야 합니다.")
    val title: String,

    @field:NotBlank(message = "질문의 내용은 필수 입력 항목입니다.")
    val content: String
)