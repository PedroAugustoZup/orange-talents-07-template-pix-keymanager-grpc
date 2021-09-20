package br.com.alura.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class ChavePix {
    @Id
    @Column(name = "id", nullable = false)
    open var id: Long? = null
}