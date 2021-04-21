package ttaomae.foodtracker

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        const val FOOD_TAB_INDEX = 0
        const val RESTAURANT_TAB_INDEX = 1
    }

    private val args: MainFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.main_tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.main_view_pager)
        viewPager.adapter = ScreenSlidePagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val context = requireContext()
            when (position) {
                FOOD_TAB_INDEX -> tab.text = context.getString(R.string.food_tab_label)
                RESTAURANT_TAB_INDEX -> tab.text = context.getString(R.string.restaurants_tab_label)
            }
        }.attach()

        viewPager.currentItem = args.tabIndex

        // Set add button behavior.
        view.findViewById<FloatingActionButton>(R.id.fab_add_item).setOnClickListener {
            val action = MainFragmentDirections.actionAddFoodItem()
            findNavController().navigate(action)
        }

    }

    private inner class ScreenSlidePagerAdapter(f: Fragment) : FragmentStateAdapter(f) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                FOOD_TAB_INDEX -> FoodItemListFragment()
                RESTAURANT_TAB_INDEX -> ListRestaurantFragment()
                else -> throw IllegalStateException()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_add_restaurant -> {
                val action = MainFragmentDirections.actionAddRestaurant()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
