package app.bright.flashlight.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import app.bright.flashlight.util.Utils;

/**
 * Created by Administrator on 2017/7/17.
 */

public class SlideLightView extends View implements GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector = null;
    //    private ImageView imageView;
    private Paint paint;
    private Matrix matrix;
    private int mCeil, mFloor;
    private int max, preindex;
    private int halfItemWidth;
    private ViewManager viewManager;
    private  DisplayMetrics displayMetrics;
    public SlideLightView(Context context) {
        this(context, null);
    }

    public SlideLightView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiView();
    }

    float scalx;
    float scaly;

    private void intiView() {
        max = 0;
        viewManager = new ViewManager();
        matrix = new Matrix();
        paint = new Paint();
        this.paint = new Paint();
        this.matrix = new Matrix();
        final Context context = getContext();
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View rootView = inflater.inflate(R.layout.slide_view, null);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        addView(rootView, params);
        this.gestureDetector = new GestureDetector(this);
//        imageView = (ImageView) rootView.findViewById(R.id.iv_slide_view);

        displayMetrics= new DisplayMetrics();
        WindowManager systemService = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        systemService.getDefaultDisplay().getMetrics(displayMetrics);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTargetDensity = displayMetrics.densityDpi;
        options.inDensity = displayMetrics.densityDpi;
        if (800 == displayMetrics.heightPixels && 480 == displayMetrics.widthPixels) {
            viewManager.create_8(getResources(), options);
           
        } else if (854 == displayMetrics.heightPixels && 480 == displayMetrics.widthPixels) {
            viewManager.create_9(getResources(), options);
        } else if (480 == displayMetrics.heightPixels && 320 == displayMetrics.widthPixels) {
            viewManager.create_4(getResources(), options);
        } else{
            viewManager.create_default(getResources(), options);
//            float f = (((float) displayMetrics.heightPixels) * 1.0f) / ((float) displayMetrics.widthPixels);
//            Log.d("wwq", "height / width = " + f);
//            float[] fArr = new float[]{viewManager.dips_1, viewManager.dips_2, viewManager.dips_3, viewManager.dips_4};
//            Log.d("wwq", "array length is: " + fArr.length);
//            float f1 = 100.0F;
//            Log.d("wwq", "array length is: " + fArr.length);
//            int z = 0;
//            for (int z2 = 0; z2 < fArr.length; z2++) {
//                float abs = Math.abs(fArr[z2] - f);
//                if (abs < f1) {
//                    f1 = abs;
//                    z = z2;
//                }
//            }
//            switch (z) {
//                case 0:
//                    Log.d("wwq", "invoke load320to480");
//                    viewManager.create_4(getResources(), options);
//                    break;
//                case 1:
//                    Log.d("wwq", "invoke 480to640");
//                    viewManager.create_9(getResources(), options);
//                    break;
//                case 2:
//                    Log.d("wwq", "invoke load480t0800");
//                    viewManager.create_8(getResources(), options);
//                    break;
//                case 3:
//                    Log.d("wwq", "invoke load480to854");
//                    viewManager.create_9(getResources(), options);
//                    break;
//                default:
//                    Log.d("wwq", "default invoke load480t0800");
//                    viewManager.create_8(getResources(), options);
//                    break;
//            }
//            imageView.setImageBitmap(viewManager.slideBitmap);
        }
        halfItemWidth = viewManager.itemWidth / 2;
        options.inTargetDensity = displayMetrics.densityDpi;
        options.inDensity = displayMetrics.densityDpi;
        scalx = (float) ((Utils.getScreenWidht() * 1.0) / (float) viewManager.mainBg.getWidth());
        scaly = (float) (((Utils.getScreenHeight() * 1.0) / (float) viewManager.mainBg.getHeight())-0.7f) ;
//        imageView.setScaleX(scalx+0.5f);
//        imageView.setScaleY(scaly);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.matrix.reset();
        if (distanceXtemp > 525) {
            distanceXtemp = 525;
        }
        this.matrix.postTranslate(viewManager.translateX + distanceXtemp, 0f);
        this.matrix.postScale(scalx, scaly);
        canvas.drawBitmap(viewManager.slideBitmap, this.matrix, this.paint);
        this.matrix.reset();
    }

    private int index;
    private boolean isSlideClick = false;
    private boolean otherClick = false;

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        if (motionEvent.getY() > 10 && motionEvent.getY() < Utils.dip2px(133)) {
            isSlideClick = true;
        } else {
            isSlideClick = false;
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return false;
    }

    private float distanceXtemp;

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        imageView.setTranslationX(distanceX);
        if (isSlideClick) {
            distanceXtemp -= distanceX;
            if (this.distanceXtemp <= 0.0f) {
                max = 0;
                if (this.distanceXtemp < ((float) (-viewManager.itemWidth / 4))) {
                    this.distanceXtemp = (float) ((-viewManager.itemWidth) / 4);
                }
            } else if (this.distanceXtemp >= ((float) viewManager.widthTemp)) {
                if (this.distanceXtemp > ((float) (viewManager.widthTemp + (viewManager.itemWidth / 4)))) {
                    this.distanceXtemp = (float) (viewManager.widthTemp + (viewManager.itemWidth / 4));
                }
                max = 9;
            } else {
                int ceil = (int) Math.ceil((double) (this.distanceXtemp / ((float) viewManager.itemWidth)));
                int floor = (int) Math.floor((double) (this.distanceXtemp / ((float) viewManager.itemWidth)));
                if (floor < this.max || ceil <= this.max) {
                    if (ceil <= max && floor < max && 1 == max - ceil) {
                        max = ceil;
                        preindex = ceil;
                    }
                } else if (1 == floor - max) {
                    max = floor;
                    preindex = floor;
                }
                mListener.onChanged(max);
            }
            invalidate();
        }
        invalidate();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        if (this.gestureDetector != null) {
            this.gestureDetector.onTouchEvent(motionEvent);
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                if (isSlideClick) {
                    if (this.distanceXtemp < 0.0f) {
                        this.distanceXtemp = 0.0f;
                    }
                    if (this.distanceXtemp > ((float) (viewManager.itemWidth * 9))) {
                        this.distanceXtemp = (float) (viewManager.itemWidth * 9);
                    }
                    float f = (float) (max * viewManager.itemWidth);
                    if (this.distanceXtemp > f) {
                        if (this.distanceXtemp - f >= this.halfItemWidth) {
                            this.max++;
                        }
                    } else if (f - this.distanceXtemp >= this.halfItemWidth) {
                        this.max--;
                    }
                    switch (max) {
                        case 0:
                            distanceXtemp = 0;
                            break;
                        case 1:
                            distanceXtemp = (float) viewManager.itemWidth * 1;
                            break;
                        case 2:
                            distanceXtemp = (float) viewManager.itemWidth * 2;
                            break;
                        case 3:
                            distanceXtemp = (float) viewManager.itemWidth * 3;
                            break;
                        case 4:
                            distanceXtemp = (float) viewManager.itemWidth * 4;
                            break;
                        case 5:
                            distanceXtemp = (float) viewManager.itemWidth * 5;
                            break;
                        case 6:
                            distanceXtemp = (float) viewManager.itemWidth * 6;
                            break;
                        case 7:
                            distanceXtemp = (float) viewManager.itemWidth * 7;
                            break;
                        case 8:
                            distanceXtemp = (float) viewManager.itemWidth * 8;
                            break;
                        case 9:
                            distanceXtemp = (float) viewManager.itemWidth * 9;
                            break;
                        default:
                            this.distanceXtemp = (float) (viewManager.itemWidth * 9);
                            break;
                    }
                    isSlideClick = false;
                    mListener.onChanged(max);
                    postInvalidate();
                }

        }

//        imageView.setTranslationX(distanceXtemp);
        return true;
    }

    onFrequenceChangedListener mListener;

    public void setOnFrequenceChangedListener(onFrequenceChangedListener listener) {
        this.mListener = listener;
    }

    public interface onFrequenceChangedListener {
        void onChanged(int flag);
    }
}
