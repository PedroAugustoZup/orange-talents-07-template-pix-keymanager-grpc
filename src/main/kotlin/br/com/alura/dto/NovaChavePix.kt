package br.com.alura.dto

import br.com.alura.Conta
import br.com.alura.TipoChave
import br.com.alura.config.validator.ValidIdCliente
import br.com.alura.config.validator.ValidPixKey
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
data class NovaChavePix(
    @field:NotBlank
    @field:ValidIdCliente
    val idCliente: String,
    @field:NotNull
    val tipoChave: TipoChave,
    @field:NotBlank
    @field:Size(max = 77)
    val valorChave: String,
    @field:NotNull
    val conta: Conta
)
