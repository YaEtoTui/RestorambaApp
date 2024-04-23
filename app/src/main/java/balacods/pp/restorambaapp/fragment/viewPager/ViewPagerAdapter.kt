package balacods.pp.restorambaapp.fragment.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import balacods.pp.restorambaapp.fragment.MainPageFragment
import balacods.pp.restorambaapp.fragment.RestaurantsFragment
import balacods.pp.restorambaapp.fragment.SearchFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MainPageFragment()
            1 -> SearchFragment()
            2 -> RestaurantsFragment()
            else -> throw IllegalArgumentException()
        }
    }
}