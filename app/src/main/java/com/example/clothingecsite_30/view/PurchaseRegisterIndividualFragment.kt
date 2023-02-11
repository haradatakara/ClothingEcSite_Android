package com.example.clothingecsite_30.view

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.FragmentPurchaseRegisterIndividualBinding
import com.example.clothingecsite_30.enum.DeliveryMethod
import com.example.clothingecsite_30.viewModel.address.AddressViewModel
import com.example.clothingecsite_30.viewModel.address.AddressViewModelFactory
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModel
import com.example.clothingecsite_30.viewModel.authentication.LoginViewModelFactory
import com.example.clothingecsite_30.viewModel.purchaseComfirm.PurchaseRegisterViewModel
import com.example.clothingecsite_30.viewModel.purchaseComfirm.PurchaseRegisterViewModelFactory
import java.util.*

/**
 * 支払い情報入力ページ
 */
class PurchaseRegisterIndividualFragment : Fragment() {

    private lateinit var addressViewModel: AddressViewModel
    private lateinit var purchaseRegisterViewModel: PurchaseRegisterViewModel
    private lateinit var loginViewModel: LoginViewModel


    private var _binding: FragmentPurchaseRegisterIndividualBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPurchaseRegisterIndividualBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressViewModel = ViewModelProvider(
            requireActivity(), AddressViewModelFactory()
        )[AddressViewModel::class.java]
        loginViewModel = ViewModelProvider(
            requireActivity(), LoginViewModelFactory()
        )[LoginViewModel::class.java]
        purchaseRegisterViewModel = ViewModelProvider(
            requireActivity(), PurchaseRegisterViewModelFactory()
        )[PurchaseRegisterViewModel::class.java]

        //loginUser情報を取得
        loginViewModel.fetchLoginUser()
        //loginUser情報を取得してきた後の処置
        loginViewModel.loginUser.observe(viewLifecycleOwner) {
            addressViewModel.fetchAddress(it.address.toInt())
            (binding.registerNameView as TextView).text = it.name
            (binding.registerEmailView as TextView).text = it.email
        }

        binding.normalDelivery.isChecked = true
        binding.deliveryContainer.visibility = View.GONE
        binding.registerDeliveryDateBtn.visibility = View.GONE

        var deliveryMethod: DeliveryMethod? = DeliveryMethod.NORMAL
        binding.deliveryMethods.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.normal_delivery -> {
                    binding.deliveryContainer.visibility = View.GONE
                    binding.registerDeliveryDateBtn.visibility = View.GONE
                    deliveryMethod = DeliveryMethod.NORMAL
                }
                R.id.specify_delivery -> {
                    binding.deliveryContainer.visibility = View.VISIBLE
                    binding.registerDeliveryDateBtn.visibility = View.VISIBLE
                    deliveryMethod = DeliveryMethod.SPECIFY
                }
            }
        }

        addressViewModel.address.observe(viewLifecycleOwner) {
            binding.loading.visibility = View.GONE
            if (it == null) {
                Toast.makeText(
                    requireContext(), "入力された住所は存在しませんでした", Toast.LENGTH_LONG
                ).show()
                (binding.registerPrefectureCityView as TextView).text = ""
                (binding.registerAddressView as TextView).text = ""
            } else {
                (binding.registerPrefectureCityView as TextView).text =
                    "${it.prefecture}${it.city}${it.address}"
                (binding.registerAddressView as TextView).text = it.zipcode.toString()
            }
        }

        binding.addressFetchBtn.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            addressViewModel.fetchAddress(binding.registerAddressView.text.toString().toInt())
        }

        binding.registerDeliveryDateBtn.setOnClickListener {
            showDatePickerDialog(requireView())
        }

        binding.btnRegister.setOnClickListener {
            purchaseRegisterViewModel.register(
                binding.registerNameView.text.toString(),
                binding.registerEmailView.text.toString(),
                binding.registerAddressView.text.toString(),
                binding.registerPrefectureCityView.text.toString(),
                binding.registerMansionView.text.toString(),
                deliveryMethod,
                "${binding.registerDeliveryYearView}/${binding.registerDeliveryDateView}/ ${binding.registerDeliveryMonthView}"
            )
        }

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

        val maxCal = Calendar.getInstance()
        maxCal.add(Calendar.MONTH, 6) //1ヶ月後
        maxCal[Calendar.HOUR_OF_DAY] = maxCal.getMaximum(Calendar.HOUR_OF_DAY)
        maxCal[Calendar.MINUTE] = maxCal.getMaximum(Calendar.MINUTE)
        maxCal[Calendar.SECOND] = maxCal.getMaximum(Calendar.SECOND)
        maxCal[Calendar.MILLISECOND] = maxCal.getMaximum(Calendar.MILLISECOND)
        calendar.datePicker.maxDate = maxCal.timeInMillis

        val minCal = Calendar.getInstance() // 本日
        minCal.add(Calendar.DATE, 2);
        minCal[Calendar.HOUR_OF_DAY] = minCal.getMinimum(Calendar.HOUR_OF_DAY)
        minCal[Calendar.MINUTE] = minCal.getMinimum(Calendar.MINUTE)
        minCal[Calendar.SECOND] = minCal.getMinimum(Calendar.SECOND)
        minCal[Calendar.MILLISECOND] = minCal.getMinimum(Calendar.MILLISECOND)
        calendar.datePicker.minDate = minCal.timeInMillis

        calendar.show()
    }

    private inner class DialogDateButtonClickLister : DatePickerDialog.OnDateSetListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onDateSet(p0: DatePicker, year: Int, month: Int, day: Int) {
            (binding.registerDeliveryYearView as TextView).text = year.toString()
            binding.registerDeliveryYearView.error = null

            (binding.registerDeliveryMonthView as TextView).text = (month + 1).toString()
            binding.registerDeliveryMonthView.error = null

            (binding.registerDeliveryDateView as TextView).text = day.toString()
            binding.registerDeliveryDateView.error = null
        }
    }
}