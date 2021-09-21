package br.com.alura.clients.dto.request

import io.micronaut.core.annotation.Introspected

@Introspected
data class ContaRequest(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: String
) {

}
