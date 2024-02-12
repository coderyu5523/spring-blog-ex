package com.example.springblog.board;


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

}
