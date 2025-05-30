package com.example.harsh

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.harsh.UserViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repository) as T
    }
}
