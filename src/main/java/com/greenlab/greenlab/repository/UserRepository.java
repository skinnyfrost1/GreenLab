package com.greenlab.greenlab.repository;
import java.util.List;
import com.greenlab.greenlab.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    
    User findByEmail(String email);
    User findByFirstname(String firstname);
    List<User> findByLastname(String lastname);
    User findByUid(String uid);
    // User findBy_Id(String _id);

}