

package com.qeats.repositoryservices;

import com.qeats.dto.Restaurant;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;

public interface RestaurantRepositoryService {


    List<Restaurant> findAllRestaurantsCloseBy(Double latitude, Double longitude,
                                               LocalTime currentTime, Double servingRadiusInKms);


    List<Restaurant> findRestaurantsByName(Double latitude, Double longitude,
                                           String searchString, LocalTime currentTime, Double servingRadiusInKms);


    List<Restaurant> findRestaurantsByAttributes(
            Double latitude, Double longitude, String searchString,
            LocalTime currentTime, Double servingRadiusInKms);


    List<Restaurant> findRestaurantsByItemName(Double latitude, Double longitude,
                                               String searchString, LocalTime currentTime, Double servingRadiusInKms);


    @Async
    Future<List<Restaurant>> findRestaurantsByNameAsync(Double latitude, Double longitude,
                                                        String searchString, LocalTime currentTime, Double servingRadiusInKms);


    List<Restaurant> findRestaurantsByItemAttributes(Double latitude, Double longitude,
                                                     String searchString, LocalTime currentTime, Double servingRadiusInKms);

}


