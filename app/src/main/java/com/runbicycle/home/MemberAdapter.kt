package com.runbicycle.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.runbicycle.R
import com.runbicycle.data.MemberBicycle

class MemberAdapter(private val listener: OnMemberItemLongClick): RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    private val memeberList = ArrayList<MemberBicycle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_row, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        bindData(holder)
    }

    override fun getItemCount(): Int {
        return memeberList.size
    }

    fun setMember(list: List<MemberBicycle>) {
        memeberList.clear()
        memeberList.addAll(list)
        notifyDataSetChanged()
    }

    fun removeMemberPay(member: MemberBicycle, position: Int) {
        memeberList.remove(member)
        notifyItemRemoved(position)
    }


    private fun bindData(holder: MemberViewHolder) {
        val name = holder.itemView.findViewById<TextView>(R.id.profileName)
        val surname = holder.itemView.findViewById<TextView>(R.id.profileSurname)
        val email = holder.itemView.findViewById<TextView>(R.id.profileEmail)
        val image = holder.itemView.findViewById<ImageView>(R.id.profileImage)

        name.text = memeberList[holder.adapterPosition].name
        surname.text = memeberList[holder.adapterPosition].surname
        email.text = memeberList[holder.adapterPosition].email
        Glide.with(holder.itemView)
            .load(memeberList[holder.adapterPosition].image)
            .circleCrop()
            .into(image)
    }

    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnLongClickListener() {
                listener.onMemberLongClick(memeberList[adapterPosition], adapterPosition)
                true
            }
        }
    }
}

interface OnMemberItemLongClick {
    fun onMemberLongClick(memberBicycle: MemberBicycle, position: Int)
}
