package com.example.swsahu.duplicatecardgame;

import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import static com.example.swsahu.duplicatecardgame.HelperClass.*;


public class Robot {

    Game CurrentGame;
    int RobotMemoryLevel;
    private int MemoryLogicID;
    private ImageView bufferCard;

    private void SetVariablesForLogic1()
    {
        switch(RobotMemoryLevel)
        {
            case 1:
                memoryIncrementValue = 200;
                memoryDecrementValue = 75;
                memoryThreshold = 50;
                break;
            case 2:
                memoryIncrementValue = 200;
                memoryDecrementValue = 30;
                memoryThreshold = 120;
                break;
            case 3:
                memoryIncrementValue = 200;
                memoryDecrementValue = 80;
                memoryThreshold = 20;
                break;
            case 4:
                memoryIncrementValue = 200;
                memoryDecrementValue = 35;
                memoryThreshold = 90;
                break;
            case 5:
                memoryIncrementValue = 200;
                memoryDecrementValue = 25;
                memoryThreshold = 100;
                break;
            case 6:
                memoryIncrementValue = 200;
                memoryDecrementValue = 25;
                memoryThreshold = 90;
                break;
            case 7:
                memoryIncrementValue = 200;
                memoryDecrementValue = 25;
                memoryThreshold = 70;
                break;
            case 8:
                memoryIncrementValue = 200;
                memoryDecrementValue = 25;
                memoryThreshold = 50;
                break;
            case 9:
                memoryIncrementValue = 200;
                memoryDecrementValue = 20;
                memoryThreshold = 60;
                break;
            case 10:
                memoryIncrementValue = 200;
                memoryDecrementValue = 10;
                memoryThreshold = 100;
                break;
        }
    }


    private void SetVariablesForLogic3()
    {
        CardLastClicked = new int[CurrentGame.RowSize][CurrentGame.ColumnSize];
        Card_Clicks = new int[CurrentGame.RowSize][CurrentGame.ColumnSize];

        switch(RobotMemoryLevel)
        {
            case 1:
                MemoryArray = new int[]{0, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6 , 7, 8, 9, 10};
                break;
            case 2:
                MemoryArray = new int[]{0, 3, 4, 4, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9, 10};
                break;
            case 3:
                MemoryArray = new int[]{0, 4, 5, 6, 6, 6, 7, 7, 8, 8, 9, 9, 10};
                break;
            case 4:
                MemoryArray = new int[]{0, 5, 6, 7, 7, 8, 8, 8, 9, 9, 10, 10, 11};
                break;
            case 5:
                MemoryArray = new int[]{0, 6, 7, 7, 8, 9, 9, 9, 10, 10, 11, 11, 12};
                break;
            case 6:
                MemoryArray = new int[]{0, 7, 8, 8, 9, 10, 10, 11, 11, 12, 12, 12, 13};
                break;
            case 7:
                MemoryArray = new int[]{0, 8, 8, 9, 9, 10, 11, 11, 12, 12, 13, 13, 14};
                break;
            case 8:
                MemoryArray = new int[]{0, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 15};
                break;
            case 9:
                MemoryArray = new int[]{0, 10, 11, 12, 13, 14,15,16,17};
                break;
            case 10:
                MemoryArray = new int[]{0, 11, 13, 14, 15, 17,18,19,20};
                break;
        }
    }

    public void InitializeVariables()
    {
        switch(MemoryLogicID)
        {
            case HURRICANE:
                Memory = new int[CurrentGame.RowSize][CurrentGame.ColumnSize];
                SetVariablesForLogic1();
                break;
            case ANDROBOT:
                Memory_Stack = new MyStack(RobotMemoryLevel+2);
                break;
            case ROCK:
                SetVariablesForLogic3();
                break;
        }
    }

    public void Clear(boolean changeMemoryLogic)
    {
        switch(MemoryLogicID)
        {
            case HURRICANE:
                Memory = null;
                break;
            case ANDROBOT:
                Memory_Stack = null;
                break;
            case ROCK:
                CardLastClicked = null;
                break;
        }

        InitializeVariables();
    }

