package com.example.swsahu.duplicatecardgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swsahu.duplicatecardgame.StoryMode.ScreenCreation;

import java.lang.ref.WeakReference;

import static com.example.swsahu.duplicatecardgame.HelperClass.ANDROBOT;
import static com.example.swsahu.duplicatecardgame.HelperClass.ARCADE;
import static com.example.swsahu.duplicatecardgame.HelperClass.BOARD_TYPE;
import static com.example.swsahu.duplicatecardgame.HelperClass.BOTH;
import static com.example.swsahu.duplicatecardgame.HelperClass.CARD_SET;
import static com.example.swsahu.duplicatecardgame.HelperClass.CARD_SET_1;
import static com.example.swsahu.duplicatecardgame.HelperClass.CARD_SET_2;
import static com.example.swsahu.duplicatecardgame.HelperClass.CARD_SET_3;
import static com.example.swsahu.duplicatecardgame.HelperClass.COLUMN_SIZE;
import static com.example.swsahu.duplicatecardgame.HelperClass.ConvertToPx;
import static com.example.swsahu.duplicatecardgame.HelperClass.DELIMITER;
import static com.example.swsahu.duplicatecardgame.HelperClass.FIVE;
import static com.example.swsahu.duplicatecardgame.HelperClass.FLIP_ANIMATION_TIME;
import static com.example.swsahu.duplicatecardgame.HelperClass.FOUR;
import static com.example.swsahu.duplicatecardgame.HelperClass.GAME_BACKGROUND;
import static com.example.swsahu.duplicatecardgame.HelperClass.GAME_MODE;
import static com.example.swsahu.duplicatecardgame.HelperClass.HORIZONTAL;
import static com.example.swsahu.duplicatecardgame.HelperClass.HURRICANE;
import static com.example.swsahu.duplicatecardgame.HelperClass.LOCKING_TIME;
import static com.example.swsahu.duplicatecardgame.HelperClass.MANUAL;
import static com.example.swsahu.duplicatecardgame.HelperClass.MAX_COL_SIZE;
import static com.example.swsahu.duplicatecardgame.HelperClass.MAX_ROW_SIZE_1B;
import static com.example.swsahu.duplicatecardgame.HelperClass.MAX_ROW_SIZE_2B;
import static com.example.swsahu.duplicatecardgame.HelperClass.NO_SCROLL;
import static com.example.swsahu.duplicatecardgame.HelperClass.ONE_BOARD;
import static com.example.swsahu.duplicatecardgame.HelperClass.ONE_PLAYER;
import static com.example.swsahu.duplicatecardgame.HelperClass.PLAYER_MODE;
import static com.example.swsahu.duplicatecardgame.HelperClass.PLAYER_ONE_NAME;
import static com.example.swsahu.duplicatecardgame.HelperClass.PLAYER_TWO_NAME;
import static com.example.swsahu.duplicatecardgame.HelperClass.PLAYER_TWO_TYPE;
import static com.example.swsahu.duplicatecardgame.HelperClass.QUICK_GAME;
import static com.example.swsahu.duplicatecardgame.HelperClass.RANDOM_BOT;
import static com.example.swsahu.duplicatecardgame.HelperClass.ROBOT_MEMORY;
import static com.example.swsahu.duplicatecardgame.HelperClass.ROBOT_PLAYER;
import static com.example.swsahu.duplicatecardgame.HelperClass.ROCK;
import static com.example.swsahu.duplicatecardgame.HelperClass.ROW_SIZE;
import static com.example.swsahu.duplicatecardgame.HelperClass.SCREEN_GAME;
import static com.example.swsahu.duplicatecardgame.HelperClass.SCROLL_TYPE;
import static com.example.swsahu.duplicatecardgame.HelperClass.SIX;
import static com.example.swsahu.duplicatecardgame.HelperClass.SetEnableControls;
import static com.example.swsahu.duplicatecardgame.HelperClass.SetFontToControls;
import static com.example.swsahu.duplicatecardgame.HelperClass.TIME_TRIAL;
import static com.example.swsahu.duplicatecardgame.HelperClass.TIME_TRIAL_TIMER;
import static com.example.swsahu.duplicatecardgame.HelperClass.TIME_TRIAL_VALUE_1;
import static com.example.swsahu.duplicatecardgame.HelperClass.TIME_TRIAL_VALUE_2;
import static com.example.swsahu.duplicatecardgame.HelperClass.TIME_TRIAL_VALUE_3;
import static com.example.swsahu.duplicatecardgame.HelperClass.TOTAL_COINS;
import static com.example.swsahu.duplicatecardgame.HelperClass.TWO_BOARD;
import static com.example.swsahu.duplicatecardgame.HelperClass.TWO_PLAYER;
import static com.example.swsahu.duplicatecardgame.HelperClass.VERTICAL;
import static com.example.swsahu.duplicatecardgame.HelperClass.applyBorderDrawableToView;

//implements GestureDetector.OnGestureListener
public class MainActivity extends Activity {

    final MainActivity thisContext = this;
    public View CurrentView;
    public int CURRENT_SCREEN;
    public Game objCardGame;
    public int LockingTime;
    HomePageTitleBar objHomePageTitleBar;
    AlertDialog CommonDialog;
    View.OnClickListener Process_Input;
    //user data
    long coins;
    String playerOneName;
    String playerTwoName;
    //region Game Related
    int PlayerMode;
    int PlayerTwoType;
    int RobotMemoryLevel;
    int GameMode;
    int TimeTrialTimer;
    int BoardType;
    int RowSize;
    int ColSize;
    int ScrollType;
    int CardSet;
    int GameBackground;
    boolean PlayerOne_FirstMove;
//endregion
    int data;

    public void loadSettingsScreen() {
        SettingsScreen objSettings = new SettingsScreen(new WeakReference<>(thisContext));
        objSettings.loadScreen();
    }

    public void loadTopScoresScreen()
    {
       TopScores objTopScoresScreen = new TopScores(new WeakReference<>(thisContext));
        LoadDefaultValues("");
        objTopScoresScreen.InitializeBoardDetails(GameMode,PlayerMode,PlayerTwoType,RobotMemoryLevel,
                BoardType,ScrollType,CardSet,RowSize,ColSize,TimeTrialTimer);
        objTopScoresScreen.Show();
    }

    public void loadStoreScreen()
    {
        Store objStoreScreen = new Store(new WeakReference<>(thisContext));
        objStoreScreen.Show();
    }

    public void loadHelpScreen()
    {
        Help objHelpScreen = new Help(new WeakReference<>(thisContext));
        objHelpScreen.Show();
    }

