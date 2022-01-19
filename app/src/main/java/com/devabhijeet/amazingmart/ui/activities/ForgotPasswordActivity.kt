package com.devabhijeet.amazingmart.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.devabhijeet.amazingmart.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        //removing status bar
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

            setupActionBar();

            btn_submit.setOnClickListener {
                val email : String = forgot_pass_et_email.text.toString().trim { it <= ' ' }
                if(email.isEmpty())
                {
                    showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true);
                }
                else
                {
                    showProgressDialog(resources.getString(R.string.please_wait))
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            hideProgressDialog();

                            if(task.isSuccessful){

                                showErrorSnackBar(resources.getString(R.string.email_sent_success),false);
                                finish();
                            }
                            else
                            {
                                showErrorSnackBar(task.exception!!.message.toString(),true);

                            }

                        }
                }
            }




    }


    private fun setupActionBar()
    {
        setSupportActionBar(toolbar_forgot_password_activity);

        val actionBar = supportActionBar
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }
    }






}