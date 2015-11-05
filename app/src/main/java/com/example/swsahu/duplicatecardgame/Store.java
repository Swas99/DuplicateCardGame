package com.example.swsahu.duplicatecardgame;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import java.lang.ref.WeakReference;

import static com.example.swsahu.duplicatecardgame.HelperClass.ConvertToPx;
import static com.example.swsahu.duplicatecardgame.HelperClass.DELIMITER;
import static com.example.swsahu.duplicatecardgame.HelperClass.DELIMITER_2;
import static com.example.swsahu.duplicatecardgame.HelperClass.POWER_COUNT;
import static com.example.swsahu.duplicatecardgame.HelperClass.POW_DESTROY;
import static com.example.swsahu.duplicatecardgame.HelperClass.POW_EXTRA_MOVES;
import static com.example.swsahu.duplicatecardgame.HelperClass.POW_FIND;
import static com.example.swsahu.duplicatecardgame.HelperClass.POW_PEEK;
import static com.example.swsahu.duplicatecardgame.HelperClass.POW_REPLACE;
import static com.example.swsahu.duplicatecardgame.HelperClass.POW_SHUFFLE;
import static com.example.swsahu.duplicatecardgame.HelperClass.POW_SWAP;
import static com.example.swsahu.duplicatecardgame.HelperClass.applyBorderDrawableToView;

public class Store {

    MainActivity mContext;
    BuyCoins objBuyCoins;
    BuyPowers objBuyPowers;
    RemoveAds objRemoveAds;


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

