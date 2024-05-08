package com.countrydelight.countrydelightlogger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.countrydelight.countrydelightlogger.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        with(binding) {
            navigateToSecondFragmentBtn.setOnClickListener {
                findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment())
            }
            navigateToPreviousActivityBtn.setOnClickListener {
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}