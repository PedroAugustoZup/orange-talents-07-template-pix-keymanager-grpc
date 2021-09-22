package br.com.alura.grpc.endpoint

import br.com.alura.ChavePixGrpcServiceGrpc
import br.com.alura.ChavePixRequest
import br.com.alura.TipoChave
import br.com.alura.TipoConta
import br.com.alura.model.ChavePix
import br.com.alura.model.Conta
import br.com.alura.model.Instituicao
import br.com.alura.model.Titular
import br.com.alura.repository.ChavePixRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest(transactional = false)
internal class RegistraChavePixTest(
    val grpcClient: ChavePixGrpcServiceGrpc.ChavePixGrpcServiceBlockingStub,
    val repository: ChavePixRepository
) {
//    @Inject
//    lateinit var service: RegistraChavePixService

//    @MockBean(RegistraChavePixService::class)
//    fun productRepository(): RegistraChavePixService {
//        return mock(RegistraChavePixService::class.java) // (3)
//    }

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

    /*
        1- caminho feliz salvando tudo corretamente
        2- caminho triste quando já existe essa have
        3- caminho quando algum valor está errado
     */

    @Test
    fun `deve dar erro ao registrar uma chave que já existe`() {
        //cenario
        val chaveCadastrada = ChavePix("c56dfef4-7901-44fb-84e2-a2cefb157890",
            TipoChave.CPF, "78394589634",
            Conta(TipoConta.CONTA_CORRENTE, Instituicao("ITAÚ UNIBANCO S.A.",
                                                    "60701190"),
                "0001", "291900", Titular("c56dfef4-7901-44fb-84e2-a2cefb157890",
                                            "Rafael M C Ponte", "02467781054")
            )
        )
        repository.save(chaveCadastrada)

        val chavePixRequest = ChavePixRequest.newBuilder()
                            .setTipoChave(TipoChave.CPF)
                            .setValorChave("78394589634")
                            .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
                            .setTipoConta(TipoConta.CONTA_CORRENTE)
                            .build()
        //acao
        val error = assertThrows<StatusRuntimeException>{
            grpcClient.registra(chavePixRequest)
        }

        //verificação
        with(error){
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
        }
    }

    @Factory
    class Cliente(){
        @Singleton
        fun clientGrpc(@GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel): ChavePixGrpcServiceGrpc.ChavePixGrpcServiceBlockingStub{
            return ChavePixGrpcServiceGrpc.newBlockingStub(channel)
        }
    }
}