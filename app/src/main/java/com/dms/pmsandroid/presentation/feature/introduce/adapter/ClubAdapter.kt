package com.dms.pmsandroid.presentation.feature.introduce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dms.pmsandroid.databinding.ItemClubBinding
import com.dms.pmsandroid.presentation.feature.introduce.model.ClubModel
import com.dms.pmsandroid.presentation.feature.introduce.viewmodel.IntroduceClubViewModel

class ClubAdapter(private val viewModel: IntroduceClubViewModel) :
    RecyclerView.Adapter<ClubAdapter.ClubViewHolder>() {
    private var clubList = ArrayList<ClubModel>()

    inner class ClubViewHolder(private val binding: ItemClubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.name = clubList[position].clubName
            binding.picture = clubList[position].pictureUrl
            binding.vm = viewModel
            binding.executePendingBindings()
            binding.notifyChange()
        }
    }

    override fun getItemCount(): Int {
        return clubList.size
    }

    fun setItem(clubs: List<ClubModel>) {
        this.clubList = clubs as ArrayList<ClubModel>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClubViewHolder {
        val binding = ItemClubBinding.inflate(LayoutInflater.from(parent.context))
        return ClubViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClubViewHolder, position: Int) {
        holder.bind(position)
    }

}