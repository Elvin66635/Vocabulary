package com.english_dev_elv.vocabulary

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.english_dev_elv.vocabulary.R
import com.english_dev_elv.vocabulary.databinding.FragmentNoConnectionBinding

class NoConnectionFragment : Fragment() {
    private var _binding: FragmentNoConnectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.updateInternetBtn.setOnClickListener {
           checkInternetConnection()
        }



    }
    private fun isNetworkAvailable(): Boolean {
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
    }

    private fun checkInternetConnection() {
        if (isNetworkAvailable()) {
            findNavController().navigate(
                R.id.action_noConnection_to_mainQuizFragment,
            )

        } else {
            Toast.makeText(requireContext(), "Проверьте интернет-соедененеие",Toast.LENGTH_SHORT).show()
        }
    }

}
