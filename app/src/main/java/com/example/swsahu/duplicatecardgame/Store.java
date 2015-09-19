package com.example.swsahu.duplicatecardgame;


import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

public class Store {

    MainActivity mContext;

    //region Listener_Variables

    View.OnClickListener RemoveAds_Click;
    View.OnClickListener Coins_Click;
    View.OnClickListener Power_Click;
    View.OnClickListener BackButton_Click;

    //endregion

    public Store(WeakReference<MainActivity> m_context)
    {
        mContext = m_context.get();
        InitializeListeners();
    }

    public void Show()
    {
        mContext.loadView(R.layout.screen_store);
        addListenerToControls();
    }


    private void InitializeListeners()
    {
        Power_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                powerClick();
            }
        };
        Coins_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinsClick();
            }
        };
        RemoveAds_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               removeAdsClick();
            }
        };
        BackButton_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.onBackPressed();
            }
        };
    }

    private void addListenerToControls()
    {
        Button btnPow = (Button)mContext.findViewById(R.id.btnPower);
        Button btnCoin = (Button)mContext.findViewById(R.id.btnCoin);
        Button btnRemoveAds = (Button)mContext.findViewById(R.id.btnRemoveAds);
        Button btn_Pow = (Button)mContext.findViewById(R.id.btn_power);
        Button btn_Coin = (Button)mContext.findViewById(R.id.btn_coin);
        Button btn_RemoveAds = (Button)mContext.findViewById(R.id.btn_removeAds);

        btnPow.setOnClickListener(Power_Click);
        btnCoin.setOnClickListener(Coins_Click);
        btnRemoveAds.setOnClickListener(RemoveAds_Click);
        btn_Pow.setOnClickListener(Power_Click);
        btn_Coin.setOnClickListener(Coins_Click);
        btn_RemoveAds.setOnClickListener(RemoveAds_Click);

//        btnLoadScores.setOnClickListener(LoadScores_Click);
//        btnResetScores.setOnClickListener(ResetScores_Click);
//        btn_next_page.setOnClickListener(NextPage_Click);
//        btn_prev_page.setOnClickListener(PreviousPage_Click);
//        btnExit.setOnClickListener(ExitButton_Click);
//        btnStore.setOnClickListener(StoreButton_Click);
//        //buttons
//        GameMode.setOnClickListener(GameMode_Edit_Click);
//        PlayerMode.setOnClickListener(PlayerMode_Edit_Click);
//        BoardType.setOnClickListener(BoardType_Edit_Click);
//        CardSet.setOnClickListener(CardSet_Edit_Click);
//        ScrollType.setOnClickListener(ScrollType_Edit_Click);
//        RowSize.setOnClickListener(BoardSize_Edit_Click);
//        ColSize.setOnClickListener(BoardSize_Edit_Click);
//
//        //Edit buttons
//        btnGameMode.setOnClickListener(GameMode_Edit_Click);
//        btnPlayerMode.setOnClickListener(PlayerMode_Edit_Click);
//        btnBoardType.setOnClickListener(BoardType_Edit_Click);
//        btnCardSet.setOnClickListener(CardSet_Edit_Click);
//        btnScrollType.setOnClickListener(ScrollType_Edit_Click);

    }

    private void powerClick()
    {
        Button btnPow = (Button)mContext.findViewById(R.id.btnPower);
        Button btnCoin = (Button)mContext.findViewById(R.id.btnCoin);
        Button btnRemoveAds = (Button)mContext.findViewById(R.id.btnRemoveAds);

        //Select
        btnPow.setBackgroundResource(R.drawable.btn_white_reverse);
        //Un-Select
        btnCoin.setBackgroundResource(R.drawable.btn_white_transparency_20);
        btnRemoveAds.setBackgroundResource(R.drawable.btn_white_transparency_20);
    }

    private void removeAdsClick()
    {
        Button btnPow = (Button)mContext.findViewById(R.id.btnPower);
        Button btnCoin = (Button)mContext.findViewById(R.id.btnCoin);
        Button btnRemoveAds = (Button)mContext.findViewById(R.id.btnRemoveAds);

        //Select
        btnRemoveAds.setBackgroundResource(R.drawable.btn_white_reverse);
        //Un-Select
        btnCoin.setBackgroundResource(R.drawable.btn_white_transparency_20);
        btnPow.setBackgroundResource(R.drawable.btn_white_transparency_20);
    }

    private void coinsClick()
    {
        Button btnPow = (Button)mContext.findViewById(R.id.btnPower);
        Button btnCoin = (Button)mContext.findViewById(R.id.btnCoin);
        Button btnRemoveAds = (Button)mContext.findViewById(R.id.btnRemoveAds);

        //Select
        btnCoin.setBackgroundResource(R.drawable.btn_white_reverse);
        //Un-Select
        btnPow.setBackgroundResource(R.drawable.btn_white_transparency_20);
        btnRemoveAds.setBackgroundResource(R.drawable.btn_white_transparency_20);

    }

}
