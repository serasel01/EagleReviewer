package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SubjectDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    private Context context;
    private String course;
    private CardView btn_subjects_select, btn_subjects_cancel, btn_subjects_download;
    private RadioGroup rg_subjects;
    private RadioButton rb_math, rb_elecs, rb_geas, rb_est;
    private ExamDialog examDialog;
    private DownloadsDialog downloadsDialog;
    private DatabaseReference subjectRef;

    public SubjectDialog(Activity activity, Context context) {
        super(activity);
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_subjects);
        initViews();
        addSubjects();
        //create button that will show a dialog of downloaded exams
    }

    public interface FirebaseSubjectCallback {
        void onSubjectCallback(ArrayList<String> gen_subjects);
    }

    private void addSubjects() { //adds list of subjects from database
        subjectRef = FirebaseDatabase.getInstance().getReference().child(course + " Subjects");

        new FirebaseCaller(context, subjectRef).getSubjects(new FirebaseSubjectCallback() {
            @Override
            public void onSubjectCallback(ArrayList<String> gen_subjects) { //generates the items in radio group
                for(String gen_subject : gen_subjects){
                    RadioButton rb_subject = new RadioButton(context);
                    rb_subject.setId(View.generateViewId());
                    rb_subject.setText(gen_subject);
                    rg_subjects.addView(rb_subject);
                }
            }
        });
    }

    private void initViews() {
        course = SharedPrefManager.getInstance(context).getKeyUserCourse();

        btn_subjects_select = findViewById(R.id.btn_subjects_select);
        btn_subjects_cancel = findViewById(R.id.btn_subjects_cancel);
        btn_subjects_download = findViewById(R.id.btn_subjects_download);
        btn_subjects_select.setOnClickListener(this);
        btn_subjects_cancel.setOnClickListener(this);
        btn_subjects_download.setOnClickListener(this);

        rg_subjects = findViewById(R.id.rg_subjects);
    }

    @Override
    public void onClick(View v) {
        int selectedId = rg_subjects.getCheckedRadioButtonId();
        RadioButton rb_genSubject;

        switch (v.getId()) {
            case R.id.btn_subjects_select:
                rb_genSubject = findViewById(selectedId);
                examDialog = new ExamDialog(activity,  rb_genSubject.getText().toString());
                examDialog.setCancelable(false);
                examDialog.show();
                break;

            case R.id.btn_subjects_download:
                downloadsDialog = new DownloadsDialog(activity);
                downloadsDialog.setCancelable(false);
                downloadsDialog.show();
                break;

            case R.id.btn_subjects_cancel:
                break;
            default:
                break;
        }
        dismiss();
    }
}
