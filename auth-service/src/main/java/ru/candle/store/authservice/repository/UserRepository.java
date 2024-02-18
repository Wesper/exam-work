package ru.candle.store.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.candle.store.authservice.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Query(value = "UPDATE auth.users SET password = :password WHERE username = :username", nativeQuery = true)
    int updatePasswordByUsername(@Param("password") String password, @Param("username") String username);

    @Modifying
    @Query(value = "UPDATE auth.users SET role = :newrole WHERE username = :username", nativeQuery = true)
    int updateRoleByUsername(@Param("username") String userName, @Param("newrole") String newRole);
}