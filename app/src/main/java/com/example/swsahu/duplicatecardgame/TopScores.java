package com.example.swsahu.duplicatecardgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import static com.example.swsahu.duplicatecardgame.HelperClass.DELIMITER_2;
import static com.example.swsahu.duplicatecardgame.HelperClass.GAME_MODE;
import static com.example.swsahu.duplicatecardgame.HelperClass.HORIZONTAL;
import static com.example.swsahu.duplicatecardgame.HelperClass.HURRICANE;
import static com.example.swsahu.duplicatecardgame.HelperClass.MANUAL;
import static com.example.swsahu.duplicatecardgame.HelperClass.MAX_COL_SIZE;
import static com.example.swsahu.duplicatecardgame.HelperClass.MAX_ROW_SIZE_1B;
import static com.example.swsahu.duplicatecardgame.HelperClass.MAX_ROW_SIZE_2B;
import static com.example.swsahu.duplicatecardgame.HelperClass.NO_SCROLL;
import static com.example.swsahu.duplicatecardgame.HelperClass.ONE_BOARD;
import static com.example.swsahu.duplicatecardgame.HelperClass.ONE_PLAYER;
import static com.example.swsahu.duplicatecardgame.HelperClass.PLAYER_MODE;
import static com.example.swsahu.duplicatecardgame.HelperClass.RANDOM_BOT;
import static com.example.swsahu.duplicatecardgame.HelperClass.ROBOT_MEMORY;
import static com.example.swsahu.duplicatecardgame.HelperClass.ROBOT_PLAYER;
import static com.example.swsahu.duplicatecardgame.HelperClass.ROCK;
import static com.example.swsahu.duplicatecardgame.HelperClass.ROW_SIZE;
import static com.example.swsahu.duplicatecardgame.HelperClass.SCROLL_TYPE;
import static com.example.swsahu.duplicatecardgame.HelperClass.SetFontToControls;
import static com.example.swsahu.duplicatecardgame.HelperClass.TIME_TRIAL;
import static com.example.swsahu.duplicatecardgame.HelperClass.TIME_TRIAL_TIMER;
import static com.example.swsahu.duplicatecardgame.HelperClass.TWO_BOARD;
import static com.example.swsahu.duplicatecardgame.HelperClass.TWO_PLAYER;
import static com.example.swsahu.duplicatecardgame.HelperClass.VERTICAL;

public class TopScores implements View.OnClickListener {

    Game CurrentGame;
    MainActivity mContext;
    boolean isFromGameScreen;

    AlertDialog CommonDialog;
    View.OnClickListener Process_Input;

    String defaultPlayerNames[] = { "Andro-Bot","Hurricane","Rock","Rock","Hurricane" };
    int defaultScores[] = new int[5];
    TextView PlayerNames[] = new TextView[5];
    TextView PlayerScore[] = new TextView[5];

    //Selected Values
    int GameMode;
    int TimeTrialTimerValue;
    int PlayerMode;
    int PlayerType;
    int RobotMemory;
    int BoardType;
    int ScrollType;
    int CardSet;
    int RowSize;
    int ColumnSize;

    final int SUMMARY_SCREEN = 3;
    final int TOP_SCORES = 0;
    final int BEST_TIME = 1;
    final int LEAST_MOVES = 2;

    int [] SCREENS = {TOP_SCORES,BEST_TIME,LEAST_MOVES,SUMMARY_SCREEN};
    int current_screen_index = TOP_SCORES;

    public TopScores(WeakReference<Game> currentGame,boolean is_fromGameScreen)
    {
        isFromGameScreen = is_fromGameScreen;
        CurrentGame = currentGame.get();
        mContext = CurrentGame.mContext;
        InitializeDialogInputListener();
    }
    public TopScores(WeakReference<MainActivity> m_context)
    {
        isFromGameScreen = false;
        mContext = m_context.get();
        InitializeDialogInputListener();
    }
    public void InitializeBoardDetails(int gameMode,int playerMode,int playerType,
                                       int robotMemory, int boardType,int scrollType,
                                       int cardSet,int rowSize,int columnSize,
                                       int timeTrialTimerValue)
    {
        if(isFromGameScreen && boardType==TWO_BOARD)
            rowSize/=2;

        GameMode = gameMode;
        PlayerMode = playerMode;
        PlayerType = playerType;
        RobotMemory = robotMemory;
        BoardType = boardType;
        ScrollType = scrollType;
        CardSet = cardSet;
        RowSize = rowSize;
        ColumnSize = columnSize;
        TimeTrialTimerValue = timeTrialTimerValue;
    }

