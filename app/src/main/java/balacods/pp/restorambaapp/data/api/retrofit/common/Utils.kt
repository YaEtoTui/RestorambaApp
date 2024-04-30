package balacods.pp.restorambaapp.data.api.retrofit.common

import android.content.Context
import android.widget.Toast
import balacods.pp.restorambaapp.R

typealias CommonColors = R.color
typealias CommonDrawables = R.drawable
typealias CommonId = R.id

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}