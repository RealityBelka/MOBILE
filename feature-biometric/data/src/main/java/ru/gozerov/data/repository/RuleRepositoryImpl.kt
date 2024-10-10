package ru.gozerov.data.repository

import ru.gozerov.domain.models.ListRuleItem
import ru.gozerov.domain.repository.RuleRepository

class RuleRepositoryImpl : RuleRepository {
    override fun getList() : List<ListRuleItem> {
        val list = mutableListOf<ListRuleItem>()
        list.add(ListRuleItem("Лицо смотрит в камеру", "Фото соответствует возрасту. Лицо открыто,\nвзгляд направлен в объектив камеры.\nВыражение - нейтральное"))
        list.add(ListRuleItem("очко", "жопа"))

        return list;
    }
}