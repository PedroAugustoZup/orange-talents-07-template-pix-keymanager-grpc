package br.com.alura.clients.dto.reponse

import br.com.alura.model.Titular
import io.micronaut.core.annotation.Introspected

@Introspected
data class TitularResponse(val id: String, val nome: String, val cpf:String) {
    fun toModel() = Titular(id, nome, cpf)
}
