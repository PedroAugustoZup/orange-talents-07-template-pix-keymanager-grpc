package br.com.alura.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class DeletePixKeyRequest(
    @field:NotBlank @field:JsonProperty("key") val valorChave: String,
    @field:NotBlank @field:JsonProperty("participant") val isbp: String
)
