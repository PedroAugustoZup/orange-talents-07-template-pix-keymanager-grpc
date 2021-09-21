package br.com.alura.clients.dto.reponse

import br.com.alura.model.Conta
import io.micronaut.core.annotation.Introspected

@Introspected
data class ContaApiResponse(
    val tipo: String,
    val instituicao: InstituicaoErpItauResponse,
    val agencia: String,
    val numero: String,
    val titular: TitularResponse
) {
    fun toModel() = Conta(
        tipo,
        instituicao.toModel(),
        agencia,
        numero,
        titular.toModel()
    )
}