    public void AddToMemory(int r,int c)
    {
        switch (MemoryLogicID)
        {
            case HURRICANE:
                UpdateMemory(memoryIncrementValue, r, c);
                break;
            case ANDROBOT:
                Memory_Stack.push(r + DELIMITER + c);
                break;
            case ROCK:
                UpdateCardClicksAndMemory(r, c);
                break;
        }
    }

    public void RemoveFromMemory(String item1,String item2)
    {
        int r = Integer.parseInt(item1.split(DELIMITER)[0]);
        int c = Integer.parseInt(item1.split(DELIMITER)[1]);
        switch (MemoryLogicID)
        {
            case HURRICANE:
                Memory[r][c] = 0;
                break;
            case ANDROBOT:
                Memory_Stack.removeItem(item1);
                if(item1 != "")
                    Memory_Stack.removeItem(item2);
                break;
            case ROCK:
                Card_Clicks[r][c] = 0;
                CardLastClicked[r][c] = 0;
                break;
        }
    }

    public Robot(final WeakReference<Game> parentGame, int robotMemoryLevel,int memoryLogicID)
    {
        CurrentGame=parentGame.get();

        if(memoryLogicID == RANDOM_BOT)
        {
            int robotType[] = new int[]{ANDROBOT,HURRICANE,ROCK};
            int i = (int)(Math.random()*1000)%3;
            memoryLogicID = robotType[i];
            int randomValue = (int)(Math.random()*1000)%5 + 1;
            if(robotMemoryLevel>5)
                robotMemoryLevel = randomValue + 5;
            else
                robotMemoryLevel = randomValue;
        }
        MemoryLogicID = memoryLogicID;
        RobotMemoryLevel = robotMemoryLevel;
        InitializeVariables();
    }

    public void SimulateMove()
    {
        switch (MemoryLogicID)
        {
            case HURRICANE:
                SimulateCardClickUsingMemoryLogic1();
                break;
            case ANDROBOT:
                SimulateCardClickUsingStackMemory();
                break;
            case ROCK:
                SimulateCardClickUsingMemoryLogic3();
                break;
        }
    }

    //region Memory Logic

    //region Logic 1
    int Memory[][];
    int memoryIncrementValue;
    int memoryDecrementValue;
    int memoryThreshold;
    private void UpdateMemory(int memory_value,int i,int j)
    {
        Memory[i][j]+=memory_value+memoryDecrementValue;
        DecreaseMemory(memoryDecrementValue);
    }
    private void DecreaseMemory(int memory_value){

        for(int i=0;i<CurrentGame.RowSize;i++)
        {
            for(int j=0;j<CurrentGame.ColumnSize;j++)
            {
                if (Memory[i][j] > 0)
                    Memory[i][j] -= memory_value;
            }
        }
    }

    private void FreshInMemoryCards(String[] probableCards,String[] OtherCards)
    {
        int length_probableCards =0;
        int length_otherCards = 0;
        for(int i=0;i<CurrentGame.RowSize;i++)
        {
            for(int j=0;j<CurrentGame.ColumnSize;j++)
            {
                if( CurrentGame.IV_AllCards[i][j]!=null) {
                    if (Memory[i][j] >= memoryThreshold)
                        probableCards[length_probableCards++] = i + DELIMITER + j;
                    else
                        OtherCards[length_otherCards++] = i+DELIMITER+j;
                }
            }
        }
    }

