package br.com.alura.clients

import br.com.alura.clients.dto.reponse.ClienteApiResponse
import br.com.alura.clients.dto.reponse.ContaApiResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client(value = "http://localhost:9091/api/v1")
interface ContasClient {

    @Get("/clientes/{idCliente}")
    fun buscaCliente(@PathVariable("idCliente") idCliente:String): HttpResponse<ClienteApiResponse>

    @Get("/clientes/{clienteId}/contas{?tipo}")
    fun buscaConta(@PathVariable("clienteId") idCliente: String, @QueryValue("tipo")tipo: String): HttpResponse<ContaApiResponse>

}
