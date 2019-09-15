package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.serasel.eagle.Activities.NavigationActivity;
import com.example.serasel.eagle.Activities.QuizActivity;
import com.example.serasel.eagle.Classes.Result;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Serasel on 11/06/2019.
 */

public class ResultDialog extends Dialog implements android.view.View.OnClickListener {
    private Activity activity;
    private TextView tv_result_subject, tv_result_total, tv_result_correct, tv_result_wrong,
            tv_result_difficulty, tv_result_percent;
    private CardView btn_result_finish;
    private Result result;
    private String id, dateTime, subtopic;
    private DatabaseReference result_ref;
    private Intent navIntent;
    private ArrayList<String> topics;
    private int topic_count = 1;

    public ResultDialog(Activity activity, Result result, ArrayList<String> topics, String datetime) {
        super(activity);
        this.activity = activity;
        this.result =  result;
        this.topics = topics;
        this.dateTime = datetime;
    }

    public ResultDialog(Activity activity, Result result, String subtopic, String datetime) {
        super(activity);
        this.activity = activity;
        this.result = result;
        this.subtopic = subtopic;
        this.dateTime = datetime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_results);
        initViews();
    }

    private void initViews() {
        tv_result_subject = findViewById(R.id.tv_result_subject);
        tv_result_total = findViewById(R.id.tv_result_total);
        tv_result_correct = findViewById(R.id.tv_result_correct);
        tv_result_wrong = findViewById(R.id.tv_result_wrong);
        tv_result_difficulty = findViewById(R.id.tv_result_difficulty);
        tv_result_percent = findViewById(R.id.tv_result_percent);

        tv_result_subject.setText(result.getRes_subject());
        tv_result_total.setText(String.valueOf(result.getRes_questions()));
        tv_result_correct.setText(String.valueOf(result.getRes_corrects()));
        tv_result_wrong.setText(String.valueOf(result.getRes_wrongs()));
        tv_result_difficulty.setText(result.getRes_difficulty());
        tv_result_percent.setText((String.valueOf(result.getRes_percent()) + "%"));

        btn_result_finish = findViewById(R.id.btn_result_finish);
        btn_result_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        saveResult();
        dismiss();
        backToHome();
    }

    private void backToHome() {
        navIntent = new Intent(getContext(), NavigationActivity.class);
        navIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(navIntent);
        activity.finish();
    }

    private void saveResult() {
        id = SharedPrefManager.getInstance(getContext()).getKeyUserId();
        result_ref = FirebaseDatabase.getInstance().getReference().child("Students").child(id)
                .child("Exam Results").child(dateTime);
        result_ref.setValue(result);
        result_ref = result_ref.child("Subtopics");

        if (subtopic == null){
            for(String subtopic : topics){
                result_ref.child(String.valueOf(topic_count)).setValue(subtopic);
                topic_count++;
            }
        } else {
            result_ref.child("1").setValue(subtopic);
        }

    }
}
