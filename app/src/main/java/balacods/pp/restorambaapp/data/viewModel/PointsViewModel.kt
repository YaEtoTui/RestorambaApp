package balacods.pp.restorambaapp.data.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.geometry.Point

class PointsViewModel: ViewModel() {
    val endPoint = MutableLiveData<Point>()
}