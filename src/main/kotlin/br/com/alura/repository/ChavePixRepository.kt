package br.com.alura.repository

import br.com.alura.model.ChavePix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository: JpaRepository<ChavePix, Long>{
    fun findByValorChave(valorChave: String): Optional<ChavePix>

    fun findByIdCliente(idCliente: String): List<ChavePix>
}
