package br.com.alura.clients.dto.request

import io.micronaut.core.annotation.Introspected

@Introspected
data class CreatePixKeyRequest(
    val keyType: String,
    val key: String,
    val bankAccount: ContaRequest,
    val owner: TitularRequest
) {

}
