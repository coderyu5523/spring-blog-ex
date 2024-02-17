package com.example.springblog.board;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@RequiredArgsConstructor
@Repository
public class BoardRepository {

    private final EntityManager em ;

    //게시글 페이징
    public List<Board> findAll(int page) {
        final int COUNT = 3;
        int value = page*COUNT ;

        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?,?",Board.class);
        query.setParameter(1,value);
        query.setParameter(2,COUNT);
        List<Board> boardList = query.getResultList();
        return boardList;
    }

    public BoardResponse.DetailDTO findByIdWithUser(int id) {
        Query query = em.createNativeQuery("select bt.id, bt.title, bt.content, bt.created_at, bt.user_id, ut.username from board_tb bt inner join user_tb ut on bt.user_id =ut.id where bt.id =?");
        query.setParameter(1,id);

        JpaResultMapper rm = new JpaResultMapper();
        BoardResponse.DetailDTO responseDTO = rm.uniqueResult(query,BoardResponse.DetailDTO.class);
        return responseDTO;
    }
    @Transactional
    public void save(BoardRequest.saveDTO requestDTO, int id) {
        Query query =em.createNativeQuery("insert into board_tb(title,content,user_id,created_at) values (?,?,?,now())");
        query.setParameter(1,requestDTO.getTitle());
        query.setParameter(2,requestDTO.getContent());
        query.setParameter(3,id);
        query.executeUpdate();
    }
    public Board findByIdCheck(int id) {
        Query query = em.createNativeQuery("select * from board_tb where id = ?",Board.class);
        query.setParameter(1,id);
        Board board = (Board) query.getSingleResult();
        return board;
    }
    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete from board_tb where id =?");
        query.setParameter(1,id);
        query.executeUpdate();
    }

    @Transactional
    public void update(BoardRequest.updateDTO requestDTO, int id) {
        Query query = em.createNativeQuery("update board_tb set title = ? , content = ? where id = ?");
        query.setParameter(1,requestDTO.getTitle());
        query.setParameter(2,requestDTO.getContent());
        query.setParameter(3,id);
        query.executeUpdate();
    }
}
