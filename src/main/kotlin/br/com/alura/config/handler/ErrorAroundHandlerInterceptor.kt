package br.com.alura.config.handler

import br.com.alura.config.exceptions.ChavePixExistenteException
import br.com.alura.config.exceptions.ChavePixNaoEncontradaException
import br.com.alura.config.exceptions.ContaInvalidaException
import io.grpc.Status
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.http.client.exceptions.HttpClientException
import jakarta.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(ErrorAroundHandler::class)
class ErrorAroundHandlerInterceptor : MethodInterceptor<Any, Any> {

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {

        try {
            return context.proceed()
        } catch (ex: Exception) {

            val responseObserver = context.parameterValues[1] as StreamObserver<*>

            val status = when(ex) {
                is ConstraintViolationException -> Status.INVALID_ARGUMENT
                    .withCause(ex)
                    .withDescription(ex.message)
                is ChavePixExistenteException -> Status.ALREADY_EXISTS
                    .withCause(ex)
                    .withDescription(ex.message)
                is HttpClientException -> Status.UNAVAILABLE
                    .withCause(ex)
                    .withDescription(ex.cause!!.message)
                is ChavePixNaoEncontradaException -> Status.NOT_FOUND
                    .withCause(ex)
                    .withDescription(ex.message)
                is IllegalArgumentException -> Status.INVALID_ARGUMENT
                    .withCause(ex)
                    .withDescription(ex.message)
                is ContaInvalidaException -> Status.INVALID_ARGUMENT
                    .withCause(ex)
                    .withDescription(ex.message)
                else -> Status.UNKNOWN
                    .withCause(ex)
                    .withDescription("Ops, um erro inesperado ocorreu")
            }

            responseObserver.onError(status.asRuntimeException())
        }

        return null
    }

}