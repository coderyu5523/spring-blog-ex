package com.example.springblog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@RequiredArgsConstructor
@Controller
public class Usercontroller {

    private final UserRepository userRepository ;

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO){

        userRepository.save(requestDTO);

        return "redirect:/loginForm";
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



