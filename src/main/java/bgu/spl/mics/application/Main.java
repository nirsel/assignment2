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
		Input input=JsonInputReader.getInputFromJson("input.json"); //todo:check how to get input
		Ewoks ewoks=Ewoks.getInstance();
		ewoks.setEwoksList(input.getEwoks());
		CountDownLatch latch=new CountDownLatch(4);
		new Thread(new HanSoloMicroservice(latch)).start();
		new Thread(new C3POMicroservice((latch))).start();
		new Thread(new R2D2Microservice(input.getR2D2(),latch)).start();
		new Thread(new LandoMicroservice(input.getLando(),latch)).start();
		latch.await(); //main thread waits until other microservices initialized and waiting for messages
		new Thread(new LeiaMicroservice(input.getAttacks())).start();


	}
}
