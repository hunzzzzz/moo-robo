package hunzz.study.moorobo.global.exception

import java.time.LocalDateTime

data class ErrorResponse(
    var errors: MutableList<Error> = mutableListOf(),
    val time: LocalDateTime = LocalDateTime.now()
) {
    fun addError(field: String?, message: String) =
        errors.add(Error(field = field, message = message))

    data class Error(
        var field: String?,
        val message: String
    )
}