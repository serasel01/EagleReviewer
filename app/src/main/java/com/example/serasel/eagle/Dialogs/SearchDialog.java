package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.serasel.eagle.Classes.Exam;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchDialog extends Dialog implements android.view.View.OnClickListener {
    private Activity activity;
    private CardView btn_search_select, btn_search_cancel;
    private TextView tv_search_for;
    private RadioGroup rg_search_results;
    private String keyword, course;
    private DatabaseReference keyword_ref;
    private ArrayList<Exam> examList;

    public SearchDialog(Activity activity, String keyword) {
        super(activity);
        this.activity = activity;
        this.keyword = keyword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search);
        initViews();
        getKeywordResults();
    }

    public interface SubjectKeywordCallback {
        void onSubjectKeywordCallback(ArrayList<Exam> exams);
    }

    private void getKeywordResults() {
        course = SharedPrefManager.getInstance(getContext()).getKeyUserCourse();
        keyword_ref = FirebaseDatabase.getInstance().getReference().child("Keywords")
                .child(course + " Subjects");

        new FirebaseCaller(getContext(), keyword_ref).getSubjectsForKeyword(keyword,
                new SubjectKeywordCallback() {
            @Override
            public void onSubjectKeywordCallback(ArrayList<Exam> exams) {
                loadKeywordResults(exams);
            }
        });
    }

    private void loadKeywordResults(ArrayList<Exam> exams) {
        for (Exam exam : exams){
            RadioButton rb_keywordResult = new RadioButton(getContext());
            rb_keywordResult.setId(View.generateViewId());
            rb_keywordResult.setText(exam.getEx_difficulty() + " " + exam.getEx_subtopic());
            rg_search_results.addView(rb_keywordResult);
        }
        examList = exams;
    }

    private void initViews() {
        btn_search_select = findViewById(R.id.btn_search_select);
        btn_search_cancel = findViewById(R.id.btn_search_cancel);
        btn_search_select.setOnClickListener(this);
        btn_search_cancel.setOnClickListener(this);

        tv_search_for = findViewById(R.id.tv_search_for);
        tv_search_for.setText("Search results for: " + keyword);

        rg_search_results = findViewById(R.id.rg_search_results);
    }

    @Override
    public void onClick(View v) {
        int selectedId = rg_search_results.getCheckedRadioButtonId();
        RadioButton rb_selectedExam;

        switch (v.getId()) {
            case R.id.btn_search_select:
                rb_selectedExam = findViewById(selectedId);
                String str_exam = rb_selectedExam.getText().toString();

                for(Exam exam : examList){
                    if(str_exam.equals(exam.getEx_difficulty() + " " + exam.getEx_subtopic())){
                        SettingsDialog settingsDialog = new SettingsDialog(activity, exam);
                        settingsDialog.setCancelable(false);
                        settingsDialog.show();
                    }
                }
                break;

            case R.id.btn_search_cancel:
                break;

            default:
                break;
        }

        dismiss();
    }
}
