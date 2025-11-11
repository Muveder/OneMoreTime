package com.example.onemoretime.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.onemoretime.model.Post
import com.example.onemoretime.model.Usuario

@Database(entities = [Post::class, Usuario::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "one_more_time_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it attempts
                     * to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
