package id.ac.ukdw.iotakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class MonitorActivity : AppCompatActivity() {


   // val gateList = ArrayList<String>()
  //  val timeStampList = ArrayList<String>()

   // val employeeList = ArrayList<Employee>()
    val checkpointList = ArrayList<Checkpoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoractivity)

        val tvGate1 = findViewById<TextView>(R.id.tv_noGate1)
        val tvuid1 = findViewById<TextView>(R.id.tv_uid1)
        val tvtimestamp1 = findViewById<TextView>(R.id.tv_timestamp1)

        val tvGate2 = findViewById<TextView>(R.id.tv_noGate2)
        val tvuid2 = findViewById<TextView>(R.id.tv_uid2)
        val tvtimestamp2 = findViewById<TextView>(R.id.tv_timestamp2)

        val tvGate3 = findViewById<TextView>(R.id.tv_noGate3)
        val tvuid3 = findViewById<TextView>(R.id.tv_uid3)
        val tvtimestamp3 = findViewById<TextView>(R.id.tv_timestamp3)


        val firebase = FirebaseDatabase.getInstance().getReference("Gate/Gate1")
        firebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val uidList = ArrayList<String>()

                //mencari masing masing uid yang berbeda kemudian memasukkan kedalam uidlist
                for (data in dataSnapshot.children) {
                    uidList.add(data.key.toString())
                }

                //mencari masing masing timestamp dari uid yang berbeda
                for (i in 0 until uidList.size) {
                    val ts =
                        dataSnapshot.child("${uidList[i]}/TimeStamp").getValue().toString()

//                    timeStampList.add(ts)
                    Log.d("ts", "Timestamp: ${ts}")
                    //membuat objek Checkpoint
                    checkpointList.add(Checkpoint("Gate1","${uidList[i]}","${ts}"))

                    //debugging checkpoint disetiap gate
//                    Log.d("checkpoint", "gatenum: ${gateList[0]} id: ${uidList[i]} Timestamp: ${timeStampList[i]}")
//
//                    Log.d("time", "Timestamp: ${timeStampList[i]}")
//                    Log.d("uidListeCheck", "${uidList[i]}")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errorrrrrr", "${error}")
            }
        })

//        Timer("SettingUp", false).schedule(1000) {
//            getData()
//        }
        //mengambil jumlah employee
       // getEmployee()

        //mengambil jumlah gate
        //getGate()



        val btnBack = findViewById<Button>(R.id.bt_kembali)
        btnBack.setOnClickListener {
            val i = Intent(this, SecondActivity::class.java)
            startActivity(i);
        }



        val btnRefresh = findViewById<Button>(R.id.bt_refresh)
        btnRefresh.setOnClickListener {

            tvGate1.setText(checkpointList[0].gateNum.toString())
            tvuid1.setText(checkpointList[0].uid.toString())
            tvtimestamp1.setText(checkpointList[0].TimeStamp.toString())

            tvGate2.setText(checkpointList[1].gateNum.toString())
            tvuid2.setText(checkpointList[1].uid.toString())
            tvtimestamp2.setText(checkpointList[1].TimeStamp.toString())

            tvGate3.setText(checkpointList[2].gateNum.toString())
            tvuid3.setText(checkpointList[2].uid.toString())
            tvtimestamp3.setText(checkpointList[2].TimeStamp.toString())
        }
    }

    fun getRecycler(){

    }

//    fun getEmployee() {
//        val firebase = FirebaseDatabase.getInstance().getReference("Access")
//
//        firebase.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                val uidListAccess = ArrayList<String>()
//
//                //mencari masing masing uid yang berbeda kemudian memasukkan kedalam uidlist
//                for (data in dataSnapshot.children) {
//                    uidListAccess.add(data.key.toString())
//                }
//
//                //mencari masing masing uid yang berbeda kemudian membuat object Employee
//                for (i in 0..(uidListAccess.size - 1)) {
//                    val Driver =
//                        dataSnapshot.child("${uidListAccess[i]}/Driver").getValue()
//                            .toString()
//                    val Registration =
//                        dataSnapshot.child("${uidListAccess[i]}/Registration").getValue()
//                            .toString()
//                    val Transport =
//                        dataSnapshot.child("${uidListAccess[i]}/Transport").getValue()
//                            .toString()
//                    val isAuthorized =
//                        dataSnapshot.child("${uidListAccess[i]}/isAuthorized").getValue()
//                            .toString()
//
//                    employeeList.add(
//                        Employee(
//                            uidListAccess[i],
//                            Driver,
//                            Registration,
//                            Transport,
//                            isAuthorized
//                        )
//                    )
//
//                    //debugging Employee
//                    Log.d("Employee", "uid:${employeeList[i].uid} Driver:${employeeList[i].Driver} Registration:${employeeList[i].Registration} Transport:${employeeList[i].Transport} isAuthorized:${employeeList[i].isAuthorized}")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }

//    fun getData(gate: String) {
//
//        val firebase = FirebaseDatabase.getInstance().getReference("${gate}")
//        Log.d("saiki", "Gate: ${gate}")
//        firebase.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//
//                //mencari masing masing uid yang berbeda kemudian memasukkan kedalam uidlist
//                for (data in dataSnapshot.children) {
//                    uidList.add(data.key.toString())
//                }
//
//                //mencari masing masing timestamp dari uid yang berbeda
//                for (i in 0..(uidList.size - 1)) {
//                    val ts =
//                        dataSnapshot.child("${uidList[i]}/TimeStamp").getValue().toString()
//
//                    timeStampList.add(ts)
//                    Log.d("ts", "Timestamp: ${ts}")
//                    //membuat objek Checkpoint
//                    checkpointList.add(Checkpoint("${gate}","${uidList[i]}","${timeStampList[i]}"))
//
//                    //debugging checkpoint disetiap gate
//                    Log.d("checkpoint", "gatenum: ${gate} id: ${uidList[i]} Timestamp: ${timeStampList[i]}")
//
//                    Log.d("time", "Timestamp: ${timeStampList[i]}")
//                    Log.d("uidListeCheck", "${uidList[i]}")
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }
    fun getData() {


    }
//    fun getGate() {
//
//        val firebase = FirebaseDatabase.getInstance().getReference("Gate")
//
//        firebase.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                //mencari masing masing gate yang berbeda kemudian memasukkan kedalam gatelist
//                for (data in dataSnapshot.children) {
//                    gateList.add(data.key.toString())
//                }
//
//                //debugging jumlah gate yang ada
//                for (gate in gateList) {
//                   Log.d("Gate", "gate: ${gate}")
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
//    }

}