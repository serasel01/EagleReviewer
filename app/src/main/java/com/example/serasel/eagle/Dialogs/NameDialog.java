package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serasel.eagle.Classes.Exam;
import com.example.serasel.eagle.Classes.Question;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class NameDialog extends Dialog implements android.view.View.OnClickListener{
    private CardView btn_name_save, btn_name_cancel;
    private TextView et_name_exam;
    private Activity activity;
    private String examName, id,  course, topic, ex_subtopic;
    private Exam exam;
    private DatabaseReference question_ref, user_ref;
    private ArrayList<String> checkedTopics;
    private HashSet<String> question_uids;
    private int count = 1, current_number = 1;

    public NameDialog(Activity activity, Exam exam, ArrayList<String> checkedTopics) {
        super(activity);
        this.activity = activity;
        this.exam = exam;
        this.checkedTopics = checkedTopics;
    }

    public NameDialog(Activity activity, Exam exam, String ex_subtopic) {
        super(activity);
        this.activity = activity;
        this.exam = exam;
        this.ex_subtopic = ex_subtopic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_name);
        initViews();
        initValues();
    }

    public interface QuizCountCallback {
        void onCountCallback(ArrayList<String> uids);
    }

    public interface QuizQuestionCallback {
        void onQuestionCallback(Question question);
    }

    public interface ExamNameCallback {
        void onExamNameCallback(Boolean isNameExist);
    }

    private void initValues() {
        id = SharedPrefManager.getInstance(getContext()).getKeyUserId();
        question_uids = new HashSet<>();
    }

    private void initViews() {
        btn_name_save = findViewById(R.id.btn_name_save);
        btn_name_cancel = findViewById(R.id.btn_name_cancel);
        et_name_exam = findViewById(R.id.et_name_exam);
        btn_name_save.setOnClickListener(this);
        btn_name_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_name_save:
                examName = et_name_exam.getText().toString();
                user_ref = FirebaseDatabase.getInstance().getReference().child("Students").child(id)
                        .child("Downloaded Exams").child(examName);

                new FirebaseCaller(getContext(), user_ref).checkExamName(new ExamNameCallback() {
                    @Override
                    public void onExamNameCallback(Boolean isNameExist) {
                        if(isNameExist){
                            Toast.makeText(getContext(), "Exam name is already taken." +
                                    "Please enter another name.", Toast.LENGTH_SHORT).show();
                        } else {
                            saveExam();
                            dismiss();
                        }
                    }
                });
                break;

            case R.id.btn_exam_cancel:
                dismiss();
                break;
            default:
                break;
        }

    }

    private void saveExam() {
        exam = new Exam(View.generateViewId(), examName, exam.getEx_difficulty(), exam.getEx_genSubject(),
                exam.getEx_hours(), exam.getEx_mins(), exam.getEx_secs(), exam.getEx_questions());
        user_ref.setValue(exam);

        if(ex_subtopic == null){
            for (String subtopic : checkedTopics){
                user_ref.child("Subtopics").child(String.valueOf(count)).setValue(subtopic);
                count++;
            }
        } else{
            user_ref.child("Subtopics").child("1").setValue(ex_subtopic);
        }

        getQuestions();

        Toast.makeText(getContext(), "Your exam has successfully downloaded",
                Toast.LENGTH_SHORT).show();
    }

    private void getQuestions() {
        if(current_number <= exam.getEx_questions()){

            if(ex_subtopic == null){
                topic = checkedTopics.get(new Random().nextInt(checkedTopics.size()));
            } else {
                topic = ex_subtopic;
            }

            course = SharedPrefManager.getInstance(getContext()).getKeyUserCourse();
            question_ref = FirebaseDatabase.getInstance().getReference().child(course + " Subjects")
                    .child(exam.getEx_genSubject()).child(topic).child(exam.getEx_difficulty());

            new FirebaseCaller(getContext(), question_ref)
                    .getQuestionCount(new NameDialog.QuizCountCallback() {
                        @Override
                        public void onCountCallback(ArrayList<String> uids) {
                            int index = new Random().nextInt(uids.size());
                            question_ref = question_ref.child(uids.get(index));

                            new FirebaseCaller(getContext(), question_ref)
                                    .getQuestion(new NameDialog.QuizQuestionCallback() {
                                        @Override
                                        public void onQuestionCallback(Question question) {
                                            if(question_uids.contains(question.getQ_uid())){
                                                getQuestions();
                                            } else {
                                                question_uids.add(question.getQ_uid());
                                                addQuestionToExam(question);
                                                current_number++;
                                                getQuestions();
                                            }
                                        }
                                    });
                        }
                    });
        }

        SharedPrefManager.getInstance(getContext()).updateDownloads(true);
    }

    private void addQuestionToExam(Question question) {
        user_ref.child("Questions").child(question.getQ_uid()).setValue(question);
    }
}
