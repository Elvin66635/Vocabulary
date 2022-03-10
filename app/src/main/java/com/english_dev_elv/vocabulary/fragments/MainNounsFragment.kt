package com.english_dev_elv.vocabulary.fragments

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.english_dev_elv.vocabulary.R
import com.english_dev_elv.vocabulary.adapter.TopicAdapter
import com.english_dev_elv.vocabulary.network.API
import com.english_dev_elv.vocabulary.repository.TopicsRepository
import com.english_dev_elv.vocabulary.viewmodel.TopicViewModel
import com.english_dev_elv.vocabulary.viewmodel.TopicViewModelFactory
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Environment
import android.view.*
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.english_dev_elv.vocabulary.databinding.FragmentMainNounsBinding
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val TAG = "MainQuizFragment"

class MainNounsFragment : Fragment() {
    private var binding: FragmentMainNounsBinding? = null
    lateinit var viewModel: TopicViewModel
    private lateinit var adapter: TopicAdapter
    private val retrofitService = API.getInstance()
    var saveResult = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentMainNounsBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        saveResult = sharedPreferences.getInt("result", 0)
        Log.d(TAG, "$saveResult")

        binding?.mainProgressbar?.max = 500

        binding?.mainProgressbar?.progress = saveResult

        binding?.recyclerViewTitles?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = TopicAdapter(TopicAdapter.OnClickListener { topic ->

            val dataBundle = Bundle()
            dataBundle.putString("image", topic.image)
            dataBundle.putString("title", topic.title)
            dataBundle.putString("question", topic.question)
            dataBundle.putInt("saved_result", saveResult)
            dataBundle.putParcelableArrayList("list", topic.quizDetails)

            val dialog = MaterialDialog(requireContext())
                .noAutoDismiss()
                .customView(R.layout.layout_checking_buttons)

            dialog.findViewById<Button>(R.id.testBtn).setOnClickListener {
                val dataBundle = Bundle()
                dataBundle.putString("image", topic.image)
                dataBundle.putString("title", topic.title)
                dataBundle.putString("question", topic.question)
                dataBundle.putInt("saved_result", saveResult)
                dataBundle.putParcelableArrayList("list", topic.quizDetails)
                findNavController().navigate(
                    R.id.action_mainQuizFragment_to_quizDetailFragment,
                    dataBundle
                )

                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.theoryBtn).setOnClickListener {
                findNavController().navigate(
                    R.id.action_mainQuizFragment_to_theoryFragment,
                    dataBundle
                )
                dialog.dismiss()
            }
            dialog.window?.setGravity(Gravity.BOTTOM)
            dialog.show()
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        })
        drawLayout()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = activity?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NET_CAPABILITY_INTERNET))
    }

    private fun drawLayout() {
        if (isNetworkAvailable()) {
            loadData()
        } else {
            findNavController().navigate(
                R.id.action_mainQuizFragment_to_noConnection,
            )
        }
    }


    private fun loadData() {

        val offlineInterceptor: Interceptor = (object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                val offlineCacheTime = 60
                if (!isNetWorkAvailable(activity!!.applicationContext)) {
                    request = request.newBuilder()
                        .header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + offlineCacheTime
                        )
                        .build()
                }
                return chain.proceed(request)
            }
        })

        val httpCacheDirectory = File(Environment.getExternalStorageDirectory(), "111cache");
        val cacheSize = 10L * 1024 * 1024; // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize)
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(netInterceptor)
            .addInterceptor(offlineInterceptor)
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("https://gank.io/api/data/Android/10/1")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("aaa", response.body!!.string())
            }
        })

        viewModel =
            ViewModelProvider(this, TopicViewModelFactory(TopicsRepository(retrofitService)))
                .get(TopicViewModel::class.java)

        viewModel.topics.observe(requireActivity(), {
            adapter.submitList(it)
            binding?.recyclerViewTitles?.scrollToPosition(9)
            binding?.recyclerViewTitles?.adapter = adapter
        })

        viewModel.errorMessage.observe(requireActivity(), {
            Log.d(TAG, "Error $it")
        })

        viewModel.getTopics()
    }
}

var netInterceptor: Interceptor = (object : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val onlineCacheTime = 30
        return response.newBuilder()
            .header("Cache-Control", "public, max-age=" + onlineCacheTime)
            .removeHeader("Pragma")
            .build()
    }

})

fun isNetWorkAvailable(context: Context): Boolean {
    var isAvailable = false
    val cm: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = cm.activeNetworkInfo
    if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
        isAvailable = true
    }
    return isAvailable
}
