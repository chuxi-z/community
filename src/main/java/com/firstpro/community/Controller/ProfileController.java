package com.firstpro.community.Controller;

import com.firstpro.community.dto.PaginationDTO;
import com.firstpro.community.model.User;
import com.firstpro.community.service.NotificationService;
import com.firstpro.community.service.QuestionService;
import com.sun.javaws.IconUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.PseudoColumnUsage;

@Controller
public class ProfileController {

    @Resource
    @Autowired
    QuestionService questionService;

    @Autowired
    @Resource
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name="page", defaultValue = "1")Integer page,
                          @RequestParam(name="size", defaultValue = "5")Integer size){
//        replaced by interceptor in controller
//        User user = null;
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null && cookies.length != 0){
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("token")) {
//                    String token = cookie.getValue();
//                    user = userMapper.findByToken(token);
//                    if(user != null){
//                        request.getSession().setAttribute("user", user);
//                    }
//                    break;
//                }
//            }
//        }

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }

        if("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "My questions");
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        }else if("replies".equals(action)){
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            Long unreadCount = notificationService.unreadCount(user.getId());

            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "My replies");
        }


        return "profile";
    }
}
