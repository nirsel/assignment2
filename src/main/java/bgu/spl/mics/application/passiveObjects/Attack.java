package bgu.spl.mics.application.passiveObjects;

import java.util.List;


/**
 * Passive data-object representing an attack object.
 * You must not alter any of the given public methods of this class.
 * <p>
 * YDo not add any additional members/method to this class (except for getters).
 */
public class Attack {
    final List<Integer> serials;
    final int duration;

    /**
     * Constructor.
     */
    public Attack(List<Integer> serialNumbers, int duration) {
        this.serials = serialNumbers;
        this.duration = duration;
    }

    /**
     * Getter of duration data member of this class.
      * @return  int duration -  the duration of sleep needed in order to complete this task.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Getter of the serials of the ewoks needed for this Attack.
     * @return List<Integer> serials - the List of serial number of the ewoks needed.
     */
    public List<Integer> getSerials() {
        return serials;
    }
}
