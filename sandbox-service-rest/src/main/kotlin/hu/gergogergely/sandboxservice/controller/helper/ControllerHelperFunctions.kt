package hu.gergogergely.sandboxservice.controller.helper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T, R> Flow<T>.toResponseEntity(mapToDTO: (dao: T) -> R): ResponseEntity<Flow<R>> =
    ResponseEntity.status(HttpStatus.OK).body(this.map { mapToDTO(it) })

fun <T, R> T?.toResponseEntity(mapToDTO: (dao: T) -> R): ResponseEntity<R> =
    this?.let { ResponseEntity(mapToDTO(it), HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NOT_FOUND)
