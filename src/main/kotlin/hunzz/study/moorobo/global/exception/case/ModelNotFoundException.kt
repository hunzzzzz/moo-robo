package hunzz.study.moorobo.global.exception.case

import hunzz.study.moorobo.domain.question.model.Question

class ModelNotFoundException(value: String) : RuntimeException(
    "존재하지 않는 ${
        when (value) {
            Question::class.simpleName.toString() -> "질문"
            else -> ""
        }
    }입니다."
)