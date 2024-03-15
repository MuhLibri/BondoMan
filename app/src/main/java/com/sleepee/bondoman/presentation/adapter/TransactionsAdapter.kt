package com.sleepee.bondoman.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sleepee.bondoman.data.model.Transaction
import com.sleepee.bondoman.databinding.TransactionCardBinding

class TransactionsAdapter(private val transactions: List<Transaction>): RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    override fun getItemCount() = transactions.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TransactionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    inner class ViewHolder(private val binding: TransactionCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.title.text = transaction.title
            binding.price.text = transaction.amount.toString()
            binding.category.text = transaction.category
            binding.location.text = transaction.location
            binding.date.text = transaction.date
        }
    }



}