package com.example.clothingecsite_30.view

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.data.authentication.LoggedInUserView
import com.example.clothingecsite_30.util.textWatcher.CustomTextWatcher
import com.example.clothingecsite_30.util.textWatcher.CustomTextWatcherListener
import com.example.clothingecsite_30.viewModel.authentication.RegisterViewModel
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.FragmentRegisterBinding
import com.example.clothingecsite_30.viewModel.address.AddressViewModel
import com.example.clothingecsite_30.viewModel.address.AddressViewModelFactory
import com.example.clothingecsite_30.viewModel.authentication.RegisterViewModelFactory
import java.util.*


class RegisterFragment : Fragment(), CustomTextWatcherListener {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var addressViewModel: AddressViewModel
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var profileImageUri: Uri? = null

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            openImage(it)
            binding.ivProfileUserImage.setImageURI(it)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewModel =
            ViewModelProvider(this, RegisterViewModelFactory())[RegisterViewModel::class.java]
        addressViewModel = ViewModelProvider(
            requireActivity(), AddressViewModelFactory()
        )[AddressViewModel::class.java]

        val loadingProgressBar = binding.loading

        val userNameEditText = binding.registerNameView
        val emailEditText = binding.registerEmailView
        val addressEditText = binding.registerAddressView
        val prefectureCityEditText = binding.registerPrefectureCityView
        val mansionEditText = binding.registerMansionView
        val birthDayEditText = binding.registerBirthDayView
        val birthMonthEditText = binding.registerBirthMonthView
        val birthYearEditText = binding.registerBirthYearView
        val passwordEditText = binding.registerPasswordView
        val oneMorePasswordEditText = binding.registerOneMorePasswordView
        val genderSelectBox = binding.RegisterGenderGroup
        val registerBtn = binding.btnRegister
        val consentCheckBox = binding.consentCheck

        val scrollView = binding.scrollRegister

        var selectGender: String? = null

        var isConsentChecked = false
        consentCheckBox.setOnClickListener {
            isConsentChecked = consentCheckBox.isChecked
            registerViewModel.isConsentCheckValid(isConsentChecked)
            if (isConsentChecked) {
                binding.errorConsentCheck.text = ""
            }
        }

        enterTextChangedListener(
            userNameEditText,
            emailEditText,
            addressEditText,
            birthDayEditText,
            birthMonthEditText,
            birthYearEditText,
            passwordEditText,
            oneMorePasswordEditText
        )

