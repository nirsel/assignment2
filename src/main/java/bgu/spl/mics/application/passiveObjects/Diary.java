package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private AtomicInteger totalAttacks=new AtomicInteger(0);
    private long HanSoloFinish=0;
    private long C3POFinish=0;
    private long R2D2Deactivate=0;
    private long LeiaTerminate=0;
    private long HanSoloTerminate=0;
    private long R2D2Terminate=0;
    private long C3POTerminate=0;
    private long LandoTerminate=0;

    private Diary(){}

    public static Diary getInstance(){
        return DiaryHolder.diaryInstance;
    }

    public int getTotalAttacks() {
        return totalAttacks.intValue();
    }

    public void setTotalAttacks() {
        totalAttacks.incrementAndGet();
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public void setHanSoloFinish(long hanSoloFinish) {
        HanSoloFinish = hanSoloFinish;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public void setR2D2Deactivate(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }

    public AtomicInteger getNumberOfAttacks(){return totalAttacks;}

    public void resetNumberAttacks(){totalAttacks=new AtomicInteger(0);}

    private static class DiaryHolder{
        private static Diary diaryInstance=new Diary();
    }



}
