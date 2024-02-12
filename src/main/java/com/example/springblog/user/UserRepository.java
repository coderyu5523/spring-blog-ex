package com.example.springblog.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
@RequiredArgsConstructor
@Repository
public class UserRepository {


    private final EntityManager em;
    @Transactional
    public void save(UserRequest.JoinDTO requestDTO) {
        Query query = em.createNativeQuery("insert into user_tb(username,password,email) values (?,?,?)");
        query.setParameter(1,requestDTO.getUsername());
        query.setParameter(2,requestDTO.getPassword());
        query.setParameter(3,requestDTO.getEmail());

        query.executeUpdate();
    }
}
