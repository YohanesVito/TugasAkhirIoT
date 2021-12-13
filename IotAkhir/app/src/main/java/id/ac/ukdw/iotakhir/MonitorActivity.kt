package id.ac.ukdw.iotakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*

class MonitorActivity : AppCompatActivity() {
    lateinit var ref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)
        val txt1 = findViewById<TextView>(R.id.txt1)
        val txt2 = findViewById<TextView>(R.id.txt2)
        val txt3 = findViewById<TextView>(R.id.txt3)
        var btn = findViewById<Button>(R.id.button2)
        ref = FirebaseDatabase.getInstance("https://tugas-akhir-iot-b307d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()

        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val gate1 = snapshot.child("Gate1").getValue().toString()
                txt1.setText(gate1)
                val gate2 = snapshot.child("Gate2").getValue().toString()
                txt2.setText(gate2)
                val gate3 = snapshot.child("Gate3").getValue().toString()
                txt3.setText(gate3)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        btn.setOnClickListener {
            val username = getIntent().getStringExtra("username")
            val i = Intent(this, AdminActivity::class.java)
            i.putExtra("username", username.toString())
            startActivity(i);
        }
    }
}