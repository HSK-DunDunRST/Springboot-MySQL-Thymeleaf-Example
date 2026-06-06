package com.hskgroup.example.controller;

import com.hskgroup.example.entity.Answer;
import com.hskgroup.example.entity.Question;
import com.hskgroup.example.entity.form.AnswerForm;
import com.hskgroup.example.service.AnswerService;
import com.hskgroup.example.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    /* POST 방식의 답변 생성 처리 */
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                                @Valid AnswerForm answerForm,
                                BindingResult bindingResult) {
        Question question = this.questionService.getQuestion(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "question_detail";
        }
        Answer answer = this.answerService.create(question,answerForm.getContent());
        return String.format("redirect:/question/detail/%s#answer",answer.getQuestion().getId());
    }

    /* GET 방식의 답변 수정 처리 */
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }

    /* POST 방식의 답변 수정 처리 */
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id){
        if (bindingResult.hasErrors()){
            return "answer_form";
        }
        /* 답변 객체 생성 */
        Answer answer = this.answerService.getAnswer(id);
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s#answer",answer.getQuestion().getId());
    }

    /* GET 방식 답변 삭제 처리 */
    @GetMapping("/delete/{id}")
    public String answerDelete(@PathVariable("id") Integer id){
        Answer answer = this.answerService.getAnswer(id);
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s",answer.getQuestion().getId());
    }
}
