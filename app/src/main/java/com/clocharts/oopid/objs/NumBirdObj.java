package com.clocharts.oopid.objs;

public class NumBirdObj {

    private Integer number;
    private boolean isTakeAll;
    private boolean isMad;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public boolean isTakeAll() {
        return isTakeAll;
    }

    public void setTakeAll(boolean takeAll) {
        isTakeAll = takeAll;
    }

    public boolean isMad() {
        return isMad;
    }

    public void setMad(boolean mad) {
        isMad = mad;
    }

    @Override
    public String toString() {
        return "NumBirdObj{" +
                "number=" + number +
                ", isTakeAll=" + isTakeAll +
                ", isMad=" + isMad +
                '}';
    }
}
