package com.clocharts.oopid.objs;

public class GameStatusObj {

    private NumBirdObj computerMove;
    private NumBirdObj playerMove;

    public NumBirdObj getComputerMove() {
        return computerMove;
    }

    public void setComputerMove(NumBirdObj computerMove) {
        this.computerMove = computerMove;
    }

    public NumBirdObj getPlayerMove() {
        return playerMove;
    }

    public void setPlayerMove(NumBirdObj playerMove) {
        this.playerMove = playerMove;
    }

    @Override
    public String toString() {
        return "GameStatusObj{" +
                "computerMove=" + computerMove +
                ", playerMove=" + playerMove +
                '}';
    }
}
