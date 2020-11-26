package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;

public class TerminateBroadcast implements Broadcast {
    private Integer num =0;

    public void setNum(Integer num) {
        this.num = num;
    }
}
