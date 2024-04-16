package balacods.pp.restorambaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.databinding.FragmentRestaurantsBinding

class RestaurantsFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
    }

    private fun onClick() {
        binding.idNav.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantsFrag_to_mainFrag)
        }
        binding.idNav.idNavMap.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantsFrag_to_yandexCardFrag)
        }
    }
}