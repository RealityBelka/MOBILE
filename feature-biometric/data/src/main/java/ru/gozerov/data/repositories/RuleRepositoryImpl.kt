package ru.gozerov.data.repositories

import ru.gozerov.domain.models.ListRuleItem
import ru.gozerov.domain.repositories.RuleRepository
import javax.inject.Inject

class RuleRepositoryImpl @Inject constructor(): RuleRepository {

    override fun getList() : List<ListRuleItem> {
        val list = mutableListOf<ListRuleItem>()
        list.add(ListRuleItem(
            "Лицо смотрит в камеру",
            "Взгляд смотрит в камеру. Лицо ничего не\nзагораживает. Выражение - нейтральное",
            "взгляд прямо",
            "часть лица закрыта",
            "https://i.ibb.co/1dYQmy3/image-3.png",
            "https://i.ibb.co/zVWs3Zn/image-4.png"))
        list.add(ListRuleItem(
            "Наличие очков, как в документе",
            "Если на фото в документе вы в очках, то\nсфотографируйтесь в них. Проверьте, чтобы\nне было бликов, а оправа не перекрывала\nглаза",
            "как в документе",
            "есть отличия",
            "https://i.ibb.co/gWrFJTm/Frame-44.png",
            "https://i.ibb.co/fHWHvdB/Frame-45.png"))
        list.add(ListRuleItem(
            "Светлый фон и дневной свет",
            "Фон однотонный, без посторонних\nпредметов и теней. Хорошее освещение:\nлучше сделать фото при дневном свете ",
            "светлый фон",
            "разноцветный фон",
            "https://i.ibb.co/SdZvBfZ/image-1.png",
            "https://i.ibb.co/m6dT2ND/image-2.png"))
        list.add(ListRuleItem("Без головных уборов и\nслужебной формы",
            "Если ваша религия обязывает вас\nпокрывать голову, проследите, чтобы овал\nлица был четко виден",
            "религиозная одежда",
            "головной убор",
            "https://i.ibb.co/NT2fZYZ/butsonya.jpg",
            "https://i.ibb.co/RH9z1Qc/Rectangle-97.png"))

        return list
    }

}