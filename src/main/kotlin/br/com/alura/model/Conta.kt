package br.com.alura.model
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Conta(
    @field:NotBlank
    val tipo: String,
    @field:Embedded
    @field:NotNull
    val instituicao: Instituicao,
    @field:NotBlank
    val agencia: String,
    @field:NotBlank
    val numero: String,
    @field:Embedded
    @field:NotNull
    val titular: Titular
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}
