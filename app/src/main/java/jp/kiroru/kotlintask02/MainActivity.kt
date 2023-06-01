package jp.kiroru.kotlintask02

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.kiroru.kotlintask02.databinding.ActivityMainBinding
import jp.kiroru.kotlintask02.databinding.CellMainBinding


interface ItemSelectionListener {
    fun requireItemEdit(position: Int)
    fun requireItemDelete(position: Int)
}

class MainActivity : AppCompatActivity(), ItemSelectionListener, MemoListener {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var items = mutableListOf<Memo>()
    private var adapter: MyAdapter? = null
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        MemoManager.setup(this, this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = MyAdapter(items, this)
        binding.recyclerView.adapter = adapter

        binding.floatingButton.setOnClickListener {
            val i = Intent(this, EntryActivity::class.java)
            startActivityForResult(i, EntryActivity.REQUEST_CODE_ENTRY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult: $requestCode")

        when (requestCode) {
            EntryActivity.REQUEST_CODE_ENTRY -> {
                Log.d(TAG, "REQUEST_CODE_ENTRY: $resultCode")
                if (resultCode == Activity.RESULT_OK) {
                    val mid = data?.getLongExtra("mid", 0L)
                    val title = data?.getStringExtra("title") ?: ""
                    val description = data?.getStringExtra("description") ?: ""

                    Log.d(TAG, "ENTRY $mid $title $description")
                    MemoManager.insert(title, description)
                }
            }
            EntryActivity.REQUEST_CODE_EDIT -> {
                Log.d(TAG, "REQUEST_CODE_EDIT: $resultCode")
                if (resultCode == Activity.RESULT_OK) {
                    val mid = data?.getLongExtra("mid", 0L) ?: 0L
                    val title = data?.getStringExtra("title") ?: ""
                    val description = data?.getStringExtra("description") ?: ""

                    Log.d(TAG, "EDIT $mid $title $description")
                    MemoManager.update(mid, title, description)
                }
            }
        }
    }

    override fun requireItemEdit(position: Int) {
        val item = items[position]
        val i = Intent(this, EntryActivity::class.java)
        i.putExtra("mid", item.mid)
        i.putExtra("title", item.title)
        i.putExtra("description", item.description)
        startActivityForResult(i, EntryActivity.REQUEST_CODE_EDIT)
    }

    override fun requireItemDelete(position: Int) {
        val item = items[position]
        AlertDialog.Builder(this)
            .setTitle("メモ削除")
            .setMessage("${item.title} を削除します。よろしいですか？")
            .setPositiveButton("OK") { _, _ ->
                MemoManager.delete(item.mid)
            }
            .setNegativeButton("削除しない", null)
            .show()
    }

    override fun notifyChanged(action: ACTION, items: List<Memo>) {
        AppExecutors.newInstance().mainExecutor.execute {
            val text = when (action) {
                ACTION.SETUP  -> "セットアップ完了しました。"
                ACTION.INSERT -> "メモを追加しました。"
                ACTION.UPDATE -> "メモを更新しました。"
                ACTION.DELETE -> "メモを削除しました。"
            }

            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

            this.items.clear()
            this.items.addAll(items)

            adapter?.notifyDataSetChanged()
        }
    }
}

class MyAdapter(private val items: List<Memo>, private val listener: ItemSelectionListener) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CellMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        val textView1 = holder.itemView.context.getString(R.string.textView1, item.mid, item.title)

        holder.tv1.text = textView1
        holder.tv2.text = item.description

        holder.b1.setOnClickListener {
            listener.requireItemEdit(position)     // Edit
        }

        holder.b2.setOnClickListener {
            listener.requireItemDelete(position)   // Delete
        }
    }

    class ViewHolder(cellMainBinding: CellMainBinding):
        RecyclerView.ViewHolder(cellMainBinding.root) {
        val tv1 = cellMainBinding.textView1
        val tv2 = cellMainBinding.textView2
        val b1 = cellMainBinding.buttonEdit
        val b2 = cellMainBinding.buttonDelete
    }
}