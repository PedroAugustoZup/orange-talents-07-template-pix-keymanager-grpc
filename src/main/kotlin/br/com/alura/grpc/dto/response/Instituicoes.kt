package br.com.alura.grpc.dto.response

import br.com.alura.model.Instituicao

class Instituicoes {
    companion object{
        fun nome(participant: String): Instituicao {
            return Instituicao("Itau", participant)
        }
    }

}
