package com.example.clothingecsite_30.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.clothingecsite_30.R
import com.example.clothingecsite_30.databinding.FragmentFirstBinding

/**
 * トップ画面
 */
class TopFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ログインボタン押下時
        binding.buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_TopFragment_to_LoginFragment)
        }

        // 会員登録ボタン押下時
        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_TopFragment_to_RegisterFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}