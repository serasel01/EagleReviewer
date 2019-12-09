package com.example.serasel.eagle.Utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.serasel.eagle.Activities.LoginActivity;
import com.example.serasel.eagle.Activities.QuizActivity;
import com.example.serasel.eagle.Classes.Exam;
import com.example.serasel.eagle.Classes.Question;
import com.example.serasel.eagle.Classes.Result;
import com.example.serasel.eagle.Classes.Student;
import com.example.serasel.eagle.Dialogs.DeleteDialog;
import com.example.serasel.eagle.Dialogs.DetailsDialog;
import com.example.serasel.eagle.Dialogs.DownloadsDialog;
import com.example.serasel.eagle.Dialogs.ExamDialog;
import com.example.serasel.eagle.Dialogs.NameDialog;
import com.example.serasel.eagle.Dialogs.SearchDialog;
import com.example.serasel.eagle.Dialogs.SubjectDialog;
import com.example.serasel.eagle.Fragments.HomeFragment;
import com.example.serasel.eagle.Fragments.ResultFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//class specifically for calling firebase functions

public class FirebaseCaller {
    private DatabaseReference db_ref;
    private Query query_ref;
    private Context context;

    public FirebaseCaller(Context context, DatabaseReference db_ref){
        this.db_ref = db_ref;
        this.context = context;
    }

    public FirebaseCaller(Context context, Query query_ref){
        this.query_ref = query_ref;
        this.context = context;
    }

