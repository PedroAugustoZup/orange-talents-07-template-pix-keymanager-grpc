package br.com.alura.grpc.endpoint

import br.com.alura.ChavePixServiceRemoveGrpc
import br.com.alura.RemoverChavePixRequest
import br.com.alura.RemoverChavePixResponse
import br.com.alura.clients.BcbClient
import br.com.alura.config.handler.ErrorAroundHandler
import br.com.alura.repository.ChavePixRepository
import br.com.alura.service.toDto
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import io.micronaut.validation.validator.Validator
import jakarta.inject.Singleton

@ErrorAroundHandler
@Validated
@Singleton
class RemoveChavePix(
    val validator: Validator,
    val chavePixRepository: ChavePixRepository,
    val bcbClient: BcbClient
): ChavePixServiceRemoveGrpc.ChavePixServiceRemoveImplBase() {

    override fun excluir(
        request: RemoverChavePixRequest?,
        responseObserver: StreamObserver<RemoverChavePixResponse>?
    ) {
        val removeChavePixDto = request?.toDto()
        validator.validate(removeChavePixDto)

        val possivelChave = chavePixRepository.findByValorChave(removeChavePixDto!!.valorChave)

        if(possivelChave.isEmpty){
            responseObserver!!.onError(
                Status.NOT_FOUND
                    .withDescription("Chave não encontrada").asRuntimeException())
        }else{
            val response = bcbClient.remover(removeChavePixDto.valorChave, removeChavePixDto)

            if(response.status.equals(HttpStatus.OK)){
                chavePixRepository.deleteById(possivelChave.get().id)
                responseObserver?.onNext(RemoverChavePixResponse.newBuilder()
                    .setMensagem("Chave pix removida com sucesso")
                    .build())
            }else{
                responseObserver!!.onError(
                    Status.UNKNOWN
                        .withDescription("Ops, algo deu errado").asRuntimeException())
            }
        }
        responseObserver?.onCompleted()
        return
    }
}