package br.com.alura.grpc.dto.response

import br.com.alura.CarregaChavePixResponse
import br.com.alura.grpc.dto.ChavePixInfo

class CarregaChavePixResponseConverter {
    fun converte(chaveInfo: ChavePixInfo): CarregaChavePixResponse? {
        return CarregaChavePixResponse.newBuilder()
            .setClienteId(chaveInfo.clienteId ?:"")
            .setPixId(chaveInfo.pixId?.toString() ?:"")
            .setChave(
                CarregaChavePixResponse.ChavePix.newBuilder()
                    .setTipoChave(chaveInfo.tipo)
                    .setChave(chaveInfo.chave)
                    .setConta(CarregaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                        .setTipoConta(chaveInfo.conta.tipo)
                        .setInstituicao(chaveInfo.conta.instituicao.nomeInstituicao)
                        .setNomeDoTitular(chaveInfo.conta.titular)
                        .setCpfDoTitular(chaveInfo.conta.cpfTitular)
                        .setAgencia(chaveInfo.conta.agencia)
                        .setNumeroDaConta(chaveInfo.conta.numero)
                        .build())
                    .build()
            )
            .build()
    }
}
