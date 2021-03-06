package my.base.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import java.util.Calendar;

public class DigitalClock extends android.widget.DigitalClock {
    private static final String m12 = "h:mm aa";
    private static final String m24 = "k:mm";
    Calendar mCalendar;
    String mFormat;
    private FormatChangeObserver mFormatChangeObserver;
    private Handler mHandler;
    private Runnable mTicker;
    private boolean mTickerStopped = false;

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        public void onChange(boolean selfChange) {
            DigitalClock.this.setFormat();
        }
    }

    public DigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        if (this.mCalendar == null) {
            this.mCalendar = Calendar.getInstance();
        }
        this.mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(System.CONTENT_URI, true, this.mFormatChangeObserver);
        setFormat();
    }

    protected void onAttachedToWindow() {
        this.mTickerStopped = false;
        super.onAttachedToWindow();
        this.mHandler = new Handler();
        this.mTicker = new Runnable() {
            public void run() {
                if (!DigitalClock.this.mTickerStopped) {
                    DigitalClock.this.mCalendar.setTimeInMillis(SystemClock.currentThreadTimeMillis());
                    DigitalClock.this.setText(DateFormat.format(DigitalClock.this.mFormat, DigitalClock.this.mCalendar));
                    DigitalClock.this.invalidate();
                    long now = SystemClock.uptimeMillis();
                    DigitalClock.this.mHandler.postAtTime(DigitalClock.this.mTicker, now + (1000 - (now % 1000)));
                }
            }
        };
        this.mTicker.run();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mTickerStopped = true;
    }

    private boolean get24HourMode() {
        return DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        if (get24HourMode()) {
            this.mFormat = m24;
        } else {
            this.mFormat = m12;
        }
    }
}
