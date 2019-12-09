package com.example.serasel.eagle.Classes;

public class Student {
    private String stu_id, stu_name, stu_course, stu_imagePath;
    private int stu_count;

    public Student(){}

    public Student(String stu_id, String stu_name, String stu_course) {
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.stu_course = stu_course;
    }

    public Student(String stu_id, String stu_name, String stu_course, int stu_count,
                   String stu_imagePath) {
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.stu_course = stu_course;
        this.stu_count = stu_count;
        this.stu_imagePath = stu_imagePath;
    }

    public int getStu_count() {
        return stu_count;
    }

    public void setStu_count(int stu_count) {
        this.stu_count = stu_count;
    }

    public String getStu_id() {
        return stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public String getStu_course() {
        return stu_course;
    }

    public String getStu_imagePath() {
        return stu_imagePath;
    }

    public void setStu_imagePath(String stu_imagePath) {
        this.stu_imagePath = stu_imagePath;
    }
}
