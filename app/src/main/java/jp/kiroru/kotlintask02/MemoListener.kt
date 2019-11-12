package jp.kiroru.kotlintask02

enum class ACTION {
    SETUP,
    INSERT,
    UPDATE,
    DELETE
}

interface MemoListener {
    fun notifyChanged(action: ACTION, items: List<Memo>)
}