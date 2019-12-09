package com.example.serasel.eagle.Classes;

import java.io.Serializable;

/**
 * Created by Serasel on 05/07/2019.
 */

public class Exam implements Serializable{
    private String ex_difficulty, ex_genSubject, ex_name, ex_subtopic;
    private int ex_hours, ex_mins, ex_secs, ex_questions, ex_id;

    public Exam() {
    }

    public Exam(String ex_genSubject, String ex_difficulty, String ex_subtopic){
        this.ex_difficulty = ex_difficulty;
        this.ex_genSubject = ex_genSubject;
        this.ex_subtopic = ex_subtopic;

    }

    public Exam(String ex_difficulty, String ex_genSubject, int ex_hours, int ex_mins,
                int ex_secs, int ex_questions) {
        this.ex_difficulty = ex_difficulty;
        this.ex_genSubject = ex_genSubject;
        this.ex_hours = ex_hours;
        this.ex_mins = ex_mins;
        this.ex_secs = ex_secs;
        this.ex_questions = ex_questions;
    }

    public Exam(int ex_id, String ex_name, String ex_difficulty, String ex_genSubject, int ex_hours,
                int ex_mins, int ex_secs, int ex_questions) {
        this.ex_difficulty = ex_difficulty;
        this.ex_genSubject = ex_genSubject;
        this.ex_hours = ex_hours;
        this.ex_mins = ex_mins;
        this.ex_secs = ex_secs;
        this.ex_questions = ex_questions;
        this.ex_id = ex_id;
        this.ex_name = ex_name;
    }

    public String getEx_difficulty() {
        return ex_difficulty;
    }

    public String getEx_genSubject() {
        return ex_genSubject;
    }

    public int getEx_hours() {
        return ex_hours;
    }

    public int getEx_mins() {
        return ex_mins;
    }

    public int getEx_secs() {
        return ex_secs;
    }

    public int getEx_questions() {
        return ex_questions;
    }

    public int getEx_id() {
        return ex_id;
    }

    public String getEx_name() {
        return ex_name;
    }

    public String getEx_subtopic() {
        return ex_subtopic;
    }

    public void setEx_difficulty(String ex_difficulty) {
        this.ex_difficulty = ex_difficulty;
    }

    public void setEx_genSubject(String ex_genSubject) {
        this.ex_genSubject = ex_genSubject;
    }

    public void setEx_name(String ex_name) {
        this.ex_name = ex_name;
    }

    public void setEx_subtopic(String ex_subtopic) {
        this.ex_subtopic = ex_subtopic;
    }

    public void setEx_hours(int ex_hours) {
        this.ex_hours = ex_hours;
    }

    public void setEx_mins(int ex_mins) {
        this.ex_mins = ex_mins;
    }

    public void setEx_secs(int ex_secs) {
        this.ex_secs = ex_secs;
    }

    public void setEx_questions(int ex_questions) {
        this.ex_questions = ex_questions;
    }

    public void setEx_id(int ex_id) {
        this.ex_id = ex_id;
    }
}
