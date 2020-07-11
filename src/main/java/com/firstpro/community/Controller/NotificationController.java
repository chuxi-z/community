package com.firstpro.community.Controller;

import com.firstpro.community.dto.NotificationDTO;
import com.firstpro.community.enums.NotificationEnum;
import com.firstpro.community.model.User;
import com.firstpro.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    @Resource
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Long id){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);

        if(NotificationEnum.REPLY_COMMENT.getType() == notificationDTO.getType() || NotificationEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }

        return "redirect:/";
    }
}
