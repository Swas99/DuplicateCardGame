package com.example.swsahu.duplicatecardgame;


import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

import static com.example.swsahu.duplicatecardgame.HelperClass.ConvertToPx;
import static com.example.swsahu.duplicatecardgame.HelperClass.OneBoard_BothScroll;
import static com.example.swsahu.duplicatecardgame.HelperClass.OneBoard_HorizontalScroll;
import static com.example.swsahu.duplicatecardgame.HelperClass.OneBoard_VerticalScroll;
import static com.example.swsahu.duplicatecardgame.HelperClass.OneBoard_WithoutScroll;
import static com.example.swsahu.duplicatecardgame.HelperClass.SetFontToControls;
import static com.example.swsahu.duplicatecardgame.HelperClass.TwoBoard_BothScroll;
import static com.example.swsahu.duplicatecardgame.HelperClass.TwoBoard_HorizontalScroll;
import static com.example.swsahu.duplicatecardgame.HelperClass.TwoBoard_VerticalScroll;
import static com.example.swsahu.duplicatecardgame.HelperClass.TwoBoard_WithoutScroll;
import static com.example.swsahu.duplicatecardgame.HelperClass.applyBorderDrawableToView;
import static com.example.swsahu.duplicatecardgame.HelperClass.createDrawableBackground;
import static com.example.swsahu.duplicatecardgame.HelperClass.getWindowSize;

public class SummaryMatrix {

    Game CurrentGame;
    int [][]ColorMatrix;
    int Colors[];
    int Colors_Top;
    final int CLICK_COUNT = 619;
    final int MOVE_TRACE = 609;
    final int RETAINING_POWER = 909;
    private int MatrixType;
    int cell_height;
    int cell_width;


    public SummaryMatrix(WeakReference<Game> currentGame,int cellHeight,int cellWidth)
    {
        CurrentGame = currentGame.get();
        CreateColorMatrix();

        //Generate cell dimension
        cell_height = cellHeight-ConvertToPx(CurrentGame.mContext,10)-cellHeight/4;
        cell_width  = cellWidth-ConvertToPx(CurrentGame.mContext,10)-cellWidth/4;
        if(cell_height<cell_width)
            cell_width=cell_height;
        else
            cell_height=cell_width;

    }

    public void SetMatrixType(int matrixType)
    {
        MatrixType = matrixType;
    }

    public void CreateMatrix()
    {
        View clickMatrix;
        switch (CurrentGame.BoardIdentifier)
        {
            case OneBoard_WithoutScroll:
                clickMatrix = getClickMatrix_OneBoardWithoutScroll_Game();
                break;
            case TwoBoard_WithoutScroll:
                clickMatrix = getClickMatrix_TwoBoardWithoutScroll_Game();
                break;
            case OneBoard_HorizontalScroll:
                clickMatrix = getClickMatrix_OneBoardHorizontalScroll_Game();
                break;
            case OneBoard_VerticalScroll:
                clickMatrix = getClickMatrix_OneBoardVerticalScroll_Game();
                break;
            case OneBoard_BothScroll:
                clickMatrix = getClickMatrix_OneBoardBothScroll_Game();
                break;
            case TwoBoard_HorizontalScroll:
                clickMatrix = getClickMatrix_TwoBoardHorizontalScroll_Game();
                break;
            case TwoBoard_VerticalScroll:
                clickMatrix
                        = getClickMatrix_TwoBoardVerticalScroll_Game();
                break;
            case TwoBoard_BothScroll:
                clickMatrix = getClickMatrix_TwoBoardBothScroll_Game();
                break;
            default:
                clickMatrix = getClickMatrix_OneBoardWithoutScroll_Game();
        }

        loadMatrixToScreen(clickMatrix);
    }

