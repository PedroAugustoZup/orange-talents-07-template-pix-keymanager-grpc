package br.com.alura.service

import br.com.alura.dto.NovaChavePix
import br.com.alura.repository.ChavePixRepository
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class RegistraChavePixService(val repository: ChavePixRepository) {
    fun registraChave(@Valid novaChave: NovaChavePix) {
        return
    }

}
