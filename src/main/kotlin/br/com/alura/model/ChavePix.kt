package br.com.alura.model

import br.com.alura.TipoChave
import br.com.alura.config.validator.ValidIdCliente
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class ChavePix(
    @field:NotBlank
    @field:ValidIdCliente
    val idCliente: String,
    @field:NotNull
    @field:Enumerated(EnumType.STRING)
    val tipoChave: TipoChave,
    @field:Size(max = 77)
    var valorChave: String,
    @field:NotNull
    @field:ManyToOne(cascade= [CascadeType.ALL])
    val conta: Conta
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null
    override fun toString(): String {
        return "ChavePix(idCliente='$idCliente', tipoChave=$tipoChave, valorChave='$valorChave', conta=$conta, id=$id)"
    }


}