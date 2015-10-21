package com.example.swsahu.duplicatecardgame.StoryMode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.swsahu.duplicatecardgame.HelperClass;
import com.example.swsahu.duplicatecardgame.MainActivity;
import com.example.swsahu.duplicatecardgame.R;

import java.lang.ref.WeakReference;

import static com.example.swsahu.duplicatecardgame.HelperClass.CURRENT_GAME_ID;
import static com.example.swsahu.duplicatecardgame.HelperClass.ConvertToPx;
import static com.example.swsahu.duplicatecardgame.HelperClass.STORY_MODE_CARD_SET;
import static com.example.swsahu.duplicatecardgame.HelperClass.STORY_MODE_DATA;
import static com.example.swsahu.duplicatecardgame.HelperClass.STORY_MODE_SCORES;
import static com.example.swsahu.duplicatecardgame.HelperClass.STORY_MODE_STARS;
import static com.example.swsahu.duplicatecardgame.HelperClass.TWO_BOARD;


public class PostGame implements View.OnClickListener {

    MainActivity mContext;
    final int STATUS_COMPLETED = 102;
    final int STATUS_IN_PROGRESS = 104;
    final int STATUS_NEW = 103;
    final int STATUS_LOCKED = 101;

    Dialog DialogWindow;
    int M,L,S,C;//Module-Level-Stage-Challenge

    public PostGame(WeakReference<MainActivity> m_context)
    {
        mContext = m_context.get();
        M=mContext.objCardGame.CurrentModule;
        L=mContext.objCardGame.CurrentLevel;
        S=mContext.objCardGame.CurrentStage;
        C=mContext.objCardGame.CurrentChallenge;
    }

    private void updateGameStatus(boolean result)
    {
        int ModuleLevelCount[] = {18,8,4,8,15,17,11};
        SharedPreferences.Editor editor = mContext.getSharedPreferences(String.valueOf(STORY_MODE_DATA),
                Context.MODE_PRIVATE).edit();
        SharedPreferences prefs = mContext.getSharedPreferences(String.valueOf(STORY_MODE_DATA),
                Context.MODE_PRIVATE);

        String id = String.valueOf(M)+ "_" +
                String.valueOf(L) + "_" +
                String.valueOf(S) + "_" +
                String.valueOf(C);
        if(result)
        {
            if(prefs.getInt(id,STATUS_NEW)!=STATUS_COMPLETED)
            {
                editor.putInt(id, STATUS_COMPLETED);

                //region check_and_unlock_next_level
                int countOfCompleteGamesForCurrentLevel=0;
                String prefix = String.valueOf(M)+"_"+String.valueOf(L)+"_";
                String temp_id;
                for(int _S=0;_S<6;_S++)
                {
                    for (int _C=0;_C<5;_C++)
                    {
                        temp_id = prefix +String.valueOf(_S)+"_"+String.valueOf(_C);
                        if(prefs.getInt(temp_id,-1) == STATUS_COMPLETED)
                            countOfCompleteGamesForCurrentLevel++;
                    }
                }
                if(countOfCompleteGamesForCurrentLevel>24)//24 = 80% of maxGamesInOneLevel(30)
                {
                    if(ModuleLevelCount[M]>L+1)
                    {
                        temp_id = String.valueOf(M)+"_"+String.valueOf(L+1)+"_0_0";
                        if(prefs.getInt(temp_id,STATUS_LOCKED)==STATUS_LOCKED)
                            editor.putInt(temp_id, STATUS_NEW);
                    }
                }
                //endregion

                //region update game_number
                int p,q,r,s;//next_id
                p=M; q=L; r=S; s=C;
                boolean moduleComplete=false;
                s++;
                if(s>4) //4 is max index of challenges
                {
                    s=0;
                    r++;
                    if(r>5) //5 is max index of stages
                    {
                        r=0;
                        q++;
                        if(q>=ModuleLevelCount[p])//check for max index of level in current module
                        {
                            q=0;
                            p++;
                            if(p>=ModuleLevelCount.length)
                            {
                                moduleComplete=true;
                                p=0;
                            }
                        }
                    }
                }
                String next_id = String.valueOf(p)+ "_" +
                        String.valueOf(q) + "_" +
                        String.valueOf(r) + "_" +
                        String.valueOf(s);

                if(!moduleComplete && prefs.getInt(next_id,STATUS_LOCKED)==STATUS_LOCKED)
                {
                    editor.putInt(next_id, STATUS_NEW);
                }

                String currentGameIndex = prefs.getString(String.valueOf(CURRENT_GAME_ID),"0_0_0_0");
                if(currentGameIndex.equals(id))
                {
                    editor.putString(String.valueOf(CURRENT_GAME_ID), next_id);
                }
                //endregion
            }
        }
        else if(prefs.getInt(id,STATUS_LOCKED)!=STATUS_COMPLETED)
            editor.putInt(id, STATUS_IN_PROGRESS);

        editor.apply();
    }

