package br.com.alura.model

import io.micronaut.core.annotation.Introspected
import javax.persistence.Embeddable

@Embeddable
@Introspected
data class Titular(val idTitular: String, val nomeTitular: String, val cpf:String)