        RelativeLayout frame = (RelativeLayout)mContext.findViewById(R.id.frame);
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.view_buy_powers, frame, true);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));

        objBuyPowers = new BuyPowers();
        objBuyPowers.load();

        final AdView mAdView = (AdView) mContext.findViewById(R.id.adView);
        mAdView.loadAd(mContext.AdRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });
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
                mContext.onBackPress();
            }
        };

    }

    private void addListenerToControls()
    {
        View btnPow = mContext.findViewById(R.id.btnPower);
        View btnCoin = mContext.findViewById(R.id.btnCoin);
        View btnRemoveAds = mContext.findViewById(R.id.btnRemoveAds);
        View btn_Pow = mContext.findViewById(R.id.btn_power);
        View btn_Coin = mContext.findViewById(R.id.btn_coin);
        View btn_RemoveAds = mContext.findViewById(R.id.btn_removeAds);
        View btnBack = mContext.findViewById(R.id.btnBack);
        View btn_back = mContext.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(BackButton_Click);
        btnPow.setOnClickListener(Power_Click);
        btnCoin.setOnClickListener(Coins_Click);
        btnRemoveAds.setOnClickListener(RemoveAds_Click);
        btn_Pow.setOnClickListener(Power_Click);
        btn_Coin.setOnClickListener(Coins_Click);
        btn_RemoveAds.setOnClickListener(RemoveAds_Click);
        btn_back.setOnClickListener(BackButton_Click);
    }

    private void powerClick()
    {
        View btnPow = mContext.findViewById(R.id.btnPower);
        View btnCoin = mContext.findViewById(R.id.btnCoin);
        View btnRemoveAds = mContext.findViewById(R.id.btnRemoveAds);

        //Select
        btnPow.setBackgroundResource(R.drawable.btn_white_reverse);
        //Un-Select
        btnCoin.setBackgroundResource(R.drawable.btn_white_transparency_20);
        btnRemoveAds.setBackgroundResource(R.drawable.btn_white_transparency_20);

        RelativeLayout frame = (RelativeLayout)mContext.findViewById(R.id.frame);
        frame.removeAllViews();
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.view_buy_powers, frame, true);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));

        objBuyPowers = new BuyPowers();
        objBuyPowers.load();
    }

    private void removeAdsClick()
    {
        View btnPow = mContext.findViewById(R.id.btnPower);
        View btnCoin = mContext.findViewById(R.id.btnCoin);
        View btnRemoveAds = mContext.findViewById(R.id.btnRemoveAds);

        //Select
        btnRemoveAds.setBackgroundResource(R.drawable.btn_white_reverse);
        //Un-Select
        btnCoin.setBackgroundResource(R.drawable.btn_white_transparency_20);
        btnPow.setBackgroundResource(R.drawable.btn_white_transparency_20);


        RelativeLayout frame = (RelativeLayout)mContext.findViewById(R.id.frame);
        frame.removeAllViews();
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.view_remove_ads, frame, true);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));

        objRemoveAds = new RemoveAds();
        objRemoveAds.InitializeListeners();
        objRemoveAds.InitializeRemoveAdsView();


    }

    private void coinsClick()
    {
        View btnPow = mContext.findViewById(R.id.btnPower);
        View btnCoin = mContext.findViewById(R.id.btnCoin);
        View btnRemoveAds = mContext.findViewById(R.id.btnRemoveAds);

        //Select
        btnCoin.setBackgroundResource(R.drawable.btn_white_reverse);
        //Un-Select
        btnPow.setBackgroundResource(R.drawable.btn_white_transparency_20);
        btnRemoveAds.setBackgroundResource(R.drawable.btn_white_transparency_20);


        RelativeLayout frame = (RelativeLayout)mContext.findViewById(R.id.frame);
        frame.removeAllViews();
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.view_buy_coins, frame, true);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
        objBuyCoins = new BuyCoins();
        objBuyCoins.load();
    }

    class BuyCoins
    {
        View.OnClickListener BuyPouchOfCoins_Click;
        View.OnClickListener BuyBagOfCoins_Click;
        View.OnClickListener BuyTrunkOfCoins_Click;

        public void load()
        {
            InitializeListeners();
            InitializeBuyCoinsView();
            //mContext.objInAppBilling.setCoinPrice(); //here
        }
        public void InitializeBuyCoinsView()
        {
            View btnBuyPouchOfCoins = mContext.findViewById(R.id.btnBuyPouchOfCoins);
            View btnBuyBagOfCoins = mContext.findViewById(R.id.btnBuyBagOfCoins);
            View btnBuyTrunkOfCoins = mContext.findViewById(R.id.btnBuyTrunkOfCoins);

            btnBuyPouchOfCoins.setOnClickListener(BuyPouchOfCoins_Click);
            btnBuyBagOfCoins.setOnClickListener(BuyBagOfCoins_Click);
            btnBuyTrunkOfCoins.setOnClickListener(BuyTrunkOfCoins_Click);
        }

        public void InitializeListeners()
        {
            BuyPouchOfCoins_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String SKU_POUCH_OF_COINS = "1";
                    mContext.objInAppBilling.LaunchPurchaseFlow(SKU_POUCH_OF_COINS);
                }
            };
            BuyBagOfCoins_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String SKU_BAG_OF_COINS = "2";
                    mContext.objInAppBilling.LaunchPurchaseFlow(SKU_BAG_OF_COINS);
                }
            };
            BuyTrunkOfCoins_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String SKU_TRUNK_OF_COINS = "3";
                    mContext.objInAppBilling.LaunchPurchaseFlow(SKU_TRUNK_OF_COINS);
                }
            };
        }

    }

    class RemoveAds
    {
        View.OnClickListener RemoveAdNow_Click;

        public void InitializeRemoveAdsView()
        {
            View btnRemoveAdNow = mContext.findViewById(R.id.btnRemoveAdNow);
            btnRemoveAdNow.setOnClickListener(RemoveAdNow_Click);
            mContext.objInAppBilling.setRemoveAdsPrice();
        }

        public void InitializeListeners()
        {
            RemoveAdNow_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"Hey There",Toast.LENGTH_SHORT).show();
                }
            };
        }

    }

    class BuyPowers
    {
        View.OnClickListener MyPower_Click;
        View.OnClickListener btn_prev_power_Click;
        View.OnClickListener btn_next_power_Click;
        View.OnClickListener btnIncreaseRange_Click;
        View.OnClickListener btnDecreaseRange_Click;
        View.OnClickListener btnIncreaseQuantity_Click;
        View.OnClickListener btnDecreaseQuantity_Click;
        View.OnClickListener Buy_Click;


        int allPowers[] = {POW_EXTRA_MOVES,POW_FIND,POW_SHUFFLE,POW_SWAP,POW_REPLACE,POW_DESTROY,POW_PEEK};
        int allPowerResId[] = {R.drawable.pow_extra_moves,R.drawable.pow_find,R.drawable.pow_shuffle,
                R.drawable.pow_swap,R.drawable.pow_replace,R.drawable.pow_destroy,R.drawable.pow_peek};
        String powerNames[] = {"Extra Moves","Find Card","Shuffle","Swap","Replace","Destroyer","Peek"};
        int minRange[] = {3,-1,-1,2,1,1,1};


        int currentIndex = 0;
        int count = 1;
        int identifier = minRange[currentIndex];

        ImageView ivPower;
        TextView tvCount;
        TextView tvIdentifier;
        TextView tvPowerName;
        TextView tvRange;
        TextView tvQuantity;
        TextView tvPrice;

        private void InitializeScreenComponents() {
            ivPower = (ImageView)mContext.findViewById(R.id.ivCard);
            tvCount = (TextView)mContext.findViewById(R.id.tvCount);
            tvIdentifier = (TextView) mContext.findViewById(R.id.tvIdentifier);
            tvPowerName = (TextView) mContext.findViewById(R.id.tvPowerName);
            applyBorderDrawableToView(tvCount, Color.RED, Color.RED, ConvertToPx(mContext, 7), 0);
            applyBorderDrawableToView(tvIdentifier, Color.argb(90, 255, 255, 255),
                    Color.BLACK, ConvertToPx(mContext, 7), 0);
            tvQuantity = (TextView) mContext.findViewById(R.id.tvQuantity);
            tvRange = (TextView) mContext.findViewById(R.id.tvRange);
            tvPrice = (TextView) mContext.findViewById(R.id.tvPrice);
        }

        public void InitializeBuyCoinsView()
        {
            View btnMyPower = mContext.findViewById(R.id.btnMyPower);
            View btn_prev_power = mContext.findViewById(R.id.btn_prev_power);
            View btn_next_power = mContext.findViewById(R.id.btn_next_power);
            View btnIncreaseRange = mContext.findViewById(R.id.btnIncreaseRange);
            View btnDecreaseRange = mContext.findViewById(R.id.btnDecreaseRange);
            View btnIncreaseQuantity = mContext.findViewById(R.id.btnIncreaseQuantity);
            View btnDecreaseQuantity = mContext.findViewById(R.id.btnDecreaseQuantity);
            View btnBuy = mContext.findViewById(R.id.btnBuy);


            btnMyPower.setOnClickListener(MyPower_Click);
            btn_prev_power.setOnClickListener(btn_prev_power_Click);
            btn_next_power.setOnClickListener(btn_next_power_Click);
            btnIncreaseRange.setOnClickListener(btnIncreaseRange_Click);
            btnDecreaseRange.setOnClickListener(btnDecreaseRange_Click);
            btnIncreaseQuantity.setOnClickListener(btnIncreaseQuantity_Click);
            btnDecreaseQuantity.setOnClickListener(btnDecreaseQuantity_Click);
            btnBuy.setOnClickListener(Buy_Click);


            TextView tvCoins = (TextView)mContext.findViewById(R.id.tvCoins);
            tvCoins.setText(String.valueOf(mContext.coins));
        }

        public void load()
        {
            InitializeListeners();
            InitializeBuyCoinsView();
            InitializeScreenComponents();
            setPowerCard();
        }

        public void setPowerCard()
        {
            ivPower.setBackgroundResource(allPowerResId[currentIndex]);
            {
                final AnimationDrawable frameAnimation = (AnimationDrawable) ivPower.getBackground();
                new CountDownTimer(200,200){
                    @Override
                    public void onTick(long i){}
                    @Override
                    public void onFinish()
                    {
                        if(frameAnimation!=null)
                            frameAnimation.start();
                    }
                }.start();
            }
            tvCount.setText(String.valueOf(count));
            tvQuantity.setText(String.valueOf(count));
            tvIdentifier.setText(String.valueOf(identifier));
            tvRange.setText(String.valueOf(identifier));
            setIdentifierVisibility(tvIdentifier, allPowers[currentIndex]);
            tvPowerName.setText(powerNames[currentIndex]);

            tvPrice.setText(String.valueOf(getPrice()));
        }

        public void InitializeListeners()
        {
            MyPower_Click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPowerClick();
                }
            };
            btn_prev_power_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousPowerClick();
                }
            };
            btn_next_power_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextPowerClick();
                }
            };
            btnIncreaseRange_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IncreaseRangeClick();
                }
            };
            btnDecreaseRange_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DecreaseRangeClick();
                }
            };
            btnIncreaseQuantity_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IncreaseQuantityClick();
                }
            };
            btnDecreaseQuantity_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DecreaseQuantityClick();
                }
            };
            Buy_Click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BuyPower();
                }
            };
        }

        public void MyPowerClick()
        {
            CreatePowerDialog();
        }
        public void previousPowerClick()
        {
            currentIndex--;
            if(currentIndex==-1)
                currentIndex=allPowers.length-1;
            if(identifier<minRange[currentIndex])
                identifier=minRange[currentIndex];
            setPowerCard();

        }
        public void nextPowerClick()
        {
            currentIndex++;
            if(currentIndex==allPowers.length)
                currentIndex=0;
            if(identifier<minRange[currentIndex])
                identifier=minRange[currentIndex];
            setPowerCard();
        }
        public void IncreaseRangeClick()
        {
            identifier++;
            if(identifier>20)
                identifier=20;
            else
            setPowerCard();
        }
        public void DecreaseRangeClick()
        {
            identifier--;
            if(identifier<minRange[currentIndex])
                identifier=minRange[currentIndex];
            else
            setPowerCard();
        }
        public void IncreaseQuantityClick()
        {
            count++;
            if (count>20)
                count=20;
            else
             setPowerCard();
        }
        public void DecreaseQuantityClick()
        {
            count--;
            if (count==0)
                count=1;
            else
                setPowerCard();
        }
        public void BuyPower()
        {
            long price = getPrice();
            mContext.updateCoins(0);
            if(mContext.coins<price)
            {
                ShowInsufficientCoinsDialog();
            }
            else
            {
                writePowersToPreferences(allPowers[currentIndex],identifier,count);
                mContext.updateCoins(-price);
                ShowPurchaseSuccessDialog();
            }
        }

        public void CreatePowerDialog()
        {
            final AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .show();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            View v =  mContext.CurrentView;
            if(v.getMeasuredHeight()<v.getMeasuredWidth()) {
                lp.width = v.getMeasuredWidth() - ConvertToPx(mContext, 100); //WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = v.getMeasuredHeight() - ConvertToPx(mContext, 70);//WindowManager.LayoutParams.WRAP_CONTENT;
            }
            else
            {
                lp.width = v.getMeasuredWidth() - ConvertToPx(mContext, 50); //WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = v.getMeasuredHeight() - ConvertToPx(mContext, 100);//WindowManager.LayoutParams.WRAP_CONTENT;
            }
            window.setAttributes(lp);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View pwd=inflater.inflate(R.layout.dialog_pow_find, null, false);
            pwd.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            final GridView gridview = (GridView) pwd.findViewById(R.id.gdCards);
            int INFINITE_EQUIVALENT = 100;
            final PowerCardAdapter powers = new PowerCardAdapter(mContext,INFINITE_EQUIVALENT);
            gridview.setAdapter(powers);
            TextView tvTitle = (TextView)pwd.findViewById(R.id.tvTitle);
            tvTitle.setText("My Powers");

            dialog.setContentView(pwd);
            dialog.show();
        }

        public void setIdentifierVisibility(View v,int card)
        {
            switch (card)
            {
                case POW_SHUFFLE:
                case POW_FIND:
                    v.setVisibility(View.INVISIBLE);
                    tvRange.setText("∞");
                    break;
                default:
                    v.setVisibility(View.VISIBLE);
            }
        }

        public long getPrice()
        {
            long price;
            long basePrice;
            switch (allPowers[currentIndex])
            {
                case POW_EXTRA_MOVES:
                    basePrice = 100;
                    price = (long)Math.pow(2, identifier - minRange[currentIndex] )*basePrice*count;
                    break;
                case POW_FIND:
                    basePrice = 150;
                    price = basePrice*count;
                    break;
                case POW_SHUFFLE:
                    basePrice = 100;
                    price = basePrice*count;
                    break;
                case POW_SWAP:
                    basePrice = 50;
                    price = (long)Math.pow(2, identifier - minRange[currentIndex] )*basePrice*count;
                    break;
                case POW_REPLACE:
                    basePrice = 50;
                    price = (long)Math.pow(2,identifier - minRange[currentIndex])*basePrice*count;
                    break;
                case POW_DESTROY:
                    basePrice = 100;
                    price = (long)Math.pow(2,identifier - minRange[currentIndex])*basePrice*count;
                    break;
                case POW_PEEK:
                    basePrice = 200;
                    price = (long)Math.pow(2,identifier - minRange[currentIndex])*basePrice*count;
                    break;
                default:
                    basePrice=999999;
                    price = (long)Math.pow(2,identifier - minRange[currentIndex])*basePrice*count;
            }


            return (price);
        }

        public void ShowPurchaseSuccessDialog()
        {
            String msg = "'" + powerNames[currentIndex] + "' bought successfully.";
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("SUCCESS");
            alertDialog.setMessage(msg);
            alertDialog.setCancelable(true);
            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }

        private void ShowInsufficientCoinsDialog()
        {
            String msg = "You do not have sufficient coins to buy this item.";
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Insufficient Coin Balance");
            alertDialog.setMessage(msg);
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("Get Coins", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    coinsClick();

                }
            });

            alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }

        public void writePowersToPreferences(int power,int range,int count)
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String power_data = preferences.getString(String.valueOf(power), "");
            int length = preferences.getInt(String.valueOf(POWER_COUNT), 0);
            SharedPreferences.Editor editor = preferences.edit();
            String array_powerData[] = power_data.split(DELIMITER_2);
            String newPowerData = "";
            boolean updated_flag = false;

            for (String val : array_powerData)
            {
                if(val.equals(""))
                    break;

                String identifier_count[] = val.split(DELIMITER);
                int card_identifier = Integer.parseInt(identifier_count[0]);
                if(card_identifier == range)
                {
                    int card_count = Integer.parseInt(identifier_count[1]) + count;
                    newPowerData+=  String.valueOf(card_identifier)+ DELIMITER+String.valueOf(card_count) + DELIMITER_2;
                    updated_flag = true;
                }
                else
                {
                    newPowerData+=val+DELIMITER_2;
                }
            }
            if(!updated_flag)
            {
                newPowerData+=  String.valueOf(range)+ DELIMITER+String.valueOf(count) + DELIMITER_2;
                length++;
            }
            editor.putString(String.valueOf(power), newPowerData);
            editor.putInt(String.valueOf(POWER_COUNT), length);
            editor.apply();
        }
    }
}
