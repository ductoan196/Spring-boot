package com.example.travelbooking.repository;

import com.example.travelbooking.entity.RefreshToken;
import com.example.travelbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserAndRefreshTokenAndInvalidated(User user, String refreshToken, boolean invalidated);

    @Modifying
    @Query("update RefreshToken r set r.invalidated = true where r.user.id = :userId")
    void logOut(Long userId);

}
