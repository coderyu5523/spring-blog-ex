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
    public String join(UserRequest.JoinDTO requestDTO){

        try{
            userRepository.save(requestDTO);
        }catch (Exception e){
            throw new RuntimeException("아이디가 중복되었어요");
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
    public String updateForm(HttpServletRequest request) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        User user = userRepository.findByUsernameAndEmail(sessionUser.getId());

        request.setAttribute("sessionUser",user);

            return "user/updateForm";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.passwordUpdateDTO requstDTO,HttpServletRequest request){
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/loginForm";
        }
        if(sessionUser.getPassword().equals(requstDTO.getPassword())){
            request.setAttribute("msg","비밀번호가 동일합니다.");
            request.setAttribute("status",400);

            return "error/40x";
        }

        userRepository.passwordUpdate(requstDTO,sessionUser.getId());


        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";}

}



