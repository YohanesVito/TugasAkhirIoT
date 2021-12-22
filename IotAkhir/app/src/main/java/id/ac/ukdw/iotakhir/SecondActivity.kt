package id.ac.ukdw.iotakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class SecondActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        val btnmonitor = findViewById<Button>(R.id.bt_lihatdata)
        val btntambahkartu = findViewById<Button>(R.id.bt_tambahdata)
        val btnlogout = findViewById<Button>(R.id.bt_logout)

        btnmonitor.setOnClickListener {
            val i = Intent(this, MonitorActivity::class.java);
            startActivity(i)
        }

        btntambahkartu.setOnClickListener {
            val username = getIntent().getStringExtra("username")
            val i = Intent(this, DaftarKartuActivity::class.java);
            i.putExtra("username", username.toString())
            startActivity(i)
        }

        btnlogout.setOnClickListener {
            Toast.makeText(this,"Log Out Berhasil!", Toast.LENGTH_SHORT).show()
            val i = Intent(this, MainActivity::class.java);
            startActivity(i)
        }
    }

}