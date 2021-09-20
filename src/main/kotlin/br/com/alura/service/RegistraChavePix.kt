package br.com.alura.service

import br.com.alura.ChavePixGrpcServiceGrpc
import br.com.alura.ChavePixRequest
import br.com.alura.ChavePixResponse
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Validated
@Singleton
class RegistraChavePix(val service: RegistraChavePixService): ChavePixGrpcServiceGrpc.ChavePixGrpcServiceImplBase() {

    val logger = LoggerFactory.getLogger(RegistraChavePix::class.java)
    override fun registra(request: ChavePixRequest,
                          responseObserver: StreamObserver<ChavePixResponse>) {
        val novaChave = request.toDto()

        service.registraChave(novaChave)

        val response = ChavePixResponse.newBuilder()
            .setIdChave("teste")
            .build()
        responseObserver.onNext(response)
        logger.info("Chave do tipo ${request.tipoChave.name} cadastrada com sucesso")
        responseObserver.onCompleted()
    }
}