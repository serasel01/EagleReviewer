package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.serasel.eagle.Activities.QuizActivity;
import com.example.serasel.eagle.Classes.Exam;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Serasel on 05/07/2019.
 */

public class DownloadsDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    private RadioGroup rg_download_exams;
    private RadioButton rb_download_exam;
    private CardView btn_download_confirm, btn_download_cancel, btn_download_delete;
    private String id, examName;
    private DatabaseReference exam_ref;
    private Intent quizIntent;
    private int examId;

    public DownloadsDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_downloads);
        initViews();
        addDownloadedExams();
    }

    public interface ExamCallback {
        void onExamCallback(ArrayList<Exam> exams);
    }

    public interface DoExamCallback {
        void onExaminationCallback(Exam dl_exam, ArrayList<String> subtopics);
    }

    private void addDownloadedExams() {
        id = SharedPrefManager.getInstance(getContext()).getKeyUserId();
        exam_ref = FirebaseDatabase.getInstance().getReference().child("Students").child(id)
                .child("Downloaded Exams");

        new FirebaseCaller(getContext(), exam_ref).getExams(new ExamCallback() {
            @Override
            public void onExamCallback(ArrayList<Exam> exams) {
                for(Exam exam : exams){
                    RadioButton rb_exam = new RadioButton(getContext());
                    rb_exam.setId(exam.getEx_id());
                    rb_exam.setText(exam.getEx_name());
                    rg_download_exams.addView(rb_exam);
                }
            }
        });
    }

    private void initViews() {
        rg_download_exams =  findViewById(R.id.rg_download_exams);

        btn_download_confirm = findViewById(R.id.btn_download_confirm);
        btn_download_cancel = findViewById(R.id.btn_download_cancel);
        btn_download_delete = findViewById(R.id.btn_download_delete);
        btn_download_confirm.setOnClickListener(this);
        btn_download_cancel.setOnClickListener(this);
        btn_download_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download_confirm:
                getSelectedExam();
                break;

            case R.id.btn_download_cancel:
                break;

            case R.id.btn_download_delete:
                DeleteDialog deleteDialog = new DeleteDialog(activity);
                deleteDialog.setCancelable(false);
                deleteDialog.show();
                break;

            default:
                break;
        }

        dismiss();
    }

    private void getSelectedExam() {
        examId = rg_download_exams.getCheckedRadioButtonId();
        rb_download_exam = findViewById(examId);
        examName = rb_download_exam.getText().toString();
        exam_ref = exam_ref.child(examName);

        new FirebaseCaller(getContext(), exam_ref).getExam(new DoExamCallback() {
            @Override
            public void onExaminationCallback(Exam dl_exam, ArrayList<String> subtopics) {
                startQuiz(dl_exam, subtopics);
            }
        });
    }

    private void startQuiz(Exam dl_exam, ArrayList<String> subtopics) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("exam", dl_exam);
        bundle.putStringArrayList("checkedTopics", subtopics);
        bundle.putString("mode", "Offline");

        quizIntent = new Intent(getContext(), QuizActivity.class);
        quizIntent.putExtras(bundle);
        activity.startActivity(quizIntent);
    }
}
