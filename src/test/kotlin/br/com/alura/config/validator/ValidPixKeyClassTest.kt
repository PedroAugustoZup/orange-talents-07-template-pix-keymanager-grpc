package br.com.alura.config.validator

import br.com.alura.TipoChave
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false)
internal class ValidPixKeyClassTest() {

    @Test
    fun `deve dar false para o UNKNOWM_CHAVE`() {
        assertFalse(TipoChave.UNKNOWM_CHAVE.validaKey("78394589634"))
    }
    @Test
    fun `deve dar true para o CPF`() {
        assertTrue(TipoChave.CPF.validaKey("78394589634"))
    }
    @Test
    fun `deve dar false para o CPF`() {
        assertFalse(TipoChave.CPF.validaKey(""))
    }
    @Test
    fun `deve dar true para o RANDOM`() {
        assertTrue(TipoChave.RANDOM.validaKey(""))
    }
    @Test
    fun `deve dar false para o RANDOM`() {
        assertFalse(TipoChave.RANDOM.validaKey("dsdasdsa"))
    }
    @Test
    fun `deve dar true para o PHONE`() {
        assertTrue(TipoChave.PHONE.validaKey("+5534999999999"))
    }
    @Test
    fun `deve dar false para o PHONE`() {
        assertFalse(TipoChave.PHONE.validaKey(""))
    }
    @Test
    fun `deve dar true para o CNPJ`() {
        assertTrue(TipoChave.CNPJ.validaKey("28.422.647/0001-20"))
    }
    @Test
    fun `deve dar false para o CNPJ`() {
        assertFalse(TipoChave.CNPJ.validaKey(""))
    }
    @Test
    fun `deve dar true para o EMAIL`() {
        assertTrue(TipoChave.EMAIL.validaKey("dasdas@gmail.com"))
    }
    @Test
    fun `deve dar false para o EMAIL`() {
        assertFalse(TipoChave.EMAIL.validaKey(""))
    }
}