    private void loadMatrixToScreen(View clickMatrix)
    {
        LayoutInflater inflater=CurrentGame.mContext.getLayoutInflater();
        View view=inflater.inflate(R.layout.screen_matrix_summary, null, false);
        Typeface font = Typeface.createFromAsset(CurrentGame.mContext.getAssets(), "fonts/hurry up.ttf");
        SetFontToControls(font,(ViewGroup)view);

        if(CurrentGame.mContext.CurrentView != null)
            CurrentGame.mContext.CurrentView.startAnimation(
                    AnimationUtils.loadAnimation(CurrentGame.mContext, android.R.anim.fade_out));
        CurrentGame.mContext.setContentView(view);
        LinearLayout  main_layout = (LinearLayout)CurrentGame.mContext.findViewById(R.id.clickMatrix);
        LinearLayout.LayoutParams lParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f);
        clickMatrix.setLayoutParams(lParam1);
        clickMatrix.setBackgroundResource(CurrentGame.GameBackground);
        ((LinearLayout)main_layout.findViewById(R.id.Matrix)).addView(clickMatrix);
        view.startAnimation(AnimationUtils.loadAnimation(CurrentGame.mContext, android.R.anim.fade_in));
        CurrentGame.mContext.CurrentView = view;

        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        TextView tvDesc = (TextView)view.findViewById(R.id.tvDescription);
        switch (MatrixType)
        {
            case CLICK_COUNT :
                tvTitle.setText("Click-Count");
                tvDesc.setText("Each cell value represents the number of times it was clicked during game play");
                break;
            case MOVE_TRACE:
                tvTitle.setText("Move-Trace");
                tvDesc.setText("Each cell value represents the serial number of last click made on that cell during game play");
                break;
            case RETAINING_POWER :
                tvTitle.setText("Retaining power");
                tvDesc.setText("For each card pair, the higher value is the move-number at the time of hit & lower value is the previous move-number");
                break;
        }
        tvDesc.setTypeface(Typeface.DEFAULT);

