package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;

public class DummyMS extends MicroService {

    protected static Integer num=0; // todo: check

    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public DummyMS(String name) {
        super(name);
    }

    public Integer getNum() {
        return num;
    }

    @Override
    protected void initialize() {
    }

    public static class CallbackTest implements Callback<TerminateBroadcast>{

        @Override
        public void call(TerminateBroadcast c) throws InterruptedException {
                DummyMS.num = 2;
        }
    }
}
