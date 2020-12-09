package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
/**
 * DeactivationEvent class represents a DeactivationEvent instance which Leia sends using the MessageBus to R2D2.
 * This class implements Event<Boolean>, where the Boolean represents if the event completed successfully or not.
 */
public class DeactivationEvent implements Event<Boolean> {

}
