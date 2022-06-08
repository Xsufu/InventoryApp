package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao): ViewModel(){

    //Получение всех товаров в качестве LiveData списка
    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData()

    /**
     * Обращение к DAO для добавления объекта в БД
     *
     * @param item добавляемый объект
     */
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    /**
     * Получение новой записи о товаре
     *
     * @param itemName название товара
     * @param itemPrice стоимость товара
     * @param itemCount доступное на складе количество
     *
     * @return Экземпляр класса [Item] с заданными параметрами
     */
    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item (
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    /**
     * Передача экземпляра класса в функцию добавления в БД
     *
     * @param itemName название товара
     * @param itemPrice стоимость товара
     * @param itemCount доступное на складе количество
     */
    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    /**
     * Проверка на заполенность полей
     *
     * @param itemName название товара
     * @param itemPrice стоимость товара
     * @param itemCount доступное на складе количество
     *
     * @return true если все поля заполнены, иначе false
     */
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    /**
     * Получение товара по ID
     *
     * @param id ID товара
     * @return сведения о товаре в виде LiveData объекта
     */
    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItem(id).asLiveData()
    }

    /**
     * Вызов запроса на обновление
     *
     * @param item обновляемый объект
     */
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    /**
     * Продажа одного товара и обновление таблицы
     *
     * @param item продаваемый товар
     */
    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    /**
     * Проверка на наличие товара в наличии
     *
     * @param item проверяемый товар
     * @return true если товар в наличии, иначе false
     */
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }
}

class InventoryViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        /**
         * Проверяем modelClass, если совпадает с классом InventoryViewModel,
         * возвращаем его экземпляр. Иначе создаём исключение
         */
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }

}