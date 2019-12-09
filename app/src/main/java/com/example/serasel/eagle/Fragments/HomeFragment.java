package com.example.serasel.eagle.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serasel.eagle.Classes.Exam;
import com.example.serasel.eagle.Classes.Result;
import com.example.serasel.eagle.Dialogs.SearchDialog;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.example.serasel.eagle.Dialogs.SubjectDialog;
import com.example.serasel.eagle.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {
    private TextView top_banner_name;
    private CardView btn_home_exam;
    private SubjectDialog subjectDialog;
    private SearchDialog searchDialog;
    private int num_questions, corrects;
    private String gen_subject, id, course, date_before, date_now;
    private Query result_ref;
    private DatabaseReference subjects_ref;
    private ArrayList<String> gen_subjects;
    private Timestamp ts_now, ts_before;
    private DateFormat dateFormat;
    private TableLayout table_result_general;
    private EditText et_nav_search;
    private ImageButton btn_nav_search;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        showGeneralReport();
    }

    private void initViews(View view) {
        top_banner_name = view.findViewById(R.id.top_banner_name);
        btn_home_exam = view.findViewById(R.id.btn_home_exam);

        top_banner_name.setText(SharedPrefManager.getInstance(getContext()).getKeyUserName() + "!");
        btn_home_exam.setOnClickListener(new TakeExam());

        table_result_general = view.findViewById(R.id.table_result_general);

        et_nav_search = view.findViewById(R.id.et_nav_search);
        btn_nav_search = view.findViewById(R.id.btn_nav_search);
        btn_nav_search.setOnClickListener(new SearchKeyword());
    }

    public interface FirebaseSubjectCallback {
        void onSubjectCallback(ArrayList<String> subjects);
    }

    public interface GenResultCallback {
        void onResultCallback(Result gen_result);
    }

    //onclicklistener for take exam button
    private class TakeExam implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            subjectDialog = new SubjectDialog(getActivity(), getContext());
            subjectDialog.setCancelable(false);
            subjectDialog.show();
        }
    }

    private void showGeneralReport(){
        getDateRange();
        getCourseSubjects();
    }

    private void getCourseSubjects() {
        course = SharedPrefManager.getInstance(getContext()).getKeyUserCourse();
        subjects_ref = FirebaseDatabase.getInstance().getReference().child(course + " Subjects");

        new FirebaseCaller(getContext(), subjects_ref).getSubjects(new FirebaseSubjectCallback() {
            @Override
            public void onSubjectCallback(ArrayList<String> subjects) {
                getUserResults(subjects);
            }
        });
    }

    private void getDateRange(){
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        ts_now = new Timestamp(new Date().getTime());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts_now.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -7);

        ts_before = new Timestamp(cal.getTime().getTime());

        date_before = dateFormat.format(new Date(ts_before.getTime()));
        date_now = dateFormat.format(new Date(ts_now.getTime()));
    }

    private void getUserResults(ArrayList<String> subjects){
        id = SharedPrefManager.getInstance(getContext()).getKeyUserId();
        result_ref = FirebaseDatabase.getInstance().getReference().child("Students").child(id)
                .child("Exam Results").orderByChild("res_tsMillis").startAt(ts_before.getTime())
                .endAt(ts_now.getTime());

        for (String gen_subject : subjects){
            new FirebaseCaller(getContext(), result_ref)
                    .getResult(gen_subject, new GenResultCallback() {
                @Override
                public void onResultCallback(Result gen_result) {
                    showReport(gen_result);
                }
            });
        }
    }

    private void showReport(Result gen_result) {
        TableRow tableRow = new TableRow(getContext());

        for (int i=0; i<4; i++){
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            switch (i){
                case 0:
                    textView.setText(gen_result.getRes_subject());
                    break;
                case 1:
                    textView.setText(gen_result.getRes_corrects() + "/" +
                            gen_result.getRes_questions());
                    break;
                case 2:
                    textView.setText(gen_result.getRes_percent() + "%");
                    break;
                case 3:
                    textView.setText(date_before + "\n-" + date_now);
                    break;
            }
            tableRow.addView(textView);
        }
        table_result_general.addView(tableRow);
    }

    private class SearchKeyword implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String keyword = et_nav_search.getText().toString();
            searchDialog = new SearchDialog(getActivity(), keyword);
            searchDialog.setCancelable(false);
            searchDialog.show();
        }
    }
}
