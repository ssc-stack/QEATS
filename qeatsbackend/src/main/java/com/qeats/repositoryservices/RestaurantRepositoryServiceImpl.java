

package com.qeats.repositoryservices;

import ch.hsr.geohash.GeoHash;
import com.qeats.configs.RedisConfiguration;
import com.qeats.dto.Restaurant;
import com.qeats.globals.GlobalConstants;
import com.qeats.models.MenuEntity;
import com.qeats.models.RestaurantEntity;
import com.qeats.repositories.ItemRepository;
import com.qeats.repositories.MenuRepository;
import com.qeats.repositories.RestaurantRepository;
import com.qeats.utils.GeoLocation;
import com.qeats.utils.GeoUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import javax.inject.Provider;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


@Service
public class RestaurantRepositoryServiceImpl implements RestaurantRepositoryService {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private RedisConfiguration redisConfiguration;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Provider<ModelMapper> modelMapperProvider;

    private boolean isOpenNow(LocalTime time, RestaurantEntity res) {
        LocalTime openingTime = LocalTime.parse(res.getOpensAt());
        LocalTime closingTime = LocalTime.parse(res.getClosesAt());

        return time.isAfter(openingTime) && time.isBefore(closingTime);
    }


    public List<Restaurant> findAllRestaurantsCloseBy(Double latitude, Double longitude,
                                                      LocalTime currentTime, Double servingRadiusInKms) {
        List<Restaurant> restaurants = null;
        if (redisConfiguration.isCacheAvailable()) {
            restaurants =
                    findAllRestaurantsCloseByFromCache(latitude, longitude, currentTime, servingRadiusInKms);
        } else {
            restaurants =
                    findAllRestaurantsCloseFromDb(latitude, longitude, currentTime, servingRadiusInKms);
        }
        return restaurants;
    }


    private List<Restaurant> findAllRestaurantsCloseFromDb(Double latitude, Double longitude,
                                                           LocalTime currentTime, Double servingRadiusInKms) {
        ModelMapper modelMapper = modelMapperProvider.get();
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            if (isRestaurantCloseByAndOpen(restaurantEntity, currentTime, latitude, longitude,
                    servingRadiusInKms)) {
                restaurants.add(modelMapper.map(restaurantEntity, Restaurant.class));
            }
        }
        return restaurants;
    }

    private List<Restaurant> findAllRestaurantsCloseByFromCache(Double latitude, Double longitude,
                                                                LocalTime currentTime, Double servingRadiusInKms) {
        List<Restaurant> restaurantList = new ArrayList<>();

        GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        GeoHash geoHash =
                GeoHash.withCharacterPrecision(geoLocation.getLatitude(), geoLocation.getLongitude(), 7);

        try (Jedis jedis = redisConfiguration.getJedisPool().getResource()) {
            String jsonStringFromCache = jedis.get(geoHash.toBase32());

            if (jsonStringFromCache == null) {

                String createdJsonString = "";
                try {
                    restaurantList = findAllRestaurantsCloseFromDb(geoLocation.getLatitude(),
                            geoLocation.getLongitude(), currentTime, servingRadiusInKms);
                    createdJsonString = new ObjectMapper().writeValueAsString(restaurantList);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }


                jedis.setex(geoHash.toBase32(), GlobalConstants.REDIS_ENTRY_EXPIRY_IN_SECONDS,
                        createdJsonString);
            } else {
                try {
                    restaurantList = new ObjectMapper().readValue(jsonStringFromCache,
                            new TypeReference<List<Restaurant>>() {
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return restaurantList;
    }


    @Override
    public List<Restaurant> findRestaurantsByName(Double latitude, Double longitude,
                                                  String searchString, LocalTime currentTime, Double servingRadiusInKms) {
        ModelMapper modelMapper = modelMapperProvider.get();
        List<RestaurantEntity> restaurantEntities =
                restaurantRepository.findRestaurantsByNameExact(searchString).get();

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            if (isRestaurantCloseByAndOpen(restaurantEntity, currentTime, latitude, longitude,
                    servingRadiusInKms)) {
                restaurants.add(modelMapper.map(restaurantEntity, Restaurant.class));
            }
        }
        return restaurants;
    }

    @Override
    @Async
    public Future<List<Restaurant>> findRestaurantsByNameAsync(Double latitude, Double longitude,
                                                               String searchString, LocalTime currentTime, Double servingRadiusInKms) {
        ModelMapper modelMapper = modelMapperProvider.get();
        List<RestaurantEntity> restaurantEntities =
                restaurantRepository.findRestaurantsByNameExact(searchString).get();

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            if (isRestaurantCloseByAndOpen(restaurantEntity, currentTime, latitude, longitude,
                    servingRadiusInKms)) {
                restaurants.add(modelMapper.map(restaurantEntity, Restaurant.class));
            }
        }

        return new AsyncResult<List<Restaurant>>(restaurants);
    }


    @Override
    public List<Restaurant> findRestaurantsByAttributes(Double latitude, Double longitude,
                                                        String searchString, LocalTime currentTime, Double servingRadiusInKms) {
        ModelMapper modelMapper = modelMapperProvider.get();
        List<RestaurantEntity> restaurantEntities =
                restaurantRepository.findRestaurantsByAttribute(searchString).get();

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            if (isRestaurantCloseByAndOpen(restaurantEntity, currentTime, latitude, longitude,
                    servingRadiusInKms)) {
                restaurants.add(modelMapper.map(restaurantEntity, Restaurant.class));
            }
        }
        return restaurants;
    }


    @Override
    public List<Restaurant> findRestaurantsByItemName(Double latitude, Double longitude,
                                                      String searchString, LocalTime currentTime, Double servingRadiusInKms) {
        ModelMapper modelMapper = modelMapperProvider.get();
        List<String> itemIdList = null;
        Optional<List<MenuEntity>> optionalMenuEntityList =
                menuRepository.findMenusByItemsItemIdIn(itemIdList);
        Optional<List<RestaurantEntity>> optionalRestaurantEntityList = Optional.empty();

        if (optionalMenuEntityList.isPresent()) {
            List<MenuEntity> menuEntityList = optionalMenuEntityList.get();
            List<String> restaurantIdList =
                    menuEntityList.stream().map(MenuEntity::getRestaurantId).collect(Collectors.toList());
            optionalRestaurantEntityList =
                    restaurantRepository.findRestaurantsByRestaurantIdIn(restaurantIdList);
        }

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        for (RestaurantEntity restaurantEntity : optionalRestaurantEntityList.get()) {
            if (isRestaurantCloseByAndOpen(restaurantEntity, currentTime, latitude, longitude,
                    servingRadiusInKms)) {
                restaurants.add(modelMapper.map(restaurantEntity, Restaurant.class));
            }
        }
        return restaurants;
    }


    @Override
    public List<Restaurant> findRestaurantsByItemAttributes(Double latitude, Double longitude,
                                                            String searchString, LocalTime currentTime, Double servingRadiusInKms) {

        return null;
    }


    private boolean isRestaurantCloseByAndOpen(RestaurantEntity restaurantEntity,
                                               LocalTime currentTime, Double latitude, Double longitude, Double servingRadiusInKms) {
        if (isOpenNow(currentTime, restaurantEntity)) {
            return GeoUtils.findDistanceInKm(latitude, longitude, restaurantEntity.getLatitude(),
                    restaurantEntity.getLongitude()) < servingRadiusInKms;
        }

        return false;
    }


}

