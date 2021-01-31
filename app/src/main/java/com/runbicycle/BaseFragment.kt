package com.runbicycle

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import com.runbicycle.activities.BicycleActivity

abstract class BaseFragment : Fragment() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val transInflater = TransitionInflater.from(requireContext())
//        enterTransition = transInflater.inflateTransition(R.transition.slide_right)
//        exitTransition = transInflater.inflateTransition(R.transition.fade_out)
//    }

    protected fun startApp() {
        val intent = Intent(requireContext(), BicycleActivity::class.java)
            .apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        startActivity(intent)
    }
}