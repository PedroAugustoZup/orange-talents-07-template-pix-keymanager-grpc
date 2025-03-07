package br.com.alura

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import jakarta.inject.Singleton

@Factory
class Cliente{
    @Singleton
    fun clientGrpcRegistra(@GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel): ChavePixServiceRegistraGrpc.ChavePixServiceRegistraBlockingStub{
        return ChavePixServiceRegistraGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun clienteGrpcRemove(@GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel): ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub{
        return ChavePixServiceRemoveGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun clienteGrpcCarrega(@GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel): ChavePixServiceCarregaGrpc.ChavePixServiceCarregaBlockingStub{
        return ChavePixServiceCarregaGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun clienteGrpcListaChaveCliente(@GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel): ChavePixServiceListaChaveClienteGrpc.ChavePixServiceListaChaveClienteBlockingStub{
        return ChavePixServiceListaChaveClienteGrpc.newBlockingStub(channel)
    }
}