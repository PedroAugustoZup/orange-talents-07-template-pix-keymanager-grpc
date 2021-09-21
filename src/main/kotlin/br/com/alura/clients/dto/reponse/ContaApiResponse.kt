package br.com.alura.clients.dto.reponse

import br.com.alura.TipoConta
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
    fun toModel(): Conta {
        val tipoConta = if(tipo == TipoConta.CONTA_CORRENTE.name) TipoConta.CONTA_CORRENTE
                        else TipoConta.CONTA_POUPANCA
        return Conta(
            tipoConta,
            instituicao.toModel(),
            agencia,
            numero,
            titular.toModel()
        )
    }
}
