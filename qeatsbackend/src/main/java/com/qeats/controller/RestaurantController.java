package com.qeats.controller;

import com.qeats.exchanges.GetRestaurantsRequest;
import com.qeats.exchanges.GetRestaurantsResponse;
import com.qeats.services.RestaurantService;

import java.time.LocalTime;
import javax.validation.Valid;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log4j2

public class RestaurantController {

    public static final String RESTAURANT_API_ENDPOINT = "/qeats/v1";
    public static final String RESTAURANTS_API = "/restaurants";
    public static final String MENU_API = "/menu";
    public static final String CART_API = "/cart";
    public static final String CART_ITEM_API = "/cart/item";
    public static final String CART_CLEAR_API = "/cart/clear";
    public static final String POST_ORDER_API = "/order";
    public static final String GET_ORDERS_API = "/orders";

    @Autowired
    private RestaurantService restaurantService;


    @GetMapping(RESTAURANT_API_ENDPOINT + RESTAURANTS_API)
    public ResponseEntity<GetRestaurantsResponse> getRestaurants(
            @Valid GetRestaurantsRequest getRestaurantsRequest) {

        log.info("getRestaurants called with {}", getRestaurantsRequest);
        GetRestaurantsResponse getRestaurantsResponse;


        if (getRestaurantsRequest.isSearchQueryPresent()) {
            getRestaurantsResponse =
                    restaurantService.findRestaurantsBySearchQuery(getRestaurantsRequest, LocalTime.now());
        } else {
            getRestaurantsResponse =
                    restaurantService.findAllRestaurantsCloseBy(getRestaurantsRequest, LocalTime.now());
        }

        log.info("getRestaurants returned {}", getRestaurantsResponse);


        return ResponseEntity.ok().body(getRestaurantsResponse);
    }


}

