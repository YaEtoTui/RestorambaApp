package balacods.pp.restorambaapp.data.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DishViewModel : ViewModel() {
    val ids = MutableLiveData<List<Long>>()
}