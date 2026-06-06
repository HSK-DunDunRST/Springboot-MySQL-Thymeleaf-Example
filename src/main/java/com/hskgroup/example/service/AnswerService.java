package com.hskgroup.example.service;

import com.hskgroup.example.entity.Answer;
import com.hskgroup.example.entity.Question;
import com.hskgroup.example.exception.DataNotFoundException;
import com.hskgroup.example.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    /* 답변글 생성하는 서비스 */
    public Answer create(Question question, String content) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setQuestion(question);
        this.answerRepository.save(answer);
        return answer;
    }

    /* ID로 답변 조회하기 */
    public Answer getAnswer(Integer id){
        Optional<Answer> answer = this.answerRepository.findById(id);
        
        if (answer.isPresent()){
            return answer.get();
        } else {
            throw new DataNotFoundException("데이터를 조회할 수 없어요.");
        }
    }

    /* 답변 수정 서비스 */
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        this.answerRepository.save(answer);
    }
    /* 답변 삭제 서비스 */
    public void delete(Answer answer){
        this.answerRepository.delete(answer);
    }
}
