package id.ac.ukdw.iotakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MonitorActivity : AppCompatActivity() {

    val uidList = ArrayList<String>()
    val gateList = ArrayList<String>()
    val timeStampList = ArrayList<String>()

    val employeeList = ArrayList<Employee>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoractivity)

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
            getData()
        }
    }

    fun getRecycler(){

    }

    fun getEmployee() {
        val firebase = FirebaseDatabase.getInstance().getReference("Access")

        firebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val uidListAccess = ArrayList<String>()

                //mencari masing masing uid yang berbeda kemudian memasukkan kedalam uidlist
                for (data in dataSnapshot.children) {
                    uidListAccess.add(data.key.toString())
                }

                //mencari masing masing uid yang berbeda kemudian membuat object Employee
                for (i in 0..(uidListAccess.size - 1)) {
                    val Driver =
                        dataSnapshot.child("${uidListAccess[i]}/Driver").getValue()
                            .toString()
                    val Registration =
                        dataSnapshot.child("${uidListAccess[i]}/Registration").getValue()
                            .toString()
                    val Transport =
                        dataSnapshot.child("${uidListAccess[i]}/Transport").getValue()
                            .toString()
                    val isAuthorized =
                        dataSnapshot.child("${uidListAccess[i]}/isAuthorized").getValue()
                            .toString()

                    employeeList.add(
                        Employee(
                            uidListAccess[i],
                            Driver,
                            Registration,
                            Transport,
                            isAuthorized
                        )
                    )

                    //debugging Employee
                    Log.d("Employee", "uid:${employeeList[i].uid} Driver:${employeeList[i].Driver} Registration:${employeeList[i].Registration} Transport:${employeeList[i].Transport} isAuthorized:${employeeList[i].isAuthorized}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

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

        val firebase = FirebaseDatabase.getInstance().getReference("Gate1")
        firebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val checkpointList = ArrayList<Checkpoint>()

                //mencari masing masing uid yang berbeda kemudian memasukkan kedalam uidlist
                for (data in dataSnapshot.children) {
                    uidList.add(data.key.toString())
                }

                //mencari masing masing timestamp dari uid yang berbeda
                for (i in 0..(uidList.size - 1)) {
                    val ts =
                        dataSnapshot.child("${uidList[i]}/TimeStamp").getValue().toString()

                    timeStampList.add(ts)
                    Log.d("ts", "Timestamp: ${ts}")
                    //membuat objek Checkpoint
                    checkpointList.add(Checkpoint("${gateList[0]}","${uidList[i]}","${timeStampList[i]}"))

                    //debugging checkpoint disetiap gate
//                    Log.d("checkpoint", "gatenum: ${gateList[0]} id: ${uidList[i]} Timestamp: ${timeStampList[i]}")
//
//                    Log.d("time", "Timestamp: ${timeStampList[i]}")
//                    Log.d("uidListeCheck", "${uidList[i]}")
                }
                val rvCheckpoint = findViewById<RecyclerView>(R.id.rvListCheckpoint)
                rvCheckpoint.layoutManager = LinearLayoutManager (rvCheckpoint.context)
                val adapter = CheckpointAdapter(checkpointList)
                rvCheckpoint.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("errorrrrrr", "${error}")
            }
        })
    }
    fun getGate() {

        val firebase = FirebaseDatabase.getInstance().getReference("Gate")

        firebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //mencari masing masing gate yang berbeda kemudian memasukkan kedalam gatelist
                for (data in dataSnapshot.children) {
                    gateList.add(data.key.toString())
                }

                //debugging jumlah gate yang ada
                for (gate in gateList) {
                   Log.d("Gate", "gate: ${gate}")
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}