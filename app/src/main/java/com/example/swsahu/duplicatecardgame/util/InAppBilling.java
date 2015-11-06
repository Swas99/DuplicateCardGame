package com.example.swsahu.duplicatecardgame.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swsahu.duplicatecardgame.MainActivity;
import com.example.swsahu.duplicatecardgame.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.swsahu.duplicatecardgame.HelperClass.AD_FREE_VERSION_HASH_MAP;
import static com.example.swsahu.duplicatecardgame.HelperClass.AD_FREE_VERSION_MAP_KEY;

/**
 * Middle layer between main activity & util helper classes for In App Billing Logic
 */
public class InAppBilling {

    MainActivity mContext;
    public IabHelper mHelper;
    String SKU_POUCH_OF_COINS = "1";
    String SKU_BAG_OF_COINS = "2";
    String SKU_TRUNK_OF_COINS = "3";
    String SKU_REMOVE_ADS = "4";
    public long POUCH_OF_COINS_VALUE = 200;
    public long BAG_OF_COINS_VALUE = 450;
    public long TRUNK_OF_COINS_VALUE = 900;
    String RemoveAdsPrice;
    String CoinPouchPrice;
    String CoinBagPrice;
    String CoinTrunkPrice;
    boolean connectedToPlayServices;

    public InAppBilling(MainActivity m_context) {
        mContext = m_context;

        RemoveAdsPrice=CoinBagPrice=CoinPouchPrice=CoinTrunkPrice="";
    }

