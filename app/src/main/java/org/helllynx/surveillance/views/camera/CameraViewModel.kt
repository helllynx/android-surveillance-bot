package org.helllynx.surveillance.views.camera

import androidx.databinding.ObservableParcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.helllynx.surveillance.domain.UserDetails
import org.helllynx.surveillance.repository.UserDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val userDetailsRepository: UserDetailsRepository
) : ViewModel() {

    val userDetails = ObservableParcelable(UserDetails())

    fun getUserDetails(user: String) = userDetailsRepository.getUserDetails(user)

    fun refreshUserDetails(user: String) = viewModelScope.launch(Dispatchers.IO) {
        userDetailsRepository.refreshUserDetails(user)
    }

}