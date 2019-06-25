package com.example.movies.utility

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.fragment.app.FragmentManager
import com.example.movies.utility.GenericDialog
import com.example.movies.utility.Message

class Utility {

    companion object {

        fun showDialogPopup(fragmentManager: FragmentManager?, message: Message) {
            var dialog = fragmentManager!!.findFragmentByTag("Popup") as GenericDialog?
            if (dialog == null) {
                dialog = GenericDialog()
                dialog.show(fragmentManager, "Popup")
            }

            dialog.setInfoText(message)
        }

        fun hideDialogPopup(fragmentManager: FragmentManager?) {

            val dialog = fragmentManager!!.findFragmentByTag("Popup") as GenericDialog?
            dialog?.dismiss()
        }

        /**
         * Baglanti kontrolu
         */
        fun isNetworkAvaible(activity: Activity?): Boolean {
            val connectivityManager = activity!!
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null
        }


        // lokalden ilgili bankanın resmini döner
        fun getBase64(context: Context, key: String): String? {
            return try {
                val prefences = context.getSharedPreferences("PREFFILE", Context.MODE_PRIVATE)
                prefences.getString(""+key, null)
            } catch (e: Exception) {
                null
            }
        }
    }

}