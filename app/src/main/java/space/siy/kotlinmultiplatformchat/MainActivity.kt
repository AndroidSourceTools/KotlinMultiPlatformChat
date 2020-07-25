package space.siy.kotlinmultiplatformchat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val chat = ChatService("192.168.2.127", 8081, "AndroidUser")
        chat.onMessage = {
            textView.text = textView.text.toString() + "${it.user}: ${it.content}\n"
        }
        chat.onReady = {
            chat.send("Hello from android")
        }
        chat.connect()
        button.setOnClickListener {
            chat.send(editText.text.toString())
            editText.text.clear()
        }
    }
}
