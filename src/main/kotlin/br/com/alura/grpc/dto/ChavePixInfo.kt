package br.com.alura.grpc.dto

import br.com.alura.TipoChave
import br.com.alura.dto.ContaAssociada
import br.com.alura.model.ChavePix

data class ChavePixInfo(
    val pixId: Long? = null,
    val clienteId: String? = null,
    val tipo: TipoChave,
    val chave: String,
    val conta: ContaAssociada
) {

    companion object{
        fun of(chave:ChavePix): ChavePixInfo{
            return ChavePixInfo(
                chave.id,
                chave.idCliente,
                chave.tipoChave,
                chave.valorChave,
                ContaAssociada(tipo= chave.conta.tipo,
                    instituicao = chave.conta.instituicao,
                    agencia = chave.conta.agencia,
                    numero = chave.conta.numero,
                    titular = chave.conta.titular.nomeTitular,
                    cpfTitular = chave.conta.titular.cpf
                )
            )
        }
    }

}
