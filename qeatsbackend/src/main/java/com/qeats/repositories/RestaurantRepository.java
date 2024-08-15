package com.qeats.repositories;

import com.qeats.models.RestaurantEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RestaurantRepository extends MongoRepository<RestaurantEntity, String> {

    @Query("{'name': {$regex: '^?0$', $options: 'i'}}")
    Optional<List<RestaurantEntity>> findRestaurantsByNameExact(String name);

    @Query("{'name': {$regex: '.*?0.*', $options: 'i'}}")
    Optional<List<RestaurantEntity>> findRestaurantsByName(String restaurantName);

    @Query(value = "{'attributes' : {$elemMatch : {$regex : '*?0.*', $options: 'i'}}}")
    Optional<List<RestaurantEntity>> findRestaurantsByAttribute(String attribute);

    Optional<List<RestaurantEntity>> findRestaurantsByRestaurantIdIn(List<String> restaurnatIdList);
}

