package br.com.alura.grpc

import br.com.alura.CarregaChavePixRequest
import br.com.alura.grpc.dto.Filtro
import io.micronaut.validation.validator.Validator
import javax.validation.ConstraintViolationException

fun CarregaChavePixRequest.toModel(validator: Validator): Filtro {
    val filtro = when(filtroCase){
        CarregaChavePixRequest.FiltroCase.PIXID -> pixId.let {
            Filtro.PorPixId(it.clienteId, it.pixId.toLong())
        }
        CarregaChavePixRequest.FiltroCase.CHAVE -> Filtro.PorChave(chave)
        CarregaChavePixRequest.FiltroCase.FILTRO_NOT_SET -> Filtro.Invalido()
    }

    val violations = validator.validate(filtro)
    if(violations.isNotEmpty()){
        throw ConstraintViolationException(violations)
    }

    return filtro
}