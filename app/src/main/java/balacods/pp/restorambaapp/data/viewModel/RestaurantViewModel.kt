package balacods.pp.restorambaapp.data.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestaurantViewModel: ViewModel() {
    val restaurantId = MutableLiveData<Long>()
}