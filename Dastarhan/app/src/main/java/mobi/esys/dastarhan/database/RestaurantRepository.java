package mobi.esys.dastarhan.database;

import java.util.List;

public interface RestaurantRepository {

    void addOrUpdate(Restaurant restaurant);

    Restaurant getById(int id);

    List<Restaurant> getByCuisine(int cuisineID);

    List<Restaurant> getAll();
}