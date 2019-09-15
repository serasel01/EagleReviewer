package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.serasel.eagle.Classes.Result;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Serasel on 23/07/2019.
 */

public class DetailsDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    private TextView tv_details_subject, tv_details_difficulty, tv_details_total, tv_details_correct,
            tv_details_wrong, tv_details_percent;
    private CardView btn_details_close;
    private ListView lv_details_topics;
    private Result result;
    private DatabaseReference topic_ref;

    public DetailsDialog(Activity activity, Result result) {
        super(activity);
        this.activity = activity;
        this.result = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_details);
        initViews();
        setTexts();
        outputTopics();
    }

    public interface TopicCallback {
        void onTopicCallback(ArrayList<String> topics);
    }

    private void outputTopics() {
        String id = SharedPrefManager.getInstance(getContext()).getKeyUserId();
        topic_ref = FirebaseDatabase.getInstance().getReference().child("Students").child(id)
                .child("Exam Results").child(String.valueOf(result.getRes_tsMillis()))
                .child("Subtopics");

        new FirebaseCaller(getContext(), topic_ref).getResultTopics(new TopicCallback() {
            @Override
            public void onTopicCallback(ArrayList<String> topics) {
                ArrayAdapter<String> subjectAdapter = new ArrayAdapter(getContext(),
                        android.R.layout.simple_list_item_1, topics);
                lv_details_topics.setAdapter(subjectAdapter);
            }
        });
    }

    private void setTexts() {
        tv_details_subject.setText(result.getRes_subject());
        tv_details_difficulty.setText(result.getRes_difficulty());
        tv_details_total.setText(String.valueOf(result.getRes_questions()));
        tv_details_correct.setText(String.valueOf(result.getRes_corrects()));
        tv_details_wrong.setText(String.valueOf(result.getRes_wrongs()));
        tv_details_percent.setText(result.getRes_percent() + "%");
    }

    private void initViews() {
        tv_details_subject = findViewById(R.id.tv_details_subject);
        tv_details_difficulty = findViewById(R.id.tv_details_difficulty);
        tv_details_total = findViewById(R.id.tv_details_total);
        tv_details_correct = findViewById(R.id.tv_details_correct);
        tv_details_wrong = findViewById(R.id.tv_details_wrong);
        tv_details_percent = findViewById(R.id.tv_details_percent);

        btn_details_close = findViewById(R.id.btn_details_close);
        btn_details_close.setOnClickListener(this);
        lv_details_topics = findViewById(R.id.lv_details_topics);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
