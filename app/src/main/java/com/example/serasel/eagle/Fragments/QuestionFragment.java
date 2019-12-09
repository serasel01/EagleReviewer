package com.example.serasel.eagle.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serasel.eagle.Activities.QuizActivity;
import com.example.serasel.eagle.Classes.Question;
import com.example.serasel.eagle.Dialogs.RationaleDialog;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class QuestionFragment extends Fragment implements android.view.View.OnClickListener{

    private TextView tv_question_question, tv_question_a, tv_question_b, tv_question_c,
            tv_question_d;
    private CardView btn_question_a, btn_question_b, btn_question_c, btn_question_d, card;
    private ImageView iv_question_photo;
    private View view;
    private RationaleDialog rationaleDialog;
    private DatabaseReference db_ref;
    private String course, selection = "";

    private Question question;
    private int corrects, wrongs;

    public QuestionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        question = (Question) getArguments().getSerializable("question");
        corrects = getArguments().getInt("corrects");
        wrongs = getArguments().getInt("wrongs");
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
    }

    private void initViews(View view) {
        tv_question_question = view.findViewById(R.id.tv_question_question);
        tv_question_a = view.findViewById(R.id.tv_question_a);
        tv_question_b = view.findViewById(R.id.tv_question_b);
        tv_question_c = view.findViewById(R.id.tv_question_c);
        tv_question_d = view.findViewById(R.id.tv_question_d);

        //set text to all answer boxes and question box
        tv_question_question.setText(question.getQ_question());
        tv_question_a.setText("A: " + question.getQ_a());
        tv_question_b.setText("B: " + question.getQ_b());
        tv_question_c.setText("C: " + question.getQ_c());
        tv_question_d.setText("D: " + question.getQ_d());

        btn_question_a = view.findViewById(R.id.btn_question_a);
        btn_question_b = view.findViewById(R.id.btn_question_b);
        btn_question_c = view.findViewById(R.id.btn_question_c);
        btn_question_d = view.findViewById(R.id.btn_question_d);

        btn_question_a.setOnClickListener(this);
        btn_question_b.setOnClickListener(this);
        btn_question_c.setOnClickListener(this);
        btn_question_d.setOnClickListener(this);

        //get question photo
        if (question.getQ_imagePath() != null){
            iv_question_photo = view.findViewById(R.id.iv_question_photo);
            Picasso.get().load(question.getQ_imagePath())
                    .placeholder(R.drawable.ic_launcher_background).into(iv_question_photo);
        }

    }

    public void answer(){ //verify if correct answer
        if (selection.equals(question.getQ_answer())){
            corrects++;
            card.setCardBackgroundColor(getResources().getColor(R.color.colorCorrect)); //colors green
            SharedPrefManager.getInstance(getContext()).updateCorrects(corrects);
        } else {
            wrongs++;
            card.setCardBackgroundColor(getResources().getColor(R.color.colorWrong)); //colors red
            SharedPrefManager.getInstance(getContext()).updateWrongs(wrongs);

            switch (question.getQ_answer()) {
                case "A":
                    btn_question_a.setCardBackgroundColor(getResources().getColor(R.color.colorCorrect));
                    break;
                case "B":
                    btn_question_b.setCardBackgroundColor(getResources().getColor(R.color.colorCorrect));
                    break;
                case "C":
                    btn_question_c.setCardBackgroundColor(getResources().getColor(R.color.colorCorrect));
                    break;
                case "D":
                    btn_question_d.setCardBackgroundColor(getResources().getColor(R.color.colorCorrect));
                    break;
                default:
                    break;
            }
        }
        showRationale(); //show rationale dialog
        ((QuizActivity)getActivity()).updateScore();
    }

    @Override
    public void onClick(View v) {
        setDefaultColor(); //resets color to orange
        card = v.findViewById(v.getId());
        switch (v.getId()) {
            case R.id.btn_question_a:
                selection = "A";
                break;
            case R.id.btn_question_b:
                selection = "B";
                break;
            case R.id.btn_question_c:
                selection = "C";
                break;
            case R.id.btn_question_d:
                selection = "D";
                break;
            default:
                break;
        }
        card.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary)); //changes the color to blue
    }

    private void setDefaultColor() {
        btn_question_a.setCardBackgroundColor(getResources().getColor(R.color.colorAccent2));
        btn_question_b.setCardBackgroundColor(getResources().getColor(R.color.colorAccent2));
        btn_question_c.setCardBackgroundColor(getResources().getColor(R.color.colorAccent2));
        btn_question_d.setCardBackgroundColor(getResources().getColor(R.color.colorAccent2));
    }

    private void showRationale() {
        rationaleDialog = new RationaleDialog(getActivity(), question);
        rationaleDialog.setCancelable(false);
        rationaleDialog.show();
    }

}
