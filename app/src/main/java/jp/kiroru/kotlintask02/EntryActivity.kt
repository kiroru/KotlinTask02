package jp.kiroru.kotlintask02

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import jp.kiroru.kotlintask02.databinding.ActivityEntryBinding

class EntryActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_ENTRY = 100
        const val REQUEST_CODE_EDIT  = 101
    }

    private lateinit var binding: ActivityEntryBinding

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
        binding = ActivityEntryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mid = intent.getLongExtra("mid", 0L)
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")

        binding.editText1.setText(title)
        binding.editText2.setText(description)
        binding.buttonOK.setOnClickListener {
            val data = Intent()
            data.putExtra("mid", mid)
            data.putExtra("title", binding.editText1.text.toString())
            data.putExtra("description", binding.editText2.text.toString())
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        binding.buttonCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.editText1.addTextChangedListener(object: MyTextAdapter() {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateUI()
            }
        })
        binding.editText2.addTextChangedListener(object: MyTextAdapter() {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateUI()
            }
        })

        updateUI()
    }

    fun updateUI() {
        binding.buttonOK.isEnabled =
            binding.editText1.text.isNotEmpty() && binding.editText2.text.isNotEmpty()
    }
}