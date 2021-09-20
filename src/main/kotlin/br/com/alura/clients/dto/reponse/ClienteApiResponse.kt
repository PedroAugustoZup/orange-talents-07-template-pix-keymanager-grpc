package br.com.alura.clients.dto.reponse

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class ClienteApiResponse(val id : UUID,
                              val nome: String,
                              val cpf: String,
                              val instituicao : InstituicaoErpItauResponse)
