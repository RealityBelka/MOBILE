package ru.gozerov.data.repositories

import ru.gozerov.domain.models.ListRuleItem
import ru.gozerov.domain.repositories.RuleRepository
import javax.inject.Inject

class RuleRepositoryImpl @Inject constructor(): RuleRepository {

    override fun getList() : List<ListRuleItem> {
        val list = mutableListOf<ListRuleItem>()
        list.add(ListRuleItem(
            "Лицо смотрит в камеру",
            "Фото соответствует возрасту. Лицо открыто,\nвзгляд направлен в объектив камеры.\nВыражение - нейтральное",
            "взгляд прямо",
            "часть лица закрыта",
            "https://i.ibb.co/gWrFJTm/Frame-44.png",
            "https://i.pinimg.com/originals/51/7d/c7/517dc74ecb60eaa708757134f4a62540.jpg"))
        list.add(ListRuleItem(
            "Наличие очков, как в документе",
            "Если на фото вы в очках,\nсфотографируйтесь в них. Проверьте, чтобы не\nбыло бликов, а оправа не скрывала глаза", "как в документе",
            "есть отличия",
            "https://i.ibb.co/gWrFJTm/Frame-44.png",
            "https://i.ibb.co/fHWHvdB/Frame-45.png"))
//        list.add(ListRuleItem("Светлый фон и дневной свет", "Фон однотонный, без посторонних предметов\nи теней. Хорошее освещение: лучше сделать фото при дневном свете ", "светлый фон", "пестрый фон", "light", "nonlight"))
//        list.add(ListRuleItem("Без головных уборов и\nслужебной формы", "Если ваша религия обязывает покрывать\nголову, проследите, чтобы овал лица был чётко\nвиден", "религиозная\nодежда", "головной убор", "muslim", "hat"))

        return list
    }

}