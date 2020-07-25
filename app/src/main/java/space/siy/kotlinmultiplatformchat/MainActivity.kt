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
        textView.text = createApplicationScreenMessage()
        fetchRemoteMessage {
            textView.text = it
        }
        val chat = ChatService("ws://192.168.2.127:8080")
        chat.onMessage = {
            Toast.makeText(this, it.content, Toast.LENGTH_LONG).show()
        }
        chat.connect()

    }
}
