package com.example.springblog.user;

import com.example.springblog._core.util.ApiUtil;
import com.example.springblog.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final UserRepository userRepository;

    @GetMapping("/api/username-same-check")
    public @ResponseBody ApiUtil<?> usernameSameCheck(String username){
        User user =userRepository.findByUsername(username);
        if(user==null){
            return new ApiUtil<>(false);
        }else {
            return new ApiUtil<>(true);
        }

    }

}
