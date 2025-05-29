package com.example.harsh

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val apiService = Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val repository = UserRepository(apiService)
        viewModel = ViewModelProvider(
            this, ViewModelFactory(repository)
        )[UserViewModel::class.java]

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is UserState.Idle -> {
                    }

                    is UserState.Loading -> showLoading()
                    is UserState.Users -> {
                        showUsers(state.users.results)
                    }

                    is UserState.Error -> {
                        showError(state.error)
                    }
                }
            }
        }

        val userDao = DatabaseProvider.getDatabase(this).userDao()

        if (isInternetAvailable(this)) {
            viewModel.handleIntent(UserIntent.FetchUsers)
        } else {
            lifecycleScope.launch {
                val users = userDao.getAll()
                if (users != null) {
                    showUsers(users)
                }
            }
        }

    }

    private fun showLoading() {
    }

    private fun showUsers(users: MutableList<User>) {
        val adapter = UserAdapter(users, this, lifecycleScope)
        recyclerView.adapter = adapter
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}
