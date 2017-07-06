package com.safeconnectionsapp.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.safeconnectionsapp.R;


public class BadgeDrawable extends Drawable {

    private float mTextSize;
    private Paint mBadgePaint;
    private Paint mTextPaint;
    private Rect mTxtRect = new Rect();

    private String mCount = "";
    private boolean mWillDraw = false;

    public BadgeDrawable(Context context) 
    {
        mTextSize = context.getResources().getDimension(R.dimen.badge_text_size);

        mBadgePaint = new Paint();
        int myColor =
        	    context.getResources().getColor(R.color.colorAccent);
        mBadgePaint.setColor(Color.RED);
        mBadgePaint.setAntiAlias(true);

        mBadgePaint.setStyle(Paint.Style.FILL);



        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mWillDraw) {
            return;
        }

        Rect bounds = getBounds();
        float width = bounds.right - bounds.left;
        float height = bounds.bottom - bounds.top;

        // Position the badge in the top-right quadrant of the icon.
        float radius = ((Math.max(width, height) / 2)) / 2;
        float centerX = (width - radius-1) +15;//positive to right   left and right
        float centerY = radius - 10;//top and bottom

        if(mCount.length() <= 2){
            // Draw badge circle.
            canvas.drawCircle(centerX, centerY, (int)(radius+7.5), mTextPaint);
            canvas.drawCircle(centerX, centerY, (int)(radius+5.5), mBadgePaint);
        }
        else{
            canvas.drawCircle(centerX, centerY, (int)(radius+8.5), mTextPaint);
            canvas.drawCircle(centerX, centerY, (int)(radius+6.5), mBadgePaint);

//	        	canvas.drawRoundRect(radius, radius, radius, radius, 10, 10, mBadgePaint);
        }

        // Draw badge circle.
       // canvas.drawCircle(centerX, centerY, radius, mBadgePaint);


        // Draw badge count text inside the circle.
        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
        float textHeight = mTxtRect.bottom - mTxtRect.top;
        float textY = centerY + (textHeight / 2f);
        if(mCount.length() > 2)
            canvas.drawText("99+", centerX, textY, mTextPaint);
        else
            canvas.drawText(mCount, centerX, textY, mTextPaint);






        //canvas.drawText(mCount, centerX, textY, mTextPaint);
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    public void setCount(int count) {
        mCount = Integer.toString(count);

        // Only draw a badge if there are notifications.
        mWillDraw = count > 0;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // do nothing
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}