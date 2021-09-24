package br.com.alura.grpc.endpoint


import br.com.alura.ChavePixRequest
import br.com.alura.ChavePixServiceRegistraGrpc
import br.com.alura.TipoChave
import br.com.alura.TipoConta
import br.com.alura.clients.BcbClient
import br.com.alura.clients.ContasClient
import br.com.alura.clients.dto.reponse.*
import br.com.alura.clients.dto.request.ContaRequest
import br.com.alura.clients.dto.request.CreatePixKeyRequest
import br.com.alura.clients.dto.request.TitularRequest
import br.com.alura.clients.dto.request.toRequest
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
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

@MicronautTest(transactional = false)
internal class RegistraChavePixTest(
    private val grpcClient: ChavePixServiceRegistraGrpc.ChavePixServiceRegistraBlockingStub,
    private val repository: ChavePixRepository
) {
    @Inject
    lateinit var bcbClient: BcbClient

    @Inject
    lateinit var contasClient: ContasClient

    @MockBean(ContasClient::class)
    fun contasClient() : ContasClient? {
        return mock(ContasClient::class.java)
    }

    @MockBean(BcbClient::class)
    fun bcbClient() : BcbClient? {
        return mock(BcbClient::class.java)
    }

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

    /*
        1- caminho feliz salvando tudo corretamente - V
        2- caminho triste quando já existe essa chave - V
        3- caminho quando algum valor está errado
     */

    @Test
    fun `deve cadastrar uma nova chave`(){
        val chaveCadastrada = chaveCadastrada()
        val createPixKeyRequest = chaveRequestBcbClient(chaveCadastrada)

        val chavePixRequest = ChavePixRequest.newBuilder()
            .setTipoChave(TipoChave.CPF)
            .setValorChave("78394589634")
            .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .build()

        `when`(contasClient.buscaCliente(chavePixRequest.idCliente))
            .thenReturn(HttpResponse.ok(ClienteApiResponse(
            UUID.fromString(chavePixRequest.idCliente),
            chaveCadastrada.conta.titular.nomeTitular,
            chaveCadastrada.conta.titular.cpf,
            InstituicaoErpItauResponse("ITAÚ UNIBANCO S.A.", "60701190"))))

        `when`(contasClient.buscaConta(chavePixRequest.idCliente, chavePixRequest.tipoConta.name))
            .thenReturn(HttpResponse.ok(
                ContaApiResponse(chaveCadastrada.conta.tipo.name,
                    InstituicaoErpItauResponse(chaveCadastrada.conta.instituicao.nomeInstituicao, chaveCadastrada.conta.instituicao.ispb),
                    chaveCadastrada.conta.agencia, chaveCadastrada.conta.numero,
                    TitularResponse(chaveCadastrada.conta.titular.idTitular, chaveCadastrada.conta.titular.nomeTitular, chaveCadastrada.conta.titular.cpf))
            ))
        `when`(bcbClient.salvaChavePix(createPixKeyRequest)).thenReturn(CreatePixKeyResponse("78394589634"))

        val response = grpcClient.registra(chavePixRequest)
        val chaveSalva = repository.findById(response.idChave.toLong())

        with(chaveSalva){
            assertTrue(chaveSalva.isPresent)
            assertEquals(chaveSalva.get().valorChave, chavePixRequest.valorChave)
        }
    }
    @Test
    fun `deve dar erro ao registrar uma chave que já existe`() {

        val chaveCadastrada = repository.save(chaveCadastrada())

        val chavePixRequest = ChavePixRequest.newBuilder()
                            .setTipoChave(chaveCadastrada.tipoChave)
                            .setValorChave(chaveCadastrada.valorChave)
                            .setIdCliente(chaveCadastrada.idCliente)
                            .setTipoConta(chaveCadastrada.conta.tipo)
                            .build()

        `when`(contasClient.buscaCliente(chavePixRequest.idCliente))
            .thenReturn(HttpResponse.ok(ClienteApiResponse(
                UUID.fromString(chavePixRequest.idCliente),
                chaveCadastrada.conta.titular.nomeTitular,
                chaveCadastrada.conta.titular.cpf,
                InstituicaoErpItauResponse(chaveCadastrada.conta.instituicao.nomeInstituicao,
                    chaveCadastrada.conta.instituicao.ispb))))

        val error = assertThrows<StatusRuntimeException>{
            grpcClient.registra(chavePixRequest)
        }


        with(error){
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Chave já existente", status.description)
        }
    }

    @Test
    fun `deve dar erro ao passar um dado inválido na chave`(){
        val chavePixRequest = ChavePixRequest.newBuilder()
            .setTipoChave(TipoChave.CPF)
            .setValorChave("")
            .setIdCliente("c56dfef4-7901-44fb-84e2-a2cefb157890")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .build()

        `when`(contasClient.buscaCliente(chavePixRequest.idCliente))
            .thenReturn(HttpResponse.ok(ClienteApiResponse(
                UUID.fromString(chavePixRequest.idCliente),
                "Rafael Ponte",
                "78394589634",
                InstituicaoErpItauResponse("ITAÚ UNIBANCO S.A.", "60701190"))))

        val error = assertThrows<StatusRuntimeException>{
            grpcClient.registra(chavePixRequest)
        }

        with(error){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertTrue(status.description!!.contains("Verifique o tipo e o valor de sua chave"))
        }
    }


    private fun chaveCadastrada(): ChavePix {
        return ChavePix("c56dfef4-7901-44fb-84e2-a2cefb157890",
            TipoChave.CPF, "78394589634",
            Conta(TipoConta.CONTA_CORRENTE, Instituicao("ITAÚ UNIBANCO S.A.",
                "60701190"),
                "0001", "291900", Titular("c56dfef4-7901-44fb-84e2-a2cefb157890",
                    "Rafael M C Ponte", "02467781054")
            )
        )
    }

    private fun chaveRequestBcbClient(chaveCadastrada: ChavePix): CreatePixKeyRequest {
        return CreatePixKeyRequest(keyType = chaveCadastrada.tipoChave.name,
            key= chaveCadastrada.valorChave,
            bankAccount= ContaRequest(participant = chaveCadastrada.conta.instituicao.ispb,
                branch= chaveCadastrada.conta.agencia,
                accountNumber= chaveCadastrada.conta.numero,
                accountType= chaveCadastrada.conta.tipo.toRequest()),
            owner = TitularRequest(type = "NATURAL_PERSON",
                name= chaveCadastrada.conta.titular.nomeTitular,
                taxIdNumber = chaveCadastrada.conta.titular.cpf)
        )
    }

    @Factory
    class Cliente(){
        @Singleton
        fun clientGrpc(@GrpcChannel(GrpcServerChannel.NAME) channel : ManagedChannel): ChavePixServiceRegistraGrpc.ChavePixServiceRegistraBlockingStub{
            return ChavePixServiceRegistraGrpc.newBlockingStub(channel)
        }
    }
}