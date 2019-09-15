package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;

import com.example.serasel.eagle.Activities.QuizActivity;
import com.example.serasel.eagle.R;

/**
 * Created by Serasel on 16/06/2019.
 */

public class PauseDialog extends Dialog implements View.OnClickListener {
    private CardView btn_pause_resume, btn_pause_quit;
    private Activity activity;

    public PauseDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pause);
        initViews();
    }

    private void initViews() {
        btn_pause_resume = findViewById(R.id.btn_pause_resume);
        btn_pause_resume.setOnClickListener(this);
        btn_pause_quit = findViewById(R.id.btn_pause_quit);
        btn_pause_quit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pause_resume:
                ((QuizActivity)activity).resumeTime();
                break;

            case R.id.btn_pause_quit:
                ((QuizActivity)activity).showResult();
                break;
            default:
                break;
        }
        dismiss();
    }
}
