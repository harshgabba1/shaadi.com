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
    //private lateinit var userDao : UserDao
    var currentPage = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //userDao = AppDatabase.getDatabase(this)?.userDao()!!
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
//                        if (currentPage > 1)
//                        {
//                            val adapter =  UserAdapter(state.users.results, this@MainActivity, lifecycleScope)
//                            if (currentPage > 1) {
//                                adapter.addUsers(state.users.results)
//                            }
//                        }
                        showUsers(state.users.results)
                    }
                    is UserState.Error -> {showError(state.error)}
                }
            }
        }

        if (isInternetAvailable(this)) {
            viewModel.handleIntent(UserIntent.FetchUsers)
        } else {
//            lifecycleScope.launch {
//                val users = userDao?.getAllUsers()
//                if (users != null) {
//                    showUsers(users)
//                }
//            }
        }

//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                val visibleItemCount = layoutManager.childCount
//                val totalItemCount = layoutManager.itemCount
//                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
//                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2 && totalItemCount >= 10) {
//                    currentPage++
//                    viewModel.handleIntent(UserIntent.FetchUsers)
//                }
//            }
//        })

    }

    private fun showLoading() {
    }

    private fun showUsers(users: MutableList<User>) {
        //val adapter = userDao?.let { UserAdapter(users, this, it, lifecycleScope) }
        val adapter =  UserAdapter(users, this, lifecycleScope)
        recyclerView.adapter = adapter
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