        Button btn_back = (Button)view.findViewById(R.id.btnBack);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentGame.objGameSummary == null)
                  CurrentGame.objGameSummary = new GameSummary(new WeakReference<>(CurrentGame),
                        CurrentGame.CurrentCard.getMeasuredHeight(),CurrentGame.CurrentCard.getMeasuredWidth());

                CurrentGame.objGameSummary.loadSummaryScreen();
            }
        });
    }
    private void CreateColorList(int n)
    {
        float i = (float)Math.random() * 10f;
        while (Colors_Top<n)
        {
            float hue = i;
            float saturation = (float) Math.random();
            float value = (float)Math.random();
            if(value<.5f)
                value+=.5f;

            int c = Color.HSVToColor(new float[]{hue, saturation, value});

            Colors[Colors_Top++]=c;
            i += 360/n;
        }
    }
    private void CreateColorMatrix()
    {
        int size = (CurrentGame.TotalCardsOnBoard)/2 +1;
        Colors = new int[size];
        ColorMatrix = new int[CurrentGame.RowSize][CurrentGame.ColumnSize];

        CreateColorList(size);
        Colors_Top = 0;

        SparseArray<Integer> imgRes_ColorMap = new SparseArray<>();
        //Assign different colors to different image resource
        for(int i=0; i<CurrentGame.RowSize;i++)
            for (int j = 0; j < CurrentGame.ColumnSize; j++)
                if (imgRes_ColorMap.indexOfKey(CurrentGame.Cards_ImageResID[i][j]) < 0)
                    imgRes_ColorMap.put(CurrentGame.Cards_ImageResID[i][j], Colors[Colors_Top++]);

        //Create Color-Matrix
        for(int i=0; i<CurrentGame.RowSize;i++)
            for (int j = 0; j < CurrentGame.ColumnSize; j++)
                ColorMatrix[i][j] = imgRes_ColorMap.get(CurrentGame.Cards_ImageResID[i][j]);
    }

    private RelativeLayout getCell(int value,int color)
    {
        RelativeLayout cell = new RelativeLayout(CurrentGame.mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1f);
        cell.setLayoutParams(layoutParams);

        TextView tv_cardClick = new TextView(CurrentGame.mContext);
        RelativeLayout.LayoutParams layoutParams_r = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams_r.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        Typeface font = Typeface.createFromAsset(CurrentGame.mContext.getAssets(), "fonts/hurry up.ttf");
        tv_cardClick.setLayoutParams(layoutParams_r);
        tv_cardClick.setTypeface(Typeface.create(font, Typeface.BOLD));
        tv_cardClick.setLayoutParams(layoutParams_r);
        tv_cardClick.setTextSize(cell_height / 2);
        tv_cardClick.setText(String.valueOf(value));
        tv_cardClick.setGravity(Gravity.CENTER);
        tv_cardClick.setHeight(cell_height);
        tv_cardClick.setWidth(cell_width);
        tv_cardClick.setTextColor(Color.BLACK);


        applyBorderDrawableToView(tv_cardClick, color, color, 2, 2);
        cell.addView(tv_cardClick);

        return cell;
    }

    private LinearLayout CreateBoardSet(int row_adjustment,int col_adjustment,
                                        LinearLayout.LayoutParams decidingLayoutParam,
                                        int rowSize,int colSize)
    {
        final LinearLayout  simple_game = new LinearLayout(CurrentGame.mContext);
        //simple_game.setBackgroundResource(R.drawable.border);
        int pad_px = ConvertToPx(CurrentGame.mContext, 1);
        simple_game.setPadding(pad_px, pad_px, pad_px, pad_px);
        simple_game.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        simple_game.setLayoutParams(lParam);
        for(int i=0;i<rowSize;i++)
        {
            LinearLayout l_col = new LinearLayout(CurrentGame.mContext);
            simple_game.addView(l_col);
            l_col.setOrientation(LinearLayout.HORIZONTAL);
            decidingLayoutParam.gravity = Gravity.CENTER;
            l_col.setLayoutParams(decidingLayoutParam);

             for(int j=0;j<colSize;j++)
            {
                switch (MatrixType)
                {
                    case CLICK_COUNT :
                        l_col.addView(getCell(CurrentGame.Card_Clicks[i + row_adjustment][j + col_adjustment],
                                ColorMatrix[i + row_adjustment][j + col_adjustment]));
                        break;
                    case MOVE_TRACE:
                        l_col.addView(getCell(CurrentGame.CardLastClicked[i + row_adjustment][j + col_adjustment],
                                ColorMatrix[i + row_adjustment][j + col_adjustment]));
                        break;
                    case RETAINING_POWER :
                        l_col.addView(getCell(CurrentGame.CardRetainingPower[i + row_adjustment][j + col_adjustment],
                                ColorMatrix[i+row_adjustment][j+col_adjustment]));
                        break;
                }
            }
        }
        return simple_game;
    }

    protected View getClickMatrix_OneBoardWithoutScroll_Game()
    {

        LinearLayout.LayoutParams lParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f);
        LinearLayout simple_game = CreateBoardSet(0,0,lParam1,CurrentGame.RowSize,CurrentGame.ColumnSize);

        return simple_game;
    }

    protected View getClickMatrix_OneBoardVerticalScroll_Game()
    {
        int row_height = getRowHeightForOneBoardVerticalScroll(CurrentGame.ColumnSize, CurrentGame.RowSize);
        LinearLayout.LayoutParams decidingLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                row_height,1f);
        LinearLayout simple_game1 = CreateBoardSet(0,0,decidingLayoutParam,CurrentGame.RowSize,CurrentGame.ColumnSize);

        LinearLayout.LayoutParams lParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f);
        ScrollView s1 = new ScrollView(CurrentGame.mContext);
        s1.setLayoutParams(lParam1);
        s1.addView(simple_game1);

        final LinearLayout board = new LinearLayout(CurrentGame.mContext);
        board.addView(s1);

        return board;
    }
    protected View getClickMatrix_OneBoardHorizontalScroll_Game()
    {
        int col_width = getFullRowWidthForOneBoardHorizontalScroll(CurrentGame.ColumnSize);
        LinearLayout.LayoutParams decidingLayoutParam = new LinearLayout.LayoutParams(col_width,ViewGroup.LayoutParams.MATCH_PARENT,1f);
        LinearLayout simple_game1 = CreateBoardSet(0,0,decidingLayoutParam,CurrentGame.RowSize,CurrentGame.ColumnSize);

        LinearLayout.LayoutParams lParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f);
        HorizontalScrollView s1 = new HorizontalScrollView(CurrentGame.mContext);
        s1.setLayoutParams(lParam1);
        s1.addView(simple_game1);

        final LinearLayout board = new LinearLayout(CurrentGame.mContext);
        board.setOrientation(LinearLayout.VERTICAL);
        board.addView(s1);

        return board;
    }
    protected View getClickMatrix_OneBoardBothScroll_Game()
    {
        Point card_size = getDisplaySizeForOneBoardScrollGame(CurrentGame.RowSize, CurrentGame.ColumnSize);
        int row_height = card_size.y;
        int col_width = card_size.x;
        LinearLayout.LayoutParams decidingLayoutParam = new LinearLayout.LayoutParams(col_width,row_height,1f);
        LinearLayout simple_game1 = CreateBoardSet(0, 0, decidingLayoutParam,CurrentGame.RowSize,CurrentGame.ColumnSize);


        LinearLayout.LayoutParams lParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        ScrollView s_v1 = new ScrollView(CurrentGame.mContext);
        s_v1.setLayoutParams(lParam1);
        s_v1.addView(simple_game1);

        lParam1.weight = 1f;
        HorizontalScrollView s_h1 = new HorizontalScrollView(CurrentGame.mContext);
        s_h1.setLayoutParams(lParam1);
        s_h1.addView(s_v1);

        final LinearLayout board = new LinearLayout(CurrentGame.mContext);
        board.addView(s_h1);

        return board;
    }

    private View getClickMatrix_TwoBoardWithoutScroll_Game()
    {
        LinearLayout.LayoutParams decidingLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f);
        int rowSize = CurrentGame.RowSize/2;
        LinearLayout simple_game1 = CreateBoardSet(0, 0, decidingLayoutParam, rowSize, CurrentGame.ColumnSize);
        LinearLayout simple_game2 = CreateBoardSet(rowSize,0,decidingLayoutParam,rowSize,CurrentGame.ColumnSize);
        LinearLayout.LayoutParams lParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f);
        simple_game1.setLayoutParams(lParam1);
        simple_game2.setLayoutParams(lParam1);

        final LinearLayout twoBoard = new LinearLayout(CurrentGame.mContext);
        twoBoard.setOrientation(LinearLayout.VERTICAL);
        twoBoard.addView(simple_game1);
        twoBoard.addView(getDivider());
        twoBoard.addView(simple_game2);

        return twoBoard;
    }

    protected View getClickMatrix_TwoBoardVerticalScroll_Game()
    {
        int rowSize = CurrentGame.RowSize/2;
        int colSize = CurrentGame.ColumnSize;
        int row_height = getRowHeightForTwoBoardVerticalScroll(rowSize, colSize);
        LinearLayout.LayoutParams decidingLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                row_height,1f);
        LinearLayout simple_game1 = CreateBoardSet(0, 0, decidingLayoutParam,rowSize,colSize);
        LinearLayout simple_game2 = CreateBoardSet(rowSize, 0, decidingLayoutParam,rowSize,colSize);


        LinearLayout.LayoutParams lParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f);
        ScrollView s1 = new ScrollView(CurrentGame.mContext);
        ScrollView s2 = new ScrollView(CurrentGame.mContext);
        s1.setLayoutParams(lParam1);
        s2.setLayoutParams(lParam1);
        s1.addView(simple_game1);
        s2.addView(simple_game2);

        final LinearLayout twoBoard = new LinearLayout(CurrentGame.mContext);
        twoBoard.setOrientation(LinearLayout.VERTICAL);
        twoBoard.addView(s1);
        twoBoard.addView(getDivider());
        twoBoard.addView(s2);

        return twoBoard;
    }
    protected View getClickMatrix_TwoBoardHorizontalScroll_Game()
    {
        int rowSize = CurrentGame.RowSize/2;
        int colSize = CurrentGame.ColumnSize;
        int col_width = getFullRowWidthForTwoBoardHorizontalScroll(colSize);
        LinearLayout.LayoutParams decidingLayoutParam = new LinearLayout.LayoutParams(col_width,ViewGroup.LayoutParams.MATCH_PARENT,1f);
        LinearLayout simple_game1 = CreateBoardSet(0, 0, decidingLayoutParam,rowSize,colSize);
        LinearLayout simple_game2 = CreateBoardSet(rowSize, 0, decidingLayoutParam,rowSize,colSize);


        LinearLayout.LayoutParams lParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f);
        HorizontalScrollView s1 = new HorizontalScrollView(CurrentGame.mContext);
        HorizontalScrollView s2 = new HorizontalScrollView(CurrentGame.mContext);
        s1.setLayoutParams(lParam1);
        s2.setLayoutParams(lParam1);
        s1.addView(simple_game1);
        s2.addView(simple_game2);

        final LinearLayout twoBoard = new LinearLayout(CurrentGame.mContext);
        twoBoard.setOrientation(LinearLayout.VERTICAL);
        twoBoard.addView(s1);
        twoBoard.addView(getDivider());
        twoBoard.addView(s2);

        return twoBoard;
    }
    protected View getClickMatrix_TwoBoardBothScroll_Game()
    {
        int colSize = CurrentGame.ColumnSize;
        int rowSize = CurrentGame.RowSize/2;
        Point card_size = getDisplaySizeForTwoBoardScrollGame(CurrentGame.RowSize, colSize);
        int row_height = card_size.y;
        int col_width = card_size.x;
        LinearLayout.LayoutParams decidingLayoutParam = new LinearLayout.LayoutParams(col_width,row_height,1f);
        LinearLayout simple_game1 = CreateBoardSet(0, 0, decidingLayoutParam,rowSize,colSize);
        LinearLayout simple_game2 = CreateBoardSet(rowSize, 0, decidingLayoutParam,rowSize,colSize);


        LinearLayout.LayoutParams lParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f);

        ScrollView s_v1 = new ScrollView(CurrentGame.mContext);
        ScrollView s_v2 = new ScrollView(CurrentGame.mContext);
        s_v1.setLayoutParams(lParam1);
        s_v2.setLayoutParams(lParam1);
        s_v1.addView(simple_game1);
        s_v2.addView(simple_game2);

        lParam1.weight = 1f;
        HorizontalScrollView s_h1 = new HorizontalScrollView(CurrentGame.mContext);
        HorizontalScrollView s_h2 = new HorizontalScrollView(CurrentGame.mContext);
        s_h1.setLayoutParams(lParam1);
        s_h2.setLayoutParams(lParam1);
        s_h1.addView(s_v1);
        s_h2.addView(s_v2);

        final LinearLayout twoBoard = new LinearLayout(CurrentGame.mContext);
        twoBoard.setOrientation(LinearLayout.VERTICAL);
        twoBoard.addView(s_h1);
        twoBoard.addView(getDivider());
        twoBoard.addView(s_h2);

        return twoBoard;
    }

    protected View getDivider()
    {
        int TwoBoardAdjustment = 2;
        RelativeLayout v = new RelativeLayout(CurrentGame.mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ConvertToPx(CurrentGame.mContext,TwoBoardAdjustment));
        v.setLayoutParams(layoutParams);
        v.setBackgroundColor(Color.argb(63,0,0,0));
        return v;
    }



    //Height for One Board vertical scroll
    private int getRowHeightForOneBoardVerticalScroll(int ColumnSize,int rowSize)
    {
        int height;
        int screen_height = getWindowSize(CurrentGame.mContext.getWindowManager().getDefaultDisplay()).y
                - ConvertToPx(CurrentGame.mContext, 95 + 60);
        int numberOfRows = ColumnSize+2;
        if (numberOfRows>rowSize)
            numberOfRows=rowSize;

        height = screen_height/numberOfRows;
        return height ;
    }

    //Height for Two Board vertical scroll
    private int getRowHeightForTwoBoardVerticalScroll(int ColumnSize,int rowSize)
    {
        int height;
        int screen_height = getWindowSize(CurrentGame.mContext.getWindowManager().getDefaultDisplay()).y
                - ConvertToPx(CurrentGame.mContext, 95 + 60);
        int numberOfRows = ColumnSize+2;
        if (numberOfRows>rowSize*2)
            numberOfRows=rowSize*2;

        if(numberOfRows%2==1)
            numberOfRows--;

        height = screen_height/numberOfRows;
        return height ;
    }

    //OneBoardScroll_H
    public int getFullRowWidthForOneBoardHorizontalScroll(int ColumnSize)
    {
        int card_width;
        int screen_width = getWindowSize(CurrentGame.mContext.getWindowManager().getDefaultDisplay()).x
                - ConvertToPx(CurrentGame.mContext,40);

        switch (ColumnSize) {
            case 1:
            case 2:
            case 3:
            case 4:
                card_width = screen_width / (ColumnSize-1);
                break;
            default:
                card_width = screen_width / 4;
        }

        return card_width*ColumnSize;
    }

    //TwoBoardScroll_H
    public int getFullRowWidthForTwoBoardHorizontalScroll(int ColumnSize)
    {
        return getFullRowWidthForOneBoardHorizontalScroll(ColumnSize);
    }

    //TwoBoardScroll_B
    public Point getDisplaySizeForTwoBoardScrollGame(int RowSize,int ColumnSize)
    {
        Point windowSize = getWindowSize(CurrentGame.mContext.getWindowManager().getDefaultDisplay());
        windowSize.x = windowSize.x - ConvertToPx(CurrentGame.mContext,40);
        windowSize.y = windowSize.y - ConvertToPx(CurrentGame.mContext,95+60);
        int width,height;
        switch(ColumnSize)
        {
            case 1:
            case 2:
            case 3:
                width = windowSize.x;
                break;
            default:
                width = windowSize.x / 4 * ColumnSize;
                break;
        }
        switch(RowSize*2)
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                height = (windowSize.y) / RowSize;
                break;
            default:
                height = (windowSize.y) / 6;
                break;
        }

        return new Point(width,height);
    }

    //OneBoardScroll_B
    public Point getDisplaySizeForOneBoardScrollGame(int RowSize,int ColumnSize)
    {
        Point windowSize = getWindowSize(CurrentGame.mContext.getWindowManager().getDefaultDisplay());
        windowSize.x = windowSize.x - ConvertToPx(CurrentGame.mContext,40);
        windowSize.y = windowSize.y - ConvertToPx(CurrentGame.mContext,95+60);
        int width,height;
        switch(ColumnSize)
        {
            case 1:
            case 2:
            case 3:
                width = windowSize.x;
                break;
            default:
                width = windowSize.x / 4 * ColumnSize;
                break;
        }
        switch(RowSize)
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                height = (windowSize.y) / RowSize;
                break;
            default:
                height = (windowSize.y) / 6;
                break;
        }

        return new Point(width,height);
    }


}
