package jp.kiroru.kotlintask02


import android.content.Context
import androidx.room.Room


object MemoManager {

    private val TAG = MemoManager::class.java.simpleName

    private var db: MemoDatabase? = null
    private var listener: MemoListener? = null

    fun setup(context: Context, listener: MemoListener) {
        this.listener = listener

        if (db == null) {
            //db = Room.databaseBuilder(context.applicationContext, MemoDatabase::class.java, MemoDatabase.DATABASE_NAME).build()
            db = Room.databaseBuilder(context.applicationContext, MemoDatabase::class.java, MemoDatabase.DATABASE_NAME).build()

            AppExecutors.newInstance().databaseExecutor.execute {

                // 最初の一回だけ実行する。
                if (isFirst(context)) {
                    db?.memoDao()?.deleteAll()

                    // dummy data
                    for (index in 1..10) {
                        val memo = Memo(0L, "title_$index", "description_$index")
                        db?.memoDao()?.insert(memo)
                    }

                    markFirst(context)
                }

                val items = findAll()
                listener?.notifyChanged(ACTION.SETUP, items)
            }
        }
    }

    private fun findAll(): List<Memo> {
        return db?.memoDao()?.findAll() ?: listOf()
    }

    fun insert(title: String, description: String) {
        AppExecutors.newInstance().databaseExecutor.execute {
            val memo = Memo(0L, title, description)
            db?.memoDao()?.insert(memo)
            val items = findAll()
            listener?.notifyChanged(ACTION.INSERT, items)
        }
    }

    fun update(mid: Long, title: String, description: String) {
        AppExecutors.newInstance().databaseExecutor.execute {
            db?.memoDao()?.update(mid, title, description)
            val items = findAll()
            listener?.notifyChanged(ACTION.UPDATE, items)
        }
    }

    fun delete(mid: Long) {
        AppExecutors.newInstance().databaseExecutor.execute {
            db?.memoDao()?.delete(mid)
            val items = findAll()
            listener?.notifyChanged(ACTION.DELETE, items)
        }
    }
}