package `in`.innovateria.wa_statussaver.Models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.innovateria.wa_statussaver.Data.StatusRepo

class StatusViewModelFactory(private val repo: StatusRepo):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatusViewModel(repo) as T
    }
}