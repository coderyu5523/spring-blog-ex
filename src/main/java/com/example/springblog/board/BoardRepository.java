package com.example.springblog.board;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@RequiredArgsConstructor
@Repository
public class BoardRepository {

    private final EntityManager em ;

    public List<Board> findAll(int page) {
        final int COUNT = 3;
        int value = page*COUNT ;

        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?,?",Board.class);
        query.setParameter(1,value);
        query.setParameter(2,COUNT);
        List<Board> boardList = query.getResultList();
        return boardList;
    }
}
