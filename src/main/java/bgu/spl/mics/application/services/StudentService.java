package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Output;
import bgu.spl.mics.application.objects.Student;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
enum Status{
    None, Trained,Tested,Published
}
public class StudentService extends MicroService {
    private Student student;
    private int currentModel;
    private Future currentFuture;
    private Status status;
    public StudentService(String name, Student student) {
        super(name);
        this.student = student;
        currentModel = 0;
        currentFuture = null;
        status = Status.None;
    }


    @Override
    protected void initialize() {

       subscribeBroadcast(TickBroadcast.class , (TickBroadcast tickBroadcast)->{
           if(currentModel<student.getRealModels().length && currentFuture == null){
               currentFuture = sendEvent(new TrainModelEvent(student.getRealModels()[currentModel]));
               status = Status.Trained;
           }
           else  if(currentModel<student.getRealModels().length && currentFuture.isDone()){
                   if(status == Status.Published) {
                       currentFuture = sendEvent(new TrainModelEvent(student.getRealModels()[currentModel]));
                       status = Status.Trained;
                   }
                    else if(status == Status.Trained){
                        currentFuture = sendEvent(new TestModelEvent(student.getSuccsessRate(), student.getRealModels()[currentModel]));
                        status = Status.Tested;
                    }
                    else if(status == Status.Tested){
                        if(student.getRealModels()[currentModel].getResult()) {
                            currentFuture = sendEvent(new PublishResultsEvent(student.getRealModels()[currentModel], student));
                        }
                       status = Status.Published;
                       currentModel++;
                    }
               }
       });
        subscribeBroadcast(PublishConferenceBroadcast.class , (PublishConferenceBroadcast pcb)->{
            if(pcb.getConfrenceMap() != null) {
                student.setPublishedModels(pcb.getConfrenceMap().get(student));
                for (Student s : pcb.getConfrenceMap().keySet()) {
                    if(s != student) {
                        student.setPapersRead(pcb.getConfrenceMap().get(s).size());
                    }
                }
            }
        });
        subscribeBroadcast(TerminationBroadcast.class, (TerminationBroadcast tb)->{
            Output o = Output.getInstance();
            o.setStudents(student);
            terminate();
        });

    }
}
