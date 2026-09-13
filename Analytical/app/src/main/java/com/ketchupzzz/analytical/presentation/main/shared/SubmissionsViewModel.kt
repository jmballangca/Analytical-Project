package com.ketchupzzz.analytical.presentation.main.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepository
import com.ketchupzzz.analytical.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class SubmissionsViewModel @Inject constructor(
     private val submissionRepository: SubmissionRepository
) : ViewModel() {
    private val _submissions = MutableLiveData<UiState<List<Submissions>>>()
    val submissions: LiveData<UiState<List<Submissions>>> = _submissions

    fun fetchSubmissions(id: String) {
        viewModelScope.launch {
            submissionRepository.getAllSubmissions(id) { uiState ->
                _submissions.postValue(uiState)
            }
        }
    }
}