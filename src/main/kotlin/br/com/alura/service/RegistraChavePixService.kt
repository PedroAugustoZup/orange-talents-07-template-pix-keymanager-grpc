package br.com.alura.service

import br.com.alura.clients.BcbClient
import br.com.alura.clients.ContasClient
import br.com.alura.clients.dto.reponse.ContaApiResponse
import br.com.alura.clients.dto.request.ContaRequest
import br.com.alura.clients.dto.request.CreatePixKeyRequest
import br.com.alura.clients.dto.request.TitularRequest
import br.com.alura.clients.dto.request.toRequest
import br.com.alura.config.exceptions.ChavePixExistenteException
import br.com.alura.dto.NovaChavePix
import br.com.alura.model.ChavePix
import br.com.alura.repository.ChavePixRepository
import br.com.alura.utils.TransactionalEvent
import io.micronaut.http.HttpResponse
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class RegistraChavePixService(val repository: ChavePixRepository,
                              val contasClient: ContasClient,
                              val transactionalEvent: TransactionalEvent,
                              val bcbClient: BcbClient
){

    fun registraChave(@Valid novaChave: NovaChavePix): String {
        if(repository.existsByValorChave(novaChave.valorChave))
            throw ChavePixExistenteException("Chave já existente")

        val contasResponse = contasClient.buscaConta(novaChave.idCliente, novaChave.tipoConta.name)

        val chavePix = comunicaBcb(novaChave.toModel(contasResponse.body().toModel()),
            contasResponse)
        transactionalEvent.execute {
            repository.save(chavePix)
        }
        return chavePix.id.toString()
    }

    private fun comunicaBcb(chavePix: ChavePix, contasResponse: HttpResponse<ContaApiResponse>): ChavePix {
        val createPixKeyRequest = CreatePixKeyRequest(keyType = chavePix.tipoChave.name,
            key= chavePix.valorChave,
            bankAccount= ContaRequest(participant = chavePix.conta.instituicao.ispb,
                                        branch= chavePix.conta.agencia,
                                        accountNumber= chavePix.conta.numero,
                                        accountType= chavePix.conta.tipo.toRequest()),
            owner = TitularRequest(type = "NATURAL_PERSON",
                                    name= chavePix.conta.titular.nomeTitular,
                                    taxIdNumber = chavePix.conta.titular.cpf) )

        val response  = bcbClient.salvaChavePix(createPixKeyRequest)
        chavePix.valorChave = response.key
        return chavePix
    }

}
