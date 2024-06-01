package balacods.pp.restorambaapp.data.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.geometry.Point

class PointsViewModel: ViewModel() {
    val code = MutableLiveData<Int>()
    val startPoints = MutableLiveData<Point>()
    val allPoints = MutableLiveData<MutableList<Point>>(mutableListOf())
}