    private ImageView getMatchingCard( String[]probableCards)
    {
        ImageView card1 = null;
        int probableCards_length =getLengthOfDynamicArray( probableCards);
        int [] resId_array = new int[probableCards_length];
        int resIdArray_length = 0;

        for(int i=0;i<probableCards_length;i++)
        {
            String item=probableCards[i];
            int indexOfDelimiter=item.indexOf('_');
            int row = Integer.parseInt (item.substring(0,indexOfDelimiter));
            int col = Integer.parseInt(item.substring(indexOfDelimiter+1));
            resId_array[resIdArray_length++] = CurrentGame.Cards_ImageResID[row][col];
        }
        Arrays.sort(resId_array);
        int indexOfMatch = -1;

        for(int i=0;i<probableCards_length-1;i++)
        {
            if(resId_array[i] == resId_array[i+1]  )
            {
                indexOfMatch=i;
                break;
            }
        }
        if(indexOfMatch!=-1)
        {
            int resId = resId_array[indexOfMatch];
            for(int i=0;i<probableCards_length;i++)
            {
                String item = probableCards[i];
                int indexOfDelimiter=item.indexOf('_');
                int row = Integer.parseInt (item.substring(0,indexOfDelimiter));
                int col = Integer.parseInt(item.substring(indexOfDelimiter+1));
                if( CurrentGame.Cards_ImageResID[row][col] == resId)
                {
                    if(card1==null)
                        card1 = CurrentGame.IV_AllCards[row][col];
                    else
                    {
                        bufferCard =  CurrentGame.IV_AllCards[row][col];
                        break;
                    }
                }
            }
        }
        return card1;
    }

    private void FindBestMatch()
    {
        String [] probableCards = new String[CurrentGame.ColumnSize*CurrentGame.RowSize];
        String[] availableCards = new String[CurrentGame.ColumnSize*CurrentGame.RowSize];
        FreshInMemoryCards(probableCards,availableCards);
        int indexOfDelimiter,row,col;
        String item;


        //Case 1 : Only one card is left on board
        if(getLengthOfDynamicArray( availableCards) == 0)
        {
            item = probableCards[0];
            indexOfDelimiter = item.indexOf('_');
            row = Integer.parseInt(item.substring(0, indexOfDelimiter));
            col = Integer.parseInt(item.substring(indexOfDelimiter + 1));
            ClickAndFocusOnView(CurrentGame.IV_AllCards[row][col]);
            return;
        }


        //Case 2 : Find a match (*hopefully*)
        indexOfDelimiter = CurrentGame.FirstCard.getTag().toString().indexOf(('_'));
        row = Integer.parseInt(CurrentGame.FirstCard.getTag().toString().substring(0, indexOfDelimiter));
        col = Integer.parseInt(CurrentGame.FirstCard.getTag().toString().substring(indexOfDelimiter + 1));
        int required_resID = CurrentGame.Cards_ImageResID[row][col];

        for(String Item : probableCards)
        {
            if(Item == null)
                break;

            indexOfDelimiter = Item.indexOf('_');
            row = Integer.parseInt(Item.substring(0, indexOfDelimiter));
            col = Integer.parseInt(Item.substring(indexOfDelimiter + 1));
            if(CurrentGame.Cards_ImageResID[row][col] == required_resID)
            {
                ClickAndFocusOnView(CurrentGame.IV_AllCards[row][col]);
                return;
            }
        }

        //Case 3 : No luck. Click random card
        int length = getLengthOfDynamicArray( availableCards);
        int randomIndex = ((int) (Math.random() * 1000)) % length;
        item = availableCards[randomIndex];
        indexOfDelimiter = item.indexOf('_');
        row = Integer.parseInt(item.substring(0, indexOfDelimiter));
        col = Integer.parseInt(item.substring(indexOfDelimiter + 1));
        ClickAndFocusOnView(CurrentGame.IV_AllCards[row][col]);
    }



    private void SimulateCardClickUsingMemoryLogic1()
    {
        if(CurrentGame.EffectiveClickCount%2!=0)
        {
            if( bufferCard != null) {
                ClickAndFocusOnView(bufferCard);
                bufferCard = null;
            }
            else
            {
                FindBestMatch();
            }
            return;
        }

        int boardSize = CurrentGame.ColumnSize*CurrentGame.RowSize;
        String [] probableCards = new String[boardSize];
        String[] availableCards = new String[boardSize];
        FreshInMemoryCards(probableCards,availableCards);
        ImageView card1;
        card1 = getMatchingCard(probableCards);

        //Find Matching pairs in Memory
        if(card1 != null)
        {
            ClickAndFocusOnView(card1);
        }
        else //Do a random click
        {
            int length = getLengthOfDynamicArray( availableCards);
            String item;
            if(length == 0)//Base case When number of cards on board is odd
            {
                item = probableCards[0];
            }
            else
            {
                int randomIndex = ((int) (Math.random() * 1000)) % length;
                item = availableCards[randomIndex];
            }
            int indexOfDelimiter = item.indexOf('_');
            int row = Integer.parseInt(item.substring(0, indexOfDelimiter));
            int col = Integer.parseInt(item.substring(indexOfDelimiter + 1));
            ClickAndFocusOnView(CurrentGame.IV_AllCards[row][col]);
        }
    }
    //endregion

