package ttaomae.foodtracker

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment(R.layout.fragment_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.main_tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.main_view_pager)
            viewPager.adapter = ScreenSlidePagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = requireContext().getString(R.string.food_tab_label)
                1 -> tab.text = requireContext().getString(R.string.restaurants_tab_label)
            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(f: Fragment) : FragmentStateAdapter(f) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ListFoodItemFragment()
                1 -> ListRestaurantFragment()
                else -> throw IllegalStateException()
            }
        }
    }
}