    public void Show( )
    {
        View this_view = mContext.loadView(R.layout.screen_top_score);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/hurry up.ttf");
        SetFontToControls(font, (ViewGroup) this_view);

        addListenerToControls();
        InitializeRobotMemoryListener();

        setBoardDetailsText();
        if(!isFromGameScreen)
        {
            initializeSpecificControls_Set2();
        }


        addFlingListenerToTopScoresScreen(this_view);
        loadPage();
    }


    //region Listeners

    private void initializeSpecificControls_Set2()
    {
        View btnBack = (mContext.findViewById(R.id.btnBack));
        View btn_back = (mContext.findViewById(R.id.btn_back));

        mContext.findViewById(R.id.btnExit).setVisibility(View.INVISIBLE);
        mContext.findViewById(R.id.btnStore).setVisibility(View.INVISIBLE);

        btn_back.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void addListenerToControls()
    {

        Button btn_prev_page = (Button)mContext.findViewById(R.id.btn_prev_page);
        Button btn_next_page = (Button)mContext.findViewById(R.id.btn_next_page);

        //buttons
        Button GameMode = (Button)mContext.findViewById(R.id.GameMode);
        Button PlayerMode = (Button)mContext.findViewById(R.id.PlayerMode);
        Button BoardType = (Button)mContext.findViewById(R.id.BoardType);
        Button ScrollType = (Button)mContext.findViewById(R.id.ScrollType);
        Button CardSet = (Button)mContext.findViewById(R.id.CardSet);
        TextView RowSize = (TextView)mContext.findViewById(R.id.RowSize);
        TextView ColSize = (TextView)mContext.findViewById(R.id.ColSize);

        //Edit buttons
        Button btnGameMode = (Button)mContext.findViewById(R.id.btnGameMode);
        Button btnPlayerMode = (Button)mContext.findViewById(R.id.btnPlayerMode);
        Button btnBoardType = (Button)mContext.findViewById(R.id.btnBoardType);
        Button btnScrollType = (Button)mContext.findViewById(R.id.btnScrollType);
        Button btnCardSet = (Button)mContext.findViewById(R.id.btnCardSet);
        TextView btnBoardSize = (TextView)mContext.findViewById(R.id.btnBoardSize);

        Button btnLoadScores = (Button)mContext.findViewById(R.id.btnLoadScores);
        TextView btnResetScores = (TextView)mContext.findViewById(R.id.btnResetScores);

        Button btnStore = (Button)mContext.findViewById(R.id.btnStore);
        Button btnExit = (Button)mContext.findViewById(R.id.btnExit);

        btnLoadScores.setOnClickListener(this);
        btnResetScores.setOnClickListener(this);
        btn_next_page.setOnClickListener(this);
        btn_prev_page.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnStore.setOnClickListener(this);
        //buttons
        GameMode.setOnClickListener(this);
        PlayerMode.setOnClickListener(this);
        BoardType.setOnClickListener(this);
        CardSet.setOnClickListener(this);
        ScrollType.setOnClickListener(this);
        RowSize.setOnClickListener(this);
        ColSize.setOnClickListener(this);

        //Edit buttons
        btnGameMode.setOnClickListener(this);
        btnPlayerMode.setOnClickListener(this);
        btnBoardType.setOnClickListener(this);
        btnCardSet.setOnClickListener(this);
        btnScrollType.setOnClickListener(this);
        btnBoardSize.setOnClickListener(this);
    }
    private void InitializeRobotMemoryListener()
    {
        final SeekBar robotMemory = (SeekBar)mContext.findViewById(R.id.RobotMemory);
        robotMemory.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                RobotMemory = seekBar.getProgress() + 1;
            }
        });
    }

    private void addFlingListenerToTopScoresScreen(View v)
    {
        final GestureDetector gdt = new GestureDetector(mContext,new GestureListener());
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnBack:
            case R.id.btn_back:
            case R.id.btnExit:
            case R.id.btn_exit:
                mContext.loadView(R.layout.screen_home);
                CurrentGame.CleanUp();
                break;
            case R.id.btn_prev_page:
                decrementScreenIndex();
                break;
            case R.id.btn_next_page:
                incrementScreenIndex();
                break;
            case R.id.btnStore:
                mContext.loadStoreScreen();
                CurrentGame.CleanUp();
                break;
            case R.id.btnGameMode:
            case R.id.GameMode:
                displayDialog(getGameModes(), true);
                break;
            case R.id.btnPlayerMode:
            case R.id.PlayerMode:
                displayDialog(getPlayerModes(),true);
                break;
            case R.id.btnBoardType:
            case R.id.BoardType:
                displayDialog(getBoardType(),true);
                break;
            case R.id.btnBoardSize:
            case R.id.RowSize:
            case R.id.ColSize:
                displayDialog(getRowSize(),true);
                break;
            case R.id.btnScrollType:
            case R.id.ScrollType:
                displayDialog(getScrollType(),true);
                break;
            case R.id.btnCardSet:
            case R.id.CardSet:
                displayDialog(getCardSet(),true);
                break;
            case R.id.btnLoadScores:
                loadPage();
                break;
            case R.id.btnResetScores:
                resetScores();
                break;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private final int SWIPE_MIN_DISTANCE = 40;
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE ) //&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                incrementScreenIndex();
                return false; //Right to left
            }
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE ) //&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                decrementScreenIndex();
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
    }

    //endregion


    //region dialog logic

    private void InitializeDialogInputListener() {
        Process_Input = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyValue = v.getTag().toString();
                int key = Integer.parseInt(keyValue.split(DELIMITER_2)[0]);
                int value = Integer.parseInt(keyValue.split(DELIMITER_2)[1]);
                switch (key)
                {
                    case GAME_MODE:
                        GameMode = value;
                        if(GameMode == TIME_TRIAL)
                        {
                            CommonDialog.dismiss();
                            displayDialog(getTimeTrialTime(),false);
                            return;
                        }
                        break;

                    case TIME_TRIAL_TIMER:
                        TimeTrialTimerValue = value;
                        break;
                    case CARD_SET:
                        CardSet = value;
                        break;

                    case PLAYER_MODE:
                        switch (value)
                        {
                            case 0:
                                PlayerMode = ONE_PLAYER;
                                PlayerType = 0;
                                break;
                            case 1:
                                PlayerMode = TWO_PLAYER;
                                PlayerType = MANUAL;
                                break;
                            case 2:
                                PlayerMode = ROBOT_PLAYER;
                                PlayerType = ANDROBOT;
                                break;
                            case 3:
                                PlayerMode = ROBOT_PLAYER;
                                PlayerType = HURRICANE;
                                break;
                            case 4:
                                PlayerMode = ROBOT_PLAYER;
                                PlayerType = ROCK;
                                break;
                            case 5:
                                PlayerMode = ROBOT_PLAYER;
                                PlayerType = RANDOM_BOT;
                                break;
                        }
                        if(PlayerMode!=ONE_PLAYER)
                            mContext.findViewById(R.id.tvMsg).setVisibility(View.VISIBLE);
                        else
                            mContext.findViewById(R.id.tvMsg).setVisibility(View.GONE);
                        break;
                    case BOARD_TYPE:
                        BoardType = value;
                        switch (BoardType)
                        {
                            case ONE_BOARD:
                                RowSize = Math.max(ColumnSize, RowSize);
                                break;
                            case TWO_BOARD:
                                RowSize = Math.min(RowSize,MAX_ROW_SIZE_2B);
                                break;
                        }
                        break;

                    case ROW_SIZE:
                        RowSize = value;
                        CommonDialog.dismiss();
                        displayDialog(getColSize(),false);
                        return;
                    case COLUMN_SIZE:
                        ColumnSize = value;
                        break;
                    case SCROLL_TYPE:
                        ScrollType = value;
                        break;

                    default:
                        Toast.makeText(mContext,"Didn't match any key. Debug !!",Toast.LENGTH_SHORT);
                }
                CommonDialog.dismiss();
                setBoardDetailsText();
            }
        };
    }


    private void displayDialog(View v,boolean isCancelable)
    {
        CommonDialog  = new AlertDialog.Builder(mContext).show();
        CommonDialog.setContentView(v);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/hurry up.ttf");
        SetFontToControls(font, (ViewGroup) v);
        CommonDialog.setCancelable(isCancelable);
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
        String text[] =  {"1P", "2P - Manual","2P - AndroBot","2P - Hurricane","2P - Rocky","2P - Random-Bot"};
        int tag [] =  {0, 1,2,3,4,5};
        return addToMainContainer(tag,text,String.valueOf(PLAYER_MODE),tag.length,"Select Player Mode");
    }
    private View getRobotMemory()
    {
        String text[] =  {"1","2","3","4","5","6","7","8","9","10"};
        int tag [] =  {1,2,3,4,5,6,7,8,9,10};
        return addToMainContainer(tag, text, String.valueOf(ROBOT_MEMORY), tag.length, "Select Robot Memory");
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
        return addToBoardSizeContainer(tag, text, String.valueOf(COLUMN_SIZE), i - 2, "Select Column Size");
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
        return addToMainContainer(tag, text, String.valueOf(CARD_SET), tag.length, "Select Card Set");
    }

    private View addToMainContainer(int[] tag, String[] text,String identifier,int length,String titleText) {
        LinearLayout mainContainer = new LinearLayout(mContext);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.addView(getTitleTextView(titleText));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ConvertToPx(mContext,190),
                ConvertToPx(mContext, 45));
        RelativeLayout.LayoutParams rl_params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                1);
        for (int i=0;i<length;i++)
        {
            TextView tv = new Button(mContext);
            tv.setText(text[i]);
            String tvTag = identifier + DELIMITER_2 + String.valueOf(tag[i]);
            tv.setTag(tvTag);
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(Color.argb(180, 255, 255, 255));
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(0,ConvertToPx(mContext,3),0,0);
            tv.setLayoutParams(layoutParams);
            mainContainer.addView(tv);
            tv.setOnClickListener(Process_Input);

            mainContainer.addView(getDivider(rl_params));
        }
        return mainContainer;
    }

    private View addToBoardSizeContainer(int[] tag, String[] text,String identifier,int length,String titleText) {
        LinearLayout row = null;
        LinearLayout mainContainer = new LinearLayout(mContext);
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        mainContainer.addView(getTitleTextView(titleText));
        int i=0;
        LinearLayout.LayoutParams row_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams box_Params = new LinearLayout.LayoutParams(ConvertToPx(mContext,72),
                ConvertToPx(mContext,40));
        RelativeLayout.LayoutParams verticalDivider_params= new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1);
        RelativeLayout.LayoutParams horizontalDivider_params= new RelativeLayout.LayoutParams(1,
                ViewGroup.LayoutParams.MATCH_PARENT);
        while(i<length)
        {
            row = new LinearLayout(mContext);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(row_Params);
            mainContainer.addView(row);
            mainContainer.addView(getDivider(verticalDivider_params));
            for(int col=0;col<3 && i<length;col++,i++)
            {
                Button tv = new Button(mContext);
                tv.setText(text[i]);
                String tvTag = identifier + DELIMITER_2 + String.valueOf(tag[i]);
                tv.setTag(tvTag);
                tv.setTextColor(Color.BLACK);
                tv.setPadding(0, ConvertToPx(mContext, 3), 0, 0);
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
            Button tv = new Button(mContext);
            tv.setLayoutParams(box_Params);
            tv.setPadding(0, ConvertToPx(mContext, 3), 0, 0);
            tv.setBackgroundColor(Color.argb(180, 255, 255, 255));
            row.addView(tv);
            row.addView(getDivider(horizontalDivider_params));
            i++;
        }

        return mainContainer;
    }

    private View getDivider(RelativeLayout.LayoutParams rl_params)
    {
        RelativeLayout divider = new RelativeLayout(mContext);
        divider.setLayoutParams(rl_params);
        divider.setBackgroundColor(Color.BLACK);
        return divider;
    }

    private View getTitleTextView(String titleText)
    {
        int five_dip = ConvertToPx(mContext, 5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tvTitle = new TextView(mContext);
        tvTitle.setText(titleText);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setBackgroundColor(Color.BLACK);
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setLayoutParams(layoutParams);
        tvTitle.setPadding(4 * five_dip, 2 * five_dip, 4 * five_dip, 2 * five_dip);
        return tvTitle;
    }

//endregion area


    public void loadSummaryScreen()
    {
        if(CurrentGame.objGameSummary == null)
            CurrentGame.objGameSummary = new GameSummary(new WeakReference<>(CurrentGame),
                    CurrentGame.CurrentCard.getMeasuredHeight(),CurrentGame.CurrentCard.getMeasuredWidth());
        CurrentGame.objGameSummary.loadSummaryScreen();
    }

    private void loadTopScoresScreen() {
        ((TextView)mContext.findViewById(R.id.tvTitle)).setText("Top Scores");
        ((TextView)mContext.findViewById(R.id.tvSubTitle)).setText("Top Scores");
        String text = LoadScores();
        ((TextView) mContext.findViewById(R.id.tvUserScore)).setText("Your top score   =   " + text);

        mContext.findViewById(R.id.tvMsg).setVisibility(View.GONE);
    }

    private void loadBestTimeScreen() {
        ((TextView)mContext.findViewById(R.id.tvTitle)).setText("Best Time");
        ((TextView)mContext.findViewById(R.id.tvSubTitle)).setText("Best Time");
        String text = LoadScores();
        if(text.equals("99999999999"))
            ((TextView) mContext.findViewById(R.id.tvUserScore)).setText(" ");
        else
            ((TextView) mContext.findViewById(R.id.tvUserScore)).setText("Your best time   =   " + text);

        if(PlayerMode!=ONE_PLAYER)
            mContext.findViewById(R.id.tvMsg).setVisibility(View.VISIBLE);
        else
            mContext.findViewById(R.id.tvMsg).setVisibility(View.GONE);
    }

    private void loadLeastMovesScreen() {
        ((TextView)mContext.findViewById(R.id.tvTitle)).setText("Least Moves");
        ((TextView)mContext.findViewById(R.id.tvSubTitle)).setText("Least Moves");
        String text = LoadScores();
        if(text.equals("99999999999"))
            ((TextView) mContext.findViewById(R.id.tvUserScore)).setText(" ");
        else
            ((TextView) mContext.findViewById(R.id.tvUserScore)).setText("Your Least moves   =   " + text);

        if(PlayerMode!=ONE_PLAYER)
            mContext.findViewById(R.id.tvMsg).setVisibility(View.VISIBLE);
        else
            mContext.findViewById(R.id.tvMsg).setVisibility(View.GONE);
    }

    private void incrementScreenIndex()
    {

        current_screen_index++;
        if(current_screen_index>=SCREENS.length)
        {
            current_screen_index=0;
        }
        else if(SCREENS[current_screen_index] == SUMMARY_SCREEN && !isFromGameScreen)
        {
            current_screen_index = 0;
        }

        loadPage();
    }

    private void decrementScreenIndex()
    {

        current_screen_index--;
        if(current_screen_index<0)
        {
            current_screen_index=SCREENS.length-1;
            if(!isFromGameScreen)
                current_screen_index--;
        }

        loadPage();
    }


    private void loadPage()
    {
        switch (SCREENS[current_screen_index])
        {
            case SUMMARY_SCREEN:
                loadSummaryScreen();
                break;
            case TOP_SCORES:
                loadTopScoresScreen();
                break;
            case BEST_TIME:
                loadBestTimeScreen();
                break;
            case LEAST_MOVES:
                loadLeastMovesScreen();
                break;
        }
    }


    public void mergeScores(String targetNameList[],long targetScoreList[],long userScores[])
    {
        int sortLogic = -1;
        if(SCREENS[current_screen_index] == TOP_SCORES)
            sortLogic*=-1;

        int defList_counter=0;
        int userList_counter=0;
        for(int i=0;i<5;i++)
        {
            if(defaultScores[defList_counter] * sortLogic >= userScores[userList_counter] * sortLogic)
            {
                targetNameList[i] = defaultPlayerNames[defList_counter];
                targetScoreList[i] = defaultScores[defList_counter++];
            }
            else
            {
                targetNameList[i] = "YOU";
                targetScoreList[i] = userScores[userList_counter++];
            }
        }
    }

    public String LoadScores()
    {
        createDefaultScores();
        PlayerNames[0] = (TextView) mContext.findViewById(R.id.tvPlayerName_1);
        PlayerNames[1] = (TextView) mContext.findViewById(R.id.tvPlayerName_2);
        PlayerNames[2] = (TextView) mContext.findViewById(R.id.tvPlayerName_3);
        PlayerNames[3] = (TextView) mContext.findViewById(R.id.tvPlayerName_4);
        PlayerNames[4] = (TextView) mContext.findViewById(R.id.tvPlayerName_5);

        PlayerScore[0] = (TextView) mContext.findViewById(R.id.tvPlayerScore_1);
        PlayerScore[1] = (TextView) mContext.findViewById(R.id.tvPlayerScore_2);
        PlayerScore[2] = (TextView) mContext.findViewById(R.id.tvPlayerScore_3);
        PlayerScore[3] = (TextView) mContext.findViewById(R.id.tvPlayerScore_4);
        PlayerScore[4] = (TextView) mContext.findViewById(R.id.tvPlayerScore_5);



        String playerNames[] = new String[5];

        long highScores [] = new long[5];
        long userHighScores[] = getHighScoresFromPreferences( );
        mergeScores(playerNames, highScores, userHighScores);
        String userHighScore = String.valueOf(userHighScores[0]);
        for (int i=0;i<5;i++)
        {
            PlayerNames[i].setText(String.valueOf(playerNames[i]));
            PlayerScore[i].setText(String.valueOf(highScores[i]));
        }
        return userHighScore;
    }

    public void createDefaultScores()
    {
        int totalCards = RowSize*ColumnSize;
        if(BoardType == TWO_BOARD)
            totalCards*=2;
        totalCards=totalCards/2*2;

        int maxScore,minScore,interval;
        switch (SCREENS[current_screen_index])
        {
            case TOP_SCORES:
                maxScore = totalCards * 7 * 3 ;
                minScore = totalCards * 5;
                interval = (maxScore-minScore)/4;
                break;
            case BEST_TIME:
                maxScore = (int)(totalCards * 1.6);
                minScore = (int)(maxScore * 3);
                interval = (maxScore-minScore)/4;
                break;
            case LEAST_MOVES:
                maxScore = (int)(totalCards * 0.7);
                minScore = (int)(maxScore * 2.7);
                interval = (maxScore-minScore)/4;
                break;
            default:
                maxScore = totalCards * 7 * 3 ;
                minScore = totalCards * 5;
                interval = (maxScore-minScore)/4;
        }


        defaultScores[0] = maxScore;
        for(int i = 1 ;i<5;i++)
        {
            defaultScores[i]=defaultScores[i-1]-interval;
        }
    }

    public long[] getHighScoresFromPreferences( )
    {
        String identifier = getBoardIdentifier();
        long []high_scores = new long[5];

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String defTopScore = "0~0~0~0~0";
        String temp = "99999999999";
        String defWorstTimes = "";
        for(int i = 0;i<5;i++)
            defWorstTimes+=temp+DELIMITER_2;
        String defMaxMoves = defWorstTimes;
        String scoring_data = preferences.getString(identifier, defTopScore+DELIMITER+defWorstTimes+DELIMITER+defMaxMoves);

        String highScores_arr[] = scoring_data.split(DELIMITER)[SCREENS[current_screen_index]].split(DELIMITER_2);
        for(int i=0;i<5;i++)
        {
            high_scores[i] = Long.parseLong(highScores_arr[i]);
        }
        return high_scores;
    }

    private void resetScores() {
        String msg = "Are you sure?\nSelect 'Yes' to continue.\nSelect 'No' to cancel.";
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Reset ?");
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        // alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String identifier_scoreData = getBoardIdentifier();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

                String defTopScore = "0~0~0~0~0";
                String temp = "99999999999";
                String defWorstTimes = "";
                for(int i = 0;i<5;i++)
                    defWorstTimes+=temp+DELIMITER_2;
                String defMaxMoves = defWorstTimes;
                String scoreData = preferences.getString(identifier_scoreData, defTopScore +
                        DELIMITER + defWorstTimes + DELIMITER + defMaxMoves);

                String allTopScores = scoreData.split(DELIMITER)[TOP_SCORES];
                String allBestTime = scoreData.split(DELIMITER)[BEST_TIME];
                String allLeastMove = scoreData.split(DELIMITER)[LEAST_MOVES];
                switch (SCREENS[current_screen_index])
                {
                    case TOP_SCORES:
                        allTopScores = "0~0~0~0~0";
                        break;
                    case BEST_TIME:
                        allBestTime = defWorstTimes;
                        break;
                    case LEAST_MOVES:
                        allLeastMove = defMaxMoves;
                        break;
                }

                scoreData = allTopScores+DELIMITER+allBestTime+DELIMITER+allLeastMove;
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(identifier_scoreData);
                editor.putString(identifier_scoreData, scoreData);
                editor.apply();

                ((TextView) mContext.findViewById(R.id.tvUserScore)).setText("");
                LoadScores();
                setBoardDetailsText();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    public void setBoardDetailsText()
    {
        String gameMode = getText(GameMode);
        if(GameMode == TIME_TRIAL)
        {
            gameMode+= " (" + String.valueOf(TimeTrialTimerValue/1000) + ")";
        }
        String playerType =getText(PlayerMode);
        if(PlayerMode != ONE_PLAYER)
        {
            playerType += " : " + getText(PlayerType);
        }
        String boardType = getText(BoardType);
        String scrollType = getText(ScrollType);
        String cardSet = getText(CardSet);
        String rowSize = String.valueOf(RowSize);
        String colSize = String.valueOf(ColumnSize);

        ((TextView) mContext.findViewById(R.id.GameMode)).setText(gameMode);
        ((TextView) mContext.findViewById(R.id.PlayerMode)).setText(playerType);
        ((TextView) mContext.findViewById(R.id.BoardType)).setText(boardType);
        ((TextView) mContext.findViewById(R.id.ScrollType)).setText(scrollType);
        ((TextView) mContext.findViewById(R.id.CardSet)).setText(cardSet);
        ((TextView) mContext.findViewById(R.id.RowSize)).setText(rowSize);
        ((TextView) mContext.findViewById(R.id.ColSize)).setText(colSize);
        SeekBar roboMemory = ((SeekBar) mContext.findViewById(R.id.RobotMemory));

        if(PlayerMode == TWO_PLAYER && PlayerType!= MANUAL)
            PlayerMode = ROBOT_PLAYER;

        if(PlayerMode != ROBOT_PLAYER)
            roboMemory.setEnabled(false);
        else
        {
            roboMemory.setEnabled(true);
            roboMemory.setProgress(RobotMemory - 1);
        }


    }

    public String getBoardIdentifier()
    {
        String identifier;
        int boardSize = ColumnSize*RowSize;
        if(BoardType == TWO_BOARD)
            boardSize*=2;
        identifier = String.valueOf(GameMode) + DELIMITER_2 +
                String.valueOf(PlayerMode) + DELIMITER_2 +
                String.valueOf(BoardType) + DELIMITER_2 +
                String.valueOf(CardSet) + DELIMITER_2 +
                String.valueOf(ScrollType) + DELIMITER_2 +
                String.valueOf(boardSize);

        if(GameMode == TIME_TRIAL)
            identifier+= DELIMITER_2 + String.valueOf(TimeTrialTimerValue);

        if(PlayerMode != ONE_PLAYER)
        {
            identifier+= String.valueOf(PlayerType);

            if(PlayerMode == ROBOT_PLAYER)
                identifier+= DELIMITER_2 + String.valueOf(RobotMemory);
        }

        return identifier;
    }

    public String getText(int identifier)
    {
        String text = "";
        switch (identifier)
        {
            case ARCADE:
                text = "ARCADE";
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

}
