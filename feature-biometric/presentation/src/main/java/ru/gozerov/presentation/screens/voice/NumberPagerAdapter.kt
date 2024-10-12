package ru.gozerov.presentation.screens.voice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.presentation.databinding.ItemNumberBinding

class NumberPagerAdapter : RecyclerView.Adapter<NumberPagerAdapter.NumberViewHolder>() {

    var data: List<SelectableNumber> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NumberViewHolder(ItemNumberBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class NumberViewHolder(private val binding: ItemNumberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(number: SelectableNumber) {
            binding.root.text = number.number.toString()
            binding.root.textSize = if (number.isSelected) 160f else 112f
        }

    }

}
