package bgu.spl.mics.application;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

/** This is the Main class of the application. You should parse the input file, 
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) throws InterruptedException, IOException {
		Input input=JsonInputReader.getInputFromJson("/home/spl211/IdeaProjects/assignment2/input.json"); //todo:check how to get input
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
		Thread t2=new Thread(c3po);
		Thread t3=new Thread(r2d2);
		Thread t4=new Thread(lando);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		latch.await(); //main thread waits until other microservices initialized and waiting for messages
		Thread t5=new Thread(new LeiaMicroservice(input.getAttacks()));
		t5.start();
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		Diary diary=Diary.getInstance();
		System.out.println("total attacks: "+diary.getTotalAttacks());
		System.out.println("HanSolo finish: "+diary.getHanSoloFinish());
		System.out.println("C3PO finish: "+diary.getC3POFinish());
		System.out.println("R2D2 deactivate: "+diary.getR2D2Deactivate());
		System.out.println("Leia terminate: "+diary.getLeiaTerminate());
		System.out.println("HanSolo terminate: "+diary.getHanSoloTerminate());
		System.out.println("C3PO terminate: "+diary.getC3POTerminate());
		System.out.println("R2D2 terminate: "+diary.getR2D2Terminate());
		System.out.println("Lando terminate: "+diary.getLandoTerminate());
		HashMap<String,Object> results=new HashMap<>();
		results.put("totalAttacks",diary.getTotalAttacks());
		results.put("HanSoloFinish",diary.getHanSoloFinish());
		results.put("C3PO finish",diary.getC3POFinish());
		results.put("R2D2 deactivate",diary.getR2D2Deactivate());
		results.put("Leia terminate",diary.getLeiaTerminate());
		results.put("HanSolo terminate",diary.getHanSoloTerminate());
		results.put("C3PO terminate",diary.getC3POTerminate());
		results.put("R2D2 terminate",diary.getR2D2Terminate());
		results.put("Lando terminate",diary.getLandoTerminate());
		Gson testBuilderJson = new GsonBuilder().create();
		try{
			FileWriter fileWriter = new FileWriter("./output.json");
			testBuilderJson.toJson(results,fileWriter);
			fileWriter.flush();
			fileWriter.close();
		}catch(Exception fileWriteException){

		}
	}

}
