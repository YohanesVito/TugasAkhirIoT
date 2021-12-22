package id.ac.ukdw.iotakhir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CheckpointAdapter (val listCheckpoint: ArrayList<Checkpoint>): RecyclerView.Adapter<CheckpointAdapter.CheckpointHolder>() {
    class CheckpointHolder(val v: View) : RecyclerView.ViewHolder(v) {
        var checkpoint: Checkpoint? = null

        fun bindView(checkpoint: Checkpoint) {
            this.checkpoint = checkpoint
            v.findViewById<TextView>(R.id.tv_noGate).text = checkpoint.gateNum
            v.findViewById<TextView>(R.id.tv_uid).text = checkpoint.uid
            v.findViewById<TextView>(R.id.tv_timestamp).text = checkpoint.TimeStamp

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckpointAdapter.CheckpointHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_checkpoint, parent, false)
        return CheckpointHolder(v)
    }

    //memilih file layout XML yang akan dijadikan container
    override fun onBindViewHolder(holder: CheckpointAdapter.CheckpointHolder, position: Int) {
        //memasang data ke dalam file layout XML yang telah dipilih
        holder.bindView(listCheckpoint[position])
    }

    override fun getItemCount(): Int {
        //mengembalikan jumlah item yang terdapat pada RecyclerView
        return listCheckpoint.size
    }
}