    private void updateScore(long currentScore)
    {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(String.valueOf(STORY_MODE_SCORES),
            Context.MODE_PRIVATE).edit();
        editor.putLong(String.valueOf(getGameNumber()),currentScore);
        editor.apply();
    }
    private long getHighScore()
    {
        SharedPreferences prefs = mContext.getSharedPreferences(String.valueOf(STORY_MODE_SCORES),
                Context.MODE_PRIVATE);
        return prefs.getLong(String.valueOf(getGameNumber()), 0l);
    }
    private int getGameNumber()
    {
        int ModuleLevelCount[] = {18,8,4,8,15,17,11};
        int maxStages=6;
        int maxChallenges=5;
        int maxGamesInOneLevel = maxStages*maxChallenges;

        int gameNumber=0;
        for(int i=0;i<M;i++)
        {
            gameNumber+=ModuleLevelCount[i]*maxGamesInOneLevel;
        }
        gameNumber+= L*maxGamesInOneLevel;
        gameNumber+= S*maxChallenges;
        gameNumber+= C+1;
        return gameNumber;
    }

    private int getOnePlayerMovesLimit() {
        int ModuleLevelCount[] = {18,8,4,8,15,17,11};
        int maxStages = 6;
        int numberOfLevels=ModuleLevelCount[M];
        int card_pairs = mContext.objCardGame.RowSize*mContext.objCardGame.ColumnSize/2;
        int minMoves= (int)Math.round(card_pairs*1.6);
        int maxMoves= (int)Math.round(card_pairs*3.06);
        float factor = (float)(maxMoves-minMoves)/(numberOfLevels+maxStages-2);
        int moves_limit = maxMoves - Math.round(factor * (L + S));
        if(C==0)
            moves_limit+=card_pairs;
        return moves_limit;
    }

    private boolean getResult()
    {
        if(C<2)
        {
            if(mContext.objCardGame.Player1_Moves<=getOnePlayerMovesLimit())
                return true;
        }
        else
        {
            if(mContext.objCardGame.PlayerOne_Score>mContext.objCardGame.PlayerTwo_Score)
                return true;
        }
        return false;
    }


    public void ShowLevelCompletedDialog()
    {
        boolean result = getResult();
        updateGameStatus(result);
        long currentScore = mContext.objCardGame.objGameSummary.Score;
        long topScore = getHighScore();
        if(currentScore>topScore)
        {
            updateScore(currentScore);
            topScore=currentScore;
        }

        LayoutInflater inflater = mContext.getLayoutInflater();
        View view;
        if(result)
        {
            view = inflater.inflate(R.layout.dialog_story_mode_summary_yay, null, true);

            View region_stars = view.findViewById(R.id.region_stars);
            String challengeText;
            if(C<2)
            {
                challengeText =String.valueOf(getOnePlayerMovesLimit());
                computeStarsFor1PlayerGame(region_stars);
                ((TextView) view.findViewById(R.id.tvChallengeLimit)).setText(challengeText);
            }
            else
            {
                computeStarsFor2PlayerGame(region_stars);
                view.findViewById(R.id.region_moves_limit).setVisibility(View.GONE);
                view.findViewById(R.id.region_moves_made).setVisibility(View.GONE);
                view.findViewById(R.id.region_two_player_result).setVisibility(View.VISIBLE);
            }

            ((TextView)view.findViewById(R.id.tvMoves)).setText(String.valueOf(mContext.objCardGame.Player1_Moves));
            ((TextView)view.findViewById(R.id.tvScore)).setText(String.valueOf(currentScore));
            ((TextView)view.findViewById(R.id.tvTopScore)).setText(String.valueOf(topScore));
            view.findViewById(R.id.btnGameSummary).setOnClickListener(this);
            TextView tvNext = (TextView)view.findViewById(R.id.btnNext);
            tvNext.setOnClickListener(this);
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/hurry up.ttf");
            tvNext.setTypeface(font);
            view.findViewById(R.id.btnRestart).setOnClickListener(this);
        }
        else
        {
            view = inflater.inflate(R.layout.dialog_story_mode_summary_oh_no, null, true);
            view.findViewById(R.id.btnGameSummary).setOnClickListener(this);
            view.findViewById(R.id.btnNext).setOnClickListener(this);
            view.findViewById(R.id.btnRestart).setOnClickListener(this);

        }


        DialogWindow = new AlertDialog.Builder(mContext).show();
        DialogWindow.setCancelable(false);
        DialogWindow.setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = DialogWindow.getWindow();
        lp.copyFrom(window.getAttributes());
        View v2 =  mContext.CurrentView;
        lp.width = v2.getMeasuredWidth() - ConvertToPx(mContext, 40); //WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height =  ConvertToPx(mContext, 360);
        window.setAttributes(lp);
    }

