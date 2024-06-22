package hunzz.study.moorobo.global.exception.case

class BannedWordInQuestionException(field: String) : QuestionException(
    "${
        when (field) {
            "title" -> "질문 제목"
            "content" -> "질문 내용"
            else -> ""
        }
    }에 비속어가 포함되어 있습니다."
) {
    override val statusCode = "400"

    init {
        addError(field = field)
    }
}