package br.com.alura.grpc.endpoint

import br.com.alura.ChavePixServiceListaChaveClienteGrpc
import br.com.alura.ListaChaveClienteRequest
import br.com.alura.ListaChaveClienteResponse
import br.com.alura.config.handler.ErrorAroundHandler
import br.com.alura.model.ChavePix
import br.com.alura.repository.ChavePixRepository
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton

@ErrorAroundHandler
@Singleton
class CarregaChavesCliente(val repository: ChavePixRepository): ChavePixServiceListaChaveClienteGrpc.ChavePixServiceListaChaveClienteImplBase() {

    override fun lista(
        request: ListaChaveClienteRequest,
        responseObserver: StreamObserver<ListaChaveClienteResponse>
    ) {
        val idCliente = request.clientId ?: ""
        val chavesEncontradas = repository.findByIdCliente(idCliente)

        responseObserver.onNext(ListaChaveClienteResponse.newBuilder()
            .addAllChaves(chavesEncontradas.map(ChavePix::toResponseCliente)).build())
        responseObserver.onCompleted()
    }
}

private fun ChavePix.toResponseCliente(): ListaChaveClienteResponse.ChaveCliente {
    return ListaChaveClienteResponse.ChaveCliente.newBuilder()
        .setPixId(id.toString())
        .setClientId(idCliente)
        .setTipoChave(tipoChave)
        .setValorChave(valorChave)
        .setTipoConta(conta.tipo)
        .build()
}