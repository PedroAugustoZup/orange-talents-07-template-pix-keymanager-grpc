package br.com.alura.service

import br.com.alura.ChavePixRequest
import br.com.alura.dto.NovaChavePix

fun ChavePixRequest.toDto(): NovaChavePix{
    return NovaChavePix(
        idCliente,
        tipoChave,
        valorChave,
        tipoConta
    )
}