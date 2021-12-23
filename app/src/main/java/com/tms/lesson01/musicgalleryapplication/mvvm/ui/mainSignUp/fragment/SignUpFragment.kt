package com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainSignUp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.tms.lesson01.musicgalleryapplication.R
import com.tms.lesson01.musicgalleryapplication.mvvm.MainActivity
import com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.localStorage.appSharedPreference.AppSharedPreferences
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.playlist.fragment.PlaylistsListFragment
import com.tms.lesson01.musicgalleryapplication.mvvm.ui.mainSignUp.SignUpViewModel

/**
 * hw02. 1. SRP - Принцип единственной ответственности. Для обновления UI имеем отдельный класс
 *
 * hw03. Переводим наше приложение на фрагменты.
 */
class SignUpFragment : Fragment() {
    // Переменные класса
    private lateinit var viewModel: SignUpViewModel
    private lateinit var frameLayout: FrameLayout
    private lateinit var progressCircular: ProgressBar
    private lateinit var nameField: TextInputLayout
    private lateinit var emailField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var confirmPasswordField: TextInputLayout
    private lateinit var buttonSignUp: AppCompatButton
    private lateinit var checkBoxRememberLoginAndPassword: AppCompatCheckBox

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Сюда копируем код из onCreate в Activity. Перед findViewById нужно добавить "view."
        // В методе subscribeOnLiveData() (если такой есть):
        // Вместо "this" в методе .observe(), как было в Activity, пишем "viewLifecycleOwner"
        // Там, где мы в Activity передавали context, писали "this". Во фрагменте нужно писать "context"
        // Однако, context может быть null. Если нужно значение точно не null(подчеркнет красным), пишем "requireContext()"

        // Сразу начинаем отправлять данные по ключу NAVIGATION_EVENT
        sendNavigationEvents()

        // Инициализируем viewModel, чтобы работать с ViewModel (согласно примеру с сайта)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        // Вызовем наш метод, к-рый будет отправлять нам SharedPreferences из фрагмента
        viewModel.setSharedPreferences(AppSharedPreferences.getInstance(requireContext())) // Вызываем наш статический метод для экземпляра класса AppSharedPreferences
        viewModel.getStoredData()

        // Оглашаем наши локальные переменные
        // Кнопки
        buttonSignUp = view.findViewById(R.id.buttonSignUp)
        checkBoxRememberLoginAndPassword = view.findViewById(R.id.checkBox_remember)
        // Переменные для отображения прогресса
        frameLayout = view.findViewById(R.id.frameLayout)
        progressCircular = view.findViewById(R.id.progressCircular)
        // Переменные для полей ввода
        nameField = view.findViewById(R.id.nameField)
        emailField = view.findViewById(R.id.emailField)
        passwordField = view.findViewById(R.id.passwordField)
        confirmPasswordField = view.findViewById(R.id.confirmPasswordField)

//        // Восстанавливаем значения при повороте экрана:
//        restoreValues()

        // Метод для всех наших listeners
        initListeners()
        // Вызываем метод, чтобы подписаться на события
        subscribeOnLiveData()
    }

    private fun initListeners() {
        // Сохраняем введенные в поля значения для последнующего восстановления при необходимости:
        nameField.editText?.addTextChangedListener(){
            viewModel.nameLiveData.value = it.toString()
        }
        emailField.editText?.addTextChangedListener(){
            viewModel.emailLiveData.value = it.toString()
        }
        passwordField.editText?.addTextChangedListener(){
            viewModel.passwordLiveData.value = it.toString()
        }
        confirmPasswordField.editText?.addTextChangedListener(){
            viewModel.confirmPasswordLiveData.value = it.toString()
        }
        // Определяем действие по клику на кнопку:
        buttonSignUp.setOnClickListener {
            val nameText = nameField.editText?.text.toString()
            val emailText = emailField.editText?.text.toString()
            val passwordText = passwordField.editText?.text.toString()
            val confirmPasswordText = confirmPasswordField.editText?.text.toString()

            viewModel.onSignUpClicked(nameText, emailText, passwordText, confirmPasswordText)
        }
        checkBoxRememberLoginAndPassword.setOnCheckedChangeListener{ _, selected ->
//            println("CHECK_BOX_SELECTED = $selected")
            viewModel.setRememberLoginAndPasswordSelectedOrNot(selected) // Передаём наш isSelected (при нажатии на кнопку) в наш listener
        }
    }

    /**
     * В случае успеха - перевести на страницу Success, как на занятии
     * В случае ошибки - вывести Toast, как на занятии
     */
    // Подвисываемся, чтобы слышать события из LiveData (в ViewModelMediator). Если какое-то из этих событий случится, будет вызван соответствующий метод, описанный в observe у этого события
    private fun subscribeOnLiveData() {
        viewModel.isSignUpSuccessLiveData.observe(viewLifecycleOwner, {
//            val intent = Intent(context, SuccessLoginActivity::class.java)
//            startActivity(intent)
            // Открываем фрагмент:
            (activity as MainActivity).openFragment(PlaylistsListFragment(), doClearBackStack = true) // Очищаем стек после Login screen
            //(activity as MainActivity) - так мы преобразовали activity в MainActivity. Мы можем так сделать, т.к. у нас будет 1 activity и множество фрагментов, соответственно мы знаем, что MainActivity - точно наша activity
        })
        viewModel.isSignUpFailureLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Something wrong with your data. Please, try again!", Toast.LENGTH_LONG).show()
        })
        viewModel.showProgressLiveData.observe(viewLifecycleOwner, {
            showProgress()
        })
        viewModel.hideProgressLiveData.observe(viewLifecycleOwner, {
            hideProgress()
        })
        viewModel.checkBoxRememberLoginAndPasswordLiveData.observe(viewLifecycleOwner, { isSelected ->
            checkBoxRememberLoginAndPassword.isChecked = isSelected
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
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "SignUpFragment  created")
        )
    }
}