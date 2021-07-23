package com.aristidevs.nuwelogin.ui.verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aristidevs.nuwelogin.core.Event
import com.aristidevs.nuwelogin.domain.SendEmailVerificationUseCase
import com.aristidevs.nuwelogin.domain.VerifyEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    val verifyEmailUseCase: VerifyEmailUseCase
) : ViewModel() {

    private val _navigateToVerifyAccount = MutableLiveData<Event<Boolean>>()
    val navigateToVerifyAccount: LiveData<Event<Boolean>>
        get() = _navigateToVerifyAccount

    init {
        viewModelScope.launch { sendEmailVerificationUseCase() }
        viewModelScope.launch {

            verifyEmailUseCase()
                .catch {
                    Timber.i("aristides 2 ${it.message}")
                }
                .collect { verification ->
                    Timber.i("aristides 3")
                    if(verification){
                        Timber.i("aristides 4")
                        _navigateToVerifyAccount.value = Event(verification)
                    }
                    Timber.i("aristides 5")
                }

        }
    }
}