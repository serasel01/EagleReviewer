package com.example.serasel.eagle.Classes;

/**
 * Created by Serasel on 11/06/2019.
 */

public class Result {
    private String res_subject, res_difficulty, res_date;
    private int res_questions, res_corrects, res_wrongs;
    private double res_percent;
    private Long res_tsMillis;

    public Result() {
    }

    public Result(String res_subject, String res_difficulty, int res_questions, int res_corrects,
                  int res_wrongs, double res_percent, String res_date, Long res_tsMillis) {
        this.res_subject = res_subject;
        this.res_difficulty = res_difficulty;
        this.res_questions = res_questions;
        this.res_corrects = res_corrects;
        this.res_wrongs = res_wrongs;
        this.res_percent = res_percent;
        this.res_date = res_date;
        this.res_tsMillis = res_tsMillis;
    }

    public Result(int res_questions, int res_corrects, double res_percent, String res_subject){
        this.res_questions = res_questions;
        this.res_corrects = res_corrects;
        this.res_percent = res_percent;
        this.res_subject = res_subject;
    }

    public String getRes_subject() {
        return res_subject;
    }

    public String getRes_difficulty() {
        return res_difficulty;
    }

    public int getRes_questions() {
        return res_questions;
    }

    public int getRes_corrects() {
        return res_corrects;
    }

    public int getRes_wrongs() {
        return res_wrongs;
    }

    public double getRes_percent() {
        return res_percent;
    }

    public String getRes_date() {
        return res_date;
    }

    public Long getRes_tsMillis() {
        return res_tsMillis;
    }
}
