package com.lizehao.watermelondiarynew.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lizehao.watermelondiarynew.R;
import com.lizehao.watermelondiarynew.bean.DiaryBean;
import com.lizehao.watermelondiarynew.db.DiaryDatabaseHelper;
import com.lizehao.watermelondiarynew.utils.GetDate;
import com.lizehao.watermelondiarynew.utils.StatusBarCompat;
import com.lizehao.watermelondiarynew.widget.LinedEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by 李 on 2017/1/26.
 */
public class UpdateDiaryActivity extends AppCompatActivity {
    @Bind(R.id.update_diary_tv_date)
    TextView mUpdateDiaryTvDate;
    @Bind(R.id.update_diary_et_title)
    EditText mUpdateDiaryEtTitle;
    @Bind(R.id.update_diary_et_content)
    LinedEditText mUpdateDiaryEtContent;
    @Bind(R.id.common_tv_title)
    TextView mCommonTvTitle;
    private DiaryDatabaseHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_update_diary);
        ButterKnife.bind(this);
        initTitle();
    }
    private void initTitle() {
        StatusBarCompat.compat(this, Color.parseColor("#161414"));
        mCommonTvTitle.setText("修改日记");
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        mUpdateDiaryTvDate.setText("今天，" + GetDate.getDate());
        mUpdateDiaryEtTitle.setText(getIntent().getStringExtra("title"));
        mUpdateDiaryEtContent.setText(getIntent().getStringExtra("content"));
    }
    @OnClick({R.id.common_iv_back, R.id.update_diary_fab_back, R.id.update_diary_fab_add, R.id.update_diary_fab_delete})
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                finish();
                break;
            case R.id.update_diary_fab_back:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("确定要删除该日记吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase dbDelete = mHelper.getWritableDatabase();
                        dbDelete.delete("Diary", "title = ?", new String[]{getTitle1()});
                        EventBus.getDefault().post("!!");
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
            case R.id.update_diary_fab_add:
                saveData();
                finish();
                break;
            case R.id.update_diary_fab_delete:
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
                alertDialogBuilder1.setMessage("是否保存日记内容？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Verify();
                        saveData();
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;
        }
    }


    public String getTitle1(){
        return mUpdateDiaryEtTitle.getText().toString();
    }
    public String getContent1(){
        return mUpdateDiaryEtContent.getText().toString();
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
        SQLiteDatabase dbUpdate = mHelper.getWritableDatabase();
        ContentValues valuesUpdate = new ContentValues();
        valuesUpdate.put("title", getTitle1());
        valuesUpdate.put("content", getContent1());
        dbUpdate.update("Diary", valuesUpdate, "title = ?", new String[]{getTitle1()});
        dbUpdate.update("Diary", valuesUpdate, "content = ?", new String[]{getContent1()});
        valuesUpdate.clear();
        EventBus.getDefault().post("!!");
    }
}