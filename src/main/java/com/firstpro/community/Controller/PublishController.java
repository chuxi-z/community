package com.firstpro.community.Controller;

import com.firstpro.community.dto.QuestionDTO;
import com.firstpro.community.mapper.QuestionMapper;
import com.firstpro.community.mapper.UserMapper;
import com.firstpro.community.model.Question;
import com.firstpro.community.model.User;
import com.firstpro.community.service.QuestionService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Resource
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id") Integer id,
                       Model model){
        QuestionDTO  question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id", question.getId());
        return "/publish";
    }

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam(value = "id", required = false) Integer id,
            HttpServletRequest request,
            Model model){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if(title == null || title == ""){
            model.addAttribute("error", "title is null...");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error", "description is null...");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error", "tag  is null...");
            return "publish";
        }

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
            model.addAttribute("error", "account is not Login...");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
//        question.setGmtCreate(System.currentTimeMillis());
//        question.setGmtModified(question.getGmtCreate());
        question.setId(id);

        questionService.createOrUpdate(question);

        return "redirect:/";
    }
}
