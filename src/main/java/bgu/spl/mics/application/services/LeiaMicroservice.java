package bgu.spl.mics.application.services;
import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private Future[] results;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		results=new Future[this.attacks.length];
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class,(event)->{
            Diary.getInstance().setLeiaTerminate(System.currentTimeMillis());
            terminate();});
        for (int i=0;i<attacks.length;i++){
            results[i]=sendEvent(new AttackEvent(attacks[i]));
        }

        for (int i=0;i<results.length;i++){
            results[i].get();
        }
        sendEvent(new DeactivationEvent()).get();
        sendEvent(new BombDestroyerEvent());


    }

}
