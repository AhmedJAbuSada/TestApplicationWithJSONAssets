package com.teknasyontestapplication.ui.main

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.teknasyontestapplication.R
import com.teknasyontestapplication.databinding.ItemRowBinding
import com.teknasyontestapplication.data.model.Satellite


class SatellitesListAdapter(private val context: Context, private val interaction: Interaction) :
    RecyclerView.Adapter<SatellitesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, differ.currentList[position], position, interaction)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(models: List<Satellite>) {
        differ.submitList(models)
    }

    private val DIFF_CALLBACK: DiffUtil.ItemCallback<Satellite> =
        object : DiffUtil.ItemCallback<Satellite>() {
            override fun areItemsTheSame(
                oldProduct: Satellite,
                newProduct: Satellite
            ) = oldProduct.id == newProduct.id

            override fun areContentsTheSame(
                oldProduct: Satellite,
                newProduct: Satellite
            ) = oldProduct == newProduct
        }

    private val differ: AsyncListDiffer<Satellite> = AsyncListDiffer(this, DIFF_CALLBACK)


    class ViewHolder(private val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(context: Context, model: Satellite, position: Int, interaction: Interaction) {
            binding.card.setOnClickListener {
                interaction.clickItem(model, position)
            }

            binding.nameTextView.text = model.name ?: ""
            if (model.active == true) {
                binding.nameTextView.setTextColor(Color.BLACK)
                binding.statusTextView.setTextColor(Color.DKGRAY)
                binding.statusTextView.text = context.getString(R.string.active)
                binding.stateCircle.setColorFilter(
                    Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.nameTextView.setTextColor(Color.GRAY)
                binding.statusTextView.setTextColor(Color.GRAY)
                binding.statusTextView.text = context.getString(R.string.passive)
                binding.stateCircle.setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }

        }
    }

    interface Interaction {
        fun clickItem(item: Satellite, position: Int)
    }

}