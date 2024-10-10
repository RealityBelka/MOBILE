package ru.gozerov.domain.repository

import androidx.recyclerview.widget.RecyclerView.Adapter
import ru.gozerov.domain.models.ListRuleItem

interface RuleRepository {
    fun getList() : List<ListRuleItem>
}