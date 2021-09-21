package br.com.alura.clients.dto.request

import io.micronaut.core.annotation.Introspected

@Introspected
data class TitularRequest(val type: String, val name: String, val taxIdNumber: String) {

}
