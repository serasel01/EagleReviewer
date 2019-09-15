package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.serasel.eagle.Activities.QuizActivity;
import com.example.serasel.eagle.Classes.Question;
import com.example.serasel.eagle.R;

public class RationaleDialog extends Dialog implements android.view.View.OnClickListener {

    private TextView tv_rationale_question, tv_rationale_answer, tv_rationale_justification;
    private CardView btn_rationale_quit, btn_rationale_next;
    private Activity activity;
    private Question question;

    public RationaleDialog(Activity activity, Question question) {
        super(activity);
        this.activity = activity;
        this.question = question;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rationale);
        initViews();
    }

    private void initViews() {
        tv_rationale_question = findViewById(R.id.tv_rationale_question);
        tv_rationale_answer = findViewById(R.id.tv_rationale_answer);
        tv_rationale_justification = findViewById(R.id.tv_rationale_justification);

        tv_rationale_question.setText("Question: " + question.getQ_question());
        tv_rationale_answer.setText("Answer: " + question.getQ_answer());
        tv_rationale_justification.setText(question.getQ_rationale());

        btn_rationale_quit = findViewById(R.id.btn_rationale_quit);
        btn_rationale_next = findViewById(R.id.btn_rationale_next);

        btn_rationale_quit.setOnClickListener(this);
        btn_rationale_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rationale_quit:
                ((QuizActivity)activity).showResult();
                break;
            case R.id.btn_rationale_next:
                ((QuizActivity)activity).getQuestion();
                break;
            default:
                break;
        }
        dismiss();
    }
}
