package br.com.alura.grpc.endpoint

import br.com.alura.ChavePixGrpcServiceGrpc
import br.com.alura.ChavePixRequest
import br.com.alura.ChavePixResponse
import br.com.alura.config.exceptions.ChavePixExistenteException
import br.com.alura.service.RegistraChavePixService
import br.com.alura.service.toDto
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.validation.ConstraintViolationException

@Validated
@Singleton
class RegistraChavePix(val service: RegistraChavePixService): ChavePixGrpcServiceGrpc.ChavePixGrpcServiceImplBase() {

    val logger: Logger = LoggerFactory.getLogger(RegistraChavePix::class.java)
    override fun registra(request: ChavePixRequest,
                          responseObserver: StreamObserver<ChavePixResponse>) {
        val novaChave = request.toDto()
        try {
            val idChavePixCadastrada = service.registraChave(novaChave)
            val response = ChavePixResponse.newBuilder()
                .setIdChave(idChavePixCadastrada)
                .build()
            responseObserver.onNext(response)
            logger.info("Chave do tipo ${request.tipoChave.name} cadastrada com sucesso")
        }catch (e: ChavePixExistenteException) {
            logger.error("Exception caused while processing the call: ${e.localizedMessage}", e)
            responseObserver.onError(Status.ALREADY_EXISTS
                .withDescription(e.message)
                .asRuntimeException())
            return
        }catch (e: ConstraintViolationException){
            logger.error("Exception caused while processing the call: ${e.localizedMessage}")
            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription(e.message)
                .asRuntimeException())
            return
        }
        responseObserver.onCompleted()
    }
}