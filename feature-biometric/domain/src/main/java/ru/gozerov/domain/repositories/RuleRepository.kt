package ru.gozerov.domain.repositories

import ru.gozerov.domain.models.ListRuleItem

interface RuleRepository {
    fun getList() : List<ListRuleItem>
}