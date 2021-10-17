package com.sanjay.contentproviderandroid

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 *  @return true if permission granted
 * */
fun Context.hasPermission(permissionId: Int) = ContextCompat.checkSelfPermission(this, getPermissionString(permissionId)) == PackageManager.PERMISSION_GRANTED

/**
 *  @return get permission String
 * */
fun Context.getPermissionString(id: Int) = when (id) {
    PERMISSION_READ_STORAGE -> Manifest.permission.READ_EXTERNAL_STORAGE
    PERMISSION_WRITE_STORAGE -> Manifest.permission.WRITE_EXTERNAL_STORAGE
    PERMISSION_READ_CONTACT -> Manifest.permission.READ_CONTACTS
    else -> ""
}
