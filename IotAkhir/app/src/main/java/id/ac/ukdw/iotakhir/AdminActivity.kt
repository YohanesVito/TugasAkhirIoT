package id.ac.ukdw.iotakhir

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class AdminActivity : AppCompatActivity() {

    lateinit var ref : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

//        val txt = findViewById<TextView>(R.id.textView)
//        txt.text = username
        //ref = FirebaseDatabase.getInstance("https://tugas-akhir-iot-b307d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Access")
        var btnSave = findViewById<Button>(R.id.button)

        btnSave.setOnClickListener {
            savedata()
            Toast.makeText(this,"berhasil masukkan data",Toast.LENGTH_SHORT).show()

        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun savedata() {
        val username = getIntent().getStringExtra("username")
        var driver = findViewById<EditText>(R.id.edtDriver)
        var transport = findViewById<EditText>(R.id.edtTrasport)
        var id = findViewById<EditText>(R.id.edtId)
        val UID = id.text.toString()
        val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = time.format(Date()).toString()
        var regis = currentDate+" "+"by "+" "+username
        FirebaseDatabase.getInstance("https://tugas-akhir-iot-b307d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Access/"+UID+"/Driver").setValue(driver.text.toString())
        FirebaseDatabase.getInstance("https://tugas-akhir-iot-b307d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Access/"+UID+"/Transport").setValue(transport.text.toString())
        FirebaseDatabase.getInstance("https://tugas-akhir-iot-b307d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Access/"+UID+"/isAuthorized").setValue("true")
        FirebaseDatabase.getInstance("https://tugas-akhir-iot-b307d-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Access/"+UID+"/Registration").setValue(regis)


    }

}