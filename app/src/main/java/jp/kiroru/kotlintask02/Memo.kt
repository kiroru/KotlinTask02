package jp.kiroru.kotlintask02

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
class Memo(
    @field:PrimaryKey(autoGenerate = true) val mid: Long,
    val title: String,
    var description: String)