    private void computeStarsFor1PlayerGame(View regionStars)
    {
        int moves = mContext.objCardGame.Player1_Moves;
        int maxLimit = getOnePlayerMovesLimit();
        int minLimit = mContext.objCardGame.RowSize*mContext.objCardGame.ColumnSize;
        minLimit = (minLimit/2) + (minLimit/4);
        int totalMiniStars = Math.round((float) 11 * (maxLimit - moves) / (maxLimit - minLimit) + 1);
        totalMiniStars+=3;
        createStarts(totalMiniStars,regionStars);
    }
    private void computeStarsFor2PlayerGame(View regionStars)
    {
        int maxHits = mContext.objCardGame.RowSize*mContext.objCardGame.ColumnSize/2;
        int minHits = (maxHits+1)/2;
        int playerOneHits = mContext.objCardGame.PlayerOne_Score;
        int totalMiniStars = Math.round((float) 7 * (playerOneHits - minHits) / (maxHits - minHits) + 1);
        totalMiniStars+=7;
        createStarts(totalMiniStars,regionStars);
    }
    private void createStarts(int totalMiniStars,View regionStars)
    {
        if(totalMiniStars>15)
            totalMiniStars=15;
        writeStars(totalMiniStars);
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
    private void writeStars(int totalMiniStars)
    {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(String.valueOf(STORY_MODE_STARS),
                Context.MODE_PRIVATE).edit();
        editor.putInt(String.valueOf(getGameNumber()), totalMiniStars);
        editor.apply();
    }


    private void NextGame()
    {
        //region create next_game values
        int ModuleLevelCount[] = {18,8,4,8,15,17,11};
        int nxt_m = M;
        int nxt_l = L;
        int nxt_s = S;
        int nxt_c = C+1;

        if (nxt_c>4)
        {
            nxt_c=0;
            nxt_s++;
            if(nxt_s>5)
            {
                nxt_s=0;
                nxt_l++;
                if(nxt_l>=ModuleLevelCount[nxt_m])
                {
                    nxt_l=0;
                    nxt_m++;
                    if(nxt_m>=ModuleLevelCount.length)
                    {
                        nxt_m=0;
                    }
                }
            }
        }
        //endregion

        StartGame objStartGame = new StartGame(new WeakReference<>(mContext),DialogWindow);
        objStartGame.setStoryModeData(nxt_m, nxt_l, nxt_s, nxt_c);
        objStartGame.showObjective();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = DialogWindow.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.height =  ConvertToPx(mContext, 390);
        window.setAttributes(lp);
        DialogWindow.findViewById(R.id.btnClose).setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnGameSummary:
                DialogWindow.dismiss();
                mContext.objCardGame.objGameSummary.loadSummaryScreenToDialog(DialogWindow);
                break;
            case R.id.btnNext:
                NextGame();
                break;
            case R.id.btnRestart:
                mContext.objCardGame.resetGameData();
                mContext.objCardGame.CardSet = STORY_MODE_CARD_SET;
                if(mContext.objCardGame.BoardType == TWO_BOARD)
                    mContext.objCardGame.RowSize/=2;
                mContext.objCardGame.createGame();
                DialogWindow.dismiss();
                break;
        }
    }
}
