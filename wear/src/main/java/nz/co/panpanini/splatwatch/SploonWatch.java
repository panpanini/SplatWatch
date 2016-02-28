/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.panpanini.splatwatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.format.Time;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import me.denley.courier.Courier;
import me.denley.courier.ReceiveData;
import nz.co.panpanini.datalayer.models.ColourSet;

/**
 * Digital watch face with seconds. In ambient mode, the seconds aren't displayed. On devices with
 * low-bit ambient mode, the text is drawn without anti-aliasing in ambient mode.
 */
public class SploonWatch extends CanvasWatchFaceService{
    private static final Typeface NORMAL_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    /**
     * Update rate in milliseconds for interactive mode. We update once a second since seconds are
     * displayed in interactive mode.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private static class EngineHandler extends Handler {
        private final WeakReference<SploonWatch.Engine> mWeakReference;

        public EngineHandler(SploonWatch.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            SploonWatch.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME:
                        engine.handleUpdateTimeMessage();
                        break;
                }
            }
        }
    }

    public class Engine extends CanvasWatchFaceService.Engine{
        final Handler mUpdateTimeHandler = new EngineHandler(this);
        boolean mRegisteredTimeZoneReceiver = false;
        Paint mBackgroundPaint;
        Paint mHairPaint;
        Paint mLeftEyePaint;
        Paint mRightEyePaint;
        Paint mClothingPaint;

        Paint mTextPaint;
        TextPaint mBatteryTextPaint;
        boolean mAmbient;
        Time mTime;
        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };
        int mTapCount;

        float mXOffset;
        float mYOffset;

        private Bitmap background;
        private Bitmap hair;
        private Bitmap leftEye;
        private Bitmap rightEye;
        private Bitmap clothing;

        private ColourSet colourSet;


        private boolean showRanked = false;

        @ReceiveData("/regular_maps")
        protected ArrayList regularMaps = new ArrayList(); // have to be generic because the generator can't handle it

        @ReceiveData("/ranked_maps")
        protected ArrayList rankedMaps = new ArrayList();

        protected short timer = 0;
        private int index = 0;

        protected String mapText;

        private StaticLayout textLayout;


        /**
         * Whether the display supports fewer bits for each color in ambient mode. When true, we
         * disable anti-aliasing in ambient mode.
         */
        boolean mLowBitAmbient;

