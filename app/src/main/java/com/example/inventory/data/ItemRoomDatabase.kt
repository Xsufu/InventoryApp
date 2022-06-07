package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase: RoomDatabase() {
    /**
     * Функция возвращает DAO
     */
    abstract fun itemDao(): ItemDao
    companion object {
        /**
         * Переменная сохраняет ссылку на БД.
         *
         * Аннотация говорит, что переменная не кэшируется
         * и все записи и чтения будут выполняться в основную память.
         * Изменения, внесенные одним потоком в [INSTANCE], видны всем другим потокам.
         */
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(context: Context): ItemRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}