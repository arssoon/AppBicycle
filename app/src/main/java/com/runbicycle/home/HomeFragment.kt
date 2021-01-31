package com.runbicycle.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.runbicycle.BaseFragment
import com.runbicycle.R
import com.runbicycle.data.MemberBicycle
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), OnMemberItemLongClick {
    private val homeVm by viewModels<HomeViewModel>()
    private val adapter = MemberAdapter(this)
    private val auth = FirebaseAuth.getInstance()
    private var member: MemberBicycle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_action -> {
                auth.signOut()
                requireActivity().finish()
            }
        }
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView_home.layoutManager = LinearLayoutManager(requireContext())
        recyclerView_home.adapter = adapter

        deleteBT.setOnClickListener {
            member?.let { it1 -> homeVm.deleteMemberBicycle(it1) }

            Toast.makeText(requireContext(), member?.name, Toast.LENGTH_LONG)
                .show()
        }

        payPersonBT.setOnClickListener {
            member?.let { it1 -> homeVm.addPayPerson(it1) }
            Toast.makeText(requireContext(), member?.name, Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeVm.memberBicycle.observe(viewLifecycleOwner) { list ->
            adapter.setMember(list)
        }
    }

    override fun onMemberLongClick(memberBicycle: MemberBicycle, position: Int) {
        setMembers(memberBicycle)
        Toast.makeText(requireContext(), memberBicycle.name, Toast.LENGTH_LONG)
            .show()
    }

    fun setMembers(memberBicycle: MemberBicycle) {
        member = memberBicycle
    }
}