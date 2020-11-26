package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Future;

public class ExampleBroadcast implements Broadcast {
    public Future<Boolean> flag=null;

    public void setFlag(Boolean result){
        flag.resolve(result);
    }

}
