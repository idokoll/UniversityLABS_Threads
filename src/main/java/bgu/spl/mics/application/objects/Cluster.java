package bgu.spl.mics.application.objects;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {
	private Output output;
	private ConcurrentHashMap<Integer, GPU> gpus;
	private ConcurrentLinkedQueue<CPU> cpus;
	private ConcurrentLinkedQueue<ArrayList<DataBatch>> disk;
	private AtomicInteger gpuUsed;
	private AtomicInteger cpuUsed;
	private AtomicInteger batchesProcessed;
	private static class singeltonHolder{
		private static Cluster instance = new Cluster();
	}
	private Cluster(){
		gpus = new ConcurrentHashMap<Integer, GPU>();
		cpus = new ConcurrentLinkedQueue<CPU>();
		disk = new ConcurrentLinkedQueue<ArrayList<DataBatch>>();
		gpuUsed = new AtomicInteger();
		cpuUsed = new AtomicInteger();
		batchesProcessed = new AtomicInteger();
		output = Output.getInstance();
	}
	/**
     * Retrieves the single instance of this class.
     */
	public static Cluster getInstance() {
		return singeltonHolder.instance;
	}
	public void registerGPU(GPU g){
		gpus.put(g.getKey(),g);
	}
	public void registerCPU(CPU c){
		cpus.add(c);
	}
	public synchronized void takeBatch(CPU c) throws InterruptedException {
		if (!disk.isEmpty()) {
			c.setData(disk.remove());
		}
	}
	public synchronized void sendDataCollection(ArrayList<DataBatch> c){
		disk.add(c);
	}
	public synchronized void returnBatch(DataBatch db,int time){
		gpus.get(db.getSender()).receiveBatch(db);
		if(!gpus.get(db.getSender()).isWorking()){
			gpus.get(db.getSender()).setStartTime(time);
			gpus.get(db.getSender()).setWorking(true);
		}

	}


	public void setBatchesProcessed() {
		output.setBatchesProcessed();
	}

	public void setCpuUsed() {
		output.setCpuTimeUsed();
	}

	public void setGpuUsed() {
		output.setGpuTimeUsed();
	}

	public ConcurrentLinkedQueue<ArrayList<DataBatch>> getDisk() {
		return disk;
	}
}
