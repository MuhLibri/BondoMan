package com.sleepee.bondoman.presentation.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.sleepee.bondoman.data.model.Transaction
import com.sleepee.bondoman.databinding.TransactionCardBinding
import com.sleepee.bondoman.presentation.activity.BaseActivity
import com.sleepee.bondoman.presentation.fragment.TransactionFragment

class TransactionsAdapter(private val listener: LocationButtonListener): RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var transactions = emptyList<Transaction>()
    private var data : Int = 0

    inner class ViewHolder(private val binding: TransactionCardBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transaction: Transaction) {
            binding.title.text = transaction.title
            binding.price.text = "Rp. ${transaction.amount.toString()}"
            binding.category.text = transaction.category
            if (binding.category.text.toString() == "Pemasukan") {
                binding.category.setTextColor(Color.parseColor("#306844"))
                binding.price.setTextColor(Color.parseColor("#306844"))
            } else {
                binding.category.setTextColor(Color.parseColor("#FF004D"))
                binding.price.setTextColor(Color.parseColor("#FF004D"))
            }
            binding.location.text = transaction.location
            binding.date.text = transaction.date

            if (transaction.locationLink != null)
                binding.locationButton.setOnClickListener {
                    listener.onLocationButtonPressed(transaction)
                }
        }

    }
    interface LocationButtonListener {
        fun onLocationButtonPressed(transaction: Transaction)
    }

    override fun getItemCount() = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TransactionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactions[position])
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, transactions[position])
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(transactionList: List<Transaction>) {
        this.transactions = transactionList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataCount(data: Int) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: Transaction)
    }



}