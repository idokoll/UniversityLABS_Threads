package bgu.spl.mics.application.objects;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {
    /**
     * @INV: data.size()>=0 && Cluster != null && cores != 0
     */
    private int cores;
    private ArrayList<DataBatch> data;
    private Cluster cluster;
    private int starttime;
    private String name;
    public CPU(int cores){
        cluster = Cluster.getInstance();
        this.cores = cores;
        data = new ArrayList<DataBatch>();
        cluster.registerCPU(this);
    }
    public void takeBatch() throws InterruptedException {
        cluster.takeBatch(this);
    }

    public int getStarttime() {
        return starttime;
    }

    public void setData(ArrayList<DataBatch> data) {
        this.data = data;
    }

    public ArrayList<DataBatch> getData() {
        return data;
    }

    public int getCores() {
        return cores;
    }
    public boolean process(int tick){
        cluster.setCpuUsed();
        if (getData().get(0).getData().getType() == Data.Type.Images){
            if(tick - starttime == ((32/cores) * 4)){
                cluster.setBatchesProcessed();
                cluster.returnBatch(data.remove(0),tick);
                return true;
            }
        }
        else if (getData().get(0).getData().getType() == Data.Type.Text){
            if(tick - starttime == ((32/cores) * 2)){
                cluster.setBatchesProcessed();
                cluster.returnBatch(data.remove(0),tick);
                return true;
            }
        }
        else if (getData().get(0).getData().getType() == Data.Type.Tabular){
            if(tick - starttime == (32/cores) ){
                cluster.setBatchesProcessed();
                cluster.returnBatch(data.remove(0),tick);
                return true;
            }
        }
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public Cluster getCluster() {
        return cluster;
    }
}