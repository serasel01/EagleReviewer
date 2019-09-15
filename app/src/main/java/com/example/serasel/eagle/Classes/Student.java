package com.example.serasel.eagle.Classes;

public class Student {
    private String stu_id, stu_name, stu_course;

    public Student(){}

    public Student(String stu_id, String stu_name, String stu_course) {
        this.stu_id = stu_id;
        this.stu_name = stu_name;
        this.stu_course = stu_course;
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
}
