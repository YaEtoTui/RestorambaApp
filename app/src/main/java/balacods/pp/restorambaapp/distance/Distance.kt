package balacods.pp.restorambaapp.distance

import com.yandex.mapkit.geometry.Point
import kotlin.math.sqrt

/**
 * Поиск кратчайшего расстояния с гео пользователя до ресторана, который он выбрал
 *
 * @author Sazhin Egor 01.06.2024
 * @param pointGeo Точка гео
 * @param pointRestaurant Точка ресторана
 * @return вернет строку расстояния
 */
fun findShortestDistance(pointGeo: Point, pointRestaurant: Point): String {
    // Доработать
    val dx = pointRestaurant.latitude - pointGeo.latitude
    val dy = pointRestaurant.longitude - pointGeo.longitude
    val distance = sqrt(dx * dx + dy * dy) // как будто в метрах
    return String.format("%.2fкм", distance)
}