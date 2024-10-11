package ru.gozerov.presentation.screens.face_rules_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.gozerov.domain.models.ListRuleItem
import ru.gozerov.presentation.databinding.RuleItemBinding

class FaceRulesAdapter :
    RecyclerView.Adapter<FaceRulesAdapter.ViewHolder>() {

    var items: List<ListRuleItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RuleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
    inner class ViewHolder(private val binding: RuleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListRuleItem) {
            binding.header.text = item.header
            binding.desc.text = item.desc

        }
    }

}