        Random rand = new Random();

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(SploonWatch.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setPeekOpacityMode(WatchFaceStyle.PEEK_OPACITY_MODE_TRANSLUCENT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(true)
                    .setAcceptsTapEvents(true)
                    .build());
            Resources resources = SploonWatch.this.getResources();
            mYOffset = resources.getDimension(R.dimen.digital_y_offset);

            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(resources.getColor(R.color.background));

            mHairPaint = new Paint();
            mLeftEyePaint = new Paint();
            mRightEyePaint = new Paint();

            mTextPaint = new Paint();
            mTextPaint = createTextPaint(resources.getColor(R.color.background2));


            mTime = new Time();

            background = ((BitmapDrawable)getResources().getDrawable(R.drawable.body)).getBitmap();

            hair = ((BitmapDrawable)getResources().getDrawable(R.drawable.hair)).getBitmap();

            leftEye = ((BitmapDrawable)getResources().getDrawable(R.drawable.left_eye)).getBitmap();
            rightEye = ((BitmapDrawable)getResources().getDrawable(R.drawable.right_eye)).getBitmap();


            clothing = ((BitmapDrawable)getResources().getDrawable(R.drawable.clothing)).getBitmap();

            // recieve data from phone
            Courier.startReceiving(getBaseContext(), this);

            if (colourSet == null){
                // default colours
                colourSet = new ColourSet(Color.RED, 0xFFCCABA6, getResources().getColor(R.color.colorPrimary));
            }

            PorterDuffColorFilter filter = new PorterDuffColorFilter(colourSet.getPrimaryColour(), PorterDuff.Mode.MULTIPLY);

            mHairPaint.setColorFilter(filter);

            mLeftEyePaint.setColorFilter(new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY));
            mRightEyePaint.setColorFilter(new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY));


            mBatteryTextPaint = createBatteryTextPaint(colourSet.getPrimaryColour());

            mClothingPaint = new Paint();
            mClothingPaint.setColor(Color.WHITE);

            // lets get some data
            Courier.deliverMessage(getApplicationContext(), "/request_update", "requesting update");

        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            Courier.stopReceiving(this);
            super.onDestroy();
        }

        private Paint createTextPaint(int textColor) {
            Paint paint = new Paint();
            paint.setColor(textColor);
            paint.setTypeface(NORMAL_TYPEFACE);
            paint.setTextSize(12);
            paint.setAntiAlias(true);
            return paint;
        }

        private TextPaint createBatteryTextPaint(int textColor) {
            TextPaint paint = new TextPaint();
            paint.setColor(textColor);
            paint.setTypeface(NORMAL_TYPEFACE);
            paint.setTextSize(25);
            paint.setAntiAlias(true);
            return paint;
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();
            } else {
                unregisterReceiver();
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            SploonWatch.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            SploonWatch.this.unregisterReceiver(mTimeZoneReceiver);
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            // Load resources that have alternate values for round watches.
            Resources resources = SploonWatch.this.getResources();
            boolean isRound = insets.isRound();
            mXOffset = resources.getDimension(isRound
                    ? R.dimen.digital_x_offset_round : R.dimen.digital_x_offset);
            float textSize = resources.getDimension(isRound
                    ? R.dimen.digital_text_size_round : R.dimen.digital_text_size);

            mTextPaint.setTextSize(textSize);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;
                if (mLowBitAmbient) {
                    mTextPaint.setAntiAlias(!inAmbientMode);
                }
                invalidate();
            }

            if (!mAmbient){
                // we're in interactive mode, so request a new schedule update
                Courier.deliverMessage(getApplicationContext(), "/request_update", "requesting update");
            }

            // Whether the timer should be running depends on whether we're visible (as well as
            // whether we're in ambient mode), so we may need to start or stop the timer.
            updateTimer();
        }

        /**
         * Captures tap event (and tap type) and toggles the background color if the user finishes
         * a tap.
         */
        @Override
        public void onTapCommand(int tapType, int x, int y, long eventTime) {
            switch (tapType) {
                case TAP_TYPE_TOUCH:
                    // The user has started touching the screen.
                    break;
                case TAP_TYPE_TOUCH_CANCEL:
                    // The user has started a different gesture or otherwise cancelled the tap.
                    break;
                case TAP_TYPE_TAP:
                    // The user has completed the tap gesture.
                    mTapCount++;
                    break;
            }
            invalidate();
        }

        private int getBatteryLevel() {

            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = getBaseContext().registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            return (int) ((level / (float) scale) * 100);
        }

        private String getTitleString(boolean isRanked){
            if (isRanked){
                if (rankedMaps.size() > 0) {
                    return rankedMaps.get(0).toString() + "\n";
                }
            }else{
                if (regularMaps.size() > 0) {
                    return regularMaps.get(0).toString() + "\n";
                }
            }

            return "";
        }

        /**
         * Returns the next map title within this group, and increments timers for the next time
         * around the loop
         * @return
         */
        public String getNextMapString(){
            String map = "";
            if (index == 0){
                index = 1; // the 0th entry is the "ranked/regular" text
            }
            if (showRanked){
                if (index < rankedMaps.size()){
                    map = rankedMaps.get(index++).toString();
                }
                if (index >= rankedMaps.size()){
                    showRanked = false;
                    index = 0;
                }
            }else{
                // regular maps
                if (index < regularMaps.size()){
                    map = regularMaps.get(index++).toString();
                }
                if (index >= regularMaps.size()){
                    showRanked = true;
                    index = 0;
                }
            }
            return map;

        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            // Draw the background.
            if (isInAmbientMode()) {
                canvas.drawColor(Color.BLACK);
            } else {
                canvas.drawColor(colourSet.getTertiaryColour());
            }

            canvas.drawBitmap(hair, 70, 0, mHairPaint);
            canvas.drawBitmap(leftEye, 70, 0, mLeftEyePaint);
            canvas.drawBitmap(rightEye, 70, 0, mRightEyePaint);
            canvas.drawBitmap(clothing, 70, 0, mClothingPaint);

            canvas.drawBitmap(background, 70, 0, mBackgroundPaint);

            if (timer == 2) {
                mapText = getTitleString(showRanked);
                mapText += getNextMapString();
                timer = 0;
            }else {
                timer++;
            }
            if (mapText == null){
                mapText = "";
            }

            // this seems like a lot of object churn :/
            textLayout = new StaticLayout(mapText, mBatteryTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);


            canvas.save();

            canvas.translate(mXOffset, (canvas.getHeight() / 2) + 15);

            textLayout.draw(canvas);

            canvas.restore();

            // Draw battery level
            String battery = getBatteryLevel() + "%";
            canvas.drawText(battery, mXOffset, (canvas.getHeight() / 2) - 20, mBatteryTextPaint);

        }

        /**
         * Starts the {@link #mUpdateTimeHandler} timer if it should be running and isn't currently
         * or stops it if it shouldn't be running but currently is.
         */
        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        /**
         * Returns whether the {@link #mUpdateTimeHandler} timer should be running. The timer should
         * only run when we're visible and in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Handle updating the time periodically in interactive mode.
         */
        private void handleUpdateTimeMessage() {
            invalidate();
            if (shouldTimerBeRunning()) {
                long timeMs = System.currentTimeMillis();
                long delayMs = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        }


    }
}
