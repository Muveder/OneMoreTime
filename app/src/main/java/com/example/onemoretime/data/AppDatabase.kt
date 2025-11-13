package com.example.onemoretime.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.onemoretime.model.Comment
import com.example.onemoretime.model.Post
import com.example.onemoretime.model.UserPostVote
import com.example.onemoretime.model.Usuario

@Database(entities = [Post::class, Usuario::class, Comment::class, UserPostVote::class], version = 8, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun voteDao(): VoteDao
    abstract fun commentDao(): CommentDao // <-- Añadido

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "one_more_time_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
