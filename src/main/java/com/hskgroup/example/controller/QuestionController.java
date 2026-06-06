package com.hskgroup.example.controller;

import com.hskgroup.example.entity.Question;
import com.hskgroup.example.entity.form.AnswerForm;
import com.hskgroup.example.entity.form.QuestionForm;
import com.hskgroup.example.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;  // 질문 처리 서비스

    /* 질문 목록 페이지 요청 처리 (페이지 및 검색어 파라미터 포함) */
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> questionPage = this.questionService.getList(page, kw);
        model.addAttribute("questionPage", questionPage);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    /* 특정 질문의 상세 페이지 요청 처리 */
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);  // 질문 정보 가져오기
        model.addAttribute("question", question);  // 모델에 질문 정보 추가
        return "question_detail";  // 질문 상세 페이지 반환
    }

    /* 질문 생성 페이지 요청 처리*/
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";  // 질문 작성 폼 페이지 반환
    }

    /* 질문 생성 요청 처리 */
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "question_form";  // 폼에 오류가 있는 경우 작성 폼 페이지로 돌아가기
        }
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());  // 질문 생성
        return "redirect:/question/list";  // 질문 목록 페이지로 리디렉션
    }

    /* 질문 수정 페이지 요청 처리 */
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);  // 질문 정보 가져오기
        questionForm.setSubject(question.getSubject());  // 기존 질문 제목 설정
        questionForm.setContent(question.getContent());  // 기존 질문 내용 설정
        return "question_form";  // 질문 수정 폼 페이지 반환
    }

    /* 질문 수정 요청 처리 */
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                    @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";  // 폼에 오류가 있는 경우 작성 폼 페이지로 돌아가기
        }
        Question question = this.questionService.getQuestion(id);  // 질문 정보 가져오기

        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());  // 질문 수정
        return String.format("redirect:/question/detail/%s", id);  // 수정된 질문 상세 페이지로 리디렉션
    }

    /* 질문 삭제 페이지 요청 처리 */
    @GetMapping("/delete/{id}")
    public String questionDelete(@PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);  // 질문 정보 가져오기
        this.questionService.delete(question);  // 질문 삭제
        return "redirect:/";  // 홈 페이지로 리디렉션
    }

}
