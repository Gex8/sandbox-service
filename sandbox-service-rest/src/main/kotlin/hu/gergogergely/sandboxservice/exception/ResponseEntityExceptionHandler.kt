package hu.gergogergely.sandboxservice.exception

import hu.gergogergely.sandboxservice.openapi.sandboxserviceexampledata.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.OffsetDateTime


@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RuntimeException::class)
    protected fun handleConflict(
        ex: RuntimeException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        return if (ex is CustomException) {
            return ResponseEntity(
                ErrorResponse(
                    code = ex.code.toString(),
                    timestamp = OffsetDateTime.now(),
                    message = ex.message
                ),
                when (ex.code) {
                    CustomExceptionCode.ALREADY_EXISTS -> HttpStatus.CONFLICT
                    CustomExceptionCode.INCONSISTENT_REQUEST,
                    CustomExceptionCode.MISSING_HEADER -> HttpStatus.BAD_REQUEST
                }
            )
        } else {
            ResponseEntity(
                ErrorResponse(code = "UNKNOWN", timestamp = OffsetDateTime.now(), message = "Unhandled error occurred."),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}