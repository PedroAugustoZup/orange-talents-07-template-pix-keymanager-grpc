package br.com.alura.config.validator

import br.com.alura.clients.ContasClient
import io.micronaut.http.HttpStatus
import jakarta.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidIdClienteClass::class])
annotation class ValidIdCliente(
    val message:String = "Id do cliente inválido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Any>> = []
)


@Singleton
class ValidIdClienteClass(val contasClient: ContasClient): ConstraintValidator<ValidIdCliente, String> {
    override fun isValid(value: String, context: ConstraintValidatorContext?): Boolean {

        val response = contasClient.buscaCliente(value)
        return response.status.equals(HttpStatus.OK)
    }

}
