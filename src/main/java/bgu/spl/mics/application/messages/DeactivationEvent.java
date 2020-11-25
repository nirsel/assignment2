package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class DeactivationEvent implements Event<Boolean> {
    private Future<Boolean> result;

    public void setResult(Future<Boolean> result) {
        this.result = result;
    }
}
