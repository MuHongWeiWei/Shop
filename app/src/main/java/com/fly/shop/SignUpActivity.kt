package com.fly.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup.setOnClickListener {
            val email = email.text.toString().trim()
            val password = password.text.toString().trim()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        alert("Success", "Sign up") {
                            okButton { dialog ->
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }.show()
                    } else {
                        alert("it.exception?.message", "Sign up") {
                            okButton {  }
                        }.show()
                    }
                 }
        }
    }
}
