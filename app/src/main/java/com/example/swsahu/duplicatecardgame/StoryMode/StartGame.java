package com.example.swsahu.duplicatecardgame.StoryMode;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.swsahu.duplicatecardgame.Game;
import com.example.swsahu.duplicatecardgame.HelperClass;
import com.example.swsahu.duplicatecardgame.MainActivity;
import com.example.swsahu.duplicatecardgame.R;

import java.lang.ref.WeakReference;

import static com.example.swsahu.duplicatecardgame.HelperClass.ARCADE;
import static com.example.swsahu.duplicatecardgame.HelperClass.BOTH;
import static com.example.swsahu.duplicatecardgame.HelperClass.ConvertToPx;
import static com.example.swsahu.duplicatecardgame.HelperClass.HORIZONTAL;
import static com.example.swsahu.duplicatecardgame.HelperClass.NO_SCROLL;
import static com.example.swsahu.duplicatecardgame.HelperClass.ONE_BOARD;
import static com.example.swsahu.duplicatecardgame.HelperClass.STORY_MODE_SCORES;
import static com.example.swsahu.duplicatecardgame.HelperClass.STORY_MODE_STARS;
import static com.example.swsahu.duplicatecardgame.HelperClass.VERTICAL;

public class StartGame implements View.OnClickListener {

    MainActivity mContext;
    ScreenCreation objScreenCreation;

    int CurrentModule;
    int CurrentLevel;
    int CurrentStage;
    int CurrentChallenge;
    int BoardSize;

    int ScrollType;
    int BoardType;
    int GameMode;
    int PlayerMode;
    int PlayerTwoType;
    int RobotMemoryLevel;
    int TTT_Value;
    int RowSize;
    int ColSize;
    int GameBackGroundIndex;
    int LockingTime;

    Dialog DialogWindow;
    View ContentView;


    public StartGame(WeakReference<MainActivity> m_context,WeakReference<ScreenCreation> obj_screenCreation,
                     Dialog dialogWindow)
    {
        mContext = m_context.get();
        objScreenCreation = obj_screenCreation.get();
        DialogWindow = dialogWindow;
    }
    public StartGame(WeakReference<MainActivity> m_context,
                     Dialog dialogWindow)
    {
        mContext = m_context.get();
        DialogWindow = dialogWindow;
    }
    public void setStoryModeData(int currentModule,int currentLevel,int currentStage,int currentChallenge)
    {
        CurrentModule=currentModule;
        CurrentLevel=currentLevel;
        CurrentStage=currentStage;
        CurrentChallenge=currentChallenge;
        configureGame();
    }
    private void configureGame()
    {
        GameValues objGameValues = new GameValues(CurrentModule,CurrentLevel,CurrentStage,CurrentChallenge);
        ScrollType = objGameValues.getScrollType();
        BoardType = objGameValues.getBoardType();
        GameMode = objGameValues.getGameMode();
        PlayerMode = objGameValues.getPlayerMode();
        PlayerTwoType = objGameValues.getPlayerTwoType();
        RobotMemoryLevel = objGameValues.getRobotMemoryLevel();
        TTT_Value = objGameValues.getTimeTrialTimer();
        RowSize = objGameValues.getRowSize();
        ColSize = objGameValues.getColSize();
        GameBackGroundIndex = objGameValues.getBackGroundIndex();
        LockingTime = objGameValues.getLockingTime();

        BoardSize=objGameValues.getBoardSize();
    }

