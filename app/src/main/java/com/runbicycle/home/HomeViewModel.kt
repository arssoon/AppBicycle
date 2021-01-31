package com.runbicycle.home

import androidx.lifecycle.ViewModel
import com.runbicycle.data.MemberBicycle
import com.runbicycle.repository.FirebaseRepo

class HomeViewModel : ViewModel() {
    private val repository = FirebaseRepo()
    val memberBicycle = repository.getMembers()

    fun addPayPerson(member: MemberBicycle) {
        repository.addPayPerson(member)
    }

    fun deleteMemberBicycle(member: MemberBicycle) {
        repository.deleteMemberBicycle(member)
    }
}