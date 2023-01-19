package hu.gergogergely.sandboxservice.exception

enum class CustomExceptionCode {
    ALREADY_EXISTS,
    INCONSISTENT_REQUEST,
    MISSING_HEADER
}

class CustomException(
    val code: CustomExceptionCode,
    message: String
): RuntimeException(message)
