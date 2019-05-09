package com.fly.shop

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_function.view.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    var signup = false
    val auth = FirebaseAuth.getInstance()
    private val RC_SIGNUP = 50
    private val RC_NICK = 40
    val colors = arrayOf("Red", "Green", "Blue")
    val functions = listOf<String>("Camera", "Invite friend", "Parking", "Download coupons", "News", "Maps", "Movie", "Bus")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        auth.addAuthStateListener { auth ->
            authChanged(auth)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, colors)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> bg.setBackgroundColor(Color.RED)
                    1 -> bg.setBackgroundColor(Color.GREEN)
                    2 -> bg.setBackgroundColor(Color.BLUE)
                }

            }
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        recycler.adapter = FunctionAdapter()



    }

    inner class FunctionAdapter : RecyclerView.Adapter<FunctionAdapter.FunctionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view = layoutInflater.inflate(R.layout.row_function, parent, false)
            return FunctionHolder(view)
        }

        override fun getItemCount(): Int {
            return functions.size
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.name.text = functions[position]
            holder.itemView.setOnClickListener{view ->
                functionClick(holder, position)
            }
        }

        inner class FunctionHolder(view : View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.name
        }
    }

    private fun functionClick(holder: FunctionAdapter.FunctionHolder, position: Int) {
        when(position) {
            1 -> startActivity<ContactActivity>()
            2 -> startActivity<ParkingActivity>()
            6 -> startActivity<MovieActivity>()
            7 -> startActivity<BusActivity>()
        }
    }

    override fun onRestart() {
        super.onRestart()
        FirebaseDatabase.getInstance()
            .getReference("users")
            .child(auth.currentUser!!.uid)
            .child("nickname")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    nickname.text =  dataSnapshot.value.toString()
                }
            })
    }


    private fun authChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, RC_SIGNUP)
        } else {
            Log.d("MainActivity", "authChanged: ${auth.currentUser?.uid}")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, NicknameActivity::class.java)
                startActivityForResult(intent, RC_NICK)
            }
        }
        if (requestCode == RC_NICK) {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_signout -> {
                auth.signOut()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}
