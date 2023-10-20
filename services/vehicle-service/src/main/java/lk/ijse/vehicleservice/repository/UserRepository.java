package lk.ijse.vehicleservice.repository;

import lk.ijse.vehicleservice.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    public User findUserByUsername(String username);
    public User findUserByEmail(String email);

    public List<User> findUsersByRole(String role);

}
