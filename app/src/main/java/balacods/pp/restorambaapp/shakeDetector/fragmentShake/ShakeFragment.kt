package balacods.pp.restorambaapp.shakeDetector.fragmentShake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import balacods.pp.restorambaapp.databinding.FragmentShakeBinding

class ShakeFragment : Fragment() {

    private lateinit var binding: FragmentShakeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShakeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initBt()
    }

    private fun initBt() {
        binding.btClose.setOnClickListener {
            binding.shakePopUp.visibility = View.GONE
        }
    }
}