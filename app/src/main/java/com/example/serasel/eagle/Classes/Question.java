package com.example.serasel.eagle.Classes;

import java.io.Serializable;

/**
 * Created by Serasel on 05/06/2019.
 */

public class Question implements Serializable{
    private String q_question, q_answer, q_a, q_b, q_c, q_d, q_rationale, q_uid;

    public Question (){}

    public Question(String q_question, String q_answer, String q_a, String q_b, String q_c,
                    String q_d, String q_rationale, String q_uid) {
        this.q_question = q_question;
        this.q_answer = q_answer;
        this.q_a = q_a;
        this.q_b = q_b;
        this.q_c = q_c;
        this.q_d = q_d;
        this.q_rationale = q_rationale;
        this.q_uid = q_uid;
    }

    public String getQ_question() {
        return q_question;
    }

    public String getQ_answer() {
        return q_answer;
    }

    public String getQ_a() {
        return q_a;
    }

    public String getQ_b() {
        return q_b;
    }

    public String getQ_c() {
        return q_c;
    }

    public String getQ_d() {
        return q_d;
    }

    public String getQ_rationale() {
        return q_rationale;
    }

    public String getQ_uid() {
        return q_uid;
    }
}
