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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.example.serasel.eagle.Classes.Result;
import com.example.serasel.eagle.Dialogs.DetailsDialog;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Serasel on 19/06/2019.
 */

public class ResultFragment extends Fragment{
    private Spinner spin_result_subjects, spin_result_topics;
    private CardView btn_result_date, btn_result_search;
    private TableLayout table_result;
    private CheckBox cb_result_subtopic, cb_result_dateRange;
    private LinearLayout ll_result_subtopic, ll_result_dateRange;
    private TextView tv_result_dateFrom, tv_result_dateTo;
    private String course, id, subject, subtopic;
    private Long dateFrom, dateTo;
    private DatePickerDialog datePicker;
    private DatabaseReference db_subjectRef, db_topicRef;
    private Query queryRef;
    private ArrayAdapter<String> subject_adapter, topic_adapter;

    public ResultFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
        loadSubjects();
    }

    public interface FirebaseSubjectCallback {
        void onSubjectCallback(ArrayList<String> gen_subjects);
    }

    public interface FirebaseTopicCallback {
        void onTopicCallback(ArrayList<String> topics);
    }

    public interface FirebaseResultCallback {
        void onResultCallback(ArrayList<Result> results);
    }

    private void loadSubjects() {
        course = SharedPrefManager.getInstance(getContext()).getKeyUserCourse();
        id = SharedPrefManager.getInstance(getContext()).getKeyUserId();
        db_subjectRef = FirebaseDatabase.getInstance().getReference().child(course + " Subjects");

        new FirebaseCaller(getContext(), db_subjectRef).getSubjects(new FirebaseSubjectCallback() {
            @Override
            public void onSubjectCallback(ArrayList<String> gen_subjects) {
                subject_adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, gen_subjects);
                subject_adapter.setDropDownViewResource
                        (android.R.layout.simple_spinner_dropdown_item);
                spin_result_subjects.setAdapter(subject_adapter);
            }
        });
    }

    private void initViews(View view) {
        spin_result_subjects = view.findViewById(R.id.spin_result_subjects);
        spin_result_topics = view.findViewById(R.id.spin_result_topics);
        btn_result_date = view.findViewById(R.id.btn_result_date);
        btn_result_search = view.findViewById(R.id.btn_result_search);
        table_result = view.findViewById(R.id.table_result);

        tv_result_dateFrom = view.findViewById(R.id.tv_result_dateFrom);
        tv_result_dateTo = view.findViewById(R.id.tv_result_dateTo);

        cb_result_subtopic = view.findViewById(R.id.cb_result_subtopic);
        cb_result_dateRange = view.findViewById(R.id.cb_result_dateRange);

        ll_result_subtopic = view.findViewById(R.id.ll_result_subtopic);
        ll_result_dateRange = view.findViewById(R.id.ll_result_dateRange);

        btn_result_date.setOnClickListener(new SetDate()); //show calendar to set date
        btn_result_search.setOnClickListener(new QuerySearch()); //starts searching for results

        spin_result_subjects.setOnItemSelectedListener(new SubjectSelection());
        spin_result_topics.setOnItemSelectedListener(new TopicSelection());

        //checkboxes
        cb_result_subtopic.setOnCheckedChangeListener(new IncludeSubtopic());
        cb_result_dateRange.setOnCheckedChangeListener(new IncludeDateRange());
    }

    private class SubjectSelection implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            subject = parent.getItemAtPosition(position).toString();
            db_topicRef = db_subjectRef.child(subject);

            new FirebaseCaller(getContext(), db_topicRef).getTopics(new FirebaseTopicCallback() {
                @Override
                public void onTopicCallback(ArrayList<String> topics) {
                    topic_adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_dropdown_item, topics);
                    topic_adapter.setDropDownViewResource
                            (android.R.layout.simple_spinner_dropdown_item);
                    spin_result_topics.setAdapter(topic_adapter);
                }
            });
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class TopicSelection implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            subtopic = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class SetDate implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dateFrom = null;
            dateTo = null;
            Calendar now = Calendar.getInstance();

           datePicker = DatePickerDialog.newInstance(new Datepicker(),
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));
            datePicker.show(getActivity().getFragmentManager(), "Datepickerdialog");
        }
    }

    private class Datepicker implements
            com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener {


        @Override
        public void onDateSet(com.borax12.materialdaterangepicker.date.DatePickerDialog view,
                              int year, int monthOfYear, int dayOfMonth, int yearEnd,
                              int monthOfYearEnd, int dayOfMonthEnd) {
            String monthFrom, dayFrom, monthTo, dayTo;

            monthOfYear = monthOfYear + 1;
            monthOfYearEnd = monthOfYearEnd + 1;

            if(monthOfYear<10){
                monthFrom = "0" + String.valueOf(monthOfYear);
            } else {
                monthFrom = String.valueOf(monthOfYear);
            }

            if(monthOfYearEnd<10){
                monthTo = "0" + String.valueOf(monthOfYearEnd);
            } else {
                monthTo = String.valueOf(monthOfYearEnd);
            }

            if(dayOfMonth<10){
                dayFrom = "0" + String.valueOf(dayOfMonth);
            } else {
                dayFrom = String.valueOf(dayOfMonth);
            }

            if(dayOfMonthEnd<10){
                dayTo = "0" + String.valueOf(dayOfMonthEnd);
            } else {
                dayTo = String.valueOf(dayOfMonthEnd);
            }

            tv_result_dateFrom.setText("From: " + monthFrom + "/" + dayFrom + "/" + year);
            tv_result_dateTo.setText("To: " + monthTo + "/" + dayTo + "/" + yearEnd);

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date dateBefore = dateFormat.parse(monthFrom + "/" + dayFrom + "/" + year);
                Date dateAfter = dateFormat.parse(monthTo + "/" + dayTo +"/" + year);

                dateFrom = dateBefore.getTime();
                dateTo = dateAfter.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
//
//
//
//                dateRef = resultRef.startAt(String.valueOf(tsFrom)).endAt(String.valueOf(tsTo));
//
////                dateRef.addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot) {
////                        if(dataSnapshot.child("Subtopics").child("1").getValue() == topic){
////                            //show results
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(DatabaseError databaseError) {
////
////                    }
////                });
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }
    }

    private class IncludeSubtopic implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                ll_result_subtopic.setVisibility(View.VISIBLE);
            } else {
                subtopic = "";
                ll_result_subtopic.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class IncludeDateRange implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                ll_result_dateRange.setVisibility(View.VISIBLE);
            } else {
                dateFrom = null;
                dateTo = null;
                ll_result_dateRange.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class QuerySearch implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            //condition if there's a date range
            if(dateFrom != null && dateTo != null){
                queryRef = FirebaseDatabase.getInstance().getReference().child("Students").child(id)
                        .child("Exam Results").orderByChild("res_tsMillis").startAt(dateFrom)
                        .endAt(dateTo).limitToLast(15); //scopes down based on query, limits to latest 15
            } else {
            //condition if no date range
                queryRef = FirebaseDatabase.getInstance().getReference().child("Students").child(id)
                        .child("Exam Results").orderByChild("res_tsMillis").limitToLast(15);
            }

            new FirebaseCaller(getContext(), queryRef).getResults(subtopic, subject,
                        new FirebaseResultCallback() {
                    @Override
                    public void onResultCallback(ArrayList<Result> results) {
                        for (Result result : results){
                            TableRow tableRow = new TableRow(getContext());

                            for (int i=0; i<4; i++){ //4 columns
                                TextView textView = new TextView(getContext());
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(Color.BLACK);
                                textView.setOnClickListener(new ViewDetails(result));
                                switch (i){
                                    case 0: //score column
                                        textView.setText(result.getRes_corrects() + "/" +
                                                result.getRes_questions());
                                        break;
                                    case 1: //difficulty
                                        textView.setText(result.getRes_difficulty());
                                        break;
                                    case 2: //percent
                                        textView.setText(result.getRes_percent() + "%");
                                        break;
                                    case 3: //date
                                        textView.setText(result.getRes_date());
                                        break;
                                }
                                tableRow.addView(textView); // add data into a row
                            }
                            table_result.addView(tableRow); //add the row into the table
                        }
                    }
                });

        }
    }

    private class ViewDetails implements View.OnClickListener {
        private Result result;

        public ViewDetails(Result result) {
            this.result = result;
        }

        @Override
        public void onClick(View v) {
            DetailsDialog detailsDialog = new DetailsDialog(getActivity(), result);
            detailsDialog.setCancelable(false);
            detailsDialog.show();
        }
    }
}
