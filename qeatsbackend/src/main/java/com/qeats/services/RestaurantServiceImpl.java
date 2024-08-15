package com.qeats.services;

import com.qeats.dto.Restaurant;
import com.qeats.exchanges.GetRestaurantsRequest;
import com.qeats.exchanges.GetRestaurantsResponse;
import com.qeats.repositoryservices.RestaurantRepositoryService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class RestaurantServiceImpl implements RestaurantService {

    private final Double peakHoursServingRadiusInKms = 3.0;
    private final Double normalHoursServingRadiusInKms = 5.0;
    @Autowired
    private RestaurantRepositoryService restaurantRepositoryService;


    private static boolean isBetween(LocalTime currentTime, LocalTime start, LocalTime end) {
        return !currentTime.isBefore(start) && !currentTime.isAfter(end);
    }


    @Override
    public GetRestaurantsResponse findAllRestaurantsCloseBy(
            GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {

        List<Restaurant> restaurants = null;

        double latitude = getRestaurantsRequest.getLatitude();
        double longitude = getRestaurantsRequest.getLongitude();

        if (isBetween(currentTime, LocalTime.of(8, 0), LocalTime.of(10, 0))
                || isBetween(currentTime, LocalTime.of(13, 0), LocalTime.of(14, 0))
                || isBetween(currentTime, LocalTime.of(19, 0), LocalTime.of(21, 0))) {

            restaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(latitude, longitude,
                    currentTime, peakHoursServingRadiusInKms);
        } else {
            restaurants = restaurantRepositoryService.findAllRestaurantsCloseBy(latitude, longitude,
                    currentTime, normalHoursServingRadiusInKms);
        }


        for (Restaurant restaurant : restaurants) {
            String name = restaurant.getName().replaceAll("[^\\p{ASCII}]", "?");
            restaurant.setName(name);
        }

        return new GetRestaurantsResponse(restaurants);
    }


    @Override
    public GetRestaurantsResponse findRestaurantsBySearchQuery(
            GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {

        List<Restaurant> restaurants = new ArrayList<>();

        double latitude = getRestaurantsRequest.getLatitude();
        double longitude = getRestaurantsRequest.getLongitude();

        String searchFor = getRestaurantsRequest.getSearchFor();

        if (searchFor.isBlank()) {
            return new GetRestaurantsResponse(restaurants);
        }
        List<Restaurant> finding = null;
        if (isBetween(currentTime, LocalTime.of(8, 0), LocalTime.of(10, 0))
                || isBetween(currentTime, LocalTime.of(13, 0), LocalTime.of(14, 0))
                || isBetween(currentTime, LocalTime.of(19, 0), LocalTime.of(21, 0))) {


            restaurants = restaurantRepositoryService.findRestaurantsByName(latitude, longitude, searchFor,
                    currentTime, peakHoursServingRadiusInKms);
            finding = restaurantRepositoryService.findRestaurantsByAttributes(latitude, longitude, searchFor,
                    currentTime, peakHoursServingRadiusInKms);

            restaurantRepositoryService.findRestaurantsByItemName(latitude, longitude, searchFor,
                    currentTime, peakHoursServingRadiusInKms);

            restaurantRepositoryService.findRestaurantsByItemAttributes(latitude, longitude, searchFor, currentTime,
                    peakHoursServingRadiusInKms);

        } else {
            restaurants = restaurantRepositoryService.findRestaurantsByName(latitude, longitude, searchFor,
                    currentTime, normalHoursServingRadiusInKms);

            finding = restaurantRepositoryService.findRestaurantsByAttributes(latitude, longitude, searchFor,
                    currentTime, normalHoursServingRadiusInKms);

            restaurantRepositoryService.findRestaurantsByItemName(latitude, longitude, searchFor,
                    currentTime, normalHoursServingRadiusInKms);

            restaurantRepositoryService.findRestaurantsByItemAttributes(latitude, longitude, searchFor,
                    currentTime, normalHoursServingRadiusInKms);
        }

        if (finding != null)
            restaurants.addAll(finding);


        for (Restaurant restaurant : restaurants) {
            String name = restaurant.getName().replaceAll("[^\\p{ASCII}]", "?");
            restaurant.setName(name);
        }

        return new GetRestaurantsResponse(restaurants);
    }


    @Override
    public GetRestaurantsResponse findRestaurantsBySearchQueryMt(
            GetRestaurantsRequest getRestaurantsRequest, LocalTime currentTime) {

        return null;
    }


}

