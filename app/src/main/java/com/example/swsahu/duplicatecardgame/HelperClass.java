package com.example.swsahu.duplicatecardgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;


public class HelperClass {

    public static final int SCREEN_GAME = 0;
    public static final int ONE_PLAYER = 1;
    public static final int TWO_PLAYER = 2;
    public static final int ROBOT_PLAYER = 3;
    public static final int ARCADE = 4;
    public static final int TIME_TRIAL = 5;
    public static final int ONE_BOARD = 6;
    public static final int TWO_BOARD = 7;
    public static final int SCROLL = 8;
    public static final int NO_SCROLL = 9;
    public static final int EASY = 10;
    public static final int MEDIUM = 11;
    public static final int HARD = 12;
    public static final int VERTICAL = 13;
    public static final int HORIZONTAL = 14;
    public static final int BOTH = 15;
    public static final int OneBoard_WithoutScroll = 16;
    public static final int TwoBoard_WithoutScroll = 17;
    public static final int OneBoard_HorizontalScroll = 18;
    public static final int OneBoard_VerticalScroll = 19;
    public static final int OneBoard_BothScroll = 20;
    public static final int TwoBoard_HorizontalScroll = 21;
    public static final int TwoBoard_VerticalScroll = 22;
    public static final int TwoBoard_BothScroll = 23;
    public final static int POW_REPLACE = 24;
    public final static int POW_SHUFFLE = 25;
    public final static int POW_PEEK = 26;
    public final static int POW_DESTROY = 27;
    public final static int POW_EXTRA_MOVES = 28;
    public final static int POW_FIND = 29;
    public final static int POW_SWAP = 30;
    public final static int CARD_SET_1 = 31;
    public final static int CARD_SET_2 = 32;
    public final static int CARD_SET_3 = 33;
    public final static int PLAYER_ID_0 = 34;
    public final static int PLAYER_ID_1 = 35;
    public final static int PLAYER_ID_2 = 36;
    public final static int PLAYER_ID_3 = 37;
    public final static int PLAYER_TWO_TYPE = 38;
    public final static int QUICK_GAME = 39;
    public final static int MANUAL = 40;
    public final static int HURRICANE = 41;
    public final static int ROCK = 42;
    public final static int ANDROBOT = 43;
    public final static int RANDOM_BOT = 44;
    public final static int POWER_COUNT = 45;
    public final static int FLIP_ANIMATION_TIME = 46;
    public final static int PLAYER_ONE_NAME = 47;
    public final static int PLAYER_TWO_NAME = 48;
    public final static int ALPHABET = 49;
    public final static int LOCKING_TIME = 58;
    public final static int STORY_MODE_CARD_SET = 68;




    //// Identifiers used to store data to shared preferences
    public final static int GAME_MODE = 100;
    public final static int PLAYER_MODE = 102;
    public final static int ROBOT_MEMORY = 103;
    public final static int BOARD_TYPE = 104;
    public final static int TIME_TRIAL_TIMER = 105;
    public final static int SCROLL_TYPE = 106;
    public final static int CARD_SET = 107;
    public final static int ROW_SIZE = 108;
    public final static int COLUMN_SIZE = 109;
    public final static int PREVIOUS_AVERAGE = 110;
    public final static int PREVIOUS_PLAYER_MODE = 111;
    public final static int PREVIOUS_WINNING_STREAK = 112;
    public final static int TOTAL_COINS = 113;
    public final static int GAME_BACKGROUND = 114;
    ////

    //Numeric Constants
    public final static int ONE = 1;
    public final static int TWO = 2;
    public final static int THREE = 3;
    public final static int FOUR = 4;
    public final static int FIVE = 5;
    public final static int SIX = 6;
    public final static int SEVEN = 7;
    public final static int EIGHT = 8;
    public final static int NINE = 9;
    public final static int ZERO = 0;
    public final static int MAX_COL_SIZE = 8;
    public final static int MAX_ROW_SIZE_1B = 15;
    public final static int MAX_ROW_SIZE_2B = 7;
    public final static int TIME_TRIAL_VALUE_1 = 5000;
    public final static int TIME_TRIAL_VALUE_2 = 10000;
    public final static int TIME_TRIAL_VALUE_3 = 15000;

