package br.com.alura.clients

import br.com.alura.clients.dto.reponse.CreatePixKeyResponse
import br.com.alura.clients.dto.request.CreatePixKeyRequest
import br.com.alura.dto.DeletePixKeyRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client(value = "http://localhost:8082/api/v1")
interface BcbClient {

    @Post(value = "/pix/keys", produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun salvaChavePix(@Body createPixKeyRequest: CreatePixKeyRequest): CreatePixKeyResponse

    @Delete(value = "/pix/keys/{key}", produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun remover(@PathVariable("key") key: String, @Body request: DeletePixKeyRequest): HttpResponse<Map<String, String>>

}
