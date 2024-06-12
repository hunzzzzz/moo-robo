package hunzz.study.moorobo.global.exception

import hunzz.study.moorobo.global.exception.case.ModelNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    // Validation 실패
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException) =
        ErrorResponse().let {
            e.fieldErrors.forEach { err ->
                it.addError(err.field, err.defaultMessage!!)
            }
            it
        }

    // id로 엔티티 조회 실패
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e: ModelNotFoundException) =
        ErrorResponse().addError(null, e.message!!)
}