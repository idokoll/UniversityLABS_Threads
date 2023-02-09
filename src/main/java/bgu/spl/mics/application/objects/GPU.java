package bgu.spl.mics.application.objects;

//import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {
    /**
     * Enum representing the type of the GPU.
     */
    /**
     *@INV:  Cluster != null && Type != null
     */
    enum Type {RTX3090, RTX2080, GTX1080}
    enum dataState {UNPROCESSED, PROCESSEDandTRAINED}

    private Type type;
    private Model model;
    private Cluster cluster;
    private LinkedBlockingQueue<DataBatch> VRAM;
    private Integer key;
    private boolean working;
    private int capacity;
    private int startTime;
    private String name;
    private int modelSize;
    public GPU(Type type, Integer key){
        model = null;
        startTime = 0;
        this.type = type;
        this.key = key;
        working = false;
        this.cluster = Cluster.getInstance();
        cluster.registerGPU(this);
        if(this.type == Type.RTX3090)
            capacity = 32;
        else if(this.type == Type.RTX2080)
            capacity = 16;
        else if(this.type == Type.GTX1080)
            capacity = 8;
        VRAM = new LinkedBlockingQueue<DataBatch>(capacity);
        modelSize = 0;
    }
    public void sendfirstDataColToCluster(){
        ArrayList<DataBatch> d = new ArrayList<DataBatch>(capacity);
        for (int i = 0; i < capacity && modelSize > 0; i++) {
            DataBatch db = new DataBatch(this.key, this.getModel().getData());
            d.add(db);
            modelSize -= 1000;
        }
        cluster.sendDataCollection(d);
        model.setStatus(Model.Status.Training);

    }
    public void sendOneToCluster(){
        if(modelSize > 0){
            ArrayList<DataBatch> db = new ArrayList<DataBatch>(1);
            modelSize -= 1000;
            db.add(new DataBatch(this.key, this.getModel().getData()));
            cluster.sendDataCollection(db);
        }
    }
    public void setModel(Model model) {
        this.model = model;
    }

    public Model getModel(){
    return this.model;
    }
    public Integer getKey(){return this.key;}
    public void receiveBatch(DataBatch db){
        VRAM.add(db);
    }
    public void trainBatch(int tick) {
        cluster.setGpuUsed();
        if (type == Type.RTX3090) {
            if (tick - startTime == 1) {
                model.getData().setProcessed(model.getData().getProcessed() + 1000);
                VRAM.remove();
                sendOneToCluster();
                if (VRAM.isEmpty()) {
                    if (model.getData().getSize() == model.getData().getProcessed()) {
                        model.setStatus(Model.Status.Trained);
                        model = null;
                    }
                    working = false;
                } else
                    startTime = tick;
            }
        } else if (type == Type.RTX2080) {
            if (tick - startTime == 2) {
                model.getData().setProcessed(model.getData().getProcessed() + 1000);
                VRAM.remove();
                sendOneToCluster();
                if (VRAM.isEmpty()) {
                    if (model.getData().getSize() == model.getData().getProcessed()) {
                        model.setStatus(Model.Status.Trained);
                        model = null;
                    }
                    working = false;
                } else
                    startTime = tick;
            }
        } else {
            if (tick - startTime == 4) {
                model.getData().setProcessed(model.getData().getProcessed() + 1000);
                VRAM.remove();
                sendOneToCluster();
                if (VRAM.isEmpty()) {
                    if (model.getData().getSize() == model.getData().getProcessed()) {
                        model.setStatus(Model.Status.Trained);
                        model = null;
                    }
                    working = false;
                } else
                    startTime = tick;
            }
        }
    }

    public LinkedBlockingQueue<DataBatch> getVRAM() {
        return VRAM;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setModelSize(int modelSize) {
        this.modelSize = modelSize;
    }

    public int getModelSize() {
        return modelSize;
    }
}

