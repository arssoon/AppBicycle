package com.runbicycle.person

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.storage.FirebaseStorage
import com.runbicycle.BaseFragment
import com.runbicycle.R
import kotlinx.android.synthetic.main.fragment_add_person.*
import java.util.*

class PersonFragment : BaseFragment() {
    private val PERSON_DEBUG = "PERSON_DEBUG"
    private val PICK_IMAGE_REQUEST = 22
    private var filePath: Uri? = null
    private val personVm by viewModels<PersonViewModel>()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.getReference()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_person, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_add.setOnClickListener {
            uploadImage()
        }
        image_add.setOnClickListener {
            SelectImage()
        }
    }

    private fun SelectImage(): Boolean {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent, "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            filePath = data?.data

            val bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath);
            val bitmapDrawable = BitmapDrawable(bitmap)
            image_add.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun uploadImage() {
        if (filePath == null) return
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        val filename = UUID.randomUUID().toString()
        val ref = storageReference.child("members/" + filename)
        ref.putFile(filePath!!)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Log.d(PERSON_DEBUG, "Udało się załadować", null)

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "File location: $it")
                    setMemberDataToFirebase(it)
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Log.d(PERSON_DEBUG, it.message.toString())
            }
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
            }
    }

    private fun setMemberDataToFirebase(it: Uri?) {
        var name = name_add.text.toString()
        var surname = surname_add.text.toString()
        var email = email_add.text.toString()
        // pobieranie tokena zdjęcia z firebase
        var image = "$it"

        personVm.addMemberBicycle(
            name,
            surname,
            email,
            image,
            requireActivity()
        )
    }
}