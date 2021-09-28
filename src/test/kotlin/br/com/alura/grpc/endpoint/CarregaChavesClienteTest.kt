package br.com.alura.grpc.endpoint

import br.com.alura.ChavePixServiceListaChaveClienteGrpc
import br.com.alura.ListaChaveClienteRequest
import br.com.alura.TipoChave
import br.com.alura.TipoConta
import br.com.alura.model.ChavePix
import br.com.alura.model.Conta
import br.com.alura.model.Instituicao
import br.com.alura.model.Titular
import br.com.alura.repository.ChavePixRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
internal class CarregaChavesClienteTest(
    private val grpcCliente: ChavePixServiceListaChaveClienteGrpc.ChavePixServiceListaChaveClienteBlockingStub,
    private val repository: ChavePixRepository
    ) {

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

    @Test
    fun `deve listar as chaves do cliente especifico`() {
        repository.save(ChavePix("5260263c-a3c1-4727-ae32-3bdb2538841b",
            TipoChave.CPF, "86135457004",
            Conta(
                TipoConta.CONTA_CORRENTE, Instituicao("ITAÚ UNIBANCO S.A.",
                    "60701190"),
                "0001", "291900", Titular("5260263c-a3c1-4727-ae32-3bdb2538841b",
                    "Yuri Matheus", "86135457004")
            )
        ))

        val chaveSalva = repository.save(
            ChavePix("c56dfef4-7901-44fb-84e2-a2cefb157890",
            TipoChave.CPF, "78394589634",
            Conta(
                TipoConta.CONTA_CORRENTE, Instituicao("ITAÚ UNIBANCO S.A.",
                    "60701190"),
                "0001", "291900", Titular("c56dfef4-7901-44fb-84e2-a2cefb157890",
                    "Rafael M C Ponte", "02467781054")
            )
        ))

        val response = grpcCliente.lista(ListaChaveClienteRequest.newBuilder()
            .setClientId("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .build())

        with(response){
            assertEquals(1, response.chavesList.size)
            assertEquals("78394589634", response.chavesList[0].valorChave)
        }
    }
}