package com.w1nlin4n.practice2;

public class Data {
    private int state=1;

    public int getState() { return state; }

    public void Tic() {
        System.out.print("Tic-");
        state=2;
    }

    public void Tac() {
        System.out.print("Tac-");
        state=3;
    }

    public void Toe() {
        System.out.println("Toe");
        state=1;
    }
}
