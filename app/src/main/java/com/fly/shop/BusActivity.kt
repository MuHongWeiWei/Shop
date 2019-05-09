package com.fly.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bus.*
import kotlinx.android.synthetic.main.row_bus.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.zip.Inflater

class BusActivity : AppCompatActivity() , AnkoLogger{
    var busAll: BusAll? = null
    val retrofit = Retrofit.Builder()
        .baseUrl("https://data.tycg.gov.tw/opendata/datalist/datasetMeta/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus)

        doAsync {
            val busService = retrofit.create(BusService::class.java)
            busAll = busService.listBus().execute().body()!!
            busAll?.datas?.forEach {
                info("${it.BusID}")
            }

            uiThread {
                recylcer.setHasFixedSize(true)
                recylcer.layoutManager = LinearLayoutManager(this@BusActivity)
                recylcer.adapter = BusAdapter()
            }
        }
    }

    inner class BusAdapter : RecyclerView.Adapter<BusAdapter.BusHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusHolder {
            var view = layoutInflater.inflate(R.layout.row_bus, parent, false)
            return BusHolder(view)
        }

        override fun getItemCount(): Int {
            return  busAll!!.datas.size
        }

        override fun onBindViewHolder(holder: BusHolder, position: Int) {
            var bus = busAll!!.datas.get(position)
            holder.bindView(bus)
        }


        inner class BusHolder(view : View) : RecyclerView.ViewHolder(view) {
            var busID : TextView = view.row_busID
            var routeID : TextView = view.row_routeID
            var speed : TextView = view.row_speed
            fun bindView(bus : Bus){
                busID.text = bus.BusID
                routeID.text = bus.RouteID
                speed.text = bus.Speed
            }
        }
    }
}

data class BusAll(
    val datas: List<Bus>
)

data class Bus(
    val BusID: String,
    val RouteID: String,
    val Speed: String
    )

interface BusService {
    @GET("download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed")
    fun listBus(): Call<BusAll>
}