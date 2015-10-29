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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
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

    final int STATUS_COMPLETED = 102;
    final int STATUS_IN_PROGRESS = 104;
    final int STATUS_NEW = 103;
    final int STATUS_LOCKED = 101;
    MainActivity mContext;
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
        //region Game Won Dialog
        if(result)
        {
            view = inflater.inflate(R.layout.dialog_story_mode_summary_yay, null, true);

            View region_stars = view.findViewById(R.id.region_stars);
            if(C<2)
            {
                computeStarsFor1PlayerGame(region_stars);
                ((TextView) view.findViewById(R.id.tvChallengeLimit)).setText(String.valueOf(getOnePlayerMovesLimit()));
                ((TextView)view.findViewById(R.id.tvMoves)).setText(String.valueOf(mContext.objCardGame.Player1_Moves));
            }
            else
            {
                computeStarsFor2PlayerGame(region_stars);
                view.findViewById(R.id.region_moves_limit).setVisibility(View.GONE);
                view.findViewById(R.id.region_moves_made).setVisibility(View.GONE);
                view.findViewById(R.id.region_two_player_result_row_1).setVisibility(View.VISIBLE);
            }

            ((TextView)view.findViewById(R.id.tvScore)).setText(String.valueOf(currentScore));
            ((TextView)view.findViewById(R.id.tvTopScore)).setText(String.valueOf(topScore));
            String gameNumber = "#Game "+String.valueOf(getGameNumber()) ;
            ((TextView) view.findViewById(R.id.tvGameNumber)).setText(gameNumber);
            view.findViewById(R.id.btnGameSummary).setOnClickListener(this);
            TextView tvNext = (TextView)view.findViewById(R.id.btnNext);
            tvNext.setOnClickListener(this);
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/hurry up.ttf");
            tvNext.setTypeface(font);
            view.findViewById(R.id.btnRestart).setOnClickListener(this);
            view.findViewById(R.id.btn_store).setOnClickListener(this);
            view.findViewById(R.id.btnStore).setOnClickListener(this);
            view.findViewById(R.id.tv_share).setOnClickListener(this);

            View tvShare = view.findViewById(R.id.tvShare);
            AnimationSet Beats_InOut = Beats_InOut();
            tvShare.startAnimation(Beats_InOut);
            tvShare.setOnClickListener(this);
        }
        //endregion
        //region Game Lost
        else
        {
            view = inflater.inflate(R.layout.dialog_story_mode_summary_oh_no, null, true);

            if(C<2)
            {
                ((TextView) view.findViewById(R.id.tvChallengeLimit)).setText(String.valueOf(getOnePlayerMovesLimit()));
                ((TextView)view.findViewById(R.id.tvMoves)).setText(String.valueOf(mContext.objCardGame.Player1_Moves));
            }
            else
            {
                view.findViewById(R.id.region_moves_limit).setVisibility(View.GONE);
                view.findViewById(R.id.region_moves_made).setVisibility(View.GONE);
                view.findViewById(R.id.region_two_player_result_row_1).setVisibility(View.VISIBLE);

                TextView tvRow3 =  (TextView)view.findViewById(R.id.tvRow3);
                tvRow3.setText("Try Again");
            }

            view.findViewById(R.id.btnGameSummary).setOnClickListener(this);
            view.findViewById(R.id.btnRestart).setOnClickListener(this);

            TextView btnNext = (TextView)view.findViewById(R.id.btnNext);
            int []nextGame = getNextGame();
            int nxt_m = nextGame[0];
            int nxt_l = nextGame[1];
            int nxt_s = nextGame[2];
            int nxt_c = nextGame[3];
            if(getCompletionStatus(nxt_m,nxt_l,nxt_s,nxt_c)==STATUS_LOCKED)
            {
                btnNext.setClickable(false);
                btnNext.setTextColor(Color.argb(153,170,170,170));
            }
            else
            {
                btnNext.setTextColor(Color.WHITE);
                btnNext.setClickable(true);
                btnNext.setOnClickListener(this);
            }

            View btn_restart = view.findViewById(R.id.btn_restart);
            btn_restart.setOnClickListener(this);
            Animation rotateAnim = RotateAnim();
            btn_restart.startAnimation(rotateAnim);

            View btnBuy = view.findViewById(R.id.btnBuy);
            Animation zoomInOut = ZoomInOut_Slow();
            btnBuy.startAnimation(zoomInOut);
            btnBuy.setOnClickListener(this);

        }
        //endregion


        DialogWindow = new AlertDialog.Builder(mContext).show();
        DialogWindow.setCancelable(false);
        DialogWindow.setContentView(view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = DialogWindow.getWindow();
        lp.copyFrom(window.getAttributes());
        View v2 =  mContext.CurrentView;
        lp.width = v2.getMeasuredWidth() - ConvertToPx(mContext, 40); //WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
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
        int []nextGame = getNextGame();
        int nxt_m = nextGame[0];
        int nxt_l = nextGame[1];
        int nxt_s = nextGame[2];
        int nxt_c = nextGame[3];
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

    private int getCompletionStatus(int M,int L,int S,int C)
    {
        String id = String.valueOf(M)+"_"+String.valueOf(L)+
                "_"+String.valueOf(S)+"_"+String.valueOf(C);

        SharedPreferences prefs = mContext.getSharedPreferences(String.valueOf(STORY_MODE_DATA),
                mContext.MODE_PRIVATE);
        //get data from preferences
        return prefs.getInt(id,getDefaultCompletionStatus(M,L,S,C));
    }
    private int getDefaultCompletionStatus(int M,int L,int S,int C)
    {
        int ModuleLevelCount[] = {18,8,4,8,15,17,11};
        if(ModuleLevelCount[M]-1==L)
            return STATUS_NEW;

        if(L<1 && S<1 && C<1)
            return STATUS_NEW;
        else
            return STATUS_LOCKED;
    }

    private int[] getNextGame()
    {
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

        int nextGame[]= {nxt_m,nxt_l,nxt_s,nxt_c};
        return nextGame;
    }
    public AnimationSet Beats_InOut()
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

    public Animation RotateAnim()
    {

        RotateAnimation rotate = new RotateAnimation(360f,0f,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,.5f);
        rotate.setDuration(800);
        rotate.setStartOffset(500);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new AccelerateInterpolator(1.1f));

        AnimationSet Reload = new AnimationSet(true);
        Reload.addAnimation(rotate);
        Reload.setInterpolator(new AccelerateInterpolator(1.1f));
        return rotate;
    }
    public Animation ZoomInOut_Slow()
    {
        ScaleAnimation zoom = new ScaleAnimation(.9f,1f,.9f,1f,
                Animation.RELATIVE_TO_SELF,.5f,
                Animation.RELATIVE_TO_SELF, .5f);
        zoom.setDuration(2700);
        zoom.setStartOffset(100);
        zoom.setFillAfter(true);
        zoom.setRepeatMode(Animation.REVERSE);
        zoom.setRepeatCount(Animation.INFINITE);
        return zoom;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnGameSummary:
                DialogWindow.dismiss();
                mContext.objCardGame.StoryMode=false;
                mContext.objCardGame.objGameSummary.loadSummaryScreen_StoryMode();
                break;
            case R.id.btnNext:
                NextGame();
                break;
            case R.id.btnRestart:
            case R.id.btn_restart:
                mContext.objCardGame.resetGameData();
                mContext.objCardGame.CardSet = STORY_MODE_CARD_SET;
                if(mContext.objCardGame.BoardType == TWO_BOARD)
                    mContext.objCardGame.RowSize/=2;
                mContext.objCardGame.createGame();
                DialogWindow.dismiss();
                break;
            case R.id.btnStore:
            case R.id.btn_store:
            case R.id.btnBuy:
            case R.id.btn_buy:
                mContext.loadStoreScreen();
                mContext.objCardGame.CleanUp();
                DialogWindow.dismiss();
                break;
            case R.id.btnShare:
            case R.id.btn_share:

                break;
        }
    }
}
