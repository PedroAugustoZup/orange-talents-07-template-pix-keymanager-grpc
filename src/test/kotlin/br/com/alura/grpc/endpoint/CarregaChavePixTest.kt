package br.com.alura.grpc.endpoint

import br.com.alura.CarregaChavePixRequest
import br.com.alura.ChavePixServiceCarregaGrpc
import br.com.alura.TipoChave
import br.com.alura.TipoConta
import br.com.alura.clients.BcbClient
import br.com.alura.clients.dto.reponse.AccountType
import br.com.alura.clients.dto.reponse.BankAccount
import br.com.alura.clients.dto.reponse.Owner
import br.com.alura.clients.dto.reponse.PixKeyDetailsResponse
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
import java.time.LocalDateTime

@MicronautTest(transactional = false)
internal class CarregaChavePixTest(
    private val grpcClient: ChavePixServiceCarregaGrpc.ChavePixServiceCarregaBlockingStub,
    private val repository: ChavePixRepository
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
    fun `deve carregar os dados do banco de dados a partir do valor da chave`() {
        repository.save(ChavePix("c56dfef4-7901-44fb-84e2-a2cefb157890",
            TipoChave.CPF, "78394589634",
            Conta(
                TipoConta.CONTA_CORRENTE, Instituicao("ITAÚ UNIBANCO S.A.",
                "60701190"),
                "0001", "291900", Titular("c56dfef4-7901-44fb-84e2-a2cefb157890",
                    "Rafael M C Ponte", "02467781054")
            )
        ))
        val request = CarregaChavePixRequest.newBuilder()
            .setChave("78394589634")
            .build()

        val response = grpcClient.carrega(request)

        with(response){
            assertEquals(request.chave, response.chave.chave)
        }
    }

    @Test
    fun `deve carregar os dados do cliente BCB a partir do valor da chave`(){
        val request = CarregaChavePixRequest.newBuilder()
            .setChave("78394589634")
            .build()

        Mockito.`when`(bcbClient.findByChave(request.chave))
            .thenReturn(HttpResponse.ok(
                PixKeyDetailsResponse(TipoChave.CPF, "78394589634",
                    BankAccount("60701190", "0001", "291900", AccountType.CACC),
                Owner("NATURAL_PERSON", "Rafael M C Ponte", "02467781054"),
                    LocalDateTime.now())))

        val response = grpcClient.carrega(request)

        with(response){
            assertEquals("02467781054",response.chave.conta.cpfDoTitular)
            assertEquals(request.chave, response.chave.chave)
        }
    }

    @Test
    fun `deve carregar os dados do banco de dados a partir do ID da chave`(){
        val responseBanco = repository.save(ChavePix("c56dfef4-7901-44fb-84e2-a2cefb157890",
            TipoChave.CPF, "78394589634",
            Conta(
                TipoConta.CONTA_CORRENTE, Instituicao("ITAÚ UNIBANCO S.A.",
                    "60701190"),
                "0001", "291900", Titular("c56dfef4-7901-44fb-84e2-a2cefb157890",
                    "Rafael M C Ponte", "02467781054")
            )
        ))

        val request = CarregaChavePixRequest.newBuilder()
            .setPixId(CarregaChavePixRequest.FiltroPorPixId.newBuilder()
                .setPixId(responseBanco.id.toString()).setClienteId("c56dfef4-7901-44fb-84e2-a2cefb157890")
                .build())
            .build()

        val response = grpcClient.carrega(request)

        with(response){
            assertEquals(responseBanco.valorChave, response.chave.chave)
        }
    }

    @Test
    fun `deve dar erro ao buscar uma chave inexistente`(){
        val request = CarregaChavePixRequest.newBuilder()
            .setChave("78394589634")
            .build()

        Mockito.`when`(bcbClient.findByChave(request.chave))
            .thenReturn(HttpResponse.notFound())

        val error = assertThrows<StatusRuntimeException>{
            grpcClient.carrega(request)
        }

        with(error){
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertTrue(status.description!!.contains("Chave pix nao encontrada"))
        }
    }

    @Test
    fun `deve dar erro ao não informar nenhum filtro`(){
        val request = CarregaChavePixRequest.newBuilder().build()

        val error = assertThrows<StatusRuntimeException>{
            grpcClient.carrega(request)
        }
        with(error){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertTrue(status.description!!.contains("Chave Pix inválida ou não informada"))
        }

    }
}