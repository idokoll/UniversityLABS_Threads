package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Data;

/**
 * CPU service is responsible for handling the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {
    private CPU cpu;
    public CPUService(String name, CPU cpu) {
        super(name);
        this.cpu = cpu;
        cpu.setName(name);
    }

    @Override
    protected void initialize() {
    subscribeBroadcast(TickBroadcast.class, (TickBroadcast tb) ->{
        if (cpu.getData().isEmpty()){
            cpu.setStarttime(tb.getTime());
            cpu.takeBatch();
        }
        if (!cpu.getData().isEmpty()) {
            if(cpu.process(tb.getTime())) {
                cpu.setStarttime(tb.getTime());
            }
        }
    });
    subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast tb)->{
        this.terminate();
    });
    }
}
