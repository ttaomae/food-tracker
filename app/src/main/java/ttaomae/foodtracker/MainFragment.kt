package ttaomae.foodtracker

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View

import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ttaomae.foodtracker.viewmodel.MainMenuViewModel
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        const val FOOD_TAB_INDEX = 0
        const val RESTAURANT_TAB_INDEX = 1

        val TIMESTAMP_FORMATTER = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT)
    }

    private val viewModel: MainMenuViewModel by viewModels()
    private val args: MainFragmentArgs by navArgs()

    // If the export or import URI is not null, that indicates that the appropriate action should
    // be executed when this fragment resumes. This is needed necessary because the ViewModel cannot
    // be accessed in the `registerForActivityResult()` callback.
    // FIXME: This is probably not a good way to handle, but it seems to work well enough for now.
    private var exportUri: Uri? = null
    private val exportFile = registerForActivityResult(CreateDocument()) { exportUri = it }

    private var importUri: Uri? = null
    private val importFile = registerForActivityResult(OpenDocument()) { importUri = it }

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

    override fun onResume() {
        super.onResume()

        // If the export/import URI is set, perform action then clear URI.
        exportUri?.let {
            export(it)
            exportUri = null
        }
        importUri?.let {
            import(it)
            importUri = null
        }
    }

    private fun export(uri: Uri) {
        activity?.applicationContext?.contentResolver?.let { contentResolver ->
            contentResolver.openFileDescriptor(uri, "w")?.use {
                viewModel.export(contentResolver, uri, viewLifecycleOwner)
            }
        }
    }

    private fun import(uri: Uri) {
        activity?.applicationContext?.contentResolver?.let { contentResolver ->
            contentResolver.openFileDescriptor(uri, "r")?.use {
                FileReader(it.fileDescriptor).use { fileReader ->
                    val json = fileReader.readText()
                    viewModel.import(json)
                }
            }
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

            R.id.menu_item_export_data -> {
                val filename = TIMESTAMP_FORMATTER.format(Calendar.getInstance().time)
                exportFile.launch("$filename.json")
                true
            }

            R.id.menu_item_import_data -> {
                importFile.launch(arrayOf("application/json"))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
