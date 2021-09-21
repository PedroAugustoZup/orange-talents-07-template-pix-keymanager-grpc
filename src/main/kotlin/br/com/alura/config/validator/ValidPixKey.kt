package br.com.alura.config.validator

import br.com.alura.TipoChave
import br.com.alura.dto.NovaChavePix
import jakarta.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidPixKeyClass::class])
annotation class ValidPixKey(
    val message:String = "Verifique o tipo e o valor de sua chave",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Any>> = []
)

@Singleton
class ValidPixKeyClass: ConstraintValidator<ValidPixKey, NovaChavePix> {
    override fun isValid(value: NovaChavePix?, context: ConstraintValidatorContext?): Boolean {
        return value?.valorChave?.let { key ->
            value.tipoChave?.validaKey(key)
        } ?: true
    }
}


fun TipoChave.validaKey(key: String): Boolean {
    when (this){

        TipoChave.UNKNOWM_CHAVE -> {
            return false
        }
        TipoChave.CPF -> {
            return key.matches("^[0-9]{11}\$".toRegex())
        }
        TipoChave.CNPJ -> {
            return key.matches("^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}\$".toRegex())
        }
        TipoChave.TELEFONE_CELULAR -> {
            return key.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
        TipoChave.EMAIL -> {
            return key.matches("[a-z0-9!#\$%&'+/=?^_{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-][a-z0-9])?".toRegex())
        }
        TipoChave.CHAVE_ALEATORIA -> {
            return key.isEmpty()
        }

        else -> return false
    }
}