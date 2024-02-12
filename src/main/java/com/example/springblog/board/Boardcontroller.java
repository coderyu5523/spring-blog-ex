package com.example.springblog.board;

import com.example.springblog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class Boardcontroller {

    private final BoardRepository boardRepository;
    private final HttpSession session;

    @GetMapping({ "/", "/board" })
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {

        List<Board> boardList = boardRepository.findAll(page);
        request.setAttribute("boardList",boardList);

        int currentPage = page ;
        int nextPage = currentPage +1;
        int prevPage = currentPage -1;

        request.setAttribute("nextPage",nextPage);
        request.setAttribute("prevPage",prevPage);
        int totalCount = 4 ;

        boolean first = (currentPage==0?true:false);
        request.setAttribute("first",first);
        int totalPage = totalCount/3;
        if(totalCount%3==0){
            int lastPage = totalPage -1;
            boolean last = (currentPage==lastPage?true:false);
            request.setAttribute("last",last);
        }else if(totalCount%3!=0){
            int lastPage = totalPage ;
            boolean last = (currentPage==lastPage?true:false);
            request.setAttribute("last",last);
        }

        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id,HttpServletRequest request) {
        BoardResponse.DetailDTO responseDTO = boardRepository.findId(id);
        boolean owner = false ;
        int boardUserId = responseDTO.getUserId();

        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser!=null){
            if(boardUserId==sessionUser.getId()){
                owner = true;
            }
            request.setAttribute("owner",owner);
        }
        request.setAttribute("board",responseDTO);
        return "board/detail";
    }
    @PostMapping("/board/save")
    private String saveWrite(BoardRequest.saveDTO requestDTO){
        User sessionUser = (User) session.getAttribute("sessionUser");

        boardRepository.save(requestDTO,sessionUser.getId());

        return "redirect:/";

   }


}


