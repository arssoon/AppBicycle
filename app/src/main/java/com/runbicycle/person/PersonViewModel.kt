package com.runbicycle.person

import android.content.Context
import androidx.lifecycle.ViewModel
import com.runbicycle.repository.FirebaseRepo

class PersonViewModel: ViewModel()  {
    private val repository = FirebaseRepo()
    val user = repository.getUserData()

    fun addMemberBicycle(name: String, surname: String, email: String, image: String, context: Context) {
        repository.addMemberBicycle(name, surname, email, image, context)
    }
}