package com.example.springblog.reply;


import com.example.springblog.board.BoardResponse;
import com.example.springblog.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReplyRepository {
    private final EntityManager em;
    @Transactional
    public void save(ReplyRequest.WriteDTO requestDTO, int userId) {
        Query query = em.createNativeQuery("insert into reply_tb(comment,board_id,user_id,created_at) values(?,?,?,now()) ");
        query.setParameter(1,requestDTO.getComment());
        query.setParameter(2,requestDTO.getBoardId());
        query.setParameter(3,userId);
        query.executeUpdate();

    }

    public List<BoardResponse.ReplyDTO> findByBoardId(int boardId, User sessionUser) {
        Query query = em.createNativeQuery("select rt.id, rt.user_id, rt.comment, ut.username from reply_tb rt \n" +
                "inner join user_tb ut on rt.user_id = ut.id where rt.board_id = ?");


        query.setParameter(1,boardId);
        List<Object[]> obs = query.getResultList();
        //DB에서 받은 정보를 ReplyDTO에 로그인한 User 정보와 함께 받는다.
        return obs.stream().map(ob -> new BoardResponse.ReplyDTO(ob,sessionUser)).toList();

    }

    public Reply findById(int id) {
        Query query = em.createNativeQuery("select * from reply_tb where id =?",Reply.class);
        query.setParameter(1,id);

        try {
            return (Reply) query.getSingleResult();
        }catch (Exception e){
            return null ;
        }
    }
    @Transactional
    public void deleteById(int id) {
        Query query = em.createNativeQuery("delete from reply_tb where id = ?");
        query.setParameter(1,id);
        query.executeUpdate();
    }
}
