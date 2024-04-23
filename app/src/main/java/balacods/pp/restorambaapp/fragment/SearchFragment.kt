package balacods.pp.restorambaapp.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import balacods.pp.restorambaapp.R
import balacods.pp.restorambaapp.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        initSwitch()
        initSearch()
    }

    private fun initSwitch() {

        binding.idRestaurant.setOnClickListener {
            binding.idSearchView.setHint(R.string.hint_restaurant)
        }
        binding.idDish.setOnClickListener {
            binding.idSearchView.setHint(R.string.hint_dish)
        }
    }

    private fun initSearch() {
        // Инициализируйте ваши элементы управления
        // Инициализируйте ваши элементы управления
        val editText: AppCompatEditText = binding.idSearchView
        val clearButton: ImageView = binding.imIconClose

        // Добавьте обработчик текстовых изменений для AppCompatEditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Пустой метод
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Измените видимость ImageView в зависимости от того, пустой ли текст в AppCompatEditText
                clearButton.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable) {
                // Пустой метод
            }
        })


        // Добавьте OnClickListener для ImageView
        clearButton.setOnClickListener { // Очистите текст в AppCompatEditText
            editText.setText("")
        }
    }
}