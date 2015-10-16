package com.example.swsahu.duplicatecardgame.StoryMode;


import com.example.swsahu.duplicatecardgame.Game;
import com.example.swsahu.duplicatecardgame.HelperClass;
import com.example.swsahu.duplicatecardgame.MainActivity;

import java.lang.ref.WeakReference;

public class StartGame {

    MainActivity mContext;
    int CurrentModule;
    int CurrentLevel;
    int CurrentStage;
    int CurrentChallenge;

    public StartGame(WeakReference<MainActivity> m_context)
    {
        mContext = m_context.get();
    }

    public void setStoryModeData(int currentModule,int currentLevel,int currentStage,int currentChallenge)
    {
        CurrentModule=currentModule;
        CurrentLevel=currentLevel;
        CurrentStage=currentStage;
        CurrentChallenge=currentChallenge;
    }


    public void StartGame()
    {
        int CardSet = HelperClass.STORY_MODE_CARD_SET;


        GameValues objGameValues = new GameValues(CurrentModule,CurrentLevel,CurrentStage,CurrentChallenge);
        if(mContext.objCardGame == null)
        mContext.objCardGame = new Game(new WeakReference<>(mContext));
        else
        {
            mContext.objCardGame.Clear();
            mContext.objCardGame.Update_mContext(new WeakReference<>(mContext));
        }
        mContext.objCardGame.GameBackground = objGameValues.getBackGroundIndex();
        mContext.objCardGame.LockingTime = objGameValues.getLockingTime();
        mContext.objCardGame.PlayerOne_Turn = true;
        mContext.objCardGame.StoryMode=true;
        mContext.objCardGame.setGameConfiguration(
                objGameValues.getPlayerMode(),
                objGameValues.getPlayerTwoType(),
                objGameValues.getRobotMemoryLevel(),
                objGameValues.getGameMode(),
                objGameValues.getTimeTrialTimer(),
                objGameValues.getBoardType(),
                objGameValues.getRowSize(),
                objGameValues.getColSize(),
                objGameValues.getScrollType(),
                HelperClass.STORY_MODE_CARD_SET
            );
        mContext.objCardGame.StartGame();
        mContext.CURRENT_SCREEN = HelperClass.SCREEN_GAME;
    }

}
