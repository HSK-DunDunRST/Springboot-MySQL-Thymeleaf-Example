package com.hskgroup.example.entity;

import com.hskgroup.example.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
public class Question extends BaseTimeEntity {
    /* 질문 id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* 질문 제목 */
    @Column(length = 200)
    private String subject;

    /* 질문 내용 */
    @Column(columnDefinition = "TEXT")
    private String content;

    /* 질문에 해당되는 대답 id */
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}
