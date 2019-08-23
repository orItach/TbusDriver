package project.java.tbusdriver.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
   public class InnerRecevier extends BroadcastReceiver
    {
        public InnerRecevier(Context mContext) {
            this.mContext = mContext;
            mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        }

        public interface OnHomePressedListener
        {
            public void onHomePressed();
            public void onRecentAppPressed();
        }
        static final String TAG = "hg";
        private Context mContext;
        private static IntentFilter mFilter;
        private static OnHomePressedListener mListener;
        private InnerRecevier mRecevier;

        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
            {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null)
                {
                    Log.e(TAG, "action:" + action + ",reason:" + reason);
                    if (mListener != null)
                    {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY))
                        {
                            mListener.onHomePressed();
                        }
                        else
                            if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS))
                            {
                            mListener.onRecentAppPressed();
                             }
                    }
                }
            }
        }
        public static void bindListener(OnHomePressedListener listener) {
            mListener = listener;
            mFilter=new IntentFilter();
        }
        public void start(){
            if(mFilter!=null)
                mContext.registerReceiver(mRecevier,mFilter);
        }
        public static void unbindListener() {
            mListener = null;
        }

    }
