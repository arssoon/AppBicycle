package com.runbicycle.profile

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.runbicycle.BaseFragment
import com.runbicycle.R
import com.runbicycle.data.MemberBicycle
import com.runbicycle.data.User
import com.runbicycle.home.MemberAdapter
import com.runbicycle.home.OnMemberItemLongClick
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream


class ProfileFragment : BaseFragment(), OnMemberItemLongClick {
    private val PROFILE_DEBUG = "PROFILE_BUG"
    private val REQUEST_CAPTURE_IMAGE = 123
    private val profileVm by viewModels<ProfileViewModel>()
    private val adapter = MemberAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSubmitDataClick()
        setupTakePictureClick()
        recyclerViewPayPerson.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewPayPerson.adapter = adapter
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileVm.user.observe(viewLifecycleOwner) { user ->
            bindUserData(user)
        }

        profileVm.payPerson.observe(viewLifecycleOwner) {list ->
            list.let {
                adapter.setMember(it)
            }
        }
    }

    override fun onMemberLongClick(memberBicycle: MemberBicycle, position: Int) {
        profileVm.removePayPerson(memberBicycle)
        adapter.removeMemberPay(memberBicycle, position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val image = data?.extras?.get("data") as Bitmap
            Glide.with(this)
                .load(image)
                .circleCrop()
                .into(userImage)

        userImage.setImageBitmap(image)
        val stream = ByteArrayOutputStream()
        val result = image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        if (result) profileVm.uploadUserPhoto(byteArray)
    }

    private fun setupTakePictureClick() {
        userImage.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE)
        } catch (exc: Exception) {
            Log.d(PROFILE_DEBUG, exc.message.toString())
        }

    }

    private fun bindUserData(user: User) {
        Log.d(PROFILE_DEBUG, user.toString())
        userNameET.setText(user.name)
        userSurnameET.setText(user.surname)
        userEmailET.setText(user.email)
        Glide.with(this)
            .load(user.image)
            .circleCrop()
            .into(userImage)
    }

    private fun setupSubmitDataClick() {
        submitDataProfile.setOnClickListener {
            val name = userNameET.text.trim().toString()
            val surname = userSurnameET.text.trim().toString()
            val map = mapOf("name" to name, "surname" to surname)
            profileVm.editProfileData(map)
        }
    }
}