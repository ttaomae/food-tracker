package ttaomae.foodtracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MainMenuFragment : Fragment(R.layout.fragment_main_menu) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_view_restaurants).setOnClickListener {
            val action = MainMenuFragmentDirections.actionLoadRestaurantList()
            findNavController().navigate(action)
        }
        view.findViewById<Button>(R.id.button_view_items).setOnClickListener {
            val action = MainMenuFragmentDirections.actionLoadFoodItemList()
            findNavController().navigate(action)
        }
    }
}
