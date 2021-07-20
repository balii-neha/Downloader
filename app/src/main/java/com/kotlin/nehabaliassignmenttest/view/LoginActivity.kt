package com.kotlin.nehabaliassignmenttest.view

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kotlin.nehabaliassignmenttest.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var otp_mstring = ""
    var  otp_ =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        login.setOnClickListener {
            otp_view?.requestFocusOTP()
            otp_view?.otpListener = object : OTPListener {
                override fun onInteractionListener() {

                }

                override fun onOTPComplete(otp: String) {
                    if (otp.equals("")) {
                        Toast.makeText(getApplication(), "Enter OTP 1234", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        otp_=true
                        otp_mstring = otp
                    }

                }
            }



            if( phoneNum.text.toString().toString().equals("")){
                phoneNum.setError("Enter Phone number")

            }else if(phoneNum.text.length < 10){
                phoneNum.setError("Enter valid 10 digit number")
            }else if(!otp_){

                Toast.makeText(getApplication(), "Enter OTP 1234", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                otp_ = false
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

    }
}