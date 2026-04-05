package com.example.ColConnect.repo;

import com.example.ColConnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  UserRepository extends JpaRepository<User,Long> {
    public User findByName(String name);

    //search bar
    @Query("select u from User u where LOWER(u.name) like LOWER(concat('%', :keyword , '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);

}
