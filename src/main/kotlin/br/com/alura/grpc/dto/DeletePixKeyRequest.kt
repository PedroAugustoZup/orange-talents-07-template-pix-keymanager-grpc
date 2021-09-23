package br.com.alura.service

import br.com.alura.RemoverChavePixRequest
import br.com.alura.dto.DeletePixKeyRequest

fun RemoverChavePixRequest.toDto(): DeletePixKeyRequest {
    return DeletePixKeyRequest(pixId, isbp)
}