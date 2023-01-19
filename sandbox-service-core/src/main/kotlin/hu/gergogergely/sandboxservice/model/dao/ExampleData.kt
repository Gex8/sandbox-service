package hu.gergogergely.sandboxservice.model.dao

import java.time.Instant

data class ExampleData (
    val id: String? = null,
    val publicIdentifier: String,
    val updatedAt: Instant = Instant.now(),
    val status: Status = Status.ACTIVE,
    val connectedData: List<String>? = null
) {
    enum class Status(private val value: String) {
        ACTIVE("Active"),
        PAUSED("Paused"),
        INACTIVE("Inactive")
    }
}
