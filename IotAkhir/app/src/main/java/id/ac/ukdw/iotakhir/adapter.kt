package id.ac.ukdw.iotakhir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapter(private val userList:ArrayList<User>):RecyclerView.Adapter<adapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = userList[position]
        holder.id.text=currentitem.id
        holder.time.text = currentitem.TimeStamp

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val id : TextView = itemView.findViewById(R.id.txt1)
        val time : TextView = itemView.findViewById(R.id.txt2)

    }
}