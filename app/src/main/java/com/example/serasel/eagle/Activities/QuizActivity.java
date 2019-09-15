package com.example.serasel.eagle.Activities;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.example.serasel.eagle.Classes.Exam;
import com.example.serasel.eagle.Classes.Question;
import com.example.serasel.eagle.Classes.Result;
import com.example.serasel.eagle.Dialogs.PauseDialog;
import com.example.serasel.eagle.Dialogs.ResultDialog;
import com.example.serasel.eagle.Fragments.QuestionFragment;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    private TextView tv_quiz_title, tv_scoring_question, tv_scoring_timer, tv_scoring_correct,
            tv_scoring_wrong;
    private CardView btn_quiz_quit, btn_quiz_pause, btn_quiz_answer;
    private PauseDialog pauseDialog;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private long timeStart, timeLeft;
    private int questions, current_number = 1, corrects, wrongs, hours, mins, secs;
    private DatabaseReference question_ref;
    private String course, gen_subject, difficulty, topic, mode, id, subtopic;
    private QuestionFragment questionFragment;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<String> topics;
    private HashSet<String> question_uids;
    private ResultDialog resultDialog;
    private Result result;
    private Activity activity;
    private Exam exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        activity = this;
        initValues();
        initViews();
        setStartTime();
        setTime();
        getQuestion();
    }

    private void setStartTime() { //timer
        timeStart = (hours * 3600000) + (mins * 60000) + (secs * 1000); //gets from settings and sets in milliseconds
        timeLeft = timeStart; //when resuming, get the remaining time
    }

    private void setTime() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) { //does the timing
                timeLeft = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() { //function when time runs out
                isTimerRunning = false;
                showResult();
            }

        }.start(); //actual function that will start the timer
        isTimerRunning = true;
    }

    private void updateCountDownText() { //displays the current time
        hours = (int) timeLeft / 3600000;
        mins = (int) timeLeft / 60000 % 60;
        secs = (int) timeLeft / 1000 % 60;

        String timeFormat = String.format(Locale.getDefault(),
                "%02d:%02d:%02d", hours, mins, secs);
        tv_scoring_timer.setText(timeFormat);
    }

    public void resumeTime(){
        setTime();
    }

    public void updateScore() {
        corrects = SharedPrefManager.getInstance(getApplicationContext()).getKeyQuizCorrects();
        wrongs = SharedPrefManager.getInstance(getApplicationContext()).getKeyQuizWrongs();

        tv_scoring_correct.setText("Correct: " + String.valueOf(corrects));
        tv_scoring_wrong.setText("Wrong: " + String.valueOf(wrongs));
    }

    public void getQuestion() {
        if(current_number <= questions){
            tv_scoring_question.setText(current_number + "/" + questions);

            switch (mode){
                case "Online":
                    if(subtopic == null){
                        topic = topics.get(new Random().nextInt(topics.size()));
                    } else {
                        topic = subtopic;
                    }
                    course = SharedPrefManager.getInstance(getApplicationContext())
                            .getKeyUserCourse();
                    question_ref = FirebaseDatabase.getInstance().getReference()
                            .child(course + " Subjects").child(gen_subject).child(topic)
                            .child(difficulty);
                    break;

                case "Offline":
                    id = SharedPrefManager.getInstance(getApplicationContext()).getKeyUserId();
                    question_ref = FirebaseDatabase.getInstance().getReference().child("Students")
                            .child(id).child("Downloaded Exams").child(exam.getEx_name())
                            .child("Questions");
                    break;
            }

            new FirebaseCaller(getApplicationContext(), question_ref)
                    .getQuestionCount(new QuizCountCallback() {
                        @Override
                        public void onCountCallback(ArrayList<String> uids) {
                            int index = new Random().nextInt(uids.size());
                            question_ref = question_ref.child(uids.get(index));

                            new FirebaseCaller(getApplicationContext(), question_ref)
                                    .getQuestion(new QuizQuestionCallback() {
                                        @Override
                                        public void onQuestionCallback(Question question) {
                                            //if statement to determine if question already used
                                            //prevents repeating questions
                                            if(question_uids.contains(question.getQ_uid())){
                                                getQuestion();
                                            } else { //stores unique id into the hashset
                                                question_uids.add(question.getQ_uid());
                                                initFragment(question);
                                                current_number++;
                                            }
                                        }
                                    });
                        }
                    });
        } else {
            showResult();
        }

    }

    public void showResult() {
        countDownTimer.cancel();
        isTimerRunning = false; //stops time

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String datetime = Long.toString(timestamp.getTime());
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        Double percentage = ((double) corrects) / questions;
        result = new Result(gen_subject, difficulty, questions, corrects, wrongs,
                percentage*100, dateFormat.format(new Date()), timestamp.getTime());

        if (subtopic == null){ //if many topics
            resultDialog = new ResultDialog(this, result, topics, datetime);
        } else { //if only one topic
            resultDialog = new ResultDialog(this, result, subtopic, datetime);
        }

        resultDialog.setCancelable(false);
        resultDialog.show();
    }

    public interface QuizCountCallback {
        void onCountCallback(ArrayList<String> uids);
    }

    public interface QuizQuestionCallback {
        void onQuestionCallback(Question question);
    }

    private void initValues() {
        exam = (Exam) getIntent().getExtras().getSerializable("exam");

        if (getIntent().getExtras().getStringArrayList("checkedTopics") != null){ //for many topics
            topics = getIntent().getExtras().getStringArrayList("checkedTopics");
        } else { //for one topic only
            subtopic = exam.getEx_subtopic();
        }

        mode = getIntent().getExtras().getString("mode"); //distinguish whether online or offline

        gen_subject = exam.getEx_genSubject();
        difficulty = exam.getEx_difficulty();
        hours = exam.getEx_hours();
        mins = exam.getEx_mins();
        secs = exam.getEx_secs();
        questions = exam.getEx_questions();

        question_uids = new HashSet<>(); //container for unique ids
    }

    private void initFragment(Question question) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("question", question);
        bundle.putInt("corrects", corrects);
        bundle.putInt("wrongs", wrongs);

        questionFragment = new QuestionFragment();
        questionFragment.setArguments(bundle);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_quiz, questionFragment, "tag").commit();
    }

    private void initViews() {
        tv_quiz_title = findViewById(R.id.tv_quiz_title);
        tv_scoring_question = findViewById(R.id.tv_scoring_question);
        tv_scoring_timer = findViewById(R.id.tv_scoring_timer);
        tv_scoring_correct = findViewById(R.id.tv_scoring_correct);
        tv_scoring_wrong = findViewById(R.id.tv_scoring_wrong);

        btn_quiz_pause = findViewById(R.id.btn_quiz_pause);
        btn_quiz_pause.setOnClickListener(new PauseButton());
        btn_quiz_answer = findViewById(R.id.btn_quiz_answer);
        btn_quiz_answer.setOnClickListener(new AnswerButton());
        btn_quiz_quit = findViewById(R.id.btn_quiz_quit);
        btn_quiz_quit.setOnClickListener(new QuitButton());

        tv_quiz_title.setText(gen_subject + " - " + difficulty);
        SharedPrefManager.getInstance(getApplicationContext()).updateCorrects(0);
        SharedPrefManager.getInstance(getApplicationContext()).updateWrongs(0);
    }

    private class PauseButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            countDownTimer.cancel();
            isTimerRunning = false; //stops time

            pauseDialog = new PauseDialog(activity); //shows pause dialog
            pauseDialog.getWindow().setDimAmount(1.5f); //makes it dark
            pauseDialog.setCancelable(false);
            pauseDialog.show();
        }
    }

    private class AnswerButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getSupportFragmentManager();
            QuestionFragment fragment = (QuestionFragment)fm.findFragmentByTag("tag");
            fragment.answer();
        }
    }

    private class QuitButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showResult();
        }
    }
}
