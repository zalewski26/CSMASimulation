package org.example;

public class App
{
    public static void main( String[] args )
    {
        CSMA obj = new CSMA( 10, 2);
        for (int i = 0; i < 1000; i++){
            obj.performAction();
            obj.print();
            System.out.println("-----------------------------");
        }
    }
}
