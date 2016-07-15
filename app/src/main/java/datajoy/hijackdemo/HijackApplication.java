package datajoy.hijackdemo;

import android.app.Application;

/**
 * Created by tigerlove on 16/7/14.
 */
public class HijackApplication extends Application {
    public boolean isHasHijackStart() {
        return hasHijackStart;
    }

    public void setHasHijackStart(boolean hasHijackStart) {
        this.hasHijackStart = hasHijackStart;
    }

    boolean hasHijackStart = false;

}
