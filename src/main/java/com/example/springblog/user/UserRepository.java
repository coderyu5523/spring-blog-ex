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

    public User findByUsernameAndPassword(UserRequest.LoginDTO requestDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username=? and password=?",User.class);
        query.setParameter(1,requestDTO.getUsername());
        query.setParameter(2,requestDTO.getPassword());
        User user = (User) query.getSingleResult();
        return user ;
    }

    public User findByUsername(String username) {
        Query query = em.createNativeQuery("select * from user_tb where username = ?",User.class);
        query.setParameter(1,username);
        try{
            User user = (User) query.getSingleResult();
            return user;
        }catch(Exception e){
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }

    }

    public User findByUsernameAndEmail(int id) {
        Query query = em.createNativeQuery("select username, email from user_tb where id=?",User.class);
        query.setParameter(1,id);

        try{
            User user = (User) query.getSingleResult();
            return user ;
        }catch(Exception e){
            throw new RuntimeException("데이터가 없습니다.");
        }


    }
    @Transactional
    public void passwordUpdate(UserRequest.passwordUpdateDTO requstDTO, int id) {
        Query query = em.createNativeQuery("update user_tb set password = ? where id = ?");
        query.setParameter(1,requstDTO.getPassword());
        query.setParameter(2,id);
        query.executeUpdate();
    }
}
