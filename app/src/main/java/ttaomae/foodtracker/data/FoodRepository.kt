package ttaomae.foodtracker.data

interface FoodRepository {
    fun get(id: String): FoodItem?
    fun getAll(): List<FoodItem>
    fun save(item: FoodItem)
    fun remove(id: Long)
}

class InMemoryFoodRepository : FoodRepository {
    private var nextId: Long = 0
    private val items: MutableMap<String, FoodItem> = mutableMapOf()

    override fun get(id: String): FoodItem? {
        return items[id]
    }

    override fun getAll(): List<FoodItem> {
        return items.values.toList()
    }

    override fun save(item: FoodItem) {
        if (item.id == null) {
            // Find next ID.
            while (items.containsKey(nextId.toString())) nextId++

            items[nextId.toString()] = FoodItem(nextId.toString(), item)
            nextId++
        } else {
            items[item.id] = item
        }
    }

    override fun remove(id: Long) {
        items.remove(id)
    }
}
