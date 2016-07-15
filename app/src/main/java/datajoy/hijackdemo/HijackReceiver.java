package datajoy.hijackdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tigerlove on 16/7/14.
 */
public class HijackReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Intent serviceIntent = new Intent(context,HijackService.class);
            context.startService(serviceIntent);
        }
    }
}
