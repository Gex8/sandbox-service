package hu.gergogergely.sandboxservice.filter

import hu.gergogergely.sandboxservice.exception.CustomException
import hu.gergogergely.sandboxservice.exception.CustomExceptionCode
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

const val HEADER_CORRELATION_ID_FIELD = "x-correlation-id"

@Component
class CorrelationIdFilter: WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val correlationId = exchange.request.headers[HEADER_CORRELATION_ID_FIELD]
        if(correlationId == null) {
            throw CustomException(
                CustomExceptionCode.MISSING_HEADER,
                "The $HEADER_CORRELATION_ID_FIELD header field is missing"
            )
        } else {
            return chain.filter(exchange).contextWrite { context ->
                context.put(HEADER_CORRELATION_ID_FIELD, correlationId)
            }
        }
    }
}
