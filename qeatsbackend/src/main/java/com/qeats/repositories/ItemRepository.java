
package com.qeats.repositories;

import com.qeats.models.ItemEntity;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<ItemEntity, String> {

}

