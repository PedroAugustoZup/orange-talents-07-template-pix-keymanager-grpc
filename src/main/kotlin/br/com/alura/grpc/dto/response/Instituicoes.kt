package br.com.alura.grpc.dto.response

import br.com.alura.model.Instituicao

class Instituicoes {
    fun nome(participant: String): Instituicao {
        return Instituicao("Itau", participant)
    }
}
