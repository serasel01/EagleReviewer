package com.example.serasel.eagle.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.serasel.eagle.Classes.Exam;
import com.example.serasel.eagle.R;
import com.example.serasel.eagle.Utilities.FirebaseCaller;
import com.example.serasel.eagle.Utilities.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DeleteDialog extends Dialog implements android.view.View.OnClickListener{
    private Activity activity;
    private CheckBox cb_delete_selectAll;
    private ListView lv_delete_exams;
    private CardView btn_delete_delete, btn_delete_cancel;
    private ArrayAdapter<String> examAdapter;
    private String id;
    private DatabaseReference exam_ref;

    public DeleteDialog (Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public interface ExamCallback {
        void onExamCallback(ArrayList<String> exams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete);
        initViews();
        addDownloadedExams();
    }

    private void addDownloadedExams() {
        id = SharedPrefManager.getInstance(getContext()).getKeyUserId();
        exam_ref = FirebaseDatabase.getInstance().getReference().child("Students").child(id)
                .child("Downloaded Exams");

        new FirebaseCaller(getContext(), exam_ref).getExams(new ExamCallback() {
            @Override
            public void onExamCallback(ArrayList<String> exams) {

                examAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_multiple_choice, exams);
                lv_delete_exams.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lv_delete_exams.setAdapter(examAdapter);
            }
        });
    }

    private void initViews() {
        cb_delete_selectAll = findViewById(R.id.cb_delete_selectAll);
        lv_delete_exams = findViewById(R.id.lv_delete_exams);
        btn_delete_delete = findViewById(R.id.btn_delete_delete);
        btn_delete_cancel = findViewById(R.id.btn_delete_cancel);

        btn_delete_delete.setOnClickListener(this);
        btn_delete_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete_delete:
                ArrayList<String> exams = new ArrayList<>();
                getCheckedExams(exams);
                deleteCheckedExams(exams);
                Toast.makeText(activity, "Exams deleted successfully", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_delete_cancel:
                break;

            default:
                break;
        }

        dismiss();
    }

    private void deleteCheckedExams(ArrayList<String> exams) {
        for (String exam : exams){
            exam_ref.child(exam).removeValue();
        }
    }

    private ArrayList<String> getCheckedExams(ArrayList<String> exams) {
        SparseBooleanArray checked = lv_delete_exams.getCheckedItemPositions();

        for (int i = 0; i < lv_delete_exams.getAdapter().getCount(); i++) {
            if (checked.get(i)) {
                exams.add(lv_delete_exams.getItemAtPosition(i).toString());
            }
        }
        return exams;

    }
}
