package jp.kiroru.kotlintask02

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.cell_main.view.*


interface ItemSelectionListener {
    fun requireItemEdit(position: Int)
    fun requireItemDelete(position: Int)
}

class MainActivity : AppCompatActivity(), ItemSelectionListener, MemoListener {

    private val TAG = MainActivity::class.java.simpleName

    private var items = mutableListOf<Memo>()
    private var adapter: MyAdapter? = null
    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MemoManager.setup(this, this)

        val view = findViewById<RecyclerView>(R.id.recyclerView)

        view.layoutManager = LinearLayoutManager(this)

        adapter = MyAdapter(items, this)
        view.adapter = adapter

        val button = findViewById<FloatingActionButton>(R.id.floatingButton)
        button.setOnClickListener {
            val i = Intent(this, EntryActivity::class.java)
            startActivityForResult(i, EntryActivity.REQUESTCODE_ENTRY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult: ${requestCode}")

        when (requestCode) {
            EntryActivity.REQUESTCODE_ENTRY -> {
                Log.d(TAG, "REQUESTCODE_ENTRY: ${resultCode}")
                if (resultCode == Activity.RESULT_OK) {
                    val mid = data?.getLongExtra("mid", 0L)
                    val title = data?.getStringExtra("title") ?: ""
                    val description = data?.getStringExtra("description") ?: ""

                    Log.d(TAG, "ENTRY $mid $title $description")
                    MemoManager.insert(title, description)
                }
            }
            EntryActivity.REQUESTCODE_EDIT -> {
                Log.d(TAG, "REQUESTCODE_EDIT: ${resultCode}")
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
        startActivityForResult(i, EntryActivity.REQUESTCODE_EDIT)
    }

    override fun requireItemDelete(position: Int) {
        val item = items[position]
        AlertDialog.Builder(this)
            .setTitle("メモ削除")
            .setMessage("${item.title} を削除します。よろしいですか？")
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, i ->
                MemoManager.delete(item.mid)
            })
            .setNegativeButton("削除しない", null)
            .show()
    }

    override fun notifyChanged(action: ACTION, items: List<Memo>) {
        handler.post {
            var text = when (action) {
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_main, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.tv1.text = "(${item.mid}) ${item.title}"
        holder.tv2.text = item.description

        holder.b1.setOnClickListener {
            listener?.requireItemEdit(position)     // Edit
        }

        holder.b2.setOnClickListener {
            listener?.requireItemDelete(position)   // Delete
        }
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val tv1 = view.textView1
        val tv2 = view.textView2
        val b1 = view.buttonEdit
        val b2 = view.buttonDelete
    }
}