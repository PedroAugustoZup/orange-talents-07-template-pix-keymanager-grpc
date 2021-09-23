package br.com.alura.grpc.endpoint

import br.com.alura.ChavePixRequest
import br.com.alura.ChavePixResponse
import br.com.alura.ChavePixServiceRegistraGrpc
import br.com.alura.config.handler.ErrorAroundHandler
import br.com.alura.service.RegistraChavePixService
import br.com.alura.service.toDto
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ErrorAroundHandler
@Validated
@Singleton
class RegistraChavePix(val service: RegistraChavePixService): ChavePixServiceRegistraGrpc.ChavePixServiceRegistraImplBase() {

    val logger: Logger = LoggerFactory.getLogger(RegistraChavePix::class.java)
    override fun registra(request: ChavePixRequest,
                          responseObserver: StreamObserver<ChavePixResponse>) {
        val novaChave = request.toDto()
        val idChavePixCadastrada = service.registraChave(novaChave)
        val response = ChavePixResponse.newBuilder()
            .setIdChave(idChavePixCadastrada)
            .build()
        responseObserver.onNext(response)
        logger.info("Chave do tipo ${request.tipoChave.name} cadastrada com sucesso")
        responseObserver.onCompleted()
    }
}