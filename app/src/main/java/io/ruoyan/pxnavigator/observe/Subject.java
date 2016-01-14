package io.ruoyan.pxnavigator.observe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruoyan on 1/13/16.
 */
public abstract class Subject {
    private List<Observer> list = new ArrayList<Observer>();

    public void attach(Observer observer){

        list.add(observer);
        System.out.println("Attached an observer");
    }

    public void detach(Observer observer){

        list.remove(observer);
    }

    public void nodifyObservers(){

        for(Observer observer : list){
            observer.update(this);
        }
    }
}
