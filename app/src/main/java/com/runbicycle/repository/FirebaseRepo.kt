package com.runbicycle.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.runbicycle.data.MemberBicycle
import com.runbicycle.data.User
import java.util.*

class FirebaseRepo {
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()
    private val REPO_DEBUG = "REPO_DEBUG"

    fun uploadUserPhoto(bytes: ByteArray) {
        storage.getReference("users")
            .child("${auth.currentUser!!.uid}.jpg")
            .putBytes(bytes)
            .addOnCompleteListener {
                Log.d(REPO_DEBUG, "COMPLETE UPLOAD IMAGE")
            }
            .addOnSuccessListener {
                getPhotoDownloadUrl(it.storage)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    private fun getPhotoDownloadUrl(storage: StorageReference) {
        storage.downloadUrl
            .addOnSuccessListener {
                updateUserPhoto(it.toString())
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    private fun updateUserPhoto(url: String?) {
        cloud.collection("users")
            .document(auth.currentUser!!.uid)
            .update("image", url)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "UPDATE USER IMAGE")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    fun getUserData(): LiveData<User> {
        val cloudResult = MutableLiveData<User>()
        val uid = auth.currentUser?.uid

        cloud.collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                cloudResult.postValue(user)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
        return cloudResult
    }

    fun getMembers(): LiveData<List<MemberBicycle>> {
        val cloudResult = MutableLiveData<List<MemberBicycle>>()

        cloud.collection("memberBicycle")
            .get()
            .addOnSuccessListener {
                val user = it.toObjects(MemberBicycle::class.java)
                cloudResult.postValue(user)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
        return cloudResult
    }

    fun getPayPerson(list: List<String>?): LiveData<List<MemberBicycle>> {
        val cloudResult = MutableLiveData<List<MemberBicycle>>()

        if (!list.isNullOrEmpty()) {
            cloud.collection("memberBicycle")
                .whereIn("id", list)
                .get()
                .addOnSuccessListener {
                    val resultList = it.toObjects(MemberBicycle::class.java)
                    cloudResult.postValue(resultList)
                }
                .addOnFailureListener {
                    Log.d(REPO_DEBUG, it.message.toString())
                }
        }
        return cloudResult
    }
    fun addPayPerson(member: MemberBicycle) {
        cloud.collection("users")
            .document(auth.currentUser?.uid!!)
            .update("memberPay", FieldValue.arrayUnion(member.id))
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Dodano do zapłaconych")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    fun addMemberBicycle(name: String, surname: String, email: String, image: String, context: Context) {
        val id = "${Date().time}"
        val data = MemberBicycle(id, name, email, image, surname)

        cloud.collection("memberBicycle")
            .document(id)
            .set(data)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Dodano uczestnika")
                Toast.makeText(context, "Dodano uczestnika", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    fun removePayPerson(member: MemberBicycle) {
        cloud.collection("users")
            .document(auth.currentUser?.uid!!)
            .update("memberPay", FieldValue.arrayRemove(member.id))
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Dodano do zapłaconych")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    fun deleteMemberBicycle(member: MemberBicycle) {
        cloud.collection("memberBicycle")
            .document(member.id!!)
            .delete()
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "usunieto")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }

    fun createNewUser(user: User) {
        cloud.collection("users")
            .document(user.uid!!)
            .set(user)
    }
    fun editProfileData(map: Map<String, String>) {
        cloud.collection("users")
            .document(auth.currentUser!!.uid)
            .update(map)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Zaktualizowano dane")
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
    }
}