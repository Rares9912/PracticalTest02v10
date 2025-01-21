package ro.pub.cs.systems.eim.practicaltest02v10

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessaging
import org.w3c.dom.Text

class MyFirebaseService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Message received: ${remoteMessage.data}")
    }
}

class PracticalTest02MainActivityv102 : AppCompatActivity() {
    private lateinit var subscribeButton: Button
    private lateinit var unsubscribeButton: Button
    private lateinit var topicText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test02_main_activityv102)

        subscribeButton = findViewById(R.id.button3)
        unsubscribeButton = findViewById(R.id.button4)

        topicText = findViewById(R.id.textView3)
        topicText.text = "testTopic"

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "FCM Token: $token")
            } else {
                Log.e("FCM", "Failed to get token", task.exception)
            }
        }

        subscribeButton.setOnClickListener {
            val topic = topicText.text.toString().trim()
            if (topic.isNotEmpty()) {
                FirebaseMessaging.getInstance().subscribeToTopic(topic)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Subscribed to $topic", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Subscription failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter a topic name", Toast.LENGTH_SHORT).show()
            }
        }

        // Dezabonare de la topic
        unsubscribeButton.setOnClickListener {
            val topic = topicText.text.toString().trim()
            if (topic.isNotEmpty()) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Unsubscribed from $topic", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Unsubscription failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter a topic name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}