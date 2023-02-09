package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Output;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    private ConfrenceInformation confrence;
    public ConferenceService(String name, ConfrenceInformation ci) {
        super(name);
        confrence = ci;
    }

    @Override
    protected void initialize() {
       subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast tb)->{
           confrence.createPublications();
           Output o = Output.getInstance();
           o.setConfrences(confrence);
           terminate();
       });
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tb)->{
            if(tb.getTime() == confrence.getDate()){
                sendBroadcast(new PublishConferenceBroadcast(confrence.getConfrenceMap()));
                confrence.createPublications();
                Output o = Output.getInstance();
                o.setConfrences(confrence);
                terminate();
            }
        });
        subscribeEvent(PublishResultsEvent.class, (PublishResultsEvent pre)->{
            confrence.addModel(pre);
            complete(pre, pre.getModel());
        });


    }
}
