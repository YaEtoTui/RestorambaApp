package balacods.pp.restorambaapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import balacods.pp.restorambaapp.databinding.FragmentQuestionsBinding

class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
//        initBtNav()
        initCheckBox()
    }

    private fun initCheckBox() {
        binding.apply {
            btCheckBox1.setOnClickListener {
                if (btCheckBox1.isChecked) {
                    idAnswer1.visibility = View.VISIBLE
                } else {
                    idAnswer1.visibility = View.GONE
                }
            }

            btCheckBox2.setOnClickListener {
                if (btCheckBox2.isChecked) {
                    idAnswer2.visibility = View.VISIBLE
                } else {
                    idAnswer2.visibility = View.GONE
                }
            }

            btCheckBox3.setOnClickListener {
                if (btCheckBox3.isChecked) {
                    idAnswer3.visibility = View.VISIBLE
                } else {
                    idAnswer3.visibility = View.GONE
                }
            }

            btCheckBox4.setOnClickListener {
                if (btCheckBox4.isChecked) {
                    idAnswer4.visibility = View.VISIBLE
                } else {
                    idAnswer4.visibility = View.GONE
                }
            }
        }
    }
}