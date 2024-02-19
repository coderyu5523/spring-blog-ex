package com.example.springblog.reply;

import com.example.springblog.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final HttpSession session ;
    private final ReplyRepository replyRepository ;
    @PostMapping("/reply/save")
    public String write(ReplyRequest.WriteDTO requestDTO){

        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/loginForm";
        }

        // 유효성 검사

        // 핵심 로직

        replyRepository.save(requestDTO,sessionUser.getId());

        return "redirect:/board/"+requestDTO.getBoardId();
    }
    @PostMapping("/reply/{id}/delete")
    public String delete(@PathVariable int id,HttpServletRequest request){
        //로그인 확인
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/loginForm";
        }

        Reply reply = replyRepository.findById(id);

        if(reply==null){
            request.setAttribute("msg","페이지를 찾을 수 없습니다.");
            request.setAttribute("status",404);
            return "error/40x";
        }
        if(reply.getUserId()!=sessionUser.getId()){
            request.setAttribute("msg","권한이 없습니다.");
            request.setAttribute("status",403);
            return "error/40x";
        }

        replyRepository.deleteById(id);

        return "redirect:/board/"+reply.getBoardId();
    }

}
