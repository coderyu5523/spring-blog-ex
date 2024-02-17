package com.example.springblog.board;


import com.example.springblog.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

public class BoardResponse {
    //조인문으로 받은 데이터를 받는 클래스
    @AllArgsConstructor
    @Data
    public  static class DetailDTO{
        private Integer id ;
        private String title;
        private String content ;
        private Timestamp createdAt;
        private Integer userId ;
        private String username;

    }

    public static class ReplyDTO{
        private Integer id ;
        private Integer userId ;
        private String username ;
        private String comment;

        public ReplyDTO(Object[] ob, User sessionUser){
            this.id = (Integer) ob[0];
            this.userId = (Integer) ob[1];
            this.comment = (String) ob[2];
            this.username = (String) ob[3];
        }

    }

}
