package com.firstpro.community.Controller;

import com.firstpro.community.dto.PaginationDTO;
import com.firstpro.community.dto.QuestionDTO;
import com.firstpro.community.mapper.QuestionMapper;
import com.firstpro.community.mapper.UserMapper;
import com.firstpro.community.model.Question;
import com.firstpro.community.model.User;
import com.firstpro.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    @Resource
    UserMapper userMapper;

    @Resource
    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name="page", defaultValue = "1")Integer page,
                        @RequestParam(name="size", defaultValue = "5")Integer size){
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null && cookies.length != 0){
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("token")) {
//                    String token = cookie.getValue();
//                    User user = userMapper.findByToken(token);
//                    if(user != null){
//                        request.getSession().setAttribute("user", user);
//                    }
//                    break;
//                }
//            }
//        }

        PaginationDTO pagination = questionService.list(page, size);
        model.addAttribute("pagination", pagination);

        return "index";
    }

}