    public View loadView(int layout_id) {
        if (CurrentView != null)
            CurrentView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        View view;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(layout_id, null, false);
            Typeface font = Typeface.createFromAsset(thisContext.getAssets(), "fonts/hurry up.ttf");
            SetFontToControls(font, (ViewGroup) view);

        view.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        setContentView(view);
        CurrentView = view;
        CURRENT_SCREEN = layout_id;
        screenSpecificControls();
        return view;
    }

    public void screenSpecificControls() {
        switch (CURRENT_SCREEN) {
            case R.layout.screen_home:
                ((TextView)findViewById(R.id.tvCoins)).setTypeface(Typeface.SANS_SERIF);
                updateCoins(0);//here
                setCoins();
                setPlayerNames();
                InitializeDialogInputListener();
                objHomePageTitleBar.requestUpdate(false);
                AnimateViews();

                break;
            case R.layout.screen_board_details:
                LockingTime = getLockingTime();
                GameBackground = getGameBackground();
                addFlingListenerForBoardBackgroundSelection();
                InitializeScreenControls_BoardDetails();
                if(GameMode == ARCADE)
                {
                    int COLOR_DISABLED_GRAY = Color.argb(45, 45, 45, 45);
                    View temp_view = findViewById(R.id.region_TimeTrial);
                    SetEnableControls(false, (ViewGroup) temp_view);
                    temp_view.setBackgroundColor(COLOR_DISABLED_GRAY);
                    findViewById(R.id.TimeTrialTime).setBackgroundResource(R.drawable.background_white45_white196_disabled);
                }
                break;
            case R.layout.screen_quick_game:
                InitializeScreenControls_QuickGame();
                break;
        }
    }

    private void AnimateViews() {
        HomeScreenAnimations objAnim = new HomeScreenAnimations();
        objAnim.StartAnimation(new WeakReference<>(thisContext));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isPlayInProgress = savedInstanceState.getBoolean(":");
        }

        if (objCardGame != null) {
            objCardGame.Clear();
        }

