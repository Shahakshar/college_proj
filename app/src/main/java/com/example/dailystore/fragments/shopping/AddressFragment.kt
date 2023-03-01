package com.example.dailystore.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.dailystore.data.Address
import com.example.dailystore.databinding.FragmentAddressBinding
import com.example.dailystore.utils.Resource
import com.example.dailystore.viewmodels.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment: Fragment() {
    private lateinit var binding: FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when(it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE

                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnAddNewAddress.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString()
                val phone = edPhone.text.toString()
                val state = edState.text.toString()
                val city = edCity.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()

                val address = Address(addressTitle, fullName, street, phone, city, state)
                viewModel.addAddress(address)
                clearInput()
            }
        }
    }

    private fun clearInput() {
        binding.apply {
            edAddressTitle.text.clear()
            edCity.text.clear()
            edState.text.clear()
            edFullName.text.clear()
            edPhone.text.clear()
            edStreet.text.clear()
        }
    }
}