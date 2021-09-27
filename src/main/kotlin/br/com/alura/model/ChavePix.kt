package br.com.alura.model

import br.com.alura.TipoChave
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class ChavePix(
    @field:NotBlank
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

    fun pertenceAoCliente(clienteId: String): Boolean {
        return conta.titular.idTitular == clienteId
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChavePix) return false

        if (idCliente != other.idCliente) return false
        if (tipoChave != other.tipoChave) return false
        if (valorChave != other.valorChave) return false
        if (conta != other.conta) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idCliente.hashCode()
        result = 31 * result + tipoChave.hashCode()
        result = 31 * result + valorChave.hashCode()
        result = 31 * result + conta.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }


}