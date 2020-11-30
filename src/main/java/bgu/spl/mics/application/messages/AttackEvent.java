package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Attack;

public class AttackEvent implements Event<Boolean> {
    private Attack info;

    public AttackEvent(Attack info){
        this.info=info;
    }

    public Attack getInfo(){return info;}

}
