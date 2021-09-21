package br.com.alura.model
import br.com.alura.TipoConta
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Conta(
    @field:NotNull
    @field:Enumerated(EnumType.STRING)
    val tipo: TipoConta,
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
