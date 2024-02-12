package com.example.springblog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@RequiredArgsConstructor
@Controller
public class Usercontroller {

    private final UserRepository userRepository ;
    private final HttpSession session;

    @Transactional
    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO,HttpServletRequest request){

        User user = userRepository.findByUsername(requestDTO.getUsername());
        if(user==null){
            userRepository.save(requestDTO);
        }else{
            request.setAttribute("msg","아이디가 중복됩니다." +
                    "");
            request.setAttribute("status",400);
            return "error/40x";
        }


        return "redirect:/loginForm";
    }
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO,HttpServletRequest request){

       User user = userRepository.findByUsernameAndPassword(requestDTO);
        if(user==null){
            request.setAttribute("msg","로그인이 필요합니다");
            request.setAttribute("status",400);
            return "error/40x";
        }else{
            session.setAttribute("sessionUser",user);
            return"redirect:/";
        }


    }




    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {return "redirect:/";}

}



