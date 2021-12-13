package id.ac.ukdw.iotakhir

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var et_user_name = findViewById<EditText>(R.id.et_user_name)
        var et_password = findViewById<EditText>(R.id.et_password)
        var btn_submit = findViewById<Button>(R.id.btn_submit)

        btn_submit.setOnClickListener {
            val user_name = et_user_name.text.toString()
            val password = et_password.text.toString()
            if (password == "admin"){
                val i = Intent(this, MonitorActivity::class.java)
                i.putExtra("username", user_name.toString())
                startActivity(i);
            }
            else{
                Toast.makeText(this,"Wrong Password",Toast.LENGTH_SHORT).show()
            }

        }
    }
}