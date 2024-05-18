package balacods.pp.restorambaapp.data.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestaurantAndDishViewModel : ViewModel() {
    val ids = MutableLiveData<Array<Long>>() // 1 - dishId, 2 - restaurantId
}