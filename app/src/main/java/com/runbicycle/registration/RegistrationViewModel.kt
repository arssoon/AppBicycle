package com.runbicycle.registration

import androidx.lifecycle.ViewModel
import com.runbicycle.data.User
import com.runbicycle.repository.FirebaseRepo

class RegistrationViewModel: ViewModel() {
    private val repository = FirebaseRepo()

    fun createNewUser(user: User) {
        repository.createNewUser(user)
    }

}