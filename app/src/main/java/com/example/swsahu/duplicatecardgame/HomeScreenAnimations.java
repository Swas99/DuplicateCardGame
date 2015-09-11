package com.example.swsahu.duplicatecardgame;


import android.graphics.Point;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import java.lang.ref.WeakReference;

public class HomeScreenAnimations {

    public void StartAnimation(WeakReference<MainActivity> m_context)
    {
        MainActivity mContext = m_context.get();

        View leftV1 = mContext.findViewById(R.id.btnQuickGame);
        View leftV2 = mContext.findViewById(R.id.btnArcade);
        View rightV1 = mContext.findViewById(R.id.btnStoryMode);
        View rightV2 = mContext.findViewById(R.id.btnTimeTrial);

        View twoCards = mContext.findViewById(R.id.btnGameLogo);

        View [] allViews = { leftV1,leftV2, rightV1, rightV2, twoCards};
        ConfigureOutOfParentAnimation(allViews);

        Point screenSize = HelperClass.getWindowSize(mContext.getWindowManager().getDefaultDisplay());
        int screenWidth = screenSize.x;
        int screenHeight = screenSize.y;


        AnimationSet slideRight = Slide((int)(screenWidth/3.3),0);
        AnimationSet slideLeft = Slide((int)(-screenWidth/3.3),0);

        leftV1.startAnimation(slideRight);
        leftV2.startAnimation(slideRight);
        rightV1.startAnimation(slideLeft);
        rightV2.startAnimation(slideLeft);


        AnimationSet ZoomIn = ZoomIn();
        twoCards.startAnimation(ZoomIn);

    }

    private void ConfigureOutOfParentAnimation(View[] v_arr)
    {
        for (View v : v_arr)
        {
            HelperClass.ConfigureOutOfParentAnimation(v,true);
        }
    }

    public AnimationSet Slide(int deltaX,int deltaY)
    {

        TranslateAnimation shake_1 = new TranslateAnimation(-deltaX, 0, 0, deltaY);
        shake_1.setDuration(800);
        shake_1.setStartOffset(400);
        shake_1.setFillAfter(true);

        AnimationSet ShakeIt = new AnimationSet(true);
        ShakeIt.addAnimation(shake_1);
        ShakeIt.setInterpolator(new OvershootInterpolator());

        return ShakeIt;
    }

    public AnimationSet ZoomIn()
    {
        AlphaAnimation fade_in = new AlphaAnimation(.7f,1f);
        fade_in.setDuration(2200);
        fade_in.setStartOffset(400);
        fade_in.setFillAfter(true);

        ScaleAnimation zoom = new ScaleAnimation(.8f,1f,.8f,1f,
                Animation.RELATIVE_TO_SELF,.5f,
                Animation.RELATIVE_TO_SELF, .5f);
        zoom.setDuration(2200);
        zoom.setStartOffset(400);
        zoom.setFillAfter(true);

        AnimationSet ZoomIn = new AnimationSet(true);
        ZoomIn.addAnimation(fade_in);
        ZoomIn.addAnimation(zoom);
        ZoomIn.setInterpolator(new OvershootInterpolator());

        return ZoomIn;
    }


}
