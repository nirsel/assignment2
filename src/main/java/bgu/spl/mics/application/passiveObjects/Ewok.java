package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	int serialNumber;
	boolean available;


    public Ewok(int number){
        serialNumber=number;
        available=true;
    }


    /**
     * Acquires an Ewok
     */
    public synchronized void acquire() {
        while (!available){
            try {wait();}
            catch (InterruptedException e){}
        }
        available=false;
    }

    /**
     * release an Ewok
     */
    public synchronized void release() {
        available=true;
        notifyAll();
    }

    /**
     * @return boolean available = true if the ewok is available, false otherwise.
     */
    public boolean isAvailable(){
        return available;
    }
}
