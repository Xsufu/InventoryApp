package com.example.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch

class InventoryViewModel(private val itemDao: ItemDao): ViewModel(){
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