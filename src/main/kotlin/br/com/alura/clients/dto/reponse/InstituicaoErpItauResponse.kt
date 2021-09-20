package br.com.alura.clients.dto.reponse

import io.micronaut.core.annotation.Introspected

@Introspected
data class InstituicaoErpItauResponse(val nome: String,
                                 val ispb: String) {

}