    //String constants
    public final static String DELIMITER = "_";
    public final static String DELIMITER_2 = "~";




    public static int ConvertToPx(Context c,int dip)
    {

        Resources r = c.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }

    public static int ConvertToDp(int px ){

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static void applyBorderDrawableToView(View v,int backgroundColor,int borderColor,int cornerRadius,int borderThickness)
    {
        Drawable drawable;
        drawable = createDrawableBackground(backgroundColor, cornerRadius, true,borderThickness,borderColor);
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
        {
            v.setBackgroundDrawable(drawable);
        }
        else
        {
            v.setBackground(drawable);
        }
    }
    public static Drawable createDrawableBackground(int color,int cornerRadius,boolean hasBorder,
                                                    int borderThickness,int borderColor)
    {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        if(hasBorder)
            drawable.setStroke(borderThickness, borderColor);
        drawable.setCornerRadius(cornerRadius);
        drawable.setColor(color);
        return drawable;
    }

    public static void applyGradientDrawableToView(View v,int resID)
    {
        v.setBackgroundResource(resID);
    }

    public static Point getWindowSize(Display defaultDisplay)
    {
        Point windowSize = new Point();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            defaultDisplay.getSize(windowSize);
        }
        else
        {
            windowSize.x = defaultDisplay.getWidth();
            windowSize.y = defaultDisplay.getHeight();
        }
        return windowSize;
    }

    public static void clearArray(int [][] Array,int rowSize,int colSize)
    {
        for(int i=0;i<rowSize;i++)
            for(int j=0; j< colSize;j++)
                Array[i][j]=0;
    }

    public static int getLengthOfDynamicArray(Object[] array)
    {
        int max_length = array.length;
        for(int actual_length=0;actual_length<max_length;actual_length++)
        {
            if(array[actual_length]==null)
                return actual_length;
        }
        return max_length;
    }

    public static void ConfigureOutOfParentAnimation(View view,boolean value)
    {
        try {
           // while (view != null)
           {
                ((ViewGroup) view.getParent()).setClipChildren(!value);
                ((ViewGroup) view.getParent()).setClipToPadding(!value);
                view = (View) view.getParent();
               ((ViewGroup) view.getParent()).setClipChildren(!value);
               ((ViewGroup) view.getParent()).setClipToPadding(!value);
             //   view = (View) view.getParent();
            }
        } catch (Exception e) {}
    }

    public static AnimationSet FlipAnimation(int duration,int repeatCount)
    {
        AnimationSet flip = new AnimationSet(true);
        if(duration>20) //Flip anim
        {
            Animation from_middle1_anim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            from_middle1_anim.setDuration(duration);
            from_middle1_anim.setStartOffset(0);
            from_middle1_anim.setRepeatMode(Animation.REVERSE);
            from_middle1_anim.setRepeatCount(repeatCount);

            flip.addAnimation(from_middle1_anim);
        }
        else // no anim
        {
            Animation noAnim = new AlphaAnimation(1f,1f);
            noAnim.setDuration(duration);
            flip.addAnimation(noAnim);
        }
        return flip;
    }

    public static AnimationSet SwapAnimation(int deltaX,int deltaY)
    {
        TranslateAnimation shake_1 = new TranslateAnimation(0, deltaX, 0, deltaY);
        shake_1.setDuration(400);
        shake_1.setStartOffset(0);
        shake_1.setFillAfter(true);
        TranslateAnimation shake_2 = new TranslateAnimation(0, -deltaX, 0, -deltaY);
        shake_2.setDuration(400);
        shake_2.setStartOffset(400);
        shake_2.setFillAfter(true);
        AnimationSet ShakeIt = new AnimationSet(true);
        ShakeIt.addAnimation(shake_1);
        ShakeIt.addAnimation(shake_2);
        ShakeIt.setInterpolator(new AnticipateInterpolator(0.8f));

        return ShakeIt;
    }

