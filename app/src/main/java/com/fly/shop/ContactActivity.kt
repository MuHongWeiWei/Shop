package com.fly.shop

import android.content.pm.PackageManager
import android.database.Cursor
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import java.util.jar.Manifest

class ContactActivity : AppCompatActivity() {

    private val RC_READCONTACT = 33

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        val permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            readContact()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), RC_READCONTACT)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_READCONTACT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContact()
            }
        }
    }

    private fun readContact() {
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            Log.d("ContactActivity", name)

            val hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
            val id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID))

            if (hasPhone == 1) {
                val cursor1 = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            + "=?",
                    arrayOf(id.toString()),
                    null
                )

                while (cursor1.moveToNext()) {
                    val phone = cursor1.getString(cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA))
                    Log.d("ContactActivity", phone)
                }
            }
        }
    }
}
