package id.ac.ukdw.iotakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*

class MonitorActivity : AppCompatActivity() {


    val uidListGate = ArrayList<String>()
    val employeeList = ArrayList<Employee>()
    val timeStampList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)
        val txt1 = findViewById<TextView>(R.id.txt1)
        val txt2 = findViewById<TextView>(R.id.txt2)
        val txt3 = findViewById<TextView>(R.id.txt3)
        var btn = findViewById<Button>(R.id.button2)

        btn.setOnClickListener {
            getData()
            getEmployee()
            val username = getIntent().getStringExtra("username")
            val i = Intent(this, AdminActivity::class.java)
            i.putExtra("username", username.toString())
            startActivity(i);
        }
    }
    fun getEmployee(){
        val firebase = FirebaseDatabase.getInstance().getReference("Access")

        firebase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val uidListAccess = ArrayList<String>()

                //mencari masing masing uid yang berbeda kemudian memasukkan kedalam uidlist
                for (data in dataSnapshot.children) {
                    uidListAccess.add(data.key.toString())
                }

                //mencari masing masing timestamp yang berbeda kemudian memasukkan kedalam uidlist
                for (i in 0 .. (uidListAccess.size-1)) {
                    val Driver = dataSnapshot.child("${uidListAccess[i]}/Driver").getValue().toString()
                    val Registration = dataSnapshot.child("${uidListAccess[i]}/Registration").getValue().toString()
                    val Transport = dataSnapshot.child("${uidListAccess[i]}/Transport").getValue().toString()
                    val isAuthorized = dataSnapshot.child("${uidListAccess[i]}/isAuthorized").getValue().toString()

                    employeeList.add(Employee(uidListAccess[i],Driver, Registration,Transport,isAuthorized))

                    Log.d("Driver", "id: ${employeeList[i].Driver}")
                    Log.d("uid", "id: ${employeeList[i].uid}")
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getData(){


        val firebase = FirebaseDatabase.getInstance().getReference("Gate1")

        firebase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                //mencari masing masing uid yang berbeda kemudian memasukkan kedalam uidlist
                for (data in dataSnapshot.children) {
                    uidListGate.add(data.key.toString())
                }

                //mencari masing masing timestamp yang berbeda kemudian memasukkan kedalam uidlist
                for (i in 0 .. (uidListGate.size-1)) {
                    val gate1 = dataSnapshot.child("${uidListGate[i]}/TimeStamp").getValue().toString()
                    timeStampList.add(gate1)
                    Log.d("lol", "id: ${uidListGate[i]} Timestamp: ${gate1}")
                }

//                //debugging tools
//                for (i in 0 .. (uidListGate.size-1)) {
//                    Log.d("uidList", "id: ${uidListGate[i]}")
//                }
//                for (i in 0 .. (timeStampList.size-1)) {
//                    Log.d("timestamp", "id: ${timeStampList[i]}")
//                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}