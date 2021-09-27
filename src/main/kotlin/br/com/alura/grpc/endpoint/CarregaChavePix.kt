package br.com.alura.grpc.endpoint

import br.com.alura.CarregaChavePixRequest
import br.com.alura.CarregaChavePixResponse
import br.com.alura.ChavePixServiceCarregaGrpc
import br.com.alura.clients.BcbClient
import br.com.alura.config.handler.ErrorAroundHandler
import br.com.alura.grpc.dto.response.CarregaChavePixResponseConverter
import br.com.alura.grpc.toModel
import br.com.alura.repository.ChavePixRepository
import io.grpc.stub.StreamObserver
import io.micronaut.validation.validator.Validator
import jakarta.inject.Singleton

@ErrorAroundHandler
@Singleton
class CarregaChavePix(
    private val repository: ChavePixRepository,
    private val bcbClient: BcbClient,
    private val validator: Validator
): ChavePixServiceCarregaGrpc.ChavePixServiceCarregaImplBase(){
    override fun carrega(
        request: CarregaChavePixRequest,
        responseObserver: StreamObserver<CarregaChavePixResponse>
    ) {
        val filtro = request.toModel(validator)
        val chaveInfo = filtro.filtra(repository, bcbClient)

        responseObserver.onNext(CarregaChavePixResponseConverter().converte(chaveInfo))
        responseObserver.onCompleted()
    }
}


