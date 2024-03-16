package com.sleepee.bondoman.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sleepee.bondoman.data.model.Transaction
import com.sleepee.bondoman.databinding.TransactionCardBinding

class TransactionsAdapter(private val transactions: List<Transaction>): RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun getItemCount() = transactions.size

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

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: Transaction)
    }

    inner class ViewHolder(private val binding: TransactionCardBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transaction: Transaction) {
            binding.title.text = transaction.title
            binding.price.text = "Rp. ${transaction.amount.toString()}"
            binding.category.text = transaction.category
            if (binding.category.text.toString() == "Pemasukan") {
                binding.category.setTextColor(Color.parseColor("#87A922"))
                binding.price.setTextColor(Color.parseColor("#87A922"))
            } else {
                binding.category.setTextColor(Color.parseColor("#FF004D"))
                binding.price.setTextColor(Color.parseColor("#FF004D"))
            }
            binding.location.text = transaction.location
            binding.date.text = transaction.date
        }
    }

    interface TransactionGetListener {
        fun getTransaction(transaction: Transaction)
    }

    interface TransactionUpdatedListener {
        fun onTransactionUpdated(transaction: Transaction)
    }

    interface TransactionDeletedListener {
        fun onTransactionDeleted(transaction: Transaction)
    }



}