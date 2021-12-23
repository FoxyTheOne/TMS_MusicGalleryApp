package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network

/**
 * hw02. 3. ISP - Принцип разделения интерфейса. Разделяем интерфейсы на более мелкие, специфические
 */
interface INetworkLoginService {
    fun onSignUpClicked(name: String, email:String, password: String, confirmPassword: String): String?
    fun onLoginClicked(email:String, password: String): String?
    fun updateUserData(data: Any)
}