package com.example.springblog.board;

import com.example.springblog.love.LoveRepository;
import com.example.springblog.love.LoveResponse;
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
    private final LoveRepository loveRepository ;

    @GetMapping({ "/"})
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "") String keyword) {

        if(keyword.isBlank()){
            List<Board> boardList = boardRepository.findAll(page);
            int count = boardRepository.count().intValue();

            int namerge = (count%3==0?0:1);
            int allPageCount = count/3 +namerge ;

            request.setAttribute("boardList",boardList);
            request.setAttribute("first",page==0);
            request.setAttribute("last",allPageCount==page+1);
            request.setAttribute("prevPage",page-1);
            request.setAttribute("nextPage",page+1);

        }else{
            List<Board> boardList = boardRepository.findAll(page,keyword);
            int count = boardRepository.count(keyword).intValue();

            int namerge = (count%3==0?0:1);
            int allPageCount = count/3 +namerge ;

            request.setAttribute("boardList",boardList);
            request.setAttribute("first",page==0);
            request.setAttribute("last",allPageCount==page+1);
            request.setAttribute("prevPage",page-1);
            request.setAttribute("nextPage",page+1);
        }


        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");



        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO boardDTO = boardRepository.findByIdWithUser(id);
        boardDTO.isBoardOwner(sessionUser);

        List<BoardResponse.ReplyDTO> replyDTOList = replyRepository.findByBoardId(id, sessionUser);
        request.setAttribute("board", boardDTO);
        request.setAttribute("replyList", replyDTOList);

        if(sessionUser == null){
            LoveResponse.DetailDTO loveDetailDTO = loveRepository.findLove(id);
            request.setAttribute("love", loveDetailDTO);
        }else{
            LoveResponse.DetailDTO loveDetailDTO = loveRepository.findLove(id, sessionUser.getId());
            request.setAttribute("love", loveDetailDTO);
        }

        // fas fa-heart text-danger
        // far fa-heart
        // request.setAttribute("css", "far fa-heart");

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