    //region Logic 2

    MyStack Memory_Stack;

    private void getAvailableCards(ImageView[] availableCards)
    {
        int length_otherCards = 0;
        for(int i=0;i<CurrentGame.RowSize;i++)
        {
            for(int j=0;j<CurrentGame.ColumnSize;j++)
            {
                if( CurrentGame.IV_AllCards[i][j]!=null && Memory_Stack.findItem(i+DELIMITER+j)==-1) {
                    availableCards[length_otherCards++] = CurrentGame.IV_AllCards[i][j];
                }
            }
        }
    }

    private ImageView SimulateFirstClick()
    {
        int length = Memory_Stack.top+1;
        int [] resId_array = new int[length];
        int resIdArray_length = 0;
        ImageView card1=null;

        for(int i=0;i<length;i++)
        {
            String item=Memory_Stack.array[i];
            int indexOfDelimiter=item.indexOf('_');
            int row = Integer.parseInt (item.substring(0,indexOfDelimiter));
            int col = Integer.parseInt(item.substring(indexOfDelimiter+1));
            resId_array[resIdArray_length++] = CurrentGame.Cards_ImageResID[row][col];
        }
        Arrays.sort(resId_array);
        int indexOfMatch = -1;

        for(int i=0;i<length-1;i++)
        {
            if(resId_array[i] == resId_array[i+1]  )
            {
                indexOfMatch=i;
                break;
            }
        }
        if(indexOfMatch!=-1)
        {
            int resId = resId_array[indexOfMatch];
            for(int i=0;i<length;i++)
            {
                String item = Memory_Stack.array[i];
                int indexOfDelimiter=item.indexOf('_');
                int row = Integer.parseInt (item.substring(0,indexOfDelimiter));
                int col = Integer.parseInt(item.substring(indexOfDelimiter+1));
                if( CurrentGame.Cards_ImageResID[row][col] == resId)
                {
                    if(card1==null)
                    {
                        card1=CurrentGame.IV_AllCards[row][col];
                    }
                    else
                    {
                        bufferCard=CurrentGame.IV_AllCards[row][col];
                        break;
                    }
                }
            }
        }
        else
        {
            int availableCards_length = CurrentGame.ColumnSize*CurrentGame.RowSize-Memory_Stack.top+1;
            ImageView[] availableCards = new ImageView[availableCards_length];

            getAvailableCards(availableCards);
            availableCards_length = getLengthOfDynamicArray(availableCards);
            if(availableCards_length == 0)//Base case When number of cards on board is odd
            {
                String item=Memory_Stack.array[0];
                int indexOfDelimiter=item.indexOf('_');
                int row = Integer.parseInt (item.substring(0,indexOfDelimiter));
                int col = Integer.parseInt(item.substring(indexOfDelimiter+1));
                return CurrentGame.IV_AllCards[row][col];
            }
            int randomIndex = (int) (Math.random()*1000%availableCards_length);
            return availableCards[randomIndex];
        }
        return card1;
    }

