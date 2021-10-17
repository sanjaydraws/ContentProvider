package com.sanjay.contentproviderandroid

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.sanjay.contentproviderandroid.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    var binding:ActivityMainBinding? = null

    val REQUEST_PERMISSIONS = 101
    private var mColumnProjection: Array<String>? = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        ContactsContract.Contacts.CONTACT_STATUS,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    private val mSortOrder = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY

    // this is to use selectionClause
//    private val mSelectionClause = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = 'Hitesh'"
//    private val mSelectionArguments = arrayOf("Hitesh")

    //to use  mSelectionArguments->  when use ? mark replaced with curuspnding index of selectionArgument array
    private val mSelectionClause = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = ?"
    private val mSelectionArguments = arrayOf("Hitesh")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(root)
        }
        askPermission()
    }
    private fun askPermission(){
        if ( !hasPermission(PERMISSION_READ_CONTACT) )
         {
            // ask permission if permission denied
            ActivityCompat.requestPermissions(this, arrayOf(getPermissionString(PERMISSION_READ_CONTACT)), REQUEST_PERMISSIONS)
        } else {
            val allContacts = getContacts()
            Log.d("MAin", "onRequestPermissionsResult: $allContacts")
        }
    }
//    fun getContacts(){
//        val contentResolver = contentResolver // returns instance for application package
//        val cursor: Cursor? = contentResolver.query(
//            ContactsContract.Contacts.CONTENT_URI,
//            mColumnProjection,
//            null,
//            null,
//            mSortOrder
//        )
//        if (cursor != null && cursor.count > 0) {
//            val stringBuilderQueryResult = StringBuilder("")
//            while (cursor.moveToNext()) {
//                stringBuilderQueryResult.append(
//                    cursor.getString(0)
//                        .toString() + " , " + cursor.getString(1) + " , " + cursor.getString(2) +
//
//                            "\n"
//                )
//            }
//            binding?.textViewResult?.text = stringBuilderQueryResult.toString()
//        } else {
//            binding?.textViewResult?.text = "No Contacts in device"
//        }
//    }


    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        if (cursor?.count?:0 > 0) {
            while (cursor?.moveToNext() == true) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                    if(cursorPhone?.count?:0 > 0) {
                        while (cursorPhone?.moveToNext() == true) {
                            val phoneNumValue = cursorPhone.getString(
                                cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            builder.append("Contact: ").append(name).append(", Phone Number: ").append(
                                phoneNumValue).append("\n\n")
                            Log.e("Name ===>",phoneNumValue);
                        }
                    }
                    cursorPhone?.close()
                }
            }
        } else {
            //   toast("No contacts available!")
        }
        cursor?.close()
        return builder
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults.isNotEmpty() && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        val allContacts = getContacts()
                        Log.d("MAin", "onRequestPermissionsResult: $allContacts")
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "App Was not allowed Contact Permission ",
                            Toast.LENGTH_LONG
                        ).show()
                        finish() // if permission denied
                        return
                    }
                    i++
                }
            }
        }

    }

}