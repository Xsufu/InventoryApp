package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    /**
     * Извлечение из БД объектов с указанным ID
     *
     * @param id идентификатор извлекаемого объекта
     */
    @Query("SELECT * FROM item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    /**
     * Извлечение всех объектов из БД.
     * Сортировка по названию в порядке возрастания
     */
    @Query("SELECT * FROM item ORDER BY name ASC")
    fun getItems(): Flow<List<Item>>

    /**
     * Функция добавления объекта в БД
     *
     * @param item добавляемый предмет
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    /**
     * Функция обновления данных объекта в БД
     *
     * @param item добавляемый предмет
     */
    @Update
    suspend fun update(item: Item)

    /**
     * Функция удаления объекта из БД
     *
     * @param item добавляемый предмет
     */
    @Delete
    suspend fun delete(item: Item)
}