        if (objHomePageTitleBar == null)
            objHomePageTitleBar = new HomePageTitleBar(new WeakReference<>(this));
        loadView(R.layout.screen_home);
        CURRENT_SCREEN = R.layout.screen_home;

    }

    public void setCoins() {
        ((TextView) findViewById(R.id.tvCoins)).setText(String.valueOf(coins));
    }

    private void setPlayerNames() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisContext);
        playerOneName = preferences.getString(String.valueOf(PLAYER_ONE_NAME), "Player 1");
        playerTwoName = preferences.getString(String.valueOf(PLAYER_TWO_NAME), "Player 2");
    }

    private void addFlingListenerForBoardBackgroundSelection() {
        final View v  = findViewById(R.id.region_boardBackground);
        final GestureDetector gdt = new GestureDetector(thisContext,new GestureListener());
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
    }

    public void myClickHandler(View v) {
        switch (v.getId()) {
            case R.id.btnGameLogo:
                objHomePageTitleBar.requestUpdate(true);
                break;
            case R.id.btnQuickGame:
                LoadDefaultValues(String.valueOf(QUICK_GAME));
                loadView(R.layout.screen_quick_game);
                InitializeRobotMemoryListener();
                break;
            case R.id.btnArcade:
                loadView(R.layout.screen_player_mode);
                LoadDefaultValues("");
                InitializeScreenControls_PlayerMode();
                CURRENT_SCREEN = R.layout.screen_player_mode;
                GameMode = ARCADE;
                break;
            case R.id.btnTimeTrial:
                loadView(R.layout.screen_player_mode);
                LoadDefaultValues("");
                InitializeScreenControls_PlayerMode();
                CURRENT_SCREEN = R.layout.screen_player_mode;
                GameMode = TIME_TRIAL;
                break;
            case R.id.btnStoryMode:
                //loadSettingsScreen();
                ScreenCreation obj = new ScreenCreation(new WeakReference<>(this));
                obj.show();
                break;
            case R.id.btnHelp:
                loadHelpScreen();
                //ShowUnderConstructionDialog();
                break;
            case R.id.btnStore:
            case R.id.btn_store:
                loadStoreScreen();
                break;
            case R.id.btnTopScores:
            case R.id.btn_topScores:
                loadTopScoresScreen();
                break;
            case R.id.btnSettings:
            case R.id.btn_settings:
                loadSettingsScreen();
                break;
            case R.id.btnRating:
            case R.id.btn_rating:
                ShowUnderConstructionDialog();
                break;
            case R.id.btnFb:
                //loadSettingsScreen();
                //ShowUnderConstructionDialog();
                break;
            case R.id.btn_fb:
                (findViewById(R.id.btnFb)).performClick();
                break;
            case R.id.btnShare:
                ShowUnderConstructionDialog();
                break;
            case R.id.btn_share:
                (findViewById(R.id.btnShare)).performClick();
                break;
            case R.id.btnBack:
            case R.id.btn_back:
            case R.id.btnExit:
            case R.id.btn_exit:
                onBackPress();
                break;
            //
        }
    }

    public void myClickHandler_QuickGame(View v)
    {
        switch (v.getId()) {
            case R.id.value_1P:
                PlayerMode = ONE_PLAYER;
                break;
            case R.id.value_2P:
                PlayerMode = TWO_PLAYER;
                break;
            case R.id.PlayerTwoType:
            case R.id.btnPlayerTwoType:
                displayDialog(getPlayerType(), true);
                return;
            case R.id.value_Arcade:
                GameMode = ARCADE;
                break;
            case R.id.value_TimeTrial:
                GameMode = TIME_TRIAL;
                break;
            case R.id.value_TTT_v1:
                TimeTrialTimer = TIME_TRIAL_VALUE_1;
                break;
            case R.id.value_TTT_v2:
                TimeTrialTimer = TIME_TRIAL_VALUE_2;
                break;
            case R.id.value_TTT_v3:
                TimeTrialTimer = TIME_TRIAL_VALUE_3;
                break;
            case R.id.btnTimeTrialTime:
            case R.id.TimeTrialTime:
                displayDialog(getTimeTrialTime(), true);
                return;
            case R.id.value_OneBoard:
                BoardType = ONE_BOARD;
                RowSize = Math.max(ColSize, RowSize);
                break;
            case R.id.value_TwoBoard:
                BoardType = TWO_BOARD;
                RowSize = Math.min(MAX_ROW_SIZE_2B, RowSize);
                break;
            case R.id.RowSize:
            case R.id.btnBoardSize:
                displayDialog(getBoardSize(), true);
                return;
            case R.id.ScrollType:
            case R.id.btnScrollType:
                displayDialog(getScrollType(), true);
                return;
            case R.id.value_CardSetOne:
                CardSet = CARD_SET_1;
                break;
            case R.id.value_CardSetTwo:
                CardSet = CARD_SET_2;
                break;
            case R.id.value_CardSetThree:
                CardSet = CARD_SET_3;
                break;
            case R.id.btnCardSet:
                displayDialog(getCardSet(), true);
                return;
            case R.id.btnRandomizeConfiguration:
                RandomizeValues();
                break;
            case R.id.btnBack:
            case R.id.btn_back:
                onBackPress();
                return;
            case R.id.btnStart:
                StartGame();
                return;
        }
        InitializeScreenControls_QuickGame();
    }

    private void InitializeScreenControls_QuickGame()
    {
        View temp_view;

        int boardType_views[] = {R.id.value_OneBoard, R.id.value_TwoBoard};
        int boardType_id[] = {ONE_BOARD, TWO_BOARD};

        int timeTrialTime_views[] = {R.id.value_TTT_v1,R.id.value_TTT_v2,R.id.value_TTT_v3};
        int timeTrialTime_values[] = { 5000, 10000, 15000};


        int cardSet_views[] = {R.id.value_CardSetOne, R.id.value_CardSetTwo, R.id.value_CardSetThree};
        int cardSet_id[] = {CARD_SET_1, CARD_SET_2, CARD_SET_3};

        int COLOR_DISABLED_GRAY = Color.argb(45, 45, 45, 45);


        if (PlayerMode == ONE_PLAYER) {
            temp_view = findViewById(R.id.region_PlayerTwo);
            SetEnableControls(false, (ViewGroup) temp_view);
            temp_view.setBackgroundColor(COLOR_DISABLED_GRAY);
            temp_view = findViewById(R.id.region_memory);
            SetEnableControls(false, (ViewGroup) temp_view);
            temp_view.setBackgroundColor(COLOR_DISABLED_GRAY);

            findViewById(R.id.PlayerTwoType).setBackgroundResource(R.drawable.background_white45_white196_disabled);
            findViewById(R.id.value_1P).setBackgroundResource(R.drawable.btn_white_pressed_thin_border);
            findViewById(R.id.value_2P).setBackgroundResource(R.drawable.btn_white_transparency_20);
        }
        else
        {

            temp_view = findViewById(R.id.region_PlayerTwo);
            SetEnableControls(true, (ViewGroup) temp_view);
            temp_view.setBackgroundColor(Color.argb(0, 0, 0, 0));
            temp_view = findViewById(R.id.region_memory);
            temp_view.setBackgroundColor(Color.argb(0, 0, 0, 0));
            SetEnableControls(true, (ViewGroup) temp_view);

            findViewById(R.id.value_2P).setBackgroundResource(R.drawable.btn_white_pressed_thin_border);
            findViewById(R.id.value_1P).setBackgroundResource(R.drawable.btn_white_transparency_20);
            findViewById(R.id.PlayerTwoType).setBackgroundResource(R.drawable.btn_white_pressed_thin_border);


            if (PlayerTwoType == MANUAL)
            {
                temp_view = findViewById(R.id.region_memory);
                SetEnableControls(false, (ViewGroup) temp_view);
            }
        }
        setContentToView(R.id.PlayerTwoType, get_text(PlayerTwoType));
        ((SeekBar) findViewById(R.id.RobotMemory)).setProgress(RobotMemoryLevel-1);

        if(GameMode == ARCADE)
        {
            findViewById(R.id.value_Arcade).setBackgroundResource(R.drawable.btn_white_pressed_thin_border);
            findViewById(R.id.value_TimeTrial).setBackgroundResource(R.drawable.btn_white_transparency_20);

            temp_view = findViewById(R.id.region_TimeTrial);
            SetEnableControls(false, (ViewGroup) temp_view);
            temp_view.setBackgroundColor(COLOR_DISABLED_GRAY);
            setBackgroundToViews(timeTrialTime_views, R.drawable.background_white45_white196_disabled);

            findViewById(R.id.region_TimeTrialTime_value_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.TimeTrialTime).setVisibility(View.GONE);
        }
        else
        {
            findViewById(R.id.value_TimeTrial).setBackgroundResource(R.drawable.btn_white_pressed_thin_border);
            findViewById(R.id.value_Arcade).setBackgroundResource(R.drawable.btn_white_transparency_20);

            temp_view = findViewById(R.id.region_TimeTrial);
            SetEnableControls(true, (ViewGroup) temp_view);
            temp_view.setBackgroundColor(Color.TRANSPARENT);
            if(TimeTrialTimer == 5000 || TimeTrialTimer == 10000 || TimeTrialTimer == 15000)
            {
                findViewById(R.id.region_TimeTrialTime_value_buttons).setVisibility(View.VISIBLE);
                findViewById(R.id.TimeTrialTime).setVisibility(View.GONE);

                setBackgroundToViews(timeTrialTime_views, R.drawable.btn_white_transparency_20);
                SetBackgroundToViewFromArray(timeTrialTime_values, timeTrialTime_views, TimeTrialTimer, R.drawable.btn_white_pressed_thin_border);
            }
            else
            {
                temp_view = findViewById(R.id.TimeTrialTime);
                String timeTrialText = String.valueOf(TimeTrialTimer/1000) + " sec";

                temp_view.setVisibility(View.VISIBLE);
                findViewById(R.id.region_TimeTrialTime_value_buttons).setVisibility(View.GONE);

                ((TextView)temp_view).setText(timeTrialText);
                temp_view.setBackgroundResource(R.drawable.btn_white_pressed_thin_border);
            }
        }

        setBackgroundToViews(boardType_views, R.drawable.btn_white_transparency_20);
        SetBackgroundToViewFromArray(boardType_id, boardType_views, BoardType, R.drawable.btn_white_pressed_thin_border);

        setContentToView(R.id.ScrollType,get_text(ScrollType));

        setContentToView(R.id.RowSize, String.valueOf(RowSize));
        setContentToView(R.id.ColSize, String.valueOf(ColSize));

        setBackgroundToViews(cardSet_views, R.drawable.btn_white_transparency_20);
        SetBackgroundToViewFromArray(cardSet_id, cardSet_views, CardSet, R.drawable.btn_white_pressed_thin_border);
    }

    public void myClickHandler_PlayerMode(View v) {
        switch (v.getId()) {
            case R.id.value_1P:
                PlayerMode = ONE_PLAYER;
                break;
            case R.id.value_2P:
                PlayerMode = TWO_PLAYER;
                break;
            case R.id.value_androbot:
                PlayerTwoType = ANDROBOT;
                break;
            case R.id.value_hurricane:
                PlayerTwoType = HURRICANE;
                break;
            case R.id.value_rock:
                PlayerTwoType = ROCK;
                break;
            case R.id.value_random:
                PlayerTwoType = RANDOM_BOT;
                break;
            case R.id.value_manual:
                PlayerTwoType = MANUAL;
                break;
            case R.id.value_robotMemory_1:
                RobotMemoryLevel = 1;
                break;
            case R.id.value_robotMemory_2:
                RobotMemoryLevel = 2;
                break;
            case R.id.value_robotMemory_3:
                RobotMemoryLevel = 3;
                break;
            case R.id.value_robotMemory_4:
                RobotMemoryLevel = 4;
                break;
            case R.id.value_robotMemory_5:
                RobotMemoryLevel = 5;
                break;
            case R.id.value_robotMemory_6:
                RobotMemoryLevel = 6;
                break;
            case R.id.value_robotMemory_7:
                RobotMemoryLevel = 7;
                break;
            case R.id.value_robotMemory_8:
                RobotMemoryLevel = 8;
                break;
            case R.id.value_robotMemory_9:
                RobotMemoryLevel = 9;
                break;
            case R.id.value_robotMemory_10:
                RobotMemoryLevel = 10;
                break;
            case R.id.btnRandomize:
                int temp = GameMode;
                RandomizeValues();
                GameMode = temp;
                break;
            case R.id.btnBack:
            case R.id.btn_back:
                loadView(R.layout.screen_home);
                CURRENT_SCREEN = R.layout.screen_home;
                return;
            case R.id.btnNext:
                loadView(R.layout.screen_board_details);
                return;
        }
        InitializeScreenControls_PlayerMode();
    }

    private void InitializeScreenControls_PlayerMode() {
        View temp_view;
        int playerMode_views[] = {R.id.value_1P, R.id.value_2P};
        //int playerMode_id[] = {ONE_PLAYER, TWO_PLAYER};
        int playerType_views[] = {R.id.value_hurricane, R.id.value_androbot, R.id.value_rock, R.id.value_random, R.id.value_manual};
        int playerType_id[] = {HURRICANE, ANDROBOT, ROCK, RANDOM_BOT, MANUAL};
        int robotMemory_views[] = {R.id.value_robotMemory_1, R.id.value_robotMemory_2, R.id.value_robotMemory_3,
                R.id.value_robotMemory_4, R.id.value_robotMemory_5, R.id.value_robotMemory_6, R.id.value_robotMemory_7,
                R.id.value_robotMemory_8, R.id.value_robotMemory_9, R.id.value_robotMemory_10};
        int robotMemory_id[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int COLOR_DISABLED_GRAY = Color.argb(45, 45, 45, 45);

        setBackgroundToViews(playerMode_views, R.drawable.background_white45_white196);

        if (PlayerMode == ONE_PLAYER) {
            temp_view = findViewById(R.id.region_PlayerTwo);
            SetEnableControls(false, (ViewGroup) temp_view);
            temp_view.setBackgroundColor(COLOR_DISABLED_GRAY);
            temp_view = findViewById(R.id.region_memory);
            SetEnableControls(false, (ViewGroup) temp_view);
            temp_view.setBackgroundColor(COLOR_DISABLED_GRAY);


            setBackgroundToViews(playerType_views, R.drawable.background_white45_white196_disabled);
            setBackgroundToViews(robotMemory_views, R.drawable.background_white45_white196_disabled);

            findViewById(R.id.value_1P).setBackgroundResource(R.drawable.background_white_orange);
            findViewById(R.id.value_2P).setBackgroundResource(R.drawable.background_white45_white196);
            return;
        }

        //ELSE
        {
            setBackgroundToViews(playerType_views, R.drawable.background_white45_white196);

            temp_view = findViewById(R.id.region_PlayerTwo);
            SetEnableControls(true, (ViewGroup) temp_view);
            temp_view.setBackgroundColor(Color.argb(0, 0, 0, 0));
            temp_view = findViewById(R.id.region_memory);
            temp_view.setBackgroundColor(Color.argb(0, 0, 0, 0));
            SetEnableControls(true, (ViewGroup) temp_view);

            findViewById(R.id.value_2P).setBackgroundResource(R.drawable.background_white_orange);
            findViewById(R.id.value_1P).setBackgroundResource(R.drawable.background_white45_white196);
            SetBackgroundToViewFromArray(playerType_id, playerType_views, PlayerTwoType, R.drawable.background_white_orange);

            if (PlayerTwoType == MANUAL) {
                setBackgroundToViews(robotMemory_views, R.drawable.background_white45_white196_disabled);
                findViewById(R.id.region_memory).setBackgroundColor(COLOR_DISABLED_GRAY);
                return;
            }
            //ELSE
            setBackgroundToViews(robotMemory_views, R.drawable.background_white45_white196);
            SetBackgroundToViewFromArray(robotMemory_id, robotMemory_views, RobotMemoryLevel, R.drawable.background_white_orange);
        }
    }

    public void myClickHandler_BoardDetails(View v) {
        switch (v.getId()) {
            case R.id.value_OneBoard:
                BoardType = ONE_BOARD;
                RowSize = Math.max(ColSize, RowSize);
                break;
            case R.id.value_TwoBoard:
                BoardType = TWO_BOARD;
                RowSize = Math.min(MAX_ROW_SIZE_2B, RowSize);
                break;
            case R.id.value_noScroll:
                ScrollType = NO_SCROLL;
                break;
            case R.id.value_verticalScroll:
                ScrollType = VERTICAL;
                break;
            case R.id.value_horizontalScroll:
                ScrollType = HORIZONTAL;
                break;
            case R.id.value_bothScroll:
                ScrollType = BOTH;
                break;
            case R.id.RowSize:
            case R.id.btnBoardSize:
                displayDialog(getBoardSize(), true);
                return;
            case R.id.value_CardSetOne:
                CardSet = CARD_SET_1;
                break;
            case R.id.value_CardSetTwo:
                CardSet = CARD_SET_2;
                break;
            case R.id.value_CardSetThree:
                CardSet = CARD_SET_3;
                break;
            case R.id.CardSet:
            case R.id.btnCardSet:
                Toast.makeText(thisContext, "Not Implemented", Toast.LENGTH_SHORT).show();
                return;
            case R.id.TimeTrialTime:
            case R.id.btnTimeTrialTime:
                displayDialog(getTimeTrialTime(), true);
                return;
            case R.id.OneTouchFlip:
                reverseOneTouchFlip();
                break;
            case R.id.LockingTime:
            case R.id.btnLockingTime:
                displayDialog(getLockingTimeValue(),true);
                return;
            case R.id.previousBoardBackground:
                GameBackground--;
                break;
            case R.id.ivBoardBackground:
            case R.id.nextBoardBackground:
                GameBackground++;
                break;

            case R.id.btnBack:
            case R.id.btn_back:
                loadView(R.layout.screen_player_mode);
                InitializeScreenControls_PlayerMode();
                return;
            case R.id.btnStart:
                StartGame();
                return;
        }
        InitializeScreenControls_BoardDetails();
    }

    public void InitializeScreenControls_BoardDetails() {

        int boardType_views[] = {R.id.value_OneBoard, R.id.value_TwoBoard};
        int boardType_id[] = {ONE_BOARD, TWO_BOARD};

        int scrollType_views[] = {R.id.value_noScroll, R.id.value_verticalScroll, R.id.value_horizontalScroll, R.id.value_bothScroll};
        int scrollType_id[] = {NO_SCROLL, VERTICAL, HORIZONTAL, BOTH};

        int cardSet_views[] = {R.id.value_CardSetOne, R.id.value_CardSetTwo, R.id.value_CardSetThree};
        int cardSet_id[] = {CARD_SET_1, CARD_SET_2, CARD_SET_3};


        setBackgroundToViews(boardType_views, R.drawable.btn_white_transparency_20);
        SetBackgroundToViewFromArray(boardType_id, boardType_views, BoardType, R.drawable.btn_white_pressed_thin_border);

        setBackgroundToViews(scrollType_views, R.drawable.btn_white_transparency_20);
        SetBackgroundToViewFromArray(scrollType_id, scrollType_views, ScrollType, R.drawable.btn_white_pressed_thin_border);

        setBackgroundToViews(cardSet_views, R.drawable.btn_white_transparency_20);
        SetBackgroundToViewFromArray(cardSet_id, cardSet_views, CardSet, R.drawable.btn_white_pressed_thin_border);

        setContentToView(R.id.RowSize, String.valueOf(RowSize));
        setContentToView(R.id.ColSize, String.valueOf(ColSize));
        setContentToView(R.id.TimeTrialTime, String.valueOf(TimeTrialTimer / 1000) + "sec");

        setContentToView(R.id.OneTouchFlip, getOneTouchFlipText());
        setContentToView(R.id.LockingTime, String.valueOf(LockingTime) + " ms");

        setGameBackgroundPreview();
    }

    public int getLockingTime()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisContext);
        return preferences.getInt(String.valueOf(LOCKING_TIME), 600);
    }

    public int getGameBackground()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisContext);
        return preferences.getInt(String.valueOf(GAME_BACKGROUND), 0);
    }

    public void setGameBackgroundPreview()
    {
        TypedArray backgrounds = thisContext.getResources().obtainTypedArray(R.array.game_backgrounds);
        int length = backgrounds.length();
        length++;
        if(GameBackground<0)
            GameBackground+=length;
        else
            GameBackground = GameBackground % length;

        int background_res;
        if(GameBackground != 0)
        {
            background_res = backgrounds.getResourceId(GameBackground - 1, -1);
        }
        else
        {
            background_res = R.drawable.background_random;
        }
        backgrounds.recycle();
        findViewById(R.id.ivBoardBackground).setBackgroundResource(background_res);
    }

    public void reverseOneTouchFlip()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisContext);
        int x = preferences.getInt(String.valueOf(FLIP_ANIMATION_TIME), 9);
        x = (x>20)? 120 : 9;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(String.valueOf(FLIP_ANIMATION_TIME), x);
        editor.apply();
    }

    public String getOneTouchFlipText()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisContext);
        int x = preferences.getInt(String.valueOf(FLIP_ANIMATION_TIME), 9);
        if(x>20)
            return "OFF";
        else
            return ("ON");
    }

    public void setContentToView(int viewResId,String value)
    {
        View v = findViewById(viewResId);
//        v.setBackgroundResource(drawableId);
        ((TextView)v).setText(value);
    }

    public void setBackgroundToViews(int view_id[],int drawableId)
    {
        View v;
        for (int id:view_id)
        {
            v = findViewById(id);
            v.setBackgroundResource(drawableId);
        }
    }

    public void SetBackgroundToViewFromArray(int identifier[],int view_id[],int id,int drawableId)
    {
        View v;
        for(int i=0;i<identifier.length;i++)
        {
            if(identifier[i]==id)
            {
                v = findViewById(view_id[i]);
                v.setBackgroundResource(drawableId);
                break;
            }
        }
    }

    private void InitializeRobotMemoryListener()
    {
        final SeekBar robotMemory = (SeekBar)findViewById(R.id.RobotMemory);
        robotMemory.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                RobotMemoryLevel = seekBar.getProgress() + 1;
            }
        });
    }

    private void SaveGameConfiguration()
    {
        String id="";
        if(CURRENT_SCREEN==R.layout.screen_quick_game)
        {
            CheckBox cb = (CheckBox) findViewById(R.id.cbSaveConfiguration);
            if(!cb.isChecked())
                return;
            id=String.valueOf(QUICK_GAME);
        }
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(thisContext);
        SharedPreferences.Editor editor = settings.edit();

         editor.putInt(String.valueOf(PLAYER_MODE) + id, PlayerMode);
         editor.putInt(String.valueOf(PLAYER_TWO_TYPE) + id, PlayerTwoType);
         editor.putInt(String.valueOf(ROBOT_MEMORY) + id, RobotMemoryLevel);
         editor.putInt(String.valueOf(GAME_MODE) + id, GameMode);
         editor.putInt(String.valueOf(TIME_TRIAL_TIMER) + id, TimeTrialTimer);
         editor.putInt(String.valueOf(BOARD_TYPE) + id, BoardType);
         editor.putInt(String.valueOf(ROW_SIZE) + id, RowSize);
         editor.putInt(String.valueOf(COLUMN_SIZE) + id, ColSize);
         editor.putInt(String.valueOf(SCROLL_TYPE) + id, ScrollType);
         editor.putInt(String.valueOf(CARD_SET) + id, CardSet);

        if(CURRENT_SCREEN == R.layout.screen_board_details)
        {
            editor.putInt(String.valueOf(LOCKING_TIME), LockingTime);
            editor.putInt(String.valueOf(GAME_BACKGROUND), GameBackground);
        }
        // Commit the edits!
        editor.apply();
    }

    private void StartGame()
    {
        SaveGameConfiguration();
        if(objCardGame == null)
            objCardGame = new Game(new WeakReference<>(this));
        else
        {
            objCardGame.Clear();
            objCardGame.Update_mContext(new WeakReference<>(this));
        }
        objCardGame.GameBackground = GameBackground;
        objCardGame.PlayerOne_Turn = true; //PlayerOne_FirstMove;
        objCardGame.setGameConfiguration(PlayerMode, PlayerTwoType, RobotMemoryLevel, GameMode, TimeTrialTimer,
                BoardType, RowSize, ColSize, ScrollType, CardSet);
        objCardGame.StartGame();
        CURRENT_SCREEN = SCREEN_GAME;
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if(CURRENT_SCREEN == R.layout.screen_home )
                return super.onKeyDown(keyCode, event);

            onBackPress();
        }
        return false;
    }

    public void onBackPress()
    {
        switch (CURRENT_SCREEN)
        {
            case R.layout.screen_home:
                thisContext.finish();
                break;
            case SCREEN_GAME:
                if(objCardGame.GameTimer!=null)
                   objCardGame.GameTimer.cancel();
                if(objCardGame.objTimeTrail!=null)
                    if(objCardGame.objTimeTrail.TimeTrialTimer!=null)
                       objCardGame.objTimeTrail.TimeTrialTimer.cancel();

                objCardGame=null;
            default:
                loadView(R.layout.screen_home);
        }
        CollectGarbage();
    }

    private void CollectGarbage() {
        Runnable myRunnable = new Runnable(){
            public void run(){
                System.gc();
            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();
    }

    //region dialog logic
    private void displayDialog(View v,boolean isCancelable)
    {
        CommonDialog  = new AlertDialog.Builder(thisContext).show();
        CommonDialog.setContentView(v);
        Typeface font = Typeface.createFromAsset(thisContext.getAssets(), "fonts/hurry up.ttf");
        SetFontToControls(font, (ViewGroup) v);
        CommonDialog.setCancelable(isCancelable);
    }

    private View getPlayerType()
    {
        String titleText = "Select Player";
        String text[] =  {"Manual","Hurricane","Rock","Androbot","Random-Bot"};
        int tag [] =  {MANUAL,HURRICANE,ROCK,ANDROBOT,RANDOM_BOT};
        return addToMainContainer(tag,text,String.valueOf(PLAYER_TWO_TYPE),tag.length, titleText);
    }

    private View getGameModes()
    {
        String titleText = "Select Game Mode";
        String text[] =  {"Arcade","Time-Trial"};
        int tag [] =  {ARCADE, TIME_TRIAL};
        return addToMainContainer(tag,text,String.valueOf(GAME_MODE),tag.length, titleText);
    }

    private View getPlayerModes()
    {
        String text[] =  {"1P", "2P - AndroBot","2P - Hurricane","2P - Rocky","2P - All Robots"};
        int tag [] =  {ONE_PLAYER, ROBOT_PLAYER,ROBOT_PLAYER,ROBOT_PLAYER,ROBOT_PLAYER};
        return addToMainContainer(tag,text,String.valueOf(PLAYER_MODE),tag.length,"Select Player Mode");
    }

    private View getRobotMemory()
    {
        String text[] =  {"1","2","3","4","5","6","7","8","9","10"};
        int tag [] =  {1,2,3,4,5,6,7,8,9,10};
        return addToMainContainer(tag,text,String.valueOf(ROBOT_MEMORY),tag.length,"Select Robot Memory");
    }

    private View getBoardType()
    {
        String text[] =  {"One-Board","Two-Board"};
        int tag [] =  {ONE_BOARD, TWO_BOARD};
        return addToMainContainer(tag, text, String.valueOf(BOARD_TYPE), tag.length, "Select Board Type");
    }

    private View getTimeTrialTime()
    {
        String text[] =  new String[27];
        int tag [] =  new int[27];
        for(int i = 4;i<=30;i++)
        {
            text[i-4] = String.valueOf(i) + " S";
            tag [i-4] = i*1000;
        }


        return addToBoardSizeContainer(tag, text, String.valueOf(TIME_TRIAL_TIMER), tag.length, "Time Trail Time");
    }

    private View getColSize()
    {
        int size;
        if(BoardType == ONE_BOARD)
            size = Math.min(RowSize, MAX_COL_SIZE);
        else
            size = Math.min(2*RowSize,MAX_COL_SIZE);

        int i =2;
        String text[] =  new String [size];
        int tag [] =  new int[size];
        while (i<=size)
        {
            tag[i-2] = i;
            text[i-2] = String.valueOf(i);
            i++;
        }
        return addToBoardSizeContainer(tag, text, String.valueOf(COLUMN_SIZE),i-2,"Select Column Size");
    }

    private View getRowSize()
    {
        int size;
        if(BoardType == ONE_BOARD)
            size = MAX_ROW_SIZE_1B;
        else
            size = MAX_ROW_SIZE_2B;

        String text[] =  new String[size];
        int tag [] =  new int[size];
        int i = 2;
        while (i<=size)
        {
            tag[i-2]=i;
            text[i-2]=String.valueOf(i);
            i++;
        }
        return addToBoardSizeContainer(tag, text, String.valueOf(ROW_SIZE), i - 2, "Select Row Size");
    }

    private View getBoardSize()
    {
        return getRowSize();
    }

    private View getScrollType()
    {
        String text[] =  {"No Scroll","Vertical Scroll","Horizontal Scroll","Both Scroll"};
        int tag [] =  {NO_SCROLL, VERTICAL,HORIZONTAL,BOTH};
        return addToMainContainer(tag, text, String.valueOf(SCROLL_TYPE), tag.length, "Select Scroll Type");
    }

    private View getCardSet()
    {
        String text[] =  {"I","II","III"};
        int tag [] =  {CARD_SET_1,CARD_SET_2,CARD_SET_3};
        return addToMainContainer(tag, text, String.valueOf(CARD_SET),tag.length,"Select Card Set");
    }

    private View getLockingTimeValue()
    {
        String text[] =  {"200 ms","400 ms","600 ms","800 ms","1000 ms","1200 ms","1400 ms" };
        int tag [] =  {200,400,600,800,1000,1200,1400};
        return addToMainContainer(tag, text, String.valueOf(LOCKING_TIME), tag.length, "Select Locking Time");
    }

    private View addToMainContainer(int[] tag, String[] text,String identifier,int length,String titleText) {
        LinearLayout mainContainer = new LinearLayout(thisContext);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.addView(getTitleTextView(titleText));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ConvertToPx(thisContext,190),
                ConvertToPx(thisContext, 45));
        RelativeLayout.LayoutParams rl_params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                1);
        for (int i=0;i<length;i++)
        {
            TextView tv = new Button(thisContext);
            tv.setText(text[i]);
            String tvTag = identifier + DELIMITER + String.valueOf(tag[i]);
            tv.setTag(tvTag);
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(Color.argb(180, 255, 255, 255));
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(0,ConvertToPx(thisContext,3),0,0);
            tv.setLayoutParams(layoutParams);
            mainContainer.addView(tv);
            tv.setOnClickListener(Process_Input);

            mainContainer.addView(getDivider(rl_params));
        }

        return mainContainer;
    }

    private View addToBoardSizeContainer(int[] tag, String[] text,String identifier,int length,String titleText) {
        LinearLayout row = null;
        LinearLayout mainContainer = new LinearLayout(thisContext);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.addView(getTitleTextView(titleText));

        int i=0;
        LinearLayout.LayoutParams row_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams box_Params = new LinearLayout.LayoutParams(ConvertToPx(thisContext,72),
                ConvertToPx(thisContext,40));
        RelativeLayout.LayoutParams verticalDivider_params= new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
        RelativeLayout.LayoutParams horizontalDivider_params= new RelativeLayout.LayoutParams(1,
                ViewGroup.LayoutParams.MATCH_PARENT);
        while(i<length)
        {
            row = new LinearLayout(thisContext);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(row_Params);
            mainContainer.addView(row);
            mainContainer.addView(getDivider(verticalDivider_params));
            for(int col=0;col<3 && i<length;col++,i++)
            {
                Button tv = new Button(thisContext);
                tv.setText(text[i]);
                String tvTag = identifier + DELIMITER + String.valueOf(tag[i]);
                tv.setTag(tvTag);
                tv.setTextColor(Color.BLACK);
                tv.setPadding(0, ConvertToPx(thisContext, 3), 0, 0);
                tv.setBackgroundColor(Color.argb(180, 255, 255, 255));
                tv.setGravity(Gravity.CENTER);
                tv.setLayoutParams(box_Params);
                tv.setOnClickListener(Process_Input);
                row.addView(tv);
                row.addView(getDivider(horizontalDivider_params));
            }
        }
        while (i%3 != 0)
        {
            Button tv = new Button(thisContext);
            tv.setLayoutParams(box_Params);
            tv.setPadding(0, ConvertToPx(thisContext, 3), 0, 0);
            tv.setBackgroundColor(Color.argb(180, 255, 255, 255));

            row.addView(tv);
            row.addView(getDivider(horizontalDivider_params));
            i++;
        }

        return mainContainer;
    }

    private View getDivider(RelativeLayout.LayoutParams rl_params)
    {
        RelativeLayout divider = new RelativeLayout(thisContext);
        divider.setLayoutParams(rl_params);
        divider.setBackgroundColor(Color.BLACK);
        return divider;
    }

    private View getTitleTextView(String titleText)
    {
        int five_dip = ConvertToPx(thisContext,5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvTitle = new TextView(thisContext);
        tvTitle.setText(titleText);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setBackgroundColor(Color.BLACK);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setLayoutParams(layoutParams);
        tvTitle.setPadding(4 * five_dip, 2 * five_dip, 4 * five_dip, 2 * five_dip);
        return tvTitle;
    }

    //endregion Dialog logic

    private void InitializeDialogInputListener() {
        Process_Input = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyValue = v.getTag().toString();
                int key = Integer.parseInt(keyValue.split(DELIMITER)[0]);
                int value = Integer.parseInt(keyValue.split(DELIMITER)[1]);
                switch (key)
                {
                    case PLAYER_TWO_TYPE:
                        PlayerTwoType = value;
                        break;
                    case ROW_SIZE:
                        RowSize = value;
                        CommonDialog.dismiss();
                        displayDialog(getColSize(),false);
                        return;
                    case COLUMN_SIZE:
                        ColSize = value;
                        break;
                    case SCROLL_TYPE:
                        ScrollType = value;
                        break;
                    case CARD_SET:
                        CardSet = value;
                        break;
                    case TIME_TRIAL_TIMER:
                        TimeTrialTimer = value;
                        break;
                    case LOCKING_TIME:
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisContext);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(String.valueOf(LOCKING_TIME), value);
                        editor.apply();
                        LockingTime=value;
                        InitializeScreenControls_BoardDetails();
                        break;

                    default:
                        Toast.makeText(thisContext,"Didn't match any key. Debug !!",Toast.LENGTH_SHORT).show();
                }
                CommonDialog.dismiss();

                switch (CURRENT_SCREEN) {
                    case R.layout.screen_board_details:
                        InitializeScreenControls_BoardDetails();
                        break;
                    case R.layout.screen_quick_game:
                        InitializeScreenControls_QuickGame();
                        break;
                }
            }
        };
    }

    //region Load default Values

    public void updateCoins(long value)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisContext);
        coins = preferences.getLong(String.valueOf(TOTAL_COINS), 0);
        coins += value;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(String.valueOf(TOTAL_COINS), coins);
        // Commit the edits!
        editor.apply();
    }

    //endregion

    private void LoadDefaultValues(String identifier) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        PlayerMode = preferences.getInt(String.valueOf(PLAYER_MODE)+ identifier,ONE_PLAYER);
        PlayerTwoType = preferences.getInt(String.valueOf(PLAYER_TWO_TYPE)+ identifier,RANDOM_BOT);
        RobotMemoryLevel = preferences.getInt(String.valueOf(ROBOT_MEMORY)+ identifier,FIVE);
        GameMode = preferences.getInt(String.valueOf(GAME_MODE)+ identifier,ARCADE);
        TimeTrialTimer = preferences.getInt(String.valueOf(TIME_TRIAL_TIMER)+ identifier,TIME_TRIAL_VALUE_2);
        BoardType = preferences.getInt(String.valueOf(BOARD_TYPE)+ identifier,ONE_BOARD);
        RowSize = preferences.getInt(String.valueOf(ROW_SIZE)+ identifier,SIX);
        ColSize = preferences.getInt(String.valueOf(COLUMN_SIZE) + identifier, FOUR);
        ScrollType = preferences.getInt(String.valueOf(SCROLL_TYPE)+ identifier,NO_SCROLL);
        CardSet = preferences.getInt(String.valueOf(CARD_SET)+ identifier,CARD_SET_1);
    }

    private void RandomizeValues() {

        int []AllPlayerModes = {ONE_PLAYER,TWO_PLAYER};
        int []AllPlayerTwoType = {HURRICANE,ROCK,ANDROBOT,RANDOM_BOT};
        int []AllRobotMemory = {1,2,3,4,5,6,7,8,9,10};
        int []AllGameMode = {ARCADE,TIME_TRIAL};
        int []AllTimeTrialTimer = {TIME_TRIAL_VALUE_1,TIME_TRIAL_VALUE_2,TIME_TRIAL_VALUE_3};
        int []AllBoardType = {ONE_BOARD,TWO_BOARD};
        int []AllScrollType = {NO_SCROLL,VERTICAL,HORIZONTAL,BOTH};
        int []AllCardSet = {CARD_SET_1,CARD_SET_2,CARD_SET_3};


        if(PlayerMode==ONE_PLAYER)
            PlayerMode=TWO_PLAYER;
        else
            PlayerMode = AllPlayerModes[(int)(Math.random()*1000)%AllPlayerModes.length];

        PlayerTwoType = AllPlayerTwoType[(int)(Math.random()*1000)%AllPlayerTwoType.length];
        RobotMemoryLevel = AllRobotMemory[(int)(Math.random()*1000)%AllRobotMemory.length];
        GameMode = AllGameMode[(int)(Math.random()*1000)%AllGameMode.length];
        TimeTrialTimer = AllTimeTrialTimer[(int)(Math.random()*1000)%AllTimeTrialTimer.length];
        BoardType = AllBoardType[(int)(Math.random()*1000)%AllBoardType.length];
        ScrollType = AllScrollType[(int)(Math.random()*1000)%AllScrollType.length];
        CardSet = AllCardSet[(int)(Math.random()*1000)%AllCardSet.length];

        //Creating a proportionate board
        int minRowSize,maxRowSize;
        int minColSize,maxColSize;
        switch (ScrollType)
        {
            case NO_SCROLL:
                if(BoardType == ONE_BOARD)
                {
                    minRowSize = 4;
                    maxRowSize = MAX_ROW_SIZE_1B-2;
                    RowSize = (int)(Math.random()*1000)%(maxRowSize-minRowSize+1)+minRowSize;
                    minColSize = RowSize/2;
                }
                else
                {
                    minRowSize = 2;
                    maxRowSize = MAX_ROW_SIZE_2B-1;
                    RowSize = (int)(Math.random()*1000)%(maxRowSize-minRowSize+1)+minRowSize;
                    minColSize = RowSize;
                }
                maxColSize = Math.min(MAX_COL_SIZE-1,RowSize);
                ColSize = ((int)(Math.random()*1000)%(maxColSize-minColSize+1))+minColSize;
                break;
            case HORIZONTAL:
                minColSize = 4;
                if(BoardType == ONE_BOARD)
                {
                    minRowSize =4;
                    maxRowSize = 8;
                }
                else
                {
                    minRowSize = 2;
                    maxRowSize = 4;
                }
                RowSize = ((int)(Math.random()*1000)%(maxRowSize-minRowSize+1))+minRowSize;
                maxColSize = MAX_COL_SIZE;
                ColSize = (int)(Math.random()*1000)%(maxColSize-minColSize+1)+minColSize;
                break;
            case VERTICAL:
                minColSize =2;
                maxColSize = MAX_COL_SIZE-2;
                ColSize = (int)(Math.random()*1000)%(maxColSize-minColSize+1)+minColSize;
                if(BoardType == ONE_BOARD)
                {
                    minRowSize = ColSize+2 + 1;
                    maxRowSize = Math.max(minRowSize,MAX_ROW_SIZE_1B - 1);

                }
                else
                {
                    minRowSize = (ColSize+2)/2 + 1;
                    maxRowSize = Math.max(minRowSize,MAX_ROW_SIZE_2B-1);
                }
                RowSize = ((int)(Math.random()*1000)%(maxRowSize-minRowSize+1))+minRowSize;
                break;
            case BOTH:
                if(BoardType == ONE_BOARD)
                {
                    minRowSize = 7;
                    maxRowSize = MAX_ROW_SIZE_1B-2;
                }
                else
                {
                    minRowSize = 4;
                    maxRowSize = MAX_ROW_SIZE_2B-1;
                }
                minColSize = 5;
                RowSize = ((int)(Math.random()*1000)%(maxRowSize-minRowSize+1))+minRowSize;
                maxColSize = MAX_COL_SIZE;
                ColSize = (int)(Math.random()*1000)%(maxColSize-minColSize+1)+minColSize;
                break;
        }
    }

    private void CleanUp()
    {
        CurrentView = null;
        objCardGame = null;
        CommonDialog = null;
        Process_Input = null;
    }

    public String get_text(int identifier)
    {
        String text = "";
        switch (identifier)
        {
            case ARCADE:
                text = "Arcade";
                break;
            case TIME_TRIAL:
                text = "Time-Trial";
                break;
            case ONE_PLAYER:
                text = "1P";
                break;
            case TWO_PLAYER:
                text = "2P";
                break;
            case ROBOT_PLAYER:
                text = "2P";
                break;
            case MANUAL:
                text = "MANUAL";
                break;
            case ANDROBOT:
                text = "ANDROBOT";
                break;
            case HURRICANE:
                text = "Hurricane";
                break;
            case ROCK:
                text = "Rock";
                break;
            case RANDOM_BOT:
                text = "Random Bot";
                break;
            case ONE_BOARD:
                text = "1 Board";
                break;
            case TWO_BOARD:
                text = "2 Board";
                break;
            case NO_SCROLL:
                text = "No Scroll";
                break;
            case VERTICAL:
                text = "Vertical";
                break;
            case HORIZONTAL:
                text = "Horizontal";
                break;
            case BOTH:
                text = "Both";
                break;
            case CARD_SET_1:
                text = "Card Set - I";
                break;
            case CARD_SET_2:
                text = "Card Set - II";
                break;
            case CARD_SET_3:
                text = "Card Set - III";
                break;
        }

        return text;
    }

    public void ShowUnderConstructionDialog()
    {
        String msg = "This module is under construction";
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(thisContext);
        alertDialog.setTitle("Not implemented");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        // alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }


//region Continue

    //Temp data : for restore
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putInt("Layout", 99);
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        data =  savedInstanceState.getInt("Layout");
    }

    public void setBackgroundToViews(int view_id[],int backgroundColor,int borderColor,int cornerRadius,int borderThickness)
    {
        View v;
        for (int id:view_id)
        {
            v = findViewById(id);
            applyBorderDrawableToView(v, backgroundColor, borderColor, cornerRadius, borderThickness);
        }
    }

    //endregion


    //region recycle

    public void SetBackgroundToViewFromArray(int identifier[],int view_id[],int id,int backgroundColor,int borderColor,
                                             int cornerRadius,int borderThickness)
    {
        for(int i=0;i<identifier.length;i++)
        {
            if(identifier[i]==id)
            {
                applyBorderDrawableToView(findViewById(view_id[i]),backgroundColor,borderColor,
                        cornerRadius,borderThickness);
                break;
            }
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private final int SWIPE_MIN_DISTANCE = 20;
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE ) //&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                GameBackground++;
                InitializeScreenControls_BoardDetails();
                return false; // Right to left
            }
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE ) //&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                GameBackground--;
                InitializeScreenControls_BoardDetails();
                return false; // Left to right
            }

            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE)// && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            {
                return false; // Bottom to top
            }
            else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE)// && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY)
            {
                return false; // Top to bottom
            }
            return true;
        }

//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//                                float distanceY) {
//            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE*10 ) //&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
//            {
//                GameBackground++;
//                InitializeScreenControls_BoardDetails();
//                return false; // Right to left
//            }
//            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE*10 ) //&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
//            {
//                GameBackground--;
//                InitializeScreenControls_BoardDetails();
//                return false; // Left to right
//            }
//            return true;
//        }
    }

    //endregion

}

