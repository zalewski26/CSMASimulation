package org.example;

import java.util.ArrayList;
import java.util.Random;

public class CSMA {
    protected ArrayList<Character>[] arr;
    private final ArrayList<Source> sources;
    private Random random = new Random();

    public CSMA(int length, int numOfSources){
        arr = new ArrayList[length];
        for (int i = 0; i < length; i++){
            arr[i] = new ArrayList<>();
        }
        sources = new ArrayList<>();
        if (numOfSources >= 1)
            sources.add(new Source('A', 0, this, 0.5));
        if (numOfSources >= 2)
            sources.add(new Source('B',length - 1, this, 0.5));
        if (numOfSources >= 3){
            int[] lengthArr = prepareArr(length);
            for (int i = 0; i < numOfSources - 2; i++){
                sources.add(new Source((char) ('C' + i),lengthArr[i], this, 0.5));
            }
        }
    }

    public void performAction(){
        for (Source s: sources){
            if (s.isBlocked())
                s.decreaseBlock();
            if (!s.isActive() && !s.isBlocked())
                s.tryActivate();
        }
        for (Source s: sources){
            s.move();
        }
        for (Source s: sources){
            if (!s.isBlocked() && s.isActive())
                s.finish();
        }
    }

    public void print(){
        for (ArrayList a : arr){
            System.out.print(a + " ");
        }
        System.out.println();
    }

    private int[] prepareArr(int length){
        int[] result = new int[length - 2];
        for (int i = 0; i < length - 2; i++){
            result[i] = i + 1;
        }
        for (int i = 0; i < length - 2; i++){
            int temp = result[i];
            int rand = i + random.nextInt(length - 2 - i);
            result[i] = result[rand];
            result[rand] = temp;
        }

        return result;
    }

}