    public void verifyUser(final String id, final String password,
                      final LoginActivity.FirebaseLoginCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gets id and password from edittext fields
                String db_id = dataSnapshot.child("stu_id").getValue().toString();
                String db_password = dataSnapshot.child("stu_password").getValue().toString();


                if(db_id.equals(id) && db_password.equals(password)){
                    Student log_student = dataSnapshot.getValue(Student.class);

                    //interface callback
                    fb_call.onLoginCallback(log_student);
                }

                else {
                    Toast.makeText(context, "Invalid id number or password",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getSubjects(final SubjectDialog.FirebaseSubjectCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> subjects = new ArrayList<>(); //gets list of object defined in <>

                //left side: gets the children by a snapshot
                //right side: getChildren returns children under that reference
                //reads all then gets one by one the keys (key : value)
                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){ //syntax of for each
                    subjects.add(subjectSnapshot.getKey()); //adds to arraylist the key
                }
                fb_call.onSubjectCallback(subjects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getSubjects(final ResultFragment.FirebaseSubjectCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> subjects = new ArrayList<>();
                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){
                    subjects.add(subjectSnapshot.getKey());
                }
                fb_call.onSubjectCallback(subjects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getSubjects(final ExamDialog.FirebaseSubjectCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> subjects = new ArrayList<>();
                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){
                    subjects.add(subjectSnapshot.getKey());
                }
                fb_call.onSubjectCallback(subjects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getSubjects(final HomeFragment.FirebaseSubjectCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> subjects = new ArrayList<>();
                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){
                    subjects.add(subjectSnapshot.getKey());
                }
                fb_call.onSubjectCallback(subjects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getSubjectsForKeyword(final String keyword, final SearchDialog.SubjectKeywordCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Exam> exams = new ArrayList<>();
                String gen_subject, subtopic, difficulty;

                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){

                    for (DataSnapshot topicSnapshot : subjectSnapshot.getChildren()){

                        for (DataSnapshot difficultySnapshot : topicSnapshot.getChildren()){

                            for (DataSnapshot keywordSnapshot : difficultySnapshot.getChildren()){
                                if (keyword.equals(keywordSnapshot.getValue().toString())){
                                    gen_subject = subjectSnapshot.getKey();
                                    subtopic = topicSnapshot.getKey();
                                    difficulty = difficultySnapshot.getKey();

                                    Exam exam = new Exam(gen_subject, difficulty, subtopic);
                                    exams.add(exam);
                                }
                            }
                        }
                    }
                }
                fb_call.onSubjectKeywordCallback(exams);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getTopics(final ResultFragment.FirebaseTopicCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> topics = new ArrayList<>();
                topics.add("");
                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){
                    topics.add(subjectSnapshot.getKey());
                }
                fb_call.onTopicCallback(topics);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getResultTopics(final DetailsDialog.TopicCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> topics = new ArrayList<>();
                for(DataSnapshot subjectSnapshot : dataSnapshot.getChildren()){
                    topics.add(subjectSnapshot.getValue().toString());
                }
                fb_call.onTopicCallback(topics);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }


    public void getQuestion(final QuizActivity.QuizQuestionCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Question question = dataSnapshot.getValue(Question.class);
                    fb_call.onQuestionCallback(question);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getQuestion(final NameDialog.QuizQuestionCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Question question = dataSnapshot.getValue(Question.class);
                    fb_call.onQuestionCallback(question);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getQuestionCount(final QuizActivity.QuizCountCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> uids = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for(DataSnapshot uidSnap : dataSnapshot.getChildren()){
                        uids.add(uidSnap.getKey());
                    }

                    fb_call.onCountCallback(uids);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getQuestionCount(final NameDialog.QuizCountCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> uids = new ArrayList<>();
                if(dataSnapshot.exists()){
                    for(DataSnapshot uidSnap : dataSnapshot.getChildren()){
                        uids.add(uidSnap.getKey());
                    }

                    fb_call.onCountCallback(uids);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getResult(final String subject, final HomeFragment.GenResultCallback fb_call){
        query_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int total_questions = 0, total_corrects = 0;
                double total_percentage;
                Result result;

                for(DataSnapshot resultSnapshot : dataSnapshot.getChildren()){
                    if(resultSnapshot.child("res_subject").getValue().toString().equals(subject)){
                        total_questions = total_questions +
                                Integer.parseInt(resultSnapshot.child("res_questions").getValue()
                                        .toString());
                        total_corrects = total_corrects +
                                Integer.parseInt(resultSnapshot.child("res_corrects").getValue()
                                        .toString());
                    }
                }

                if(total_corrects != 0 && total_questions != 0){
                    total_percentage = (total_corrects / total_questions) * 100;
                    result = new Result(total_questions, total_corrects, total_percentage, subject);
                    fb_call.onResultCallback(result);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getResults(final String subtopic, final String subject,
                           final ResultFragment.FirebaseResultCallback fb_call){

        query_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Result> results = new ArrayList<>();

                for(DataSnapshot resultSnapshot : dataSnapshot.getChildren()){
                    //check if result is at the right general subject
                    if(resultSnapshot.child("res_subject").getValue().toString().equals(subject)
                            && subtopic.equals("")){

                        Result result = resultSnapshot.getValue(Result.class);
                        results.add(result);

                    }

                    //check if user chose a specific subtopic and right subject
                    //1st condition: check if correct subject
                    //2nd: check if only one topic
                    //3rd: check if correct topic
                    else if(resultSnapshot.child("res_subject").getValue().toString().equals(subject)
                            && resultSnapshot.child("Subtopics").getChildrenCount() == 1
                            && resultSnapshot.child("Subtopics").child("1").getValue().toString()
                            .equals(subtopic)){

                        Result result = resultSnapshot.getValue(Result.class);
                        results.add(result);
                    }
                }

                fb_call.onResultCallback(results);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getExams(final DownloadsDialog.ExamCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Exam> exams = new ArrayList<>();

                for(DataSnapshot examSnapshot : dataSnapshot.getChildren()){
                    Exam exam = examSnapshot.getValue(Exam.class);
                    exams.add(exam);
                }

                fb_call.onExamCallback(exams);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getExams(final DeleteDialog.ExamCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> exams = new ArrayList<>();

                for(DataSnapshot examSnapshot : dataSnapshot.getChildren()){
                    Exam exam = examSnapshot.getValue(Exam.class);
                    exams.add(exam.getEx_name());
                }

                fb_call.onExamCallback(exams);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getExam(final DownloadsDialog.DoExamCallback fb_call){
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> subtopics = new ArrayList<>();
                Exam exam = dataSnapshot.getValue(Exam.class);

                for (DataSnapshot topicSnapshot : dataSnapshot.child("Subtopics").getChildren()){
                    subtopics.add(topicSnapshot.getValue().toString());
                }

                fb_call.onExaminationCallback(exam, subtopics);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void checkExamName(final NameDialog.ExamNameCallback fb_call){
        db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fb_call.onExamNameCallback(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

}
