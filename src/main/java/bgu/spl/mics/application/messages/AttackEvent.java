package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Attack;

public class AttackEvent implements Event<Boolean> {
	private Future<Boolean> result;

	@Override
	public void setResult(Future<Boolean> result) {
		this.result = result;
	}

}
