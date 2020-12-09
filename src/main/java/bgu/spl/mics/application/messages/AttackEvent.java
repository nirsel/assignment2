package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * AttackEvent class represents an AttackEvent instance which Leia sends using the MessageBus.
 * This class implements Event<Boolean>, where the Boolean represents if the event completed successfully or not.
 * Each instance of AttackEvents holds an "info" field of type "Attack" where the relevant information is held.
 */
public class AttackEvent implements Event<Boolean> {
    private Attack info;

/**
* Public constructor of this class.
* @param info the Attack instance which holds the information about the ewoks needed and the duration to sleep in order
* to complete the event.
 */
    public AttackEvent(Attack info){
        this.info=info;
    }

/**
* A getter used to retrieve the information regarding the AttackEvent instance.
* @return return the Attack data member of this instance.
*
*/
    public Attack getInfo(){return info;}

}