    public void showObjective()
    {
        LayoutInflater inflater = mContext.getLayoutInflater();
        ContentView = inflater.inflate(R.layout.dialog_story_mode_start_game, null);
        ContentView.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogWindow.dismiss();
            }
        });
        DialogWindow.setContentView(ContentView);

        TextView tvGameNumber = (TextView)ContentView.findViewById(R.id.tvGameNumber);
        TextView tvObjective = (TextView)ContentView.findViewById(R.id.tvObjective);
        TextView tvScore = (TextView)ContentView.findViewById(R.id.tvScore);
        TextView btnGameMode = (TextView)ContentView.findViewById(R.id.btnGameMode);
        TextView btnBoardType = (TextView)ContentView.findViewById(R.id.btnBoardType);
        TextView btnScrollType = (TextView)ContentView.findViewById(R.id.btnScrollType);
        TextView btnStart = (TextView)ContentView.findViewById(R.id.btnStart);


        long topScore = getScore();
        tvGameNumber.setText("#Game "+String.valueOf(getGameNumber()));
        tvObjective.setText(getObjectiveInfo());
        tvScore.setText(String.valueOf(topScore));
        btnGameMode.setText(mContext.get_text(GameMode));
        btnBoardType.setText(mContext.get_text(BoardType));
        String scrollType = mContext.get_text(ScrollType);
        if(!scrollType.equals("No Scroll"))
            scrollType+= " Scroll";
        btnScrollType.setText(scrollType);
        btnGameMode.setOnClickListener(this);
        btnBoardType.setOnClickListener(this);
        btnScrollType.setOnClickListener(this);
        btnStart.setOnClickListener(this);

        if(topScore>0)
        {
         createStarts(ContentView.findViewById(R.id.region_stars));
        }
    }
    private void createStarts(View regionStars)
    {
        int totalMiniStars = readStars();
        if(totalMiniStars==0)
            return;

        View tvShare = ContentView.findViewById(R.id.tvShare);
        tvShare.setVisibility(View.VISIBLE);
        AnimationSet zoomInOut = ZoomInOut();
        tvShare.startAnimation(zoomInOut);
        tvShare.setOnClickListener(this);

        regionStars.setVisibility(View.VISIBLE);
        while (totalMiniStars>2)
        {
            ((LinearLayout)regionStars).addView(getStar(R.drawable.img_star_full));
            totalMiniStars-=3;
        }
        if(totalMiniStars==2)
            ((LinearLayout)regionStars).addView(getStar(R.drawable.img_star_two_third));
        else if (totalMiniStars==1)
            ((LinearLayout)regionStars).addView(getStar(R.drawable.img_star_one_third));
    }
    private View getStar(int drawableID)
    {
        RelativeLayout rl = new RelativeLayout(mContext);
        LinearLayout.LayoutParams l_params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1f);
        rl.setLayoutParams(l_params);

        TextView tv = new TextView(mContext);
        RelativeLayout.LayoutParams r_params = new RelativeLayout.LayoutParams(
                HelperClass.ConvertToPx(mContext,40),
                HelperClass.ConvertToPx(mContext,40)
        );
        r_params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        tv.setLayoutParams(r_params);
        tv.setBackgroundResource(drawableID);
        rl.addView(tv);

        return rl;
    }

    private int getGameNumber()
    {
        int ModuleLevelCount[] = {18,8,4,8,15,17,11};
        int maxStages=6;
        int maxChallenges=5;
        int maxGamesInOneLevel = maxStages*maxChallenges;

        int gameNumber=0;
        for(int i=0;i<CurrentModule;i++)
        {
            gameNumber+=ModuleLevelCount[i]*maxGamesInOneLevel;
        }
        gameNumber+= CurrentLevel*maxGamesInOneLevel;
        gameNumber+= CurrentStage*maxChallenges;
        gameNumber+= CurrentChallenge+1;
        return gameNumber;
    }
    private String getObjectiveInfo()
    {
        String objective="Defeat ";

        switch (CurrentChallenge)
        {
            case 0:
                objective = "Complete within " + getOnePlayerMovesLimit() + " moves";
                break;
            case 1:
                objective = "Complete within " + getOnePlayerMovesLimit() + " moves";
                break;
            case 2:
                objective+= "HURRICANE";
                break;
            case 3:
                objective+= "ROCK";
                break;
            case 4:
                objective+= "ANDROBOT";
                break;
        }

        return objective;
    }
    private String getOnePlayerMovesLimit() {
        int ModuleLevelCount[] = {18,8,4,8,15,17,11};
        int maxStages = 6;
        int numberOfLevels=ModuleLevelCount[CurrentModule];
        int card_pairs = BoardSize/2;
        int minMoves= (int)Math.round(card_pairs*1.53);
        int maxMoves= (int)Math.round(card_pairs*3.06);
        float factor = (float)(maxMoves-minMoves)/(numberOfLevels+maxStages-2);
        int moves_limit = maxMoves - Math.round(factor * (CurrentLevel + CurrentStage));
        if(CurrentChallenge==0)
            moves_limit+=card_pairs;
        return String.valueOf(moves_limit);
    }
    private long getScore()
    {
        SharedPreferences prefs = mContext.getSharedPreferences(String.valueOf(STORY_MODE_SCORES),
                mContext.MODE_PRIVATE);
        return prefs.getLong(String.valueOf(getGameNumber()), 0l);
    }

    private void loadContentToDialog(int layoutResId)
    {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(layoutResId, null);
        DialogWindow.setContentView(view);
        DialogWindow.setCancelable(false);
        View.OnClickListener go_back = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogWindow.setContentView(ContentView);
                DialogWindow.setCancelable(true);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = DialogWindow.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.height =  ConvertToPx(mContext, 390);
                window.setAttributes(lp);
            }
        };
        view.findViewById(R.id.btnClose).setOnClickListener(go_back);
        view.findViewById(R.id.btnOk).setOnClickListener(go_back);
    }

    private void Start_Game()
    {
        if(mContext.objCardGame == null)
            mContext.objCardGame = new Game(new WeakReference<>(mContext));
        else
        {
            mContext.objCardGame.Clear();
            mContext.objCardGame.Update_mContext(new WeakReference<>(mContext));
        }
        mContext.objCardGame.StoryMode=true;
        mContext.objCardGame.CurrentModule=CurrentModule;
        mContext.objCardGame.CurrentLevel=CurrentLevel;
        mContext.objCardGame.CurrentStage=CurrentStage;
        mContext.objCardGame.CurrentChallenge=CurrentChallenge;

        mContext.objCardGame.GameBackground = GameBackGroundIndex;
        mContext.objCardGame.LockingTime = LockingTime;
        mContext.objCardGame.PlayerOne_Turn = true;
        mContext.objCardGame.setGameConfiguration(
                PlayerMode,
                PlayerTwoType,
                RobotMemoryLevel,
                GameMode,
                TTT_Value,
                BoardType,
                RowSize,
                ColSize,
                ScrollType,
                HelperClass.STORY_MODE_CARD_SET
        );

        mContext.objCardGame.StartGame();
        mContext.CURRENT_SCREEN = HelperClass.SCREEN_GAME;

        DialogWindow.dismiss();
    }

    private int readStars()
    {
        SharedPreferences pref = mContext.getSharedPreferences(String.valueOf(STORY_MODE_STARS),
                Context.MODE_PRIVATE);
        return pref.getInt(String.valueOf(getGameNumber()), 0);
    }

    public AnimationSet ZoomInOut()
    {
        ScaleAnimation zoom = new ScaleAnimation(1.05f,.96f,1.05f,.96f,
                Animation.RELATIVE_TO_SELF,.5f,
                Animation.RELATIVE_TO_SELF,.5f);
        zoom.setDuration(800);
        zoom.setStartOffset(200);
        zoom.setFillAfter(true);
        zoom.setRepeatCount(Animation.INFINITE);
        zoom.setInterpolator(new OvershootInterpolator(1f));

        AnimationSet ZoomIn = new AnimationSet(true);
        ZoomIn.addAnimation(zoom);
        return ZoomIn;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnGameMode:
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = DialogWindow.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                if(GameMode == ARCADE)
                    loadContentToDialog(R.layout.dialog_story_mode_arcade);
                else
                    loadContentToDialog(R.layout.dialog_story_mode_time_trial);
                break;
            case R.id.btnBoardType:
                if(BoardType == ONE_BOARD)
                    loadContentToDialog(R.layout.dialog_help_1board);
                else
                    loadContentToDialog(R.layout.dialog_help_2board);
                break;
            case R.id.btnScrollType:
                switch (ScrollType)
                {
                    case NO_SCROLL:
                        loadContentToDialog(R.layout.dialog_help_no_scroll);
                        break;
                    case VERTICAL:
                        loadContentToDialog(R.layout.dialog_help_vertical_scroll);
                        break;
                    case HORIZONTAL:
                        loadContentToDialog(R.layout.dialog_help_horizontal_scroll);
                        break;
                    case BOTH:
                        loadContentToDialog(R.layout.dialog_help_both_scroll);
                        break;
                }
                break;
            case R.id.tvShare:
                break;
            case R.id.btnStart:
                Start_Game();
                DialogWindow.dismiss();
                CleanUp();
                break;
        }
    }

    private void CleanUp()
    {
        Runnable myRunnable = new Runnable(){
            public void run(){
                if(objScreenCreation!=null)
                {
                    objScreenCreation.cleanUp();
                    objScreenCreation=null;
                }
                mContext=null;
                DialogWindow=null;
                ContentView=null;
                System.gc();
            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();
    }

}
