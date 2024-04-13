package balacods.pp.restorambaapp.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import balacods.pp.restorambaapp.databinding.FragmentMainBinding


class MainPageFragment : Fragment() {

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

        onClick()

//        val instructions: ConstraintLayout = binding.idInstructions.root
//
//        mSettings = activity?.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) ?: return
//
//        if (mSettings!!.contains(APP_PREFERENCES)) {
//            Log.i("contains", mSettings!!.contains(APP_PREFERENCES).toString())
//            APP_PREFERENCES_INSTRUCTIONS =
//                mSettings!!.getBoolean(APP_PREFERENCES, APP_PREFERENCES.toBoolean())
//        }
//
//        if (!APP_PREFERENCES_INSTRUCTIONS) {
//
//
//            instructions.visibility = View.VISIBLE
//
//            with(mSettings!!.edit()) {
//                putBoolean(APP_PREFERENCES, true)
//                apply()
//            }
//        }
    }

    private fun onClick() {
//        binding.idButtonGetRandomDish.setOnClickListener {
//            findNavController().navigate(R.id.action_mainFrag_to_yandexCardFrag)
//        }
    }
}