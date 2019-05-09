package com.fly.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_parking.*
import org.jetbrains.anko.*
import java.net.URL

class ParkingActivity : AppCompatActivity(), AnkoLogger{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)


        doAsync {
            val json = URL("https://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=f4cc0b12-86ac-40f9-8745-885bddc18f79&rid=0daad6e6-0632-44f5-bd25-5e1de1e9146f").readText()
            info(json)
            uiThread {
                tvResult.text = json
                alert {
                    message = "Got it"
                    title = "Alert"
                    okButton {
                        parseGSON(json)
                    }
                }.show()
            }
        }
    }

    private fun parseGSON(json: String) {
        val parking = Gson().fromJson<Parking>(json, Parking::class.java)
        parking.parkingLots.forEach {
            info {
                "${it.areaName}  ${it.totalSpace}"
            }
        }
    }
}

data class Parking(
    val parkingLots: List<ParkingLot>
)

data class ParkingLot(
    val address: String,
    val areaId: String,
    val areaName: String,
    val introduction: String,
    val parkId: String,
    val parkName: String,
    val payGuide: String,
    val surplusSpace: String,
    val totalSpace: Int,
    val wgsX: Double,
    val wgsY: Double
)
