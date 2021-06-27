package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Source {
    private char token;
    private int position;
    private int arrLength;
    private CSMA obj;
    private double prob;

    private ArrayList<Signal> signals = new ArrayList<>();
    Random random = new Random();

    private boolean active = false;
    private boolean blocked = false;
    private int blockedCounter;
    private int colisionCounter = 0;

    public Source(char token, int position, CSMA obj, double prob){
        this.token = token;
        this.position = position;
        this.arrLength = obj.arr.length;
        this.obj = obj;
        this.prob = prob;
    }

    public void decreaseBlock(){
        if (blockedCounter > 0) {
            blockedCounter--;
        } else {
            blocked = false;
            System.out.println("Source " + token + " is unblocked");
        }
    }

    public void tryActivate(){
        if (obj.arr[position].size() == 0){
            if (random.nextDouble() > prob)
                sendSignal();
        }
    }

    private void sendSignal(){
        active = true;
        if (position < arrLength - 1)
            signals.add(new Signal(token, position, arrLength, true, obj));
        if (position > 0)
            signals.add(new Signal(token, position, arrLength, false, obj));
    }

    public void move(){
        for (Signal s: signals){
            s.step();
        }
        for (int i = 0; i < signals.size(); i++){
            if (signals.get(i).isTerminated()){
                signals.remove(i);
            }
        }
    }

    public void finish(){
        if (obj.arr[position].size() > 1){
            colisionDetected();
        } else {
            boolean allSent = true;
            for (int i = 0; i < signals.size(); i++){
                if (!signals.get(i).isSent()){
                    allSent = false;
                }
            }
            if (allSent){
                active = false;
                blocked = true;
                blockedCounter = 2*arrLength;
                colisionCounter = 0;
                System.out.println("Source " + token + " successfully sent a signal");
            }
        }
    }

    private void colisionDetected(){
        blocked = true;
        active = false;
        for (Signal s: signals){
            s.stopSending();
        }
        colisionCounter += 1;
        blockedCounter = drawTimeToWait();
        System.out.println("Source " + token + " is blocked for " + blockedCounter + " moves");
    }

    private int drawTimeToWait(){
        int temp = 0;
        if (colisionCounter == 16){
            System.out.println("Impossible to send!");
            System.exit(1);
        } else if (colisionCounter >= 10){
            temp = random.nextInt((int) Math.round(Math.pow(2, 10)));
        } else {
            temp = random.nextInt((int) Math.round(Math.pow(2, colisionCounter)));
        }
        return temp * 2 * arrLength;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isActive() {
        return active;
    }
}
