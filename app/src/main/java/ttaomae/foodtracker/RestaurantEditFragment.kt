package ttaomae.foodtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ttaomae.foodtracker.data.Restaurant
import ttaomae.foodtracker.data.RestaurantRepository
import ttaomae.foodtracker.databinding.FragmentRestaurantEditBinding
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantEditFragment : Fragment(R.layout.fragment_restaurant_edit) {
    @Inject lateinit var restaurantRepository: RestaurantRepository
    private val args: RestaurantEditFragmentArgs by navArgs()
    private var restaurant: Restaurant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restaurant = args.restaurant
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentRestaurantEditBinding>(
            layoutInflater, R.layout.fragment_restaurant_edit, container, false
        )
        binding.restaurant = restaurant
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set save button behavior.
        view.findViewById<Button>(R.id.button_save_restaurant).setOnClickListener {
            val savedRestaurant = saveRestaurant(view)
            val action =
                RestaurantEditFragmentDirections.actionReturnToRestaurantDetail(savedRestaurant)
            findNavController().navigate(action)
        }
    }

    private fun saveRestaurant(view: View): Restaurant {
        val name = view.findViewById<EditText>(R.id.text_input_restaurant_name).text.toString()
        val restaurant = Restaurant(restaurant?.id, name)

        var restaurantId: Long? = null
        runBlocking {
            launch {
                restaurantId = restaurantRepository.save(restaurant)
            }
        }

        return Restaurant(restaurantId!!, name)
    }
}
