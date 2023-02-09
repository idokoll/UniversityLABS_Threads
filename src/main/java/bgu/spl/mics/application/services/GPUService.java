package bgu.spl.mics.application.services;

import bgu.spl.mics.Event;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
* GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {
private GPU g;
private LinkedBlockingQueue<Event> messageQueue;
private Event currentMSG;

    public GPUService(String name, GPU g) {
        super(name);
        messageQueue = new LinkedBlockingQueue<>();
        currentMSG = null;
        this.g = g;
        g.setName(name);

    }

    @Override
    protected void initialize() {
        subscribeEvent(TrainModelEvent.class,(TrainModelEvent tme)->{
            messageQueue.put(tme);
        });
        subscribeEvent(TestModelEvent.class,(TestModelEvent tme)->{
            Event interupptedevent = currentMSG;
            Model interupptedModel = g.getModel();
            currentMSG = tme;
            Random rand = new Random();
            g.setModel(((TestModelEvent) currentMSG).getModel());
            double success = rand.nextDouble();
            if (((TestModelEvent) currentMSG).getSuccessrate() >= success){
                ((TestModelEvent) currentMSG).getModel().setResult(true);
            }
            else{
                ((TestModelEvent) currentMSG).getModel().setResult(false);
            }
            complete(((TestModelEvent) currentMSG),((TestModelEvent) currentMSG).getModel());
            g.setModel(interupptedModel);
            currentMSG = interupptedevent;
        });
        subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast tb)->{
            terminate();
        });
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick)->{
            if(g.getModel() == null){
                if(!messageQueue.isEmpty()) {
                    currentMSG = messageQueue.remove();
                    g.setModel(((TrainModelEvent) currentMSG).getModel());
                    g.setModelSize(((TrainModelEvent) currentMSG).getModel().getData().getSize());
                    g.sendfirstDataColToCluster();
                }
            }
            else {
                if(!g.getVRAM().isEmpty() && !g.isWorking()){
                    g.setStartTime(tick.getTime());
                    g.setWorking(true);
                }
                 if(!g.getVRAM().isEmpty()) {
                    g.trainBatch(tick.getTime());
                }
                if(g.getModel() == null) {
                    complete(((TrainModelEvent) currentMSG), ((TrainModelEvent) currentMSG).getModel());
                    if(!messageQueue.isEmpty()) {
                        currentMSG = messageQueue.remove();
                        g.setModel(((TrainModelEvent) currentMSG).getModel());
                        g.setModelSize(((TrainModelEvent) currentMSG).getModel().getData().getSize());
                        g.sendfirstDataColToCluster();
                    }
                }
            }
        });


    }

}
