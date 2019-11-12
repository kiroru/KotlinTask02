package jp.kiroru.kotlintask02


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Memo::class], version = 1)
abstract class MemoDatabase : RoomDatabase() {

    companion object {
        val DATABASE_NAME = "memo"
    }

    abstract fun memoDao(): MemoDao

}