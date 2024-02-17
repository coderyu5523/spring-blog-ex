package com.example.springblog.board;

import com.example.springblog.reply.ReplyRepository;
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
    private final ReplyRepository replyRepository ;

    @GetMapping({ "/"})
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
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/loginForm";
        }

        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id,HttpServletRequest request) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        //DB데이터를 DetailDTO에 받음
        BoardResponse.DetailDTO boardDTO = boardRepository.findByIdWithUser(id);

        //DB데이터를 ReplyDTO에 받음
        List<BoardResponse.ReplyDTO> replyDTOList = replyRepository.findByBoardId(id,sessionUser);

        request.setAttribute("board", boardDTO);
        request.setAttribute("replyList",replyDTOList);


        return "board/detail";

    }


    @PostMapping("/board/save")
    private String saveWrite(BoardRequest.saveDTO requestDTO){
        User sessionUser = (User) session.getAttribute("sessionUser");

        if(sessionUser==null){
            return "redirect:/loginForm";
        }

        boardRepository.save(requestDTO,sessionUser.getId());

        return "redirect:/";

   }
   @PostMapping("/board/{id}/delete")
   public String delete(@PathVariable int id,HttpServletRequest request){
       User sessionUser = (User) session.getAttribute("sessionUser");
       if(sessionUser==null){
           return "redirect:/loginForm";
       }
       Board board =boardRepository.findByIdCheck(id);

       if(board.getUserId()!=sessionUser.getId()){
           request.setAttribute("status",403);
           request.setAttribute("msg","게시글을 삭제할 권한이 없습니다.");
           return "error/40x";
       }
       boardRepository.deleteById(id);

       return "redirect:/";
   }
   @GetMapping("/board/{id}/updateForm")
   public String updateForm(@PathVariable int id,HttpServletRequest request){
       User sessionUser = (User) session.getAttribute("sessionUser");
       if(sessionUser==null){
           return "redirect:/loginForm";
       }
       Board board = boardRepository.findByIdCheck(id);

       if(board.getUserId()!=sessionUser.getId()){
           request.setAttribute("status",403);
           request.setAttribute("msg","게시글을 수정할 권한이 없습니다.");
           return "error/40x";
       }

       request.setAttribute("board",board);

        return "board/updateForm";
   }
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id,BoardRequest.updateDTO requestDTO,HttpServletRequest request){
        //1. 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/loginForm";
        }
        //DB 조회
        Board board = boardRepository.findByIdCheck(id);

        //2. 권한 체크
        if(board.getUserId()!=sessionUser.getId()){
            request.setAttribute("status",403);
            request.setAttribute("msg","게시글을 수정할 권한이 없습니다.");
            return "error/40x";
        }

        //3. 핵심 로직
        boardRepository.update(requestDTO,id);
        return "redirect:/board/"+id;

    }


}


