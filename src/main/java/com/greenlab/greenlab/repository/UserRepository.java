package com.greenlab.greenlab.repository;
import java.util.List;
import com.greenlab.greenlab.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    
    public User findByEmail(String email);
    public User findByFirstname(String firstname);
    public List<User> findByLastname(String lastname);

}