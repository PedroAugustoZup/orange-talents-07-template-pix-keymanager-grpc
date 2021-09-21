package br.com.alura.clients.dto.request

import br.com.alura.TipoConta

fun TipoConta.toRequest(): String {
    if(this.equals(TipoConta.CONTA_CORRENTE)) return "CACC"
    else return "SVGS"
}