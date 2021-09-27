package br.com.alura.dto

import br.com.alura.TipoConta
import br.com.alura.model.Instituicao

data class ContaAssociada(
    val tipo: TipoConta,
    val instituicao: Instituicao,
    val agencia: String,
    val numero: String,
    val titular: String,
    val cpfTitular: String
)