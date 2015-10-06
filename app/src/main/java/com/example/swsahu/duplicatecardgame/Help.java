package com.example.swsahu.duplicatecardgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

/**
 * Created by swsahu on 10/1/2015.
 */
public class Help {
    View.OnClickListener Objective_Click;
    View.OnClickListener PlayerMode_Click;
    View.OnClickListener BoardDetails_Click;
    View.OnClickListener Power_Click;
    View.OnClickListener GameMode_Click;
    View.OnClickListener BackButton_Click;


    MainActivity mContext;

    public Help(WeakReference<MainActivity> m_context)
    {
        mContext = m_context.get();
        InitializeListeners();
    }


    public void Show()
    {
        mContext.loadView(R.layout.screen_help);
        addListenerToControls();
    }

    private void addListenerToControls()
    {
        Button btnObjective = (Button)mContext.findViewById(R.id.btnObjective);
        Button btnPlayerMode = (Button)mContext.findViewById(R.id.btnPlayerMode);
        Button btnBoardDetails = (Button)mContext.findViewById(R.id.btnBoardDetails);
        Button btnPower = (Button)mContext.findViewById(R.id.btnPower);
        Button btnGameMode = (Button)mContext.findViewById(R.id.btnGameMode);
        Button btnBack = (Button)mContext.findViewById(R.id.btnBack);

        btnObjective.setOnClickListener(Objective_Click);
        btnPlayerMode.setOnClickListener(PlayerMode_Click);
        btnBoardDetails.setOnClickListener(BoardDetails_Click);
        btnPower.setOnClickListener(Power_Click);
        btnGameMode.setOnClickListener(GameMode_Click);
        btnBack.setOnClickListener(BackButton_Click);

    }

    private void InitializeListeners()
    {
        Objective_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectiveClick();
            }
        };

        PlayerMode_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerModeClick();
            }
        };

        BoardDetails_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardDetailsClick();
            }
        };
        GameMode_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameModeClick();
            }
        };

        Power_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PowerClick();
            }
        };

        BackButton_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.onBackPress();
            }
        };

    }

    private void ObjectiveClick()
    {
        deSelectedAllButtons();
        Button btnObjective = (Button)mContext.findViewById(R.id.btnObjective);
        btnObjective.setBackgroundResource(R.drawable.btn_white_reverse);

        RelativeLayout frame = (RelativeLayout)mContext.findViewById(R.id.frame);
        frame.removeAllViews();
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.view_game_objective, frame, true);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
    }

    private void PlayerModeClick()
    {
        deSelectedAllButtons();
        Button btnPlayerMode = (Button)mContext.findViewById(R.id.btnPlayerMode);
        btnPlayerMode.setBackgroundResource(R.drawable.btn_white_reverse);

        RelativeLayout frame = (RelativeLayout)mContext.findViewById(R.id.frame);
        frame.removeAllViews();
//        LayoutInflater inflater = mContext.getLayoutInflater();
//        View view = inflater.inflate(R.layout.view_buy_powers, frame, true);
//        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
    }
    private void BoardDetailsClick()
    {
        deSelectedAllButtons();
        Button btnBoardDetails = (Button)mContext.findViewById(R.id.btnBoardDetails);
        btnBoardDetails.setBackgroundResource(R.drawable.btn_white_reverse);

        RelativeLayout frame = (RelativeLayout)mContext.findViewById(R.id.frame);
        frame.removeAllViews();
        LayoutInflater inflater = mContext.getLayoutInflater();
//        View view = inflater.inflate(R.layout.view_buy_powers, frame, true);
//        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
    }
    private void GameModeClick()
    {
        deSelectedAllButtons();
        Button btnGameMode = (Button)mContext.findViewById(R.id.btnGameMode);
        btnGameMode.setBackgroundResource(R.drawable.btn_white_reverse);

        RelativeLayout frame = (RelativeLayout)mContext.findViewById(R.id.frame);
        frame.removeAllViews();
        LayoutInflater inflater = mContext.getLayoutInflater();
//        View view = inflater.inflate(R.layout.view_buy_powers, frame, true);
//        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
    }
    private void PowerClick()
    {
        deSelectedAllButtons();
        Button btnPow = (Button)mContext.findViewById(R.id.btnPower);
        btnPow.setBackgroundResource(R.drawable.btn_white_reverse);

        RelativeLayout frame = (RelativeLayout)mContext.findViewById(R.id.frame);
        frame.removeAllViews();
        LayoutInflater inflater = mContext.getLayoutInflater();
//        View view = inflater.inflate(R.layout.view_buy_powers, frame, true);
//        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
    }

    private void deSelectedAllButtons()
    {
        Button btnObjective = (Button)mContext.findViewById(R.id.btnObjective);
        Button btnPlayerMode = (Button)mContext.findViewById(R.id.btnPlayerMode);
        Button btnBoardDetails = (Button)mContext.findViewById(R.id.btnBoardDetails);
        Button btnPower = (Button)mContext.findViewById(R.id.btnPower);
        Button btnGameMode = (Button)mContext.findViewById(R.id.btnGameMode);

        Button allButtons[] = { btnObjective,btnPlayerMode,btnBoardDetails,btnPower,btnGameMode};

        for(int i=0;i<allButtons.length;i++)
        {
            allButtons[i].setBackgroundResource(R.drawable.btn_white_transparency_20);
        }
    }


}
