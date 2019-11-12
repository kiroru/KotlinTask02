package jp.kiroru.kotlintask02

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

class EntryActivity : AppCompatActivity() {

    companion object {
        val REQUESTCODE_ENTRY = 100
        val REQUESTCODE_EDIT  = 101
    }

    lateinit var ed1: EditText
    lateinit var ed2: EditText
    lateinit var buttonOK: Button
    lateinit var buttonCancel: Button

    abstract class MyTextAdapter : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            // Do nothing
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // Do nothing
        }

        abstract override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        val mid = intent.getLongExtra("mid", 0L)
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")

        ed1 = findViewById<EditText>(R.id.editText1)
        ed1.setText(title)

        ed2 = findViewById<EditText>(R.id.editText2)
        ed2.setText(description)

        buttonOK = findViewById<Button>(R.id.buttonOK)
        buttonOK.setOnClickListener {
            val data = Intent()
            data.putExtra("mid", mid)
            data.putExtra("title", ed1.text.toString())
            data.putExtra("description", ed2.text.toString())
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        buttonCancel = findViewById<Button>(R.id.buttonCancel)
        buttonCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        ed1.addTextChangedListener(object: MyTextAdapter() {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateUI()
            }
        })
        ed2.addTextChangedListener(object: MyTextAdapter() {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateUI()
            }
        })

        updateUI()
    }

    fun updateUI() {
        buttonOK.isEnabled = ed1.text.isNotEmpty() && ed2.text.isNotEmpty()
    }
}
