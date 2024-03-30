package balacods.pp.restorambaapp.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import balacods.pp.restorambaapp.databinding.FragmentMainBinding




class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    // это будет именем файла настроек
    private final val APP_PREFERENCES: String = "mysettings"
    private var APP_PREFERENCES_INSTRUCTIONS: Boolean = false

    var mSettings: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val str: TextView = binding.idInst

        mSettings = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) ?: return

        if (mSettings!!.contains(APP_PREFERENCES)) {
            Log.i("contains", mSettings!!.contains(APP_PREFERENCES).toString())
            APP_PREFERENCES_INSTRUCTIONS = mSettings!!.getBoolean(APP_PREFERENCES, APP_PREFERENCES.toBoolean())
        }

        if (!APP_PREFERENCES_INSTRUCTIONS) {

            str.visibility = View.VISIBLE

            with (mSettings!!.edit()) {
                putBoolean(APP_PREFERENCES, true)
                apply()
            }
        }
    }
}