package com.sanjay.contentproviderandroid.helper

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import com.sanjay.contentproviderandroid.SORT_BY_DISPLAY_NAME_PRIMARY
import com.sanjay.contentproviderandroid.data.Contacts

 fun Context?.getContactsList(): ArrayList<Contacts> {
    var contacts: ArrayList<Contacts> = ArrayList()

    val resolver: ContentResolver? = this?.contentResolver;
    val cursor = resolver?.query(
        ContactsContract.Contacts.CONTENT_URI, null, null, null,
        null)

    if (cursor?.count?:0 > 0) {
        while (cursor?.moveToNext() == true) {
            val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val hasPhoneNumber = (cursor.getString(
                cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
            val phoneNumbers:ArrayList<String>? = ArrayList()
            if (hasPhoneNumber > 0) {
                val cursorPhone = this?.contentResolver?.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), SORT_BY_DISPLAY_NAME_PRIMARY)


                if(cursorPhone?.count?:0 > 0) {
                    while (cursorPhone?.moveToNext() == true) {
                        val phoneNumValue = cursorPhone.getString(cursorPhone.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER))
                        phoneNumbers?.add(phoneNumValue)
                    }
                }
                val contact = Contacts(id, name, hasPhoneNumber ,phoneNumbers )
                contacts.add(contact)
                cursorPhone?.close()
            }
        }
    } else {
        //   toast("No contacts available!")
    }
    cursor?.close()
    return contacts
}
