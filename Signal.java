package org.example;

public class Signal {
    private boolean sent = false;
    private boolean terminated = false;

    private char token;
    private int startPosition;
    private int arrLength;
    private boolean rightDirection;
    private CSMA obj;
    private int signalCounter = 0;

    public Signal(char token, int startPosition, int arrLength, boolean rightDirection, CSMA obj) {
        this.obj = obj;
        this.arrLength = arrLength;
        this.rightDirection = rightDirection;
        this.startPosition = startPosition;
        this.token = token;
    }

    public void step() {
        int tempCounter = signalCounter;
        if (!rightDirection)
            tempCounter = -tempCounter;
        if (sent) {
            if (startPosition + tempCounter >= 0 && startPosition + tempCounter < arrLength){
                if (obj.arr[startPosition + tempCounter].contains(token))
                    obj.arr[startPosition + tempCounter].remove((Character) token);
            }
            signalCounter++;
            if ((rightDirection && signalCounter == arrLength - startPosition) || (!rightDirection && signalCounter == startPosition + 1))
                terminated = true;
            else {
                tempCounter = signalCounter;
                if (rightDirection){
                    while (startPosition + tempCounter < arrLength){
                        if (obj.arr[startPosition + tempCounter].contains(token)){
                            tempCounter++;
                        } else{
                            obj.arr[startPosition + tempCounter].add(token);
                            break;
                        }
                    }
                } else{
                    tempCounter = -tempCounter;
                    while (startPosition + tempCounter >= 0){
                        if (obj.arr[startPosition + tempCounter].contains(token)){
                            tempCounter--;
                        } else{
                            obj.arr[startPosition + tempCounter].add(token);
                            break;
                        }
                    }
                }
            }
        } else {
            if (startPosition + tempCounter >= 0 && startPosition + tempCounter < arrLength){
                if (!obj.arr[startPosition + tempCounter].contains(token))
                    obj.arr[startPosition + tempCounter].add(token);
            }
            signalCounter++;
            if (signalCounter == arrLength * 2){
                sent = true;
                signalCounter = 0;
            }
        }
    }

    public void stopSending(){
        this.sent = true;
        signalCounter = 0;
    }

    public char getToken() {
        return token;
    }

    public int getSignalCounter() {
        return signalCounter;
    }

    public boolean isSent() {
        return sent;
    }

    public boolean isTerminated() {
        return terminated;
    }
}
