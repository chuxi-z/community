package com.firstpro.community.Controller;

import com.firstpro.community.dto.QuestionDTO;
import com.firstpro.community.exception.CustomizeErrorCode;
import com.firstpro.community.exception.CustomizeException;
import com.firstpro.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model){
        Long questionId = id;
//        try {
//            questionId = Long.parseLong(id);
//        } catch (NumberFormatException e) {
//            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
//        }
        QuestionDTO questionDTO = questionService.getById(questionId);

        //add views
        questionService.incView(questionId);
        model.addAttribute("question", questionDTO);
        return "question";
    }
}
