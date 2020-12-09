package bgu.spl.mics.application.passiveObjects;

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


    /**
     * Sets the list of ewoks in ewokList.
     * @param num - represents the number of ewoks.
     */
    public void setEwoksList(int num){
        ewokList=new Vector<Ewok>();
        for (int i=1;i<=num;i++)
            ewokList.add(new Ewok(i));
    }

    /**
     * @return an instance of Ewoks.
     */
    public static Ewoks getInstance(){
        return EwoksHolder.instance;
    }

    /**
     * Acquires the needed ewoks listed in resources.
     * @param resources - List of required ewoks to be acquired from this.
     */
    public void acquireEwoks(List<Integer> resources){
        resources.sort(Comparator.comparingInt(o -> o)); //sorts the resources list to avoid deadlocks
        for (Integer num:resources)
            ewokList.get(num-1).acquire();
    }


    /**
     * Release the ewoks.
     * @param resources - list of ewoks to be released.
     */
    public void releaseEwoks(List<Integer> resources){
        for (Integer num:resources){
            ewokList.get(num-1).release();
        }
    }
}


