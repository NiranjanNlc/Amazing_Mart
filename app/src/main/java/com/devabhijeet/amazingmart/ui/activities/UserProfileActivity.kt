package com.devabhijeet.amazingmart.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.devabhijeet.amazingmart.R
import com.devabhijeet.amazingmart.firestore.FirestoreClass
import com.devabhijeet.amazingmart.models.User
import com.devabhijeet.amazingmart.utils.Constants
import com.devabhijeet.amazingmart.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

private lateinit var mUserDetails: User

private var mSelectedImageFileUri: Uri? = null
private var mUserProfileImageURL: String = ""

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }


        if (mUserDetails.profileCompleted == 0)
        {
            tv_title.text = resources.getString(R.string.title_complete_profile)

            et_first_name_usr_prof.isEnabled = false

            et_last_name_usr_prof.isEnabled = false

        }
        else
        {
            setupActionBar()
            tv_title.text = resources.getString(R.string.title_edit_profile)

            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, iv_user_photo)

            if (mUserDetails.mobile != 0L)
            {
                et_mobile_number_usr_prof.setText(mUserDetails.mobile.toString())
            }

            if (mUserDetails.gender == Constants.MALE)
            {
                rb_male.isChecked = true
            }
            else
            {
                rb_female.isChecked = true
            }

        }
        // et_first_name_usr_prof.isEnabled = false
        et_first_name_usr_prof.setText(mUserDetails.firstName)

        // et_last_name_usr_prof.isEnabled = false
        et_last_name_usr_prof.setText(mUserDetails.lastName)

        et_email_usr_prof.isEnabled = false
        et_email_usr_prof.setText(mUserDetails.email)


        iv_user_photo.setOnClickListener(this@UserProfileActivity)

        btn_submit_usr_prof.setOnClickListener(this@UserProfileActivity)


    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {

                R.id.iv_user_photo -> {

                    // Here we will check if the permission is already allowed or we need to request for it.
                    // First of all we will check the READ_EXTERNAL_STORAGE permission and if it is not allowed we will request for the same.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {

                    //    showErrorSnackBar("You already have the storage permission.", false)

                        Constants.showImageChooser(this@UserProfileActivity)

                    } else {

                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }


                R.id.btn_submit_usr_prof ->{

                    if (validateUserProfileDetails())
                    {

                        showProgressDialog(resources.getString(R.string.please_wait))


                        if(mSelectedImageFileUri != null)
                        FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.USER_PROFILE_IMAGE)

                        else
                        {
                            updateUserProfileDetails()
                        }

                    }
                }

            }
        }

    }


    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_user_profile_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }
    // END





    private fun updateUserProfileDetails(){

        val userHashMap = HashMap<String,Any>()

        val mobileNumber = et_mobile_number_usr_prof.text.toString().trim() { it <= ' ' }

        val firstName = et_first_name_usr_prof.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        // Get the LastName from editText and trim the space
        val lastName = et_last_name_usr_prof.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }


        val gender = if(rb_male.isChecked){
            Constants.MALE
        }
        else
        {
            Constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty())
        {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()){

            userHashMap[Constants.MOBILE]= mobileNumber.toLong()
        }
        //  key: gender value:male
        //gender:male
        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }
       // userHashMap[Constants.GENDER] = gender;

        userHashMap[Constants.COMPLETE_PROFILE] = 1

      //  showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().updateUserProfileData(this,userHashMap)



    }



    fun userProfileUpdateSuccess()
    {
        hideProgressDialog()

        showErrorSnackBar(resources.getString(R.string.msg_profile_ukpdate_success),false)

        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()

    }


    /**
     * This function will identify the result of runtime permission after the user allows or deny permission based on the unique code.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //showErrorSnackBar("The storage permission is granted.", false)
                Constants.showImageChooser(this@UserProfileActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                         mSelectedImageFileUri = data.data!!

                        //iv_user_photo.setImageURI(Uri.parse(selectedImageFileUri.toString()))

                        GlideLoader(this@UserProfileActivity).loadUserPicture(mSelectedImageFileUri!!,iv_user_photo)

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    // START
    /**
     * A function to validate the input entries for profile details.
     */
    private fun validateUserProfileDetails(): Boolean {
        return when {

            // We have kept the user profile picture is optional.
            // The FirstName, LastName, and Email Id are not editable when they come from the login screen.
            // The Radio button for Gender always has the default selected value.

            // Check if the mobile number is not empty as it is mandatory to enter.
            TextUtils.isEmpty(et_mobile_number_usr_prof.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }
    // END

    // START
    /**
     * A function to notify the success result of image upload to the Cloud Storage.
     *
     * @param imageURL After successful upload the Firebase Cloud returns the URL.
     */
    fun imageUploadSuccess(imageURL: String) {

        // Hide the progress dialog
       // hideProgressDialog()

        mUserProfileImageURL = imageURL

        updateUserProfileDetails()
    }
    // END

}