        binding.registerBirthDayBtn.setOnClickListener {
            showDatePickerDialog(requireView())
        }
        genderSelectBox.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.gender_man -> {
                    selectGender = "男性"
                    binding.errorGender.text = ""
                }
                R.id.gender_woman -> {
                    selectGender = "女性"
                    binding.errorGender.text = ""
                }
                R.id.gender_other -> {
                    selectGender = "その他"
                    binding.errorGender.text = ""
                }
            }
        }
        registerViewModel.registerFormState.observe(viewLifecycleOwner,
            Observer { registerFormState ->
                if (registerFormState == null) {
                    return@Observer
                }
                registerFormState.usernameError?.let {
                    userNameEditText.error = getString(it)
                }
                registerFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
                registerFormState.oneMorePasswordError?.let {
                    oneMorePasswordEditText.error = getString(it)
                }
                registerFormState.birthDayError?.let {
                    birthDayEditText.error = getString(it)
                }
                registerFormState.birthMonthError?.let {
                    birthMonthEditText.error = getString(it)
                }
                registerFormState.birthYearError?.let {
                    birthYearEditText.error = getString(it)
                }
                registerFormState.mailAddressError?.let {
                    emailEditText.error = getString(it)
                }
                registerFormState.zipcodeError?.let {
                    addressEditText.error = getString(it)
                }
                registerFormState.consentCheckError?.let {
                    binding.errorConsentCheck.text = getString(it)
                }
                registerFormState.cannotRegisterError?.let {
                    scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_UP) }
                    loadingProgressBar.visibility = View.GONE
                    binding.errorCannotRegister.text = getString(it)
                }
                registerFormState.genderError?.let {
                    binding.errorGender.text = getString(it)
                }
                registerFormState.matchPassError?.let {
                    binding.errorMatchPassCheck.text = getString(it)
                }
            })

        registerViewModel.registerResult.observe(viewLifecycleOwner, Observer { loginResult ->
            loginResult ?: return@Observer
            loadingProgressBar.visibility = View.GONE
            scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_UP) }
            loginResult.error?.let {
                showLoginFailed(it)
            }
            loginResult.success?.let {
                startActivity(Intent(this.requireContext(), MainActivity::class.java))
                updateUiWithUser(it)
            }
        })

        binding.ivProfileUserImage.setOnClickListener {
            resultLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.addressFetchBtn.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            addressViewModel.fetchAddress(binding.registerAddressView.text.toString().toInt())
        }


        addressViewModel.address.observe(viewLifecycleOwner) {
            binding.loading.visibility = View.GONE
            if (it == null) {
                Toast.makeText(
                    requireContext(), "入力された住所は存在しませんでした", Toast.LENGTH_LONG
                ).show()
                (prefectureCityEditText as TextView).text = ""
                (addressEditText as TextView).text = ""
            } else {
                (prefectureCityEditText as TextView).text =
                    "${it.prefecture}${it.city}${it.address}"
                (addressEditText as TextView).text = it.zipcode.toString()
            }
        }

        // ログインボタン押下時の処理
        registerBtn.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_UP) }
            registerViewModel.register(
                profileImageUri,
                userNameEditText.text.toString(),
                emailEditText.text.toString(),
                addressEditText.text.toString(),
                prefectureCityEditText.text.toString(),
                mansionEditText.text.toString(),
                birthYearEditText.text.toString(),
                birthMonthEditText.text.toString(),
                birthDayEditText.text.toString(),
                passwordEditText.text.toString(),
                oneMorePasswordEditText.text.toString(),
                isConsentChecked,
                selectGender
            )
        }


        super.onViewCreated(view, savedInstanceState)
    }

    //ログイン成功時のトースト
    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    // ログイン失敗時のトースト
    private fun showLoginFailed(@StringRes errorString: Int) {
        binding.registerEmailView.error = getString(errorString)
    }

    private fun enterTextChangedListener(
        userNameEditText: EditText,
        emailEditText: EditText,
        addressEditText: EditText,
        birthDayEditText: EditText,
        birthMonthEditText: EditText,
        birthYearEditText: EditText,
        passwordEditText: EditText,
        oneMorePasswordEditText: EditText,
    ) {
        userNameEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this, this@RegisterFragment
                )
            )
        }
        emailEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this, this@RegisterFragment
                )
            )
        }
        addressEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this, this@RegisterFragment
                )
            )
        }
        birthDayEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this, this@RegisterFragment
                )
            )
        }
        birthMonthEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this, this@RegisterFragment
                )
            )
        }
        birthYearEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this, this@RegisterFragment
                )
            )
        }
        passwordEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this, this@RegisterFragment
                )
            )
        }
        oneMorePasswordEditText.apply {
            addTextChangedListener(
                CustomTextWatcher(
                    this, this@RegisterFragment
                )
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun afterTextChanged(view: View, p0: Editable?) {
        val inputStr: String = p0.toString()
        when (view.id) {
            R.id.registerNameView -> {
                registerViewModel.isUserNameValid(inputStr)
            }
            R.id.registerEmailView -> {
                registerViewModel.isEmailValid(inputStr)
            }
            R.id.registerAddressView -> {
                registerViewModel.isAddressValid(inputStr)
            }
            R.id.registerBirthYearView -> {
                registerViewModel.isBirthYearValid(inputStr)
            }
            R.id.registerBirthMonthView -> {
                registerViewModel.isBirthMonthValid(inputStr)
            }
            R.id.registerBirthDayView -> {
                registerViewModel.isBirthDayValid(inputStr)
            }
            R.id.registerPasswordView -> {
                registerViewModel.isPasswordValid(inputStr)
            }
        }
    }

    override fun beforeTextChanged(view: View, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(view: View, p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDatePickerDialog(v: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val calendar = DatePickerDialog(
            requireContext(),
            R.style.DatePickerDialog_Spinner,
            DialogDateButtonClickLister(),
            year,
            month,
            day
        )
        calendar.show()
    }

    private fun openImage(uri: Uri?) {
        profileImageUri = uri
    }

    private inner class DialogDateButtonClickLister : DatePickerDialog.OnDateSetListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onDateSet(p0: DatePicker, year: Int, month: Int, day: Int) {
            val yearText = activity?.findViewById<EditText>(R.id.registerBirthYearView) as TextView
            yearText.text = year.toString()
            binding.registerBirthYearView.error = null

            val monthText =
                activity?.findViewById<EditText>(R.id.registerBirthMonthView) as TextView
            monthText.text = (month + 1).toString()
            binding.registerBirthMonthView.error = null

            val dayText = activity?.findViewById<EditText>(R.id.registerBirthDayView) as TextView
            dayText.text = day.toString()
            binding.registerBirthDayView.error = null
        }
    }
}