    private void FindMatchUsingStackMemory()
    {
        int length = Memory_Stack.top+1;
        //Get resource ID to search
        String item = CurrentGame.FirstCard.getTag().toString();
        int indexOfDelimiter=item.indexOf('_');
        int row = Integer.parseInt (item.substring(0,indexOfDelimiter));
        int col = Integer.parseInt(item.substring(indexOfDelimiter+1));
        int loc = Memory_Stack.findCard(row,col,CurrentGame.Cards_ImageResID);

        if(loc>-1)
        {
            item = Memory_Stack.array[loc];
            indexOfDelimiter=item.indexOf('_');
            row = Integer.parseInt (item.substring(0,indexOfDelimiter));
            col = Integer.parseInt(item.substring(indexOfDelimiter+1));
            ClickAndFocusOnView(CurrentGame.IV_AllCards[row][col]);
        }
        else
        {
            int availableCards_length = CurrentGame.ColumnSize*CurrentGame.RowSize-length;
            ImageView[] availableCards = new ImageView[availableCards_length];

            getAvailableCards(availableCards);
            availableCards_length = getLengthOfDynamicArray(availableCards);
            int randomIndex = (int) (Math.random()*1000%availableCards_length);
            ClickAndFocusOnView(availableCards[randomIndex]);
        }

    }

    private void SimulateCardClickUsingStackMemory()
    {

        if(CurrentGame.EffectiveClickCount%2 == 0)
        {
            ImageView card =  SimulateFirstClick();
            ClickAndFocusOnView(card);
        }
        else
        {
            if( bufferCard != null) {
                ClickAndFocusOnView(bufferCard);
                bufferCard = null;
            }
            else
            {
                FindMatchUsingStackMemory();
            }
        }
    }

    //endregion


    //region Logic 3

    int CardLastClicked[][];
    int MemoryArray[];
    int Card_Clicks[][];

    private void UpdateCardClicksAndMemory(int i,int j)
    {
        Card_Clicks[i][j]++;
        CardLastClicked[i][j] = CurrentGame.ActualClickCount;
    }

    private boolean IsCardInMemory(int i,int j)
    {
        int click_difference = CurrentGame.ActualClickCount - CardLastClicked[i][j];
        int memory_value;
        if(MemoryArray.length > Card_Clicks[i][j])
            memory_value = MemoryArray[Card_Clicks[i][j]];
        else
            memory_value = MemoryArray[MemoryArray.length-1];

        return click_difference <= memory_value;
    }

    private void CreateProbableCards(String[] probableCards,String[] OtherCards)
    {
        int length_probableCards =0;
        int length_otherCards = 0;
        for(int i=0;i<CurrentGame.RowSize;i++)
        {
            for(int j=0;j<CurrentGame.ColumnSize;j++)
            {
                if( CurrentGame.IV_AllCards[i][j]!=null) {
                    if (IsCardInMemory(i,j))
                        probableCards[length_probableCards++] = i + DELIMITER + j;
                    else
                        OtherCards[length_otherCards++] = i+DELIMITER+j;
                }
            }
        }
    }

    private void FindBestMatchUsingLogic3()
    {
        int boardSize = CurrentGame.ColumnSize*CurrentGame.RowSize;
        String [] probableCards = new String[boardSize];
        String[] availableCards = new String[boardSize];
        CreateProbableCards(probableCards, availableCards);
        int indexOfDelimiter,row,col;
        String item;


        //Case 1 : Only one card is left on board
        if(getLengthOfDynamicArray( availableCards) == 0)
        {
            item = probableCards[0];
            indexOfDelimiter = item.indexOf('_');
            row = Integer.parseInt(item.substring(0, indexOfDelimiter));
            col = Integer.parseInt(item.substring(indexOfDelimiter + 1));
            ClickAndFocusOnView(CurrentGame.IV_AllCards[row][col]);
            return;
        }


        //Case 2 : Find a match (*hopefully*)
        indexOfDelimiter = CurrentGame.FirstCard.getTag().toString().indexOf(('_'));
        row = Integer.parseInt(CurrentGame.FirstCard.getTag().toString().substring(0, indexOfDelimiter));
        col = Integer.parseInt(CurrentGame.FirstCard.getTag().toString().substring(indexOfDelimiter + 1));
        int required_resID = CurrentGame.Cards_ImageResID[row][col];

        for(String Item : probableCards)
        {
            if(Item == null)
                break;

            indexOfDelimiter = Item.indexOf('_');
            row = Integer.parseInt(Item.substring(0, indexOfDelimiter));
            col = Integer.parseInt(Item.substring(indexOfDelimiter + 1));
            if(CurrentGame.Cards_ImageResID[row][col] == required_resID)
            {
                ClickAndFocusOnView(CurrentGame.IV_AllCards[row][col]);
                return;
            }
        }

        //Case 3 : No luck. Click random card
        int length = getLengthOfDynamicArray( availableCards);
        int randomIndex = ((int) (Math.random() * 1000)) % length;
        item = availableCards[randomIndex];
        indexOfDelimiter = item.indexOf('_');
        row = Integer.parseInt(item.substring(0, indexOfDelimiter));
        col = Integer.parseInt(item.substring(indexOfDelimiter + 1));
        ClickAndFocusOnView(CurrentGame.IV_AllCards[row][col]);
    }

