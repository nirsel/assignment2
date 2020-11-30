package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MessageBusImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;


/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private Vector<Ewok> ewokList;

    private static class EwoksHolder{
        private static Ewoks instance=new Ewoks();
    }

    public static Ewoks getInstance(){
        return EwoksHolder.instance;
    }

    public void acquireEwoks(List<Integer> resources){
        resources.sort(Comparator.comparingInt(o -> o)); //sorts the resources list to avoid deadlocks
        for (Integer num:resources){
            if (ewokList.get(num).isAvailable())
                ewokList.get(num).acquire();
            else {
                    synchronized (this) {
                        while (!ewokList.get(num).isAvailable()) {
                            try {
                                wait();
                            }
                            catch (InterruptedException e) {
                            }

                        }
                    }
            }
        }
    }

    public void releaseEwoks(List<Integer> resources){
        for (Integer num:resources)
            ewokList.get(num).release();
        synchronized (this){notifyAll();}
    }
}
