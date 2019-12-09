package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.serasel.eagle.Activities.QuizActivity;
import com.example.serasel.eagle.Classes.Exam;
import com.example.serasel.eagle.Classes.Question;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class SettingsDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    private Intent quizIntent;
    private CardView btn_settings_takeOnline, btn_settings_download, btn_settings_cancel;
    private EditText et_settings_hour, et_settings_min, et_settings_sec;
    private TextView tv_settings_difficulty;
    private Spinner spin_settings_difficulty;
    private ArrayAdapter<CharSequence> difficulty_adapter;
    private String gen_subject, difficulty;
    private int questions = 100, hours, mins, secs;
    private ArrayList<String> checkedTopics;
    private Exam exam;

    public SettingsDialog(Activity activity, String gen_subject, ArrayList<String> checkedTopics) {
        super(activity);
        this.activity = activity;
        this.gen_subject = gen_subject;
        this.checkedTopics = checkedTopics;
    }

    public SettingsDialog(Activity activity, Exam exam){
        super(activity);
        this.activity = activity;
        this.exam = exam;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_settings);
        initViews();
    }

    private void initViews() {
        btn_settings_takeOnline = findViewById(R.id.btn_settings_takeOnline);
        btn_settings_takeOnline.setOnClickListener(this);
        btn_settings_cancel = findViewById(R.id.btn_settings_cancel);
        btn_settings_cancel.setOnClickListener(this);
        btn_settings_download = findViewById(R.id.btn_settings_download);
        btn_settings_download.setOnClickListener(this);

        et_settings_hour = findViewById(R.id.et_settings_hour);
        et_settings_min = findViewById(R.id.et_settings_min);
        et_settings_sec = findViewById(R.id.et_settings_sec);

        tv_settings_difficulty = findViewById(R.id.tv_settings_difficulty);

        spin_settings_difficulty = findViewById(R.id.spin_settings_difficulty);

        difficulty_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.difficulty, android.R.layout.simple_spinner_item);
        difficulty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //if statement for whether online or offline
        if(exam == null){ //for online
            tv_settings_difficulty.setVisibility(View.VISIBLE);
            spin_settings_difficulty.setVisibility(View.VISIBLE);
            spin_settings_difficulty.setAdapter(difficulty_adapter);
            spin_settings_difficulty.setOnItemSelectedListener(new DifficultySettings());
        } else { //for offline
            spin_settings_difficulty.setVisibility(View.INVISIBLE);
            tv_settings_difficulty.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        hours = Integer.parseInt(et_settings_hour.getText().toString());
        mins = Integer.parseInt(et_settings_min.getText().toString());
        secs = Integer.parseInt(et_settings_sec.getText().toString());

        if(exam == null){ //if online
            exam = new Exam(difficulty, gen_subject, hours, mins, secs, questions);
        } else {
            exam.setEx_questions(questions);
            exam.setEx_hours(hours);
            exam.setEx_mins(mins);
            exam.setEx_secs(secs);
        }

        switch (v.getId()) {
            case R.id.btn_settings_takeOnline:
                startQuiz();
                break;

            case R.id.btn_settings_download:
                enterExamName();
                break;

            case R.id.btn_settings_cancel:
                break;
            default:
                break;
        }

        dismiss();
    }

    private void enterExamName() {
        NameDialog nameDialog;

        if(exam.getEx_subtopic() == null){
            nameDialog = new NameDialog(activity, exam, checkedTopics);
        } else {
            nameDialog = new NameDialog(activity, exam, exam.getEx_subtopic());
        }

        nameDialog.setCancelable(false);
        nameDialog.show();
    }

    private void startQuiz(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("exam", exam); //sends a class to another activity

        if(exam.getEx_subtopic() == null){
            bundle.putStringArrayList("checkedTopics", checkedTopics);
        }
        bundle.putString("mode", "Online");

        quizIntent = new Intent(getContext(), QuizActivity.class);
        quizIntent.putExtras(bundle);
        activity.startActivity(quizIntent);
    }

    private class DifficultySettings implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            difficulty = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }
}
