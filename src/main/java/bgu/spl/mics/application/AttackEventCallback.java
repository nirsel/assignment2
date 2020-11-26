package bgu.spl.mics.application;


import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.messages.AttackEvent;

public class AttackEventCallback implements Callback<AttackEvent> {
    @Override
    public void call(AttackEvent c) throws InterruptedException {
        c.setResult();
    }
}