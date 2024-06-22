package hunzz.study.moorobo.global.exception.case

import hunzz.study.moorobo.global.exception.ErrorResponse.Error

abstract class QuestionException(private val errorMessage: String) : RuntimeException(errorMessage) {
    abstract val statusCode: String
    val errors: MutableList<Error> = mutableListOf()

    fun addError(field: String) = errors.add(Error(field = field, message = errorMessage))
}