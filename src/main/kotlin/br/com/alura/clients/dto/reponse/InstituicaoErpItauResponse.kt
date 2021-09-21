package br.com.alura.clients.dto.reponse

import br.com.alura.model.Instituicao
import io.micronaut.core.annotation.Introspected

@Introspected
data class InstituicaoErpItauResponse(val nome: String,
                                 val ispb: String) {
    fun toModel() = Instituicao(nome, ispb)

}
