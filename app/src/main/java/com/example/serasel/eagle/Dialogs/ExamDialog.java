package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.example.serasel.eagle.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExamDialog extends Dialog implements android.view.View.OnClickListener{

    private Activity activity;
    private CardView btn_exam_confirm, btn_exam_cancel;
    private CheckBox cb_quiz_selectAll;
    private ListView lv_quiz_categories;
    private ArrayAdapter<String> subjectAdapter;
    private SettingsDialog settingsDialog;
    private String gen_subject, course;
    private DatabaseReference db_ref;

    public interface FirebaseSubjectCallback {
        void onSubjectCallback(ArrayList<String> gen_subjects);
    }

    public ExamDialog(Activity activity, String gen_subject) {
        super(activity);
        this.activity = activity;
        this.gen_subject = gen_subject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_exam);
        initViews();
        addCategories();
    }

    private void addCategories() {
        course = SharedPrefManager.getInstance(getContext()).getKeyUserCourse();
        db_ref = FirebaseDatabase.getInstance().getReference().child(course + " Subjects")
                .child(gen_subject);

        new FirebaseCaller(getContext(), db_ref).getSubjects(new FirebaseSubjectCallback() {
            @Override
            public void onSubjectCallback(ArrayList<String> gen_subjects) {

                subjectAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_multiple_choice, gen_subjects);
                lv_quiz_categories.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lv_quiz_categories.setAdapter(subjectAdapter);
            }
        });
    }

    private void initViews() {
        btn_exam_confirm = findViewById(R.id.btn_exam_confirm);
        btn_exam_cancel = findViewById(R.id.btn_exam_cancel);

        btn_exam_confirm.setOnClickListener(this);
        btn_exam_cancel.setOnClickListener(this);

        lv_quiz_categories = findViewById(R.id.lv_quiz_categories);
        cb_quiz_selectAll = findViewById(R.id.cb_quiz_selectAll);
        cb_quiz_selectAll.setOnClickListener(new SelectAll());
    }

    @Override
    public void onClick(View v) {
        settingsDialog = new SettingsDialog(activity, gen_subject, getCheckedTopics());
        switch (v.getId()) {
            case R.id.btn_exam_confirm:
                settingsDialog.setCancelable(false);
                settingsDialog.show();
                break;
            case R.id.btn_exam_cancel:
                break;
            default:
                break;
        }
        dismiss();
    }

    private ArrayList<String> getCheckedTopics() {
        SparseBooleanArray checked = lv_quiz_categories.getCheckedItemPositions();
        ArrayList<String> topics = new ArrayList<>();

        for (int i = 0; i < lv_quiz_categories.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                topics.add(lv_quiz_categories.getItemAtPosition(i).toString());
            }
        }
        return topics;
    }

    private class SelectAll implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(cb_quiz_selectAll.isChecked())
            {
                for ( int i=0; i < lv_quiz_categories.getChildCount(); i++) {
                    lv_quiz_categories.setItemChecked(i, true);
                }
            }
            if(!cb_quiz_selectAll.isChecked())
            {
                for ( int i=0; i < lv_quiz_categories.getChildCount(); i++) {
                    lv_quiz_categories.setItemChecked(i, false);
                }
            }
        }
    }
}
