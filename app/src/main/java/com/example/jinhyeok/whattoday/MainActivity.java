package com.example.jinhyeok.whattoday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //Toolbar Bind
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;

    //Layout Bind & Layout parameter Setting
    @BindView(R.id.alarmLayout)
    LinearLayout alarmLayout;
    LinearLayout.LayoutParams paramText = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

    //Button Bind
    @BindView(R.id.startAlarm)
    Button startAlarmButton;
    @BindView(R.id.stopAlarm)
    Button stopAlarmButton;

    //AlarmManger declaration
    public static AlarmManager mAlarmManger = null;
    public static PendingIntent mAlarmIntent = null;

    //Alarm Count
    int count;

    //DB
    private DBHelper dbHelper;

    String dbName = "time_content.db";

    //DB View
    @BindView(R.id.lvWorks)
    ListView lvWork;
    @BindView(R.id.btnSelectAllData)
    Button btnSelectAllData;

    //Setting
    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Use Toolbar
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("메인화면");

        //Setting
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //DB creation
        dbHelper = new DBHelper(
                MainActivity.this, //현재 화면의 제어권자
                dbName, //데이터베이스 이름
                null, //커서팩토리 - null 이면 표준 커서 사용
                1); //데이터베이스 버전

        dbHelper.testDB();


    }

    //ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        Intent settingIntent = new Intent(this, SettingActivity.class);

        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getApplicationContext(), "환경설정 버튼 클릭됨", Toast.LENGTH_LONG).show();
                startActivity(settingIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }

    //알람 설정
    @OnClick(R.id.startAlarm)
    public void registerAlarm() {
        //알람 매너저 생성
        mAlarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // startAlarm 실행 Debug용 Toast
        Toast.makeText(this, "알람 시작", Toast.LENGTH_SHORT).show();
        // Device를 깨운 후 시스템 시간 기준 1초 후 부터 alarmIntent 실행 , 50초 단위로 반복 실행
//        mAlarmManger.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + 1000,
//                settingalarmIntervalTime(), alarmIntent());

        //realTime으로 수정 완료
        mAlarmManger.setRepeating(AlarmManager.RTC_WAKEUP,
                alarmTriggerTime(),
                settingalarmIntervalTime(), alarmIntent());
    }

    //알람을 실행하는 시간 설정 함수
    private long alarmTriggerTime() {
        Calendar calendar = Calendar.getInstance();

        // 시간 단위
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        int timeFormat = Integer.parseInt(formatter.format(System.currentTimeMillis()));
        int alarmHour = timeFormat/100;
        int alarmMinute = timeFormat%100;

        // 1시간 단위 알람
        alarmHour = alarmHour+1;
        alarmHour = 0;

        //30분 단위로 알람을 울릴 경우
//        if(timeFormat%100 >= 30){
//            alarmHour = alarmHour+1;
//            alarmMinute = 0;
//        }else{
//            alarmMinute = 30;
//        }

        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }


    //알람 해제
    @OnClick(R.id.stopAlarm)
    public void unregisterAlarm() {
        // startAlarm 실행 Debug용 Toast
        Toast.makeText(this, "알람 해제 버튼", Toast.LENGTH_SHORT).show();

        if (mAlarmIntent != null) {
            Toast.makeText(this, "알람 해제", Toast.LENGTH_SHORT).show();

            //알람 매너저 생성
            mAlarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            mAlarmManger.cancel(alarmIntent());
            mAlarmIntent.cancel();

            mAlarmManger = null;
            mAlarmIntent = null;
        }

    }


    private PendingIntent alarmIntent() {

        // alarmIntent 실행 Debug용 Toast
        Toast.makeText(this, "인텐트 실행", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, AlarmService_Service.class);

        intent.putExtra("data", "Test Popup");
        mAlarmIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return mAlarmIntent;
    }

    @OnClick(R.id.btnSelectAllData)
    public void visibleData(){
        lvWork.setVisibility(View.VISIBLE);
        // DB Helper가 Null이면 초기화
        if( dbHelper == null){
            dbHelper = new DBHelper(MainActivity.this, //현재 화면의 제어권자
                    dbName, //데이터베이스 이름
                    null, //커서팩토리 - null 이면 표준 커서 사용
                    1); //데이터베이스 버전
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = formatter.format(System.currentTimeMillis());


        List works = dbHelper.getDayWorkData(currentTime);

        // 1. DayWork 데이터를 모두 가져온다.
//        List works = dbHelper.getAllWorkData();

        // 2. ListView에 DayWork 데이터를 모두 보여줌
        lvWork.setAdapter(new WorkListAdapter(works, MainActivity.this));
    }

    public int settingalarmIntervalTime(){

        int time = 0;

        Toast.makeText(this, "설정 들어감", Toast.LENGTH_SHORT).show();


        switch(sharedPref.getString("time_interval_list", "1분")){
            case "1분":
                time = 60000;
                break;
            case "2분":
                time = 120000;
                break;
            case "3분":
                time = 180000;
                break;
            case "4분":
                time = 240000;
                break;
            default:
                time = 60000;
        }

        Log.d("알람 설정 들어감", String.valueOf(time));

        return time;

    }

    // 사용자가 일시 중지된 상태에서 액티비티로 돌아오면 시스템은 액티비티를 재개하고
    // onResume() 메서드를 호출합니다.
    @Override
    protected void onResume() {
        super.onResume();
        lvWork.setVisibility(View.VISIBLE);
        // DB Helper가 Null이면 초기화
        if( dbHelper == null){
            dbHelper = new DBHelper(MainActivity.this, //현재 화면의 제어권자
                    dbName, //데이터베이스 이름
                    null, //커서팩토리 - null 이면 표준 커서 사용
                    1); //데이터베이스 버전
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = formatter.format(System.currentTimeMillis());


        List works = dbHelper.getDayWorkData(currentTime);

        // 1. DayWork 데이터를 모두 가져온다.
//        List works = dbHelper.getAllWorkData();

        // 2. ListView에 DayWork 데이터를 모두 보여줌
        lvWork.setAdapter(new WorkListAdapter(works, MainActivity.this));
    }
}