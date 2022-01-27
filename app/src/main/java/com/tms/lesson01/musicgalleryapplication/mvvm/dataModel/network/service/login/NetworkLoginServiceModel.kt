package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login

import com.tms.lesson01.musicgalleryapplication.mvvm.utils.extencion.isEmailValid
import com.tms.lesson01.musicgalleryapplication.mvvm.utils.extencion.isPasswordValid

/**
 * hw01. В Модели написать метод для проверки name: просто проверить что он не пустой
 * В Модели написать метод для проверки email: проверить что это действительно email (это мы уже делали)
 * В Модели написать методы для проверки password: минимум 5 символов + проверить что мы ввели пароль в двох полях одинаковый
 *
 * hw02. 1. SRP - Принцип единственной ответственности. Для работы с запросами на сервер о данных пользователя имеем отдельный класс. Обработка данных и возвращение результата
 * hw02. 2. Методы пишем на основе OCP - Принцип открытости/закрытости. Программные сущности (классы, модули, ф-ции и проч.) должны быть открыты для расширения, но закрыты для изменений
 * hw02. 3. ISP - Принцип разделения интерфейса. Наследуемся от более мелкого, специфического интерфейса
 * hw02. 4. LSP - Принцип подстановки Барбары Лисков. Подклассы должны переопределять методы базового класса так, чтобы не нарушалась функциональность с точки зрения клиента
 */
class NetworkLoginServiceModel : INetworkLoginService {
    override fun onSignUpClicked(name: String, email: String, password: String, confirmPassword: String): String? {
        val isNameValid = name.isNotBlank()
        val isEmailValid = email.isEmailValid()
        val isPasswordValid = password.isPasswordValid()
        val isConfirmPasswordValid = password == confirmPassword // В Kotlin сравнение строк через ==, а не equals. Если нужно сравнить адрес - ===

        // Если всё верно, логинимся и возвращается токен. Если не верно - не возвращается
        return if (isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid) {
            doSignUp(name, email, password, confirmPassword)
        } else {
            null
        }
    }

    override fun onLoginClicked(email: String, password: String): String? {
        val isEmailValid = email.isEmailValid()
        val isPasswordValid = password.isPasswordValid()

        // Если всё верно, логинимся и возвращается токен. Если не верно - не возвращается
        return if (isEmailValid && isPasswordValid) {
            doLogin(email, password)
        } else {
            null
        }
    }

    override fun updateUserData(data: Any) {
//        TODO("Not yet implemented")
    }

    // Если пользователь успешно зарегистрировался, он получает token. Token - это ключ к другим запросам. Чтобы использовать его в дальнейшем, его нужно закэшировать.
    private fun doSignUp(name: String, email: String, password: String, confirmPassword: String): String {
        return "successToken"
    }

    private fun doLogin(email: String, password: String): String? {
        return "successToken"
    }
}