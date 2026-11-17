package soa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import soa.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {

    Users findByUsernameAndPassword(String username, String password);
}