
package com.greenlab.greenlab.repository;

import com.greenlab.greenlab.model.Photo;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface PhotoRepository extends MongoRepository<Photo, String> {
    Photo findByTitle(String title);
}