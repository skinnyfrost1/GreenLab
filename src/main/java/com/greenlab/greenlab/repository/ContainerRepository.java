package com.greenlab.greenlab.repository;

import com.greenlab.greenlab.model.Container;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContainerRepository extends MongoRepository<Container, String> {
    Container findBy_Id(String _id);
}