package com.runbicycle.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.runbicycle.data.MemberBicycle
import com.runbicycle.repository.FirebaseRepo

class ProfileViewModel : ViewModel() {
    private val repository = FirebaseRepo()
    val user = repository.getUserData()

    val payPerson = user.switchMap {
        repository.getPayPerson(it.memberPay)
    }

    fun removePayPerson(member: MemberBicycle) {
        repository.removePayPerson(member)
    }

    fun editProfileData(map: Map<String, String>) {
        repository.editProfileData(map)
    }

    fun uploadUserPhoto(bytes: ByteArray) {
        repository.uploadUserPhoto(bytes)
    }
}