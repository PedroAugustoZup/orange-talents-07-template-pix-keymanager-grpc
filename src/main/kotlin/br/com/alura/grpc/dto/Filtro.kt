package br.com.alura.grpc.dto

import br.com.alura.clients.BcbClient
import br.com.alura.config.exceptions.ChavePixNaoEncontradaException
import br.com.alura.repository.ChavePixRepository
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpStatus
import org.slf4j.LoggerFactory
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
sealed class Filtro {

    abstract fun filtra(repository: ChavePixRepository, bcbClient: BcbClient):ChavePixInfo

    @Introspected
    data class PorPixId(
        @field:NotBlank val clienteId: String,
        @field:NotNull val pixId: Long
    ): Filtro() {
        override fun filtra(repository: ChavePixRepository, bcbClient: BcbClient): ChavePixInfo {
            return repository.findById(pixId)
                .filter{it.pertenceAoCliente(clienteId)}
                .map(ChavePixInfo::of)
                .orElseThrow{ ChavePixNaoEncontradaException("Chave Pix não encontrada") }
        }

    }

    @Introspected
    data class PorChave(@field:NotBlank @Size(max = 77) val chave: String): Filtro() {
        private val LOGGER = LoggerFactory.getLogger(this::class.java)
        override fun filtra(repository: ChavePixRepository, bcbClient: BcbClient): ChavePixInfo {
           return repository.findByValorChave(chave)
               .map(ChavePixInfo::of).orElseGet {
                   LOGGER.info("Buscando no banco central")

                   val response = bcbClient.findByChave(chave)
                   when(response.status){
                       HttpStatus.OK-> response.body().toModel()
                       else -> throw ChavePixNaoEncontradaException("Chave pix nao encontrada")
                   }
               }
        }
    }

    @Introspected
    class Invalido: Filtro(){
        override fun filtra(repository: ChavePixRepository, bcbClient: BcbClient): ChavePixInfo {
            throw IllegalArgumentException("Chave Pix inválida ou não informada")
        }

    }
}
