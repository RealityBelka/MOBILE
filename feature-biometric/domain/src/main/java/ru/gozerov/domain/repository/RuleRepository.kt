package ru.gozerov.domain.repository

import ru.gozerov.domain.models.ListRuleItem

interface RuleRepository {
    fun getList() : List<ListRuleItem>
}