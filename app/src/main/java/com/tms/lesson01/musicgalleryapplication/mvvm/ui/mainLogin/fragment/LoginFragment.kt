package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.IUserStorage
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.LocalStorageModel
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.AppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.INetworkLoginService
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network.service.login.NetworkLoginServiceModel
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin.LoginViewModel
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainLogin.LoginViewModelFactory
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainSignUp.fragment.SignUpFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.fragment.PlaylistsListFragment

/**
 * 1.3. Настройка View Model в соответствии с принципом обеспечения зависимостей:
 * Создаём объект View Model с помощью своей фабрики
 */
class LoginFragment: Fragment() {
    // Переменные класса
//    private lateinit var viewModel: LoginViewModel
    private lateinit var frameLayout: FrameLayout
    private lateinit var progressCircular: ProgressBar

    private lateinit var emailField: TextInputLayout
    private lateinit var passwordField: TextInputLayout

    private lateinit var buttonLogin: AppCompatButton
    private lateinit var textGoToSignUp: TextView
    private lateinit var checkBoxRememberLoginAndPassword: AppCompatCheckBox

    // Инициализируем View Model с помощью своей фабрики
    private val viewModel by viewModels<LoginViewModel> {
        LoginViewModelFactory(
            // Инициализируем объекты в конструкторе
            NetworkLoginServiceModel() as INetworkLoginService,
            LocalStorageModel() as IUserStorage,
            AppSharedPreferences.getInstance(requireContext())
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Сразу начинаем отправлять данные по ключу NAVIGATION_EVENT
        sendNavigationEvents()

//        // Инициализируем viewModel, чтобы работать с ViewModel (согласно примеру с сайта)
//        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
//        // Вызовем наш метод, к-рый будет отправлять нам SharedPreferences из фрагмента
//        viewModel.setSharedPreferences(AppSharedPreferences.getInstance(requireContext())) // Вызываем наш статический метод для экземпляра класса AppSharedPreferences

        viewModel.getStoredData()

        // Оглашаем наши локальные переменные
        // Кнопки
        buttonLogin = view.findViewById(R.id.buttonLogin)
        textGoToSignUp = view.findViewById(R.id.bottomComponent2_lowerText2)
        checkBoxRememberLoginAndPassword = view.findViewById(R.id.checkBox_remember)
        // Переменные для отображения прогресса
        frameLayout = view.findViewById(R.id.frameLayout)
        progressCircular = view.findViewById(R.id.progressCircular)
        // Переменные для полей ввода
        emailField = view.findViewById(R.id.emailField)
        passwordField = view.findViewById(R.id.passwordField)

//        // Восстанавливаем значения при повороте экрана:
//        restoreValues()

        // Метод для всех наших listeners
        initListeners()
        // Вызываем метод, чтобы подписаться на события
        subscribeOnLiveData()
    }

    private fun initListeners() {
        // Сохраняем введенные в поля значения для последнующего восстановления при необходимости:
        emailField.editText?.addTextChangedListener(){
            it?.let {
//            viewModel.emailLiveData.value = it.toString()
            viewModel.setUpdatedEmail(it.toString())
            // Изменение цвета границы поля ввода при вводе символов в это поле:
                if (it.isBlank()) {
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.box_stroke_color_default
                    )?.let { colorList ->
                        emailField.setBoxStrokeColorStateList(colorList)
                    }
                } else {
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.box_stroke_color_with_text
                    )?.let { colorList ->
                        emailField.setBoxStrokeColorStateList(colorList)
                        emailField.hintTextColor = colorList
                    }
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.icon_in_box_with_text
                    )?.let { colorList ->
                        emailField.setStartIconTintList(colorList)
                    }
                }
            }
        }
        passwordField.editText?.addTextChangedListener(){
//            viewModel.passwordLiveData.value = it.toString()
            viewModel.setUpdatedPassword(it.toString())
            // Изменение цвета границы поля ввода при вводе символов в это поле:
            it?.let {
                if (it.isBlank()) {
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.box_stroke_color_default
                    )?.let { colorList ->
                        passwordField.setBoxStrokeColorStateList(colorList)
                    }
                } else {
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.box_stroke_color_with_text
                    )?.let { colorList ->
                        passwordField.setBoxStrokeColorStateList(colorList)
                        passwordField.hintTextColor = colorList
                    }
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.icon_in_box_with_text
                    )?.let { colorList ->
                        passwordField.setStartIconTintList(colorList)
                        passwordField.setEndIconTintList(colorList)
                    }
                }
            }
        }
        // Определяем действие по клику на кнопку:
        buttonLogin.setOnClickListener {
            val emailText = emailField.editText?.text.toString()
            val passwordText = passwordField.editText?.text.toString()

            viewModel.onLoginClicked(emailText, passwordText)
        }
        textGoToSignUp.setOnClickListener {
            (activity as MainActivity).openFragment(SignUpFragment())
            (activity as MainActivity).actionBar?.hide()
        }
        checkBoxRememberLoginAndPassword.setOnCheckedChangeListener{ _, selected ->
//            println("CHECK_BOX_SELECTED = $selected") - проверяли, работает ли check box
            viewModel.setRememberLoginAndPasswordSelectedOrNot(selected) // Передаём наш isSelected (при нажатии на кнопку) в наш listener
        }
    }

    /**
     * В случае успеха - перевести на страницу Success, как на занятии
     * В случае ошибки - вывести Toast, как на занятии
     */
    // Подвисываемся, чтобы слышать события из LiveData (в ViewModelMediator). Если какое-то из этих событий случится, будет вызван соответствующий метод, описанный в observe у этого события
    private fun subscribeOnLiveData() {
        viewModel.isLoginSuccessLiveData.observe(viewLifecycleOwner, {
//            val intent = Intent(context, SuccessLoginActivity::class.java)
//            startActivity(intent)
            // Открываем фрагмент:
            (activity as MainActivity).openFragment(PlaylistsListFragment(), doClearBackStack = true) // Очищаем стек после Login screen
            //(activity as MainActivity) - так мы преобразовали activity в MainActivity. Мы можем так сделать, т.к. у нас будет 1 activity и множество фрагментов, соответственно мы знаем, что MainActivity - точно наша activity
        })
        viewModel.isLoginFailureLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Something wrong with your data. Please, try again!", Toast.LENGTH_LONG).show()
        })
        viewModel.showProgressLiveData.observe(viewLifecycleOwner, {
            showProgress()
        })
        viewModel.hideProgressLiveData.observe(viewLifecycleOwner, {
            hideProgress()
        })
        // Слушаем check box
        viewModel.checkBoxRememberLoginAndPasswordLiveData.observe(viewLifecycleOwner, { isSelected ->
            checkBoxRememberLoginAndPassword.isChecked = isSelected
        })
        // Слушаем email и password
        viewModel.emailLiveData.observe(viewLifecycleOwner, { email ->
            emailField.editText?.setText(email)
            emailField.editText?.setSelection(email.length)
        })
        viewModel.passwordLiveData.observe(viewLifecycleOwner, { password ->
            passwordField.editText?.setText(password)
            passwordField.editText?.setSelection(password.length)
        })
    }

//    private fun restoreValues() {
//        nameField.editText?.setText(viewModel.nameLiveData.value ?: "")
//        emailField.editText?.setText(viewModel.emailLiveData.value ?: "")
//        passwordField.editText?.setText(viewModel.passwordLiveData.value ?: "")
//        confirmPasswordField.editText?.setText(viewModel.confirmPasswordLiveData.value ?: "")
//    }

    private fun showProgress() {
        frameLayout.isVisible = true
        progressCircular.isVisible = true
    }

    private fun hideProgress() {
        frameLayout.isVisible = false
        progressCircular.isVisible = false
    }

    // отправляем данные в MainActivity, которое подписано на ключ NAVIGATION_EVENT
    private fun sendNavigationEvents() {
        requireActivity().supportFragmentManager.setFragmentResult( // Пишем requireActivity() вместо activity, т.к. activity м.б. null и нужна будет дополнительная проверка
            // Буду отправлять на слушателя с ключом NAVIGATION_EVENT (т.е. это ключ, по которому мы регистрировали слушателя ранее)
            MainActivity.NAVIGATION_EVENT,
            // В bundle указываемключ-значение данных, которые хотим передать
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "LoginFragment  created")
        )
    }
}