    public static AnimationSet ShuffleAnimation(int deltaX,int deltaY)
    {

        TranslateAnimation shake_1 = new TranslateAnimation(0, deltaX, 0, deltaY);
        shake_1.setDuration(400);
        shake_1.setStartOffset(0);
        shake_1.setFillAfter(true);
        TranslateAnimation shake_2 = new TranslateAnimation(0, -deltaX, 0, -deltaY);
        shake_2.setDuration(400);
        shake_2.setStartOffset(400);
        shake_2.setFillAfter(true);
        AnimationSet ShakeIt = new AnimationSet(true);
        ShakeIt.addAnimation(shake_1);
        ShakeIt.addAnimation(shake_2);
        ShakeIt.setInterpolator(new OvershootInterpolator());

        return ShakeIt;
    }

    public static AnimationSet RotateAndFadeOutAnimation()
    {
        AlphaAnimation fade_out = new AlphaAnimation(1f,0.2f);
        fade_out.setDuration(500);
        fade_out.setStartOffset(0);
        fade_out.setFillAfter(true);

        ScaleAnimation shrink = new ScaleAnimation(1f,0.2f,1f,0.2f, Animation.RELATIVE_TO_SELF,.5f, Animation.RELATIVE_TO_SELF  , .5f);
        shrink.setDuration(400);
        shrink.setStartOffset(0);
        shrink.setFillAfter(true);

        RotateAnimation rotate = new RotateAnimation(0.0f,360f,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.5f);
        rotate.setDuration(500);
        rotate.setStartOffset(0);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);


        AnimationSet Reload = new AnimationSet(true);
        Reload.addAnimation(fade_out);
        Reload.addAnimation(shrink);
        Reload.addAnimation(rotate);
        Reload.setInterpolator(new AccelerateInterpolator(1.1f));

        return Reload;
    }

    public static AnimationSet RotateAndFadeInAnimation()
    {

        AlphaAnimation fade_in = new AlphaAnimation(0.2f,1f);
        fade_in.setDuration(400);
        fade_in.setStartOffset(0);
        fade_in.setFillAfter(true);

        ScaleAnimation expand = new ScaleAnimation(0.2f,1,0.2f,1,Animation.RELATIVE_TO_SELF,.5f, Animation.RELATIVE_TO_SELF  , .5f);
        expand.setDuration(500);
        expand.setStartOffset(0);
        expand.setFillAfter(true);

        RotateAnimation rotate = new RotateAnimation(0f, 360f,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.5f);
        rotate.setDuration(500);
        rotate.setStartOffset(0);
        // rotate.setInterpolator(new LinearInterpolator());
        rotate.setFillAfter(true);


        AnimationSet Reload = new AnimationSet(true);
        Reload.addAnimation(fade_in);
        Reload.addAnimation(expand);
        Reload.addAnimation(rotate);
        Reload.setInterpolator(new DecelerateInterpolator(1.3f));
        return Reload;
    }

    public static TransitionDrawable CreateTransitionDrawable(int imgResource1,int imgResource2,Context mContext)
    {
        Drawable d[] = new Drawable[2];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            d[0] = mContext.getDrawable(imgResource1);
            d[1] = mContext.getDrawable(imgResource2);
        }
        else
        {
            d[0] = mContext.getResources().getDrawable(imgResource1);
            d[1] = mContext.getResources().getDrawable(imgResource2);
        }
        return new TransitionDrawable(d);
    }

    public static final void ClickAndFocusOnView(View v){
        if(v.getParent() instanceof ViewGroup) {
                v.getParent().requestChildFocus(v,v);
                v.performClick();
        }
        else
        {
            v.performClick();
        }
    }

    public static void SetFontToControls(Typeface font, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            if(child instanceof TextView)
                ((TextView)child).setTypeface(font);
            if (child instanceof ViewGroup) {
                SetFontToControls(font, (ViewGroup) child);
            }
        }
    }

    public static void SetEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                SetEnableControls(enable, (ViewGroup) child);
            }
        }
    }

}
