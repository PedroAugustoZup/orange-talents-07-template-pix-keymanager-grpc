package br.com.alura.service

import br.com.alura.clients.ContasClient
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
        val contasResponse = contasClient.buscaConta(novaChave.idCliente, novaChave.tipoConta.name)
        val chavePix = novaChave.toModel(contasResponse.body().toModel())
        transactionalEvent.execute {
            repository.save(chavePix)
        }

        return chavePix.id.toString()
    }

}
