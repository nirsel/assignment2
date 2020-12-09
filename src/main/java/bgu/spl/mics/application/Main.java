package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/** This is the Main class of the application. You should parse the input file, 
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws InterruptedException, IOException {
		Input input=JsonInputReader.getInputFromJson("input.json");
		Ewoks ewoks=Ewoks.getInstance();
		ewoks.setEwoksList(input.getEwoks());
		CountDownLatch latch=new CountDownLatch(4);
		HanSoloMicroservice han=new HanSoloMicroservice();
		han.setLatch(latch);
		C3POMicroservice c3po=new C3POMicroservice();
		c3po.setLatch(latch);
		R2D2Microservice r2d2=new R2D2Microservice(input.getR2D2());
		r2d2.setLatch(latch);
		LandoMicroservice lando=new LandoMicroservice(input.getLando());
		lando.setLatch(latch);
		Thread t1=new Thread(han);
		t1.start();
		Thread t2=new Thread(r2d2);
		t2.start();
		Thread t3=new Thread(c3po);
		t3.start();
		Thread t4=new Thread(lando);
		t4.start();
		latch.await(); //main thread waits until other microservices initialized and waiting for messages
		Thread t5=new Thread(new LeiaMicroservice(input.getAttacks()));
		t5.start();
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		Gson testBuilderJson = new GsonBuilder().create();
		try{
			FileWriter fileWriter = new FileWriter("output.json");
			testBuilderJson.toJson(Diary.getInstance(),fileWriter);
			fileWriter.flush();
			fileWriter.close();
		}catch(Exception fileWriteException){

		}
	}

}
