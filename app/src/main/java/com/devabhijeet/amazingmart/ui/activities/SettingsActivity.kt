package com.devabhijeet.amazingmart.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.devabhijeet.amazingmart.R
import com.devabhijeet.amazingmart.firestore.FirestoreClass
import com.devabhijeet.amazingmart.models.User
import com.devabhijeet.amazingmart.utils.Constants
import com.devabhijeet.amazingmart.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(),View.OnClickListener {

   private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupActionBar()

        btn_logout_settings.setOnClickListener(this)
        tv_edit_settings.setOnClickListener(this)
        ll_address_settings.setOnClickListener(this)


    }

    override fun onResume() {
        super.onResume()

        getUserDetails()
    }
    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }
    // END

    /**
     * A function to get the user details from firestore.
     */
    private fun getUserDetails() {

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@SettingsActivity)
    }
    // END

    // START
    /**
     * A function to receive the user details and populate it in the UI.
     */
    fun userDetailsSuccess(user: User) {

        mUserDetails = user

        // TODO Step 9: Set the user details to UI.
        // START
        // Hide the progress dialog
        hideProgressDialog()

        // Load the image using the Glide Loader class.
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_user_photo_settings)

        tv_name_settings.text = "${user.firstName} ${user.lastName}"
        tv_gender_settings.text = user.gender
        tv_email_settings.text = user.email
        tv_mobile_number_settings.text = "${user.mobile}"
        // END
    }

    override fun onClick(v: View?) {

        if(v!= null)
        {
            when(v.id)
            {
                R.id.btn_logout_settings ->{
                    FirebaseAuth.getInstance().signOut();
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

                R.id.tv_edit_settings ->  {

                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
                    startActivity(intent)

                }

                R.id.ll_address_settings ->  {

                    val intent = Intent(this@SettingsActivity, AddressListActivity::class.java)
                    startActivity(intent)

                }


            }



        }

    }
    // END
}