package com.example.swsahu.duplicatecardgame;

public class MyStack {

    int length;
    int top;
    String array[];

    MyStack(int l)
    {
        array = new String[l];
        length = l;
        top = -1;
    }

    public void clear()
    {
        top=-1;
    }

    private void shiftBackAtIndex(int index)
    {
        System.arraycopy(array, index + 1, array, index, top - index);
        top--;
    }
    public void push(String item)
    {

        if(findItem(item)<0) {

            if (top+1 == length)
                shiftBackAtIndex(0);

            array[++top] = item;
        }
        else
        {
            shiftBackAtIndex(findItem(item));
            array[++top] = item;
        }
    }

    public int findItem(String item)
    {
        for(int i = 0;i<=top;i++ )
        {
            if(array[i].equals(item))
                return i;
        }
        return -1;
    }

    public int findCard(int row,int col,int I[][])
    {
        for(int i = 0;i<=top;i++ )
        {
            String item = array[i];
            int indexOfDelimiter=item.indexOf('_');
            int this_row = Integer.parseInt (item.substring(0,indexOfDelimiter));
            int this_col = Integer.parseInt(item.substring(indexOfDelimiter+1));
            int Img_resource = I[this_row][this_col];
            if(Img_resource == I[row][col] && !(this_row==row && this_col==col))
                return i;
        }
        return -1;
    }

    public void removeItem(String item)
    {
        int indexOfItem = findItem(item);
        if(indexOfItem>-1)
            shiftBackAtIndex(indexOfItem);
    }
}
