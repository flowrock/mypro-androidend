package io.ruoyan.pxnavigator.helper;


import io.ruoyan.pxnavigator.observe.Subject;

/**
 * Created by ruoyan on 1/13/16.
 */
public class DayHelper extends Subject {
    private final String[] DAYS = {"1","3","7"};
    private int pos = 0;
    private static DayHelper instance = new DayHelper();

    public static DayHelper instance() {
        return instance;
    }

    public void setDay(int index) {
        pos = index;
        changeDay();
    }

    public String getDay() {
        return DAYS[pos];
    }

    private void changeDay() {
        instance.nodifyObservers();
    }
}
