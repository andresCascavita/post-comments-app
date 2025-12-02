package com.unipiloto.myapplication.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unipiloto.myapplication.data.local.dao.CommentDao
import com.unipiloto.myapplication.data.local.dao.PostDao
import com.unipiloto.myapplication.data.local.entity.CommentEntity
import com.unipiloto.myapplication.data.local.entity.PostEntity

@Database(
    entities = [PostEntity::class, CommentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "post_comments_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}