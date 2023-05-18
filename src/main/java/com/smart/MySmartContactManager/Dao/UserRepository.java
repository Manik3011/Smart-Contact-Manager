package com.smart.MySmartContactManager.Dao;

import com.smart.MySmartContactManager.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select u from User u where u.email =:email")
    public User getUserBuUsername(@Param("email")String email);

}
