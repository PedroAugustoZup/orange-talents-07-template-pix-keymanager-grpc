package br.com.alura.service

import br.com.alura.clients.ContasClient
import br.com.alura.config.exceptions.ChavePixExistenteException
import br.com.alura.dto.NovaChavePix
import br.com.alura.repository.ChavePixRepository
import br.com.alura.utils.TransactionalEvent
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class RegistraChavePixService(val repository: ChavePixRepository,
                              val contasClient: ContasClient,
                              val transactionalEvent: TransactionalEvent){

    fun registraChave(@Valid novaChave: NovaChavePix): String {
        if(repository.existsByValorChave(novaChave.valorChave))
            throw ChavePixExistenteException("Chave já existente")
        val contasResponse = contasClient.buscaConta(novaChave.idCliente, novaChave.tipoConta.name)

//        TODO:Comunicar com o BCB passando a chave e os dados do cliente

        val chavePix = novaChave.toModel(contasResponse.body().toModel())
        transactionalEvent.execute {
            repository.save(chavePix)
        }

        return chavePix.id.toString()
    }

}