    private ImageView getMatchingCardUsingLogic3(String[]probableCards)
    {
        ImageView card1 = null;
        int probableCards_length =getLengthOfDynamicArray( probableCards);
        int [] resId_array = new int[probableCards_length];
        int resIdArray_length = 0;

        for(int i=0;i<probableCards_length;i++)
        {
            String item=probableCards[i];
            int indexOfDelimiter=item.indexOf('_');
            int row = Integer.parseInt (item.substring(0,indexOfDelimiter));
            int col = Integer.parseInt(item.substring(indexOfDelimiter+1));
            resId_array[resIdArray_length++] = CurrentGame.Cards_ImageResID[row][col];
        }
        Arrays.sort(resId_array);
        int indexOfMatch = -1;

        for(int i=0;i<probableCards_length-1;i++)
        {
            if(resId_array[i] == resId_array[i+1]  )
            {
                indexOfMatch=i;
                break;
            }
        }
        if(indexOfMatch!=-1)
        {
            int resId = resId_array[indexOfMatch];
            for(int i=0;i<probableCards_length;i++)
            {
                String item = probableCards[i];
                int indexOfDelimiter=item.indexOf('_');
                int row = Integer.parseInt (item.substring(0,indexOfDelimiter));
                int col = Integer.parseInt(item.substring(indexOfDelimiter+1));
                if( CurrentGame.Cards_ImageResID[row][col] == resId)
                {
                    if(card1==null)
                        card1 = CurrentGame.IV_AllCards[row][col];
                    else
                    {
                        bufferCard =  CurrentGame.IV_AllCards[row][col];
                        break;
                    }
                }
            }
        }
        return card1;
    }
    private void SimulateFirstClickForLogic3()
    {
        int boardSize = CurrentGame.ColumnSize*CurrentGame.RowSize;
        String [] probableCards = new String[boardSize];
        String[] availableCards = new String[boardSize];
        CreateProbableCards(probableCards,availableCards);

        ImageView card1;
        card1 = getMatchingCardUsingLogic3(probableCards);

        //Find Matching pairs in Memory
        if(card1 != null)
        {
            ClickAndFocusOnView(card1);
        }
        else //Do a random click
        {
            int length = getLengthOfDynamicArray(availableCards);
            String item;
            if(length == 0)//Base case When number of cards on board is odd
            {
                item = probableCards[0];
            }
            else
            {
                int randomIndex = ((int) (Math.random() * 1000)) % length;
                item = availableCards[randomIndex];
            }
            int indexOfDelimiter = item.indexOf('_');
            int row = Integer.parseInt(item.substring(0, indexOfDelimiter));
            int col = Integer.parseInt(item.substring(indexOfDelimiter + 1));
            ClickAndFocusOnView(CurrentGame.IV_AllCards[row][col]);
        }
    }

    private void SimulateCardClickUsingMemoryLogic3()
    {

        if(CurrentGame.EffectiveClickCount%2 == 0)
        {
            SimulateFirstClickForLogic3();
            return;
        }
        else
        {
            if( bufferCard != null) {
                ClickAndFocusOnView(bufferCard);
                bufferCard = null;
            }
            else
            {
                FindBestMatchUsingLogic3();
            }
        }
    }

    //endregion


    //endregion

}
