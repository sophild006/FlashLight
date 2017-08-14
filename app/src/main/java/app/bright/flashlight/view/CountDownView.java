package app.bright.flashlight.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.solid.lock.util.Utils;

/**
 * Created by caosc on 2017/7/20.
 */

public class CountDownView extends View {

    public CountDownView(Context context) {
        this(context, null);
        init();
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paintText;
    private Paint paintCircle;
    private Paint paintArc;
    private boolean isStart;
    private int count;
    private int sweepAngle;
    private ValueAnimator animator;
    private final Rect mBounds = new Rect();
    private int duration;
    private int distance = Utils.dip2px(4);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count--;
            invalidate();
            if (count > 0) {
                handler.sendEmptyMessageDelayed(0, 1000);
            } else {
                animator.start();
            }
        }
    };

    private void init() {
        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setColor(Color.WHITE);
        paintText.setStyle(Paint.Style.FILL_AND_STROKE);

        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        paintArc = new Paint();
        paintArc.setAntiAlias(true);
        paintArc.setStrokeWidth(Utils.dip2px(2));
        paintArc.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (!isStart) {
            return;
        }

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paintCircle);
        RectF rectF = new RectF(distance, distance, getWidth() - distance, getHeight() - distance);
        canvas.drawArc(rectF, -90, sweepAngle, false, paintArc);
        String text = "";
        if (count > 0) {
            text = count + " s";
        } else {
            text = "skip";
        }
        paintText.getTextBounds(text, 0, text.length(), mBounds);
        canvas.drawText(text, getWidth() / 2 - mBounds.exactCenterX(), getHeight() / 2 - mBounds.exactCenterY(), paintText);
    }

    public void start() {
        isStart = true;
        count = 2;
        sweepAngle = 360;
        animator = ValueAnimator.ofInt(360, 0).setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                sweepAngle = -value;
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.endCallBack();
                }
            }
        });
        invalidate();
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    public void setProperty(int bgColor, int circleColor, int textSize, int duration) {
        this.duration = duration;
        paintCircle.setColor(bgColor);
        paintArc.setColor(circleColor);
        paintText.setTextSize(textSize);
    }

    private EndCallBack listener;

    public interface EndCallBack {
        void endCallBack();
    }

    public void setCallBack(EndCallBack listener) {
        this.listener = listener;
    }


    public boolean isCloseDisabled() {
        return count > 0;
    }

}