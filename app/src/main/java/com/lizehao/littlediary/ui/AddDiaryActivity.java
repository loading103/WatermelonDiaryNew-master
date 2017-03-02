package com.lizehao.littlediary.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lizehao.littlediary.R;
import com.lizehao.littlediary.db.DiaryDatabaseHelper;
import com.lizehao.littlediary.utils.GetDate;
import com.lizehao.littlediary.utils.StatusBarCompat;
import com.lizehao.littlediary.widget.LinedEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class AddDiaryActivity extends AppCompatActivity {
    @Bind(R.id.add_diary_tv_date)
    TextView mAddDiaryTvDate;
    @Bind(R.id.add_diary_et_title)
    EditText mAddDiaryEtTitle;
    @Bind(R.id.add_diary_et_content)
    LinedEditText mAddDiaryEtContent;
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    private DiaryDatabaseHelper mHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_add_diary);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, Color.parseColor("#161414"));

        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);

        mCommonTvTitle.setText("添加日记");
        mAddDiaryTvDate.setText("今天，" + GetDate.getDate());

        mAddDiaryEtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=50){
                    Toast.makeText(AddDiaryActivity.this, "标题最多输入50字", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @OnClick({R.id.common_iv_back, R.id.add_diary_fab_back, R.id.add_diary_fab_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                finish();
                break;
            case R.id.add_diary_fab_back:
                Verify();
                saveData();
                finish();
                break;
            case R.id.add_diary_fab_add:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("是否保存日记内容？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Verify();
                            saveData();
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                break;
        }
    }
    public String getTitle1(){
        return mAddDiaryEtTitle.getText().toString();
    }
    public String getContent1(){
        return mAddDiaryEtContent.getText().toString();
    }
    public String getData(){
        return GetDate.getDate().toString();
    }
    public void showToast(String ms){
        Toast.makeText(this, ms, Toast.LENGTH_SHORT).show();
    }
    public void Verify(){
        if(TextUtils.isEmpty(getTitle1())){
            showToast("标题不能为空");
            return;
        }else  if(TextUtils.isEmpty(getContent1())){
            showToast("内容不能为空");
            return;
        }
    }
    public void saveData(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", getData());
        values.put("title", getTitle1());
        values.put("content", getContent1());
        db.insert("Diary", null, values);
        EventBus.getDefault().post("11");
        values.clear();
    }
}











