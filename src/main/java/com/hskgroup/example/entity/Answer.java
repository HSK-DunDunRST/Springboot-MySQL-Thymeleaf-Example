package com.hskgroup.example.entity;

import com.hskgroup.example.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer extends BaseTimeEntity {
    /* 답변 id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /* 답변 내용 */
    @Column(columnDefinition = "TEXT")
    private String content;

    /* 답변에 해당되는 질문 id */
    @ManyToOne
    private Question question;
}
