package com.code.mintyn.persistence.repository;



import com.code.mintyn.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findFirstByUsername(String username);





    Page<User> findUsersByDeletedFalse(Pageable pageable);

    Optional<User> findByIdAndDeletedFalse(long userId);



}
