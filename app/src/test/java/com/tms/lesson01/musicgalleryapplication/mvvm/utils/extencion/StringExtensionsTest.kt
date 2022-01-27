package com.tms.lesson01.musicgalleryapplication.mvvm.utils.extencion

import org.junit.Assert.*
import org.junit.Test

/**
 * В начале аннотация @Test, в конце - метод assertEquals()
 */
class StringExtensionsTest {
    // 1. Проверка метода String?.isEmailValid(): Boolean
    // 1.1. Если пароль пустой
    @Test
    fun testIsPasswordValid_ifEmpty() {
        val password = ""
        val isPasswordValid = password.isPasswordValid()

        assertEquals(false, isPasswordValid)
    }

    // 1.2. Если пароль null
    @Test
    fun testIsPasswordValid_ifNull() {
        val password = null
        val isPasswordValid = password.isPasswordValid()

        assertEquals(false, isPasswordValid)
    }

    // 1.3. Если пароль - пробелы
    @Test
    fun testIsPasswordValid_ifBlank() {
        val password = "   "
        val isPasswordValid = password.isPasswordValid()

        assertEquals(false, isPasswordValid)
    }

    // 1.4. Если в пароле меньше 5 символов
    @Test
    fun testIsPasswordValid_ifLessThen5Chars() {
        val password = "123"
        val isPasswordValid = password.isPasswordValid()

        assertEquals(false, isPasswordValid)
    }

    // 1.5. Если в пароле 5 символов
    @Test
    fun testIsPasswordValid_if5Chars() {
        val password = "12345"
        val isPasswordValid = password.isPasswordValid()

        assertEquals(false, isPasswordValid)
    }

    // 1.6. Если в пароле больше 5 символов
    @Test
    fun testIsPasswordValid_ifMoreThen5Chars() {
        val password = "1234567"
        val isPasswordValid = password.isPasswordValid()

        assertEquals(true, isPasswordValid)
    }

    // 2. Проверка метода String?.isEmailValid(): Boolean
    // 2.1. Если email пустой
    @Test
    fun testIsEmailValid_ifEmpty() {
        val email = ""
        val isEmailValid = email.isEmailValid()

        assertEquals(false, isEmailValid)
    }

    // 2.2. Если email null
    @Test
    fun testIsEmailValid_ifNull() {
        val email = null
        val isEmailValid = email.isEmailValid()

        assertEquals(false, isEmailValid)
    }

    // 2.3. Если email - пробелы
    @Test
    fun testIsEmailValid_ifBlank() {
        val email = "   "
        val isEmailValid = email.isEmailValid()

        assertEquals(false, isEmailValid)
    }

    // 2.4. Если введен не email
    @Test
    fun testIsEmailValid_ifIsNotEmail() {
        val email = "qqqqqw"
        val isEmailValid = email.isEmailValid()

        assertEquals(false, isEmailValid)
    }

    // 2.5. Если введен формат email
    @Test
    fun testIsEmailValid_ifCorrectEmail() {
        val email = "qqqqqw@gmail.com"
        val isEmailValid = email.isEmailValid()

        assertEquals(true, isEmailValid)
    }
}