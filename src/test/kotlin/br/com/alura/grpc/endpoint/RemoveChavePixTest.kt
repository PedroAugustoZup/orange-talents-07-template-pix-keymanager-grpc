package br.com.alura.grpc.endpoint

import br.com.alura.ChavePixServiceRemoveGrpc
import br.com.alura.RemoverChavePixRequest
import br.com.alura.TipoChave
import br.com.alura.TipoConta
import br.com.alura.clients.BcbClient
import br.com.alura.dto.DeletePixKeyRequest
import br.com.alura.model.ChavePix
import br.com.alura.model.Conta
import br.com.alura.model.Instituicao
import br.com.alura.model.Titular
import br.com.alura.repository.ChavePixRepository
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

@MicronautTest(transactional = false)
internal class RemoveChavePixTest(
    private val grpcClient: ChavePixServiceRemoveGrpc.ChavePixServiceRemoveBlockingStub,
    val repository: ChavePixRepository
) {
    @Inject
    lateinit var bcbClient: BcbClient

    @MockBean(BcbClient::class)
    fun bcbClient() : BcbClient? {
        return Mockito.mock(BcbClient::class.java)
    }

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

    @Test
    fun `deve excluir a chave pix`() {
        val chaveCadastrada = repository.save(chaveCadastrada())

        Mockito.`when`(bcbClient.remover(chaveCadastrada.valorChave, DeletePixKeyRequest(
            chaveCadastrada.valorChave,
            chaveCadastrada.conta.instituicao.ispb
        ))).thenReturn(HttpResponse.ok())

        val response = grpcClient.excluir(RemoverChavePixRequest.newBuilder().setPixId(chaveCadastrada.valorChave)
            .setIsbp(chaveCadastrada.conta.instituicao.ispb).build())
        val possivelChave = repository.findById(chaveCadastrada.id)

        with(response){
            assertTrue(possivelChave.isEmpty)
        }
    }

    @Test
    fun `deve dar erro ao tentar encontrar a chave no banco para excluir`(){
        val error = assertThrows<StatusRuntimeException> {
            grpcClient.excluir(
                RemoverChavePixRequest.newBuilder().setPixId("78394589634")
                    .setIsbp("60701190").build()
            )
        }

        with(error){
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave não encontrada", status.description)
        }
    }

    @Test
    fun `deve dar erro no cliente http`(){
        val chaveCadastrada = repository.save(chaveCadastrada())
        Mockito.`when`(bcbClient.remover(chaveCadastrada.valorChave, DeletePixKeyRequest(
            chaveCadastrada.valorChave,
            chaveCadastrada.conta.instituicao.ispb
        ))).thenReturn(HttpResponse.notFound())

        val error = assertThrows<StatusRuntimeException> {
            grpcClient.excluir(
                RemoverChavePixRequest.newBuilder().setPixId("78394589634")
                    .setIsbp("60701190").build()
            )
        }

        with(error){
            assertEquals(Status.UNKNOWN.code, status.code)
            assertEquals("Ops, algo deu errado", status.description)
        }
    }

    private fun chaveCadastrada(): ChavePix {
        return ChavePix("c56dfef4-7901-44fb-84e2-a2cefb157890",
            TipoChave.CPF, "78394589634",
            Conta(
                TipoConta.CONTA_CORRENTE, Instituicao("ITAÚ UNIBANCO S.A.",
                "60701190"),
                "0001", "291900", Titular("c56dfef4-7901-44fb-84e2-a2cefb157890",
                    "Rafael M C Ponte", "02467781054")
            )
        )
    }
}