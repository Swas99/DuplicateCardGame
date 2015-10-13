package com.example.swsahu.duplicatecardgame.StoryMode;


import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.swsahu.duplicatecardgame.HelperClass;
import com.example.swsahu.duplicatecardgame.MainActivity;
import com.example.swsahu.duplicatecardgame.R;

import java.lang.ref.WeakReference;

import static com.example.swsahu.duplicatecardgame.HelperClass.getWindowSize;

public class ScreenCreation {

    MainActivity mContext;
    View temp1;
    View temp2;

    public ScreenCreation(WeakReference<MainActivity> m_context)
    {
        mContext = m_context.get();
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.screen_story_mode, null, false);
        ScrollView mainContainer = (ScrollView)view.findViewById(R.id.mainContainer);
        mainContainer.removeAllViews();

        LinearLayout linear_layout = new LinearLayout(mContext);
        linear_layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        linear_layout.setLayoutParams(layoutParams);

        mainContainer.addView(linear_layout);

        linear_layout.addView(getStage());
        linear_layout.addView(getDivider(HelperClass.ConvertToPx(mContext, 2), Color.BLACK));
        linear_layout.addView(getDivider(HelperClass.ConvertToPx(mContext, 1), Color.argb(60, 255, 255, 255)));
        linear_layout.addView(getStage());
        linear_layout.addView(getDivider(HelperClass.ConvertToPx(mContext, 2), Color.BLACK));
        linear_layout.addView(getDivider(HelperClass.ConvertToPx(mContext, 1), Color.argb(60, 255, 255, 255)));
        linear_layout.addView(getStage());
        linear_layout.addView(getDivider(HelperClass.ConvertToPx(mContext, 2), Color.BLACK));
        linear_layout.addView(getDivider(HelperClass.ConvertToPx(mContext, 1), Color.argb(60, 255, 255, 255)));
        linear_layout.addView(getStage());



        temp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp2.getVisibility()==View.VISIBLE)
                {
                    temp1.setBackgroundResource(R.drawable.btn_expand);
                    temp2.setVisibility(View.GONE);
                }
                else
                {
                    temp1.setBackgroundResource(R.drawable.btn_collapse);
                    temp2.setVisibility(View.VISIBLE);
                    temp2.getParent().requestChildFocus(temp2, temp2);
                }
            }
        });



        if (mContext.CurrentView != null)
            mContext.CurrentView.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));

        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
        mContext.setContentView(view);
        mContext.CurrentView = view;
        mContext.CURRENT_SCREEN = R.layout.screen_story_mode;
    }



    private View getStage()
    {
        LinearLayout linear_Layout = new LinearLayout(mContext);
        linear_Layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linear_Layout.setLayoutParams(layoutParams);

        linear_Layout.addView(getTitleBar("Love Affair:"));
        linear_Layout.addView(getDivider(1, Color.BLACK));
        linear_Layout.addView(getLevels());

        return linear_Layout;
    }

    private View getTitleBar(String header)
    {
        LinearLayout linear_layout = new LinearLayout(mContext);
        linear_layout.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout.setBackgroundColor(Color.argb(17, 250, 250, 250));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linear_layout.setLayoutParams(layoutParams);

        LinearLayout.LayoutParams layout_params1 = new LinearLayout.LayoutParams(0,
                HelperClass.ConvertToPx(mContext,36),1);
        TextView tv = new TextView(mContext);
        tv.setPadding(HelperClass.ConvertToPx(mContext, 9), 0, 0, 0);
        tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tv.setText(header);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        tv.setLayoutParams(layout_params1);
        linear_layout.addView(tv);


        RelativeLayout cell = new RelativeLayout(mContext);
        LinearLayout.LayoutParams layout_params2 = new LinearLayout.LayoutParams(
                HelperClass.ConvertToPx(mContext,36), HelperClass.ConvertToPx(mContext,36));
        layout_params2.gravity = Gravity.CENTER;
        cell.setLayoutParams(layout_params2);
        int five_dp = HelperClass.ConvertToPx(mContext, 5);
        int two_dp = HelperClass.ConvertToPx(mContext, 2);
        cell.setPadding(five_dp, five_dp, five_dp, five_dp);
        TextView btn_expand = new TextView(mContext);
        RelativeLayout.LayoutParams layoutParams_r = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams_r.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        btn_expand.setLayoutParams(layoutParams_r);
        btn_expand.setGravity(Gravity.CENTER);
        btn_expand.setBackgroundResource(R.drawable.btn_expand);
        btn_expand.setClickable(true);
        btn_expand.setLayoutParams(layoutParams_r);
        temp1 = btn_expand;
        cell.addView(btn_expand);

        linear_layout.addView(cell);
        return linear_layout;
    }

    private View getLevels()
    {
        LinearLayout linear_layout_1 = new LinearLayout(mContext);
        temp2 = linear_layout_1;
        linear_layout_1.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linear_layout_1.setLayoutParams(layoutParams1);

        for(int i=0;i<5;i++)
        {
            LinearLayout linear_layout_2 = new LinearLayout(mContext);
            linear_layout_2.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            linear_layout_2.setLayoutParams(layoutParams2);
            int five_dp = HelperClass.ConvertToPx(mContext,5);
            linear_layout_2.setPadding(five_dp, five_dp, five_dp, five_dp);
//            linear_layout_2.setBackgroundColor(colors[i % 2]);

            linear_layout_1.addView(linear_layout_2);
//            linear_layout_1.addView(getDivider(1,Color.BLACK));

            for (int j=0;j<5;j++)
            {
                linear_layout_2.addView(getCell(j));
            }
        }
        return linear_layout_1;
    }

    private RelativeLayout getCell(int value)
    {
        Point windowSize = getWindowSize(mContext.getWindowManager().getDefaultDisplay());
        int screen_width = windowSize.x - HelperClass.ConvertToPx(mContext,150);

        RelativeLayout cell = new RelativeLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT,1f );
        cell.setLayoutParams(layoutParams);
        int five_dp = HelperClass.ConvertToPx(mContext, 5);
        int two_dp = HelperClass.ConvertToPx(mContext, 2);
        cell.setPadding(two_dp, two_dp, two_dp, two_dp);

        TextView tv = new TextView(mContext);
        RelativeLayout.LayoutParams layoutParams_r = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams_r.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/hurry up.ttf");
        tv.setLayoutParams(layoutParams_r);
//        tv.setTypeface(Typeface.create(font, Typeface.BOLD));
        tv.setLayoutParams(layoutParams_r);
        tv.setTextSize(screen_width / 12);
        tv.setText(String.valueOf(10 + value));
        tv.setGravity(Gravity.CENTER);
        tv.setHeight(screen_width / 5);
        tv.setWidth(screen_width / 5);
        tv.setTextColor(Color.BLACK);
        tv.setClickable(true);
        tv.setBackgroundResource(R.drawable.btn_level);

        cell.addView(tv);

        return cell;

    }

    protected View getDivider(int height,int color)
    {
        RelativeLayout v = new RelativeLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height);
        v.setLayoutParams(layoutParams);
        v.setBackgroundColor(color);
        return v;
    }



}
