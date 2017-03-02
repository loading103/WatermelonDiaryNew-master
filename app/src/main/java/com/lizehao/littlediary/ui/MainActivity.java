package com.lizehao.littlediary.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lizehao.littlediary.R;
import com.lizehao.littlediary.bean.DiaryBean;
import com.lizehao.littlediary.db.DiaryDatabaseHelper;
import com.lizehao.littlediary.event.StartUpdateDiaryEvent;
import com.lizehao.littlediary.utils.GetDate;
import com.lizehao.littlediary.utils.StatusBarCompat;
import com.lizehao.littlediary.widget.RecyclerViewItemSpaces;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.common_iv_back)   //返回键
    ImageView mCommonIvBack;
    @Bind(R.id.common_tv_title)  //Titile
    TextView mCommonTvTitle;
    @Bind(R.id.common_iv_test)
    ImageView mCommonIvTest;
    @Bind(R.id.main_tv_date)
    TextView mMainTvDate;
    @Bind(R.id.main_rv_show_diary)
    RecyclerView mMainRvShowDiary;
    @Bind(R.id.item_first)
    LinearLayout mItemFirst;
    private List<DiaryBean> mDiaryBeanList;
    private DiaryDatabaseHelper mHelper;
    private static String IS_WRITE = "true";
    private DiaryAdapter adapter;
    /**
     * 标识今天是否已经写了日记
     */
    private boolean isWrite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        StatusBarCompat.compat(this, Color.parseColor("#161414"));      // app状态栏变色

        EventBus.getDefault().register(this);                           //初始化EventBus
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);   //初始化数据库

        initTitle();                                                     //  初始化标题栏
        getDiaryBeanList();

        LinearLayoutManager manager=new LinearLayoutManager(this);          //创建列表布局
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mMainRvShowDiary.setLayoutManager(manager);
        mMainRvShowDiary.addItemDecoration(new RecyclerViewItemSpaces(0, 0, 0, 120));
        adapter=new DiaryAdapter(this, mDiaryBeanList);
        mMainRvShowDiary.setAdapter(adapter);

    }
    /**
     * 初始化标题栏
     * @return
     */
    private void initTitle() {
        mMainTvDate.setText("今天，" + GetDate.getDate());
        mCommonTvTitle.setText("日记");
        mCommonIvBack.setVisibility(View.INVISIBLE);
        mCommonIvTest.setVisibility(View.INVISIBLE);
    }
    /**
     * 获取数据（数据库查询）
     * @return
     */
    private List<DiaryBean> getDiaryBeanList() {
        if(mDiaryBeanList==null){
            mDiaryBeanList = new ArrayList<>();
        }
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);
        if (cursor!=null) {
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String dateSystem = GetDate.getDate().toString();
                mDiaryBeanList.add(0,new DiaryBean(date, title, content));
                if (date.equals(dateSystem)) {
                    mItemFirst.setVisibility(View.GONE);
                }else {
                    mItemFirst.setVisibility(View.VISIBLE);
                }
            }
        }
        cursor.close();
        return mDiaryBeanList;
    }
    /**
     * 点击跳转
     */
    @OnClick(R.id.main_fab_enter_edit)
    public void onClick() {
        Intent intent = new Intent(this, AddDiaryActivity.class);
        startActivity(intent);
    }
    /**
     * 解除绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    /**
     * EventBus刷新数据
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String event) {
        mDiaryBeanList.clear();
        mDiaryBeanList=getDiaryBeanList();
        mItemFirst.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }
    @Subscribe
    public void startUpdateActivity(StartUpdateDiaryEvent event) {
        Intent intent=new Intent(this,UpdateDiaryActivity.class);
        intent.putExtra("title",mDiaryBeanList.get(event.getPosition()).getTitle());
        intent.putExtra("content", mDiaryBeanList.get(event.getPosition()).getContent());
        startActivity(intent);
    }
}