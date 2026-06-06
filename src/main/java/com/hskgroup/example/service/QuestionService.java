package com.hskgroup.example.service;

import com.hskgroup.example.entity.Answer;
import com.hskgroup.example.entity.Question;
import com.hskgroup.example.exception.DataNotFoundException;
import com.hskgroup.example.repository.QuestionRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    /* 질문 리스트 불러오기 */
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        
        if (kw == null || kw.trim().isEmpty()) {
            return this.questionRepository.findAll(pageable);
        } else {
            Specification<Question> spec = search(kw);
            return this.questionRepository.findAll(spec, pageable);
        }
    }

    /* 검색 조건 생성 */
    @SuppressWarnings("null")
    private Specification<Question> search(String kw) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);  // 중복 제거
            Join<Question, Answer> answerJoin = root.join("answerList", JoinType.LEFT);
            return criteriaBuilder.or(
                criteriaBuilder.like(root.get("subject"), "%" + kw + "%"), // 제목
                criteriaBuilder.like(root.get("content"), "%" + kw + "%"), // 내용
                criteriaBuilder.like(answerJoin.get("content"), "%" + kw + "%") // 답변 내용
            );
        };
    }

    /* id로 질문 불러오기 */
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("데이터를 찾을 수 없어요.");
        }
    }

    /* 질문 등록 서비스 */
    public void create(String subject, String content) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        this.questionRepository.save(question);
    }

    /* 질문 내용 수정 서비스 */
    public void modify(Question question, String subject, String content){
        question.setSubject(subject);
        question.setContent(content);
        this.questionRepository.save(question);
    }

    /* 질문 내용 삭제 서비스 */
    public void delete(Question question){
        this.questionRepository.delete(question);
    }
}
