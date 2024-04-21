package balacods.pp.restorambaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.databinding.FragmentRestaurantBinding

class RestaurantFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClick()
    }

    private fun onClick() {
        binding.idNavMain.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_mainFrag)
        }
        binding.idNavRestaurants.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFrag_to_restaurantsFrag)
        }
    }
}