package com.example.springblog.user;

import com.example.springblog._core.util.ApiUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.JDBCType;

@RequiredArgsConstructor
@Controller
public class Usercontroller {

    private final UserRepository userRepository ;
    private final HttpSession session;

    @GetMapping("/api/username-same-check")
    public @ResponseBody ApiUtil<?> usernameSameCheck(String username){ //@ResponseBody 데이터 리턴을 위해
        User user= userRepository.findByUsername(username);
        if(user==null){
            return new ApiUtil<>(false);
        }else {
            return new ApiUtil<>(true);
        }

    }


    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO){

        String rawPassword = requestDTO.getPassword();
        String encPassword = BCrypt.hashpw(rawPassword,BCrypt.gensalt());
        requestDTO.setPassword(encPassword);


        try{
            userRepository.save(requestDTO);
        }catch (Exception e){
            throw new RuntimeException("아이디가 중복되었어요");
        }

        return "redirect:/loginForm";
    }
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO){


        User user = userRepository.findByUsername(requestDTO.getUsername());

        if(requestDTO.getUsername().length()<3){
            throw new RuntimeException("유저네임이 너무 짧습니다");
        }

        if(!BCrypt.checkpw(requestDTO.getPassword(),user.getPassword())){
            // !가 있으면 역치. 패스워드 검증이 실패하면
            throw new RuntimeException("패스워드가 틀렸습니다.");
        }

       session.setAttribute("sessionUser",user);

            // ! 안쓴 긍정문일 때
//        if(BCrypt.checkpw(requestDTO.getPassword(),user.getPassword())){
//            session.setAttribute("sessionUser",user);
//        }else{
//            throw new RuntimeException("패스워드가 틀렸습니다.");
//        }


       return"redirect:/";

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



