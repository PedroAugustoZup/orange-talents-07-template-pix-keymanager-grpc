package br.com.alura.clients.dto.reponse

import br.com.alura.TipoChave
import br.com.alura.TipoConta
import br.com.alura.dto.ContaAssociada
import br.com.alura.grpc.dto.ChavePixInfo
import br.com.alura.grpc.dto.response.Instituicoes
import java.time.LocalDateTime

data class PixKeyDetailsResponse(
    val keyType: TipoChave,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
) {
    fun toModel(): ChavePixInfo? {
        return ChavePixInfo(
            tipo = keyType,
            chave = key,
            conta = ContaAssociada(tipo= when(bankAccount.accountType){
                            AccountType.CACC -> TipoConta.CONTA_CORRENTE
                            AccountType.SVGS -> TipoConta.CONTA_POUPANCA
                        },
                        instituicao = Instituicoes.nome(bankAccount.participant),
                        agencia = bankAccount.branch,
                        numero = bankAccount.accountNumber,
                        titular = owner.name,
                        cpfTitular = owner.taxIdNumber
                )
        )
    }
}
