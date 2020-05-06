package jp.kiroru.kotlintask02

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemoDao {

    @Query("SELECT mid, title, description FROM memo ORDER BY mid")
    fun findAll(): List<Memo>

    @Query("SELECT COUNT(mid) FROM memo")
    fun count(): Int

    @Insert
    fun insert(memo: Memo)

    @Query("UPDATE memo SET title = :title, description = :description WHERE mid = :mid")
    fun update(mid: Long, title: String, description: String)

    @Query("DELETE FROM memo WHERE mid = :mid")
    fun delete(mid: Long)

    @Query("DELETE FROM memo")
    fun deleteAll()

}