package br.com.alura.model

import io.micronaut.core.annotation.Introspected
import javax.persistence.Embeddable

@Embeddable
@Introspected
data class Instituicao(val nomeInstituicao: String,
                  val ispb: String)
