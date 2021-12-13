package id.ac.ukdw.iotakhir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MonitorActivity : AppCompatActivity() {
    lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)
        ref = FirebaseDatabase.getInstance("https://tugas-akhir-iot-b307d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
    }
}