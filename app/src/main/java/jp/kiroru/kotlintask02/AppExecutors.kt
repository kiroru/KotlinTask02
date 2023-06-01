package jp.kiroru.kotlintask02

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors private constructor(val databaseExecutor: Executor, val mainExecutor: Executor) {

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {

        private var instance: AppExecutors? = null

        @Synchronized
        fun newInstance(): AppExecutors {
            if (instance == null) {
                instance = AppExecutors(Executors.newSingleThreadExecutor(), MainThreadExecutor())
            }
            return instance as AppExecutors
        }
    }
}