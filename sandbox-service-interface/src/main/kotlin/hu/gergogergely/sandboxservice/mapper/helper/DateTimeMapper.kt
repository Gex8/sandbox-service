package hu.gergogergely.sandboxservice.mapper.helper

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class DateTimeMapper {
    fun toOffsetDateTime(instant: Instant?): OffsetDateTime? =
        instant?.let {
            OffsetDateTime.ofInstant(it, ZoneId.of("CET"))
                .withOffsetSameInstant(ZoneOffset.of("+00:00"))
        }

    fun toInstant(offsetDateTime: OffsetDateTime?): Instant? =
        offsetDateTime?.let { Instant.from(it) }
}
