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

        try {
            User user = (User) query.getSingleResult();
            return user ;

        } catch (Exception e) {
            throw new RuntimeException("아이디 혹은 비밀번호를 찾을 수 없습니다.") ;
            // 모든 스로우를 @ControllerAdvice로 던짐
        }
    }


    public User findByUsername(String username) {
        Query query = em.createNativeQuery("select * from user_tb where username=?", User.class); // 알아서 매핑해줌
        query.setParameter(1, username);

        try { // 내부적으로 터지면 터지는 위치를 찾아서 내가 잡으면 됨
            User user = (User) query.getSingleResult();
            return user;
        } catch (Exception e) {
            return null ;
        }
    }

    public User findByUsernameAndEmail(int id) {
        Query query = em.createNativeQuery("select username, email from user_tb where id=?",User.class);
        query.setParameter(1,id);

        try{
            User user = (User) query.getSingleResult();
            return user ;
        }catch(Exception e){
            return null ;
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
