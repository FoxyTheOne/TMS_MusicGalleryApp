package com.tms.lesson01.musicgalleryapplication.mvvm.dataModel.network

/**
 * hw02. 1. SRP - Принцип единственной ответственности. Для работы с запросами на сервер о музыке имеем отдельный класс. Обработка данных и возвращение результата
 * hw02. 2. Методы пишем на основе OCP - Принцип открытости/закрытости. Программные сущности (классы, модули, ф-ции и проч.) должны быть открыты для расширения, но закрыты для изменений
 * hw02. 3. ISP - Принцип разделения интерфейса. Наследуемся от более мелкого, специфического интерфейса
 * hw02. 4. LSP - Принцип подстановки Барбары Лисков. Подклассы должны переопределять методы базового класса так, чтобы не нарушалась функциональность с точки зрения клиента
 */
class NetworkMusicServiceModel : INetworkMusicService {
    override fun getFavouriteMusic(): List<Any> {
        TODO("Not yet implemented")
    }

    override fun updateFavouriteMusic(data: Any) {
//        TODO("Not yet implemented")
    }

}