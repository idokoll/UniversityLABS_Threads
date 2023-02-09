package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) throws FileNotFoundException {

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(args[0]));
        Univ bgu = gson.fromJson(reader, Univ.class);
        GPU[] GPUS = bgu.createGPUS();
        CPU[] CPUS = bgu.createCPUS();
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < GPUS.length; i++) {
            GPUService g = new GPUService("GPU" + i,GPUS[i]);
            Thread gpu = new Thread(g);
            gpu.start();
            threads.add(gpu);
        }
        for (int i = 0; i < CPUS.length; i++) {
            CPUService c = new CPUService("CPU" + i,CPUS[i]);
            Thread cpu = new Thread(c);
            cpu.start();
            threads.add(cpu);
        }
        for (int i = 0; i < bgu.getStudents().length; i++) {
            bgu.getStudents()[i].createRealModels();
            StudentService s = new StudentService("Student" + i,bgu.getStudents()[i]);
            Thread student = new Thread(s);
            student.start();
            threads.add(student);
        }
        for (int i = 0; i < bgu.getConferences().length; i++) {
            ConferenceService c = new ConferenceService("Conference " + i,bgu.getConferences()[i]);
            Thread conference = new Thread(c);
            conference.start();
            threads.add(conference);
        }
        TimeService clock = new TimeService(bgu.getTickTime(), bgu.getDuration());
        Thread time = new Thread(clock);
        time.start();
        threads.add(time);
        try {
            for (Thread t: threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Output o = Output.getInstance();
        o.createConarray();
        o.createStuarray();
        JsonOutput jo = new JsonOutput(o);
        try(Writer writer = new FileWriter("output.json")){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder = gsonBuilder.setPrettyPrinting();
            Gson json = gsonBuilder.create();
            json.toJson(jo, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
