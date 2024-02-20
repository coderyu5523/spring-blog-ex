package com.example.springblog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
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
        //비밀번호 암호화
        String rawPassword = requestDTO.getPassword();
        String encPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        requestDTO.setPassword(encPassword);

        if(requestDTO.getUsername().length()<3){
            throw new RuntimeException("아이디는 3자리 이상이어야 합니다.");
        }

        //유저네임 중복여부 확인
        try{
            userRepository.save(requestDTO);
        }catch (Exception e){
            throw new RuntimeException("아이디가 중복되었어요");
        }

        try{
            userRepository.save(requestDTO);
        }catch (Exception e){
            throw new RuntimeException("아이디가 중복되었어요");
        }

        return "redirect:/loginForm";
    }
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO,HttpServletRequest request){
        // 유효성 검사
        if(requestDTO.getUsername().length()<3){
            throw new RuntimeException("아이디는 3자리 이상이어야 합니다.");
        }
        // 유저네임으로 데이터를 가져옴
        User user = userRepository.findByUsername(requestDTO.getUsername());

        // 비밀번호 비교
        if(!BCrypt.checkpw(requestDTO.getPassword(),user.getPassword())){
            // !가 있으면 역치.
            throw new RuntimeException("패스워드가 틀렸습니다.");
        }
        session.setAttribute("sessionUser",user);

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
    public String update(UserRequest.passwordUpdateDTO requestDTO,HttpServletRequest request){
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser==null){
            return "redirect:/loginForm";
        }

        if(BCrypt.checkpw(requestDTO.getPassword(),sessionUser.getPassword())){
            throw new RuntimeException("패스워드가 틀렸습니다.");
        }

        userRepository.passwordUpdate(requestDTO,sessionUser.getId());


        return "redirect:/";
    }


    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";}

}