    public void initializeInAppBilling() {

        String base64EncodedPublicKey= "";
        // compute your public key and store it in base64EncodedPublicKey
        //base64EncodedPublicKey = getPublicKey();

        mHelper = new IabHelper(mContext, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    String msg = "Oops. Your device is not supported for in-app billing\n" +
                            "We are working hard to fix this";
                    Toast.makeText(mContext.getApplication(), msg, Toast.LENGTH_SHORT).show();
                }
                else
                {
                  connectedToPlayServices=true;
                }
            }
        });

        //check for pending consumptions
        mHelper.queryInventoryAsync(mGotInventoryListener);
    }

    //region Check For pending items to consume
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener()
    {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            if (result.isFailure()) {
                // handle error here
            }
            else {
                Purchase purchase;
                //consume pending purchases
                purchase = inventory.getPurchase(SKU_BAG_OF_COINS);
                if (purchase != null && verifyDeveloperPayload(purchase)) {
                    mHelper.consumeAsync(purchase,
                            mConsumeFinishedListener);
                }
                purchase = inventory.getPurchase(SKU_POUCH_OF_COINS);
                if (purchase != null && verifyDeveloperPayload(purchase)) {
                    mHelper.consumeAsync(purchase,
                            mConsumeFinishedListener);
                }
                purchase = inventory.getPurchase(SKU_TRUNK_OF_COINS);
                if (purchase != null && verifyDeveloperPayload(purchase)) {
                    mHelper.consumeAsync(purchase,
                            mConsumeFinishedListener);
                }
                if(!mContext.adFreeVersion)
                {
                    purchase = inventory.getPurchase(SKU_REMOVE_ADS);
                    if (purchase != null && verifyDeveloperPayload(purchase)) {
                        mContext.adFreeVersion = true;
                        SharedPreferences.Editor editor = mContext.getSharedPreferences(String.valueOf(AD_FREE_VERSION_HASH_MAP),
                                Context.MODE_PRIVATE).edit();
                        editor.putBoolean(String.valueOf(AD_FREE_VERSION_MAP_KEY),true);
                        editor.apply();
                    }
                }

            }
        }
    };
    //endregion

    //region update data after consumption
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess())
                    {
                        if (purchase.getSku().equals(SKU_POUCH_OF_COINS)) {
                            mContext.updateCoins(POUCH_OF_COINS_VALUE);
                        }
                        else if (purchase.getSku().equals(SKU_BAG_OF_COINS)) {
                            mContext.updateCoins(BAG_OF_COINS_VALUE);
                        }else if (purchase.getSku().equals(SKU_TRUNK_OF_COINS)) {
                            mContext.updateCoins(TRUNK_OF_COINS_VALUE);
                        }

                        try {
                            View tvCoins = mContext.findViewById(R.id.tvCoins);
                            if(tvCoins!=null)
                            {
                                ((TextView)tvCoins).setText(String.valueOf(mContext.coins));
                            }
                        } catch (Exception ex) {/* do nothing */}
                    }
                    else
                    {
                        // handle error
                    }
                }
            };
    //endregion


    public void setCoinPrice()
    {
        if(CoinBagPrice.equals(""))
        {
            IabHelper.QueryInventoryFinishedListener
                    mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
                public void onQueryInventoryFinished(IabResult result, Inventory inventory)
                {
                    if (result.isFailure()) {
                        // handle error
                        return;
                    }

                    try {
                        CoinPouchPrice =
                                inventory.getSkuDetails(SKU_POUCH_OF_COINS).getPrice();
                        ((TextView)mContext.findViewById(R.id.tvPouchPrice)).setText("At " + CoinPouchPrice);
                    }
                    catch (Exception ex){ /* do nothing  */ }
                    try {
                        CoinBagPrice =
                                inventory.getSkuDetails(SKU_BAG_OF_COINS).getPrice();
                        ((TextView)mContext.findViewById(R.id.tvBagPrice)).setText("At " + CoinBagPrice);
                    }
                    catch (Exception ex){ /* do nothing  */ }
                    try {
                        CoinTrunkPrice =
                                inventory.getSkuDetails(SKU_TRUNK_OF_COINS).getPrice();
                        ((TextView)mContext.findViewById(R.id.tvTrunkPrice)).setText("At " + CoinTrunkPrice);
                    }
                    catch (Exception ex){ /* do nothing  */ }
                }
            };
            List<String> additionalSkuList = new ArrayList<>();
            additionalSkuList.add(SKU_BAG_OF_COINS);
            additionalSkuList.add(SKU_POUCH_OF_COINS);
            additionalSkuList.add(SKU_TRUNK_OF_COINS);
            mHelper.queryInventoryAsync(true, additionalSkuList,
                    mQueryFinishedListener);
        }
        else
        {
            try {
                ((TextView)mContext.findViewById(R.id.tvPouchPrice)).setText("At " + CoinPouchPrice);
            }
            catch (Exception ex){ /* do nothing  */ }
            try {
                ((TextView)mContext.findViewById(R.id.tvBagPrice)).setText("At " + CoinBagPrice);
            }
            catch (Exception ex){ /* do nothing  */ }
            try {
                ((TextView)mContext.findViewById(R.id.tvTrunkPrice)).setText("At " + CoinTrunkPrice);
            }
            catch (Exception ex){ /* do nothing  */ }
        }
    }

    public void setRemoveAdsPrice()
    {
        if(RemoveAdsPrice.equals(""))
        {
            IabHelper.QueryInventoryFinishedListener
                    mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
                public void onQueryInventoryFinished(IabResult result, Inventory inventory)
                {
                    if (result.isFailure()) {
                        // handle error
                        return;
                    }

                    try {
                        RemoveAdsPrice =
                                inventory.getSkuDetails(SKU_REMOVE_ADS).getPrice();
                        ((TextView)mContext.findViewById(R.id.tvRemoveAdsPrice)).setText("At just " + RemoveAdsPrice + "!");
                    }
                    catch (Exception ex){ /* do nothing  */ }
                }
            };
            List<String> additionalSkuList = new ArrayList<>();
            additionalSkuList.add(SKU_REMOVE_ADS);
            mHelper.queryInventoryAsync(true, additionalSkuList,
                    mQueryFinishedListener);
        }
        else
        {
            try {
                ((TextView)mContext.findViewById(R.id.tvRemoveAdsPrice)).setText("At just " + RemoveAdsPrice +"!");
            } catch (Exception ex){ /* do nothing  */ }
        }

    }

    public void LaunchPurchaseFlow(String SKU_ID)
    {
        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase)
            {
                if (result.isFailure()) {
                    Toast.makeText(mContext,"Purchased Failed",Toast.LENGTH_SHORT).show();
                }

                if (purchase.getSku().equals(SKU_REMOVE_ADS))
                {
                    mContext.adFreeVersion = true;
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(String.valueOf(AD_FREE_VERSION_HASH_MAP),
                            Context.MODE_PRIVATE).edit();
                    editor.putBoolean(String.valueOf(AD_FREE_VERSION_MAP_KEY),true);
                    editor.apply();

                    Toast.makeText(mContext,"Application upgraded successfully." +
                    "\nPlease give us a moment to refresh things!",Toast.LENGTH_SHORT).show();
                }
                else //Consume other items
                {
                    mHelper.consumeAsync(purchase,mConsumeFinishedListener);

                    Toast.makeText(mContext,"Purchase successful." +
                            "\nPlease give us a moment to refresh things!",Toast.LENGTH_SHORT).show();
                }
            }
        };

        String code = "2_Cards";
        int transactionID = code.hashCode();
        mHelper.launchPurchaseFlow(mContext, SKU_ID, transactionID,
                mPurchaseFinishedListener, jumble("69"));
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return payload.equals(jumble("619"));
    }

    public String jumble(String x)
    {
        x="SwaS.RiShi_NeVeR.GiVe.Up";
        char y[] = x.toCharArray();
        int []xx = {3,9,5,7,2,11,3,9,5,7,2,11,};
        int l1 = xx.length;
        int l2 = y.length;
        for(int i=0;i<l1;i++)
        {
            int xxx = xx[i];
            int index = xxx;
            for(int j=0;j<l2;j++)
            {
                char yy = y[j];
                y[j] = y[index%l2];
                index+=xxx;
            }
        }
        x=y.toString();
        return x;
    }

}
