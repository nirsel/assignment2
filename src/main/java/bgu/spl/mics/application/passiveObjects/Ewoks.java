package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
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
    private static Vector<Ewok> ewokList;

    public synchronized static void getEwoks(List<Integer> resources){
        for (Integer num:resources){
            if (ewokList.get(num).isAvailable())
                ewokList.get(num).acquire();
            //todo: complete if the ewok is not available
        }
    }

    public static void releaseEwoks(List<Integer> resources){
        for (Integer num:resources)
            ewokList.get(num).release();
    }
}
