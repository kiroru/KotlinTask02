package jp.kiroru.kotlintask02

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Memo::class], version = 1, exportSchema = false)
abstract class MemoDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "memo"
    }

    abstract fun memoDao(): MemoDao

}