package com.major.spoileralert.repository;

import com.major.spoileralert.entity.User;
import com.major.spoileralert.enumeration.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String token);

    Optional<User> findByResetToken(String token);

    Optional<User> findByUsername(String userName);

    List<User> findByDepartmentIdAndRole(Long id, UserRole userRole);

    List<User> findByDepartmentId(Long id);

}
