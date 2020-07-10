package com.firstpro.community.Controller;

import com.firstpro.community.cache.TagCache;
import com.firstpro.community.dto.QuestionDTO;
import com.firstpro.community.mapper.UserMapper;
import com.firstpro.community.model.Question;
import com.firstpro.community.model.User;
import com.firstpro.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Resource
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    //修改
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name="id") Long id,
                       Model model){
        QuestionDTO  question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        return "/publish";
    }

    //新建
    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }

    //跳转回
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());

        if(title == null || title == ""){
            model.addAttribute("error", "title is null...");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error", "description is null...");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error", "tag is null...");
            return "publish";
        }

        //规范化标签
//        String invalid = TagCache.filterInvalid(tag);
//        if(StringUtils.isNotBlank(invalid)){
//            model.addAttribute("error", "tag is illegal..."+invalid);
//            return "publish";
//        }

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
