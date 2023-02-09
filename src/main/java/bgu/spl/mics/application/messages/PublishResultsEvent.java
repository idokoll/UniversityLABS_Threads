package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class PublishResultsEvent implements Event<Model> {
    private Model model;
    private Student sender;
    public PublishResultsEvent(Model model, Student s){
        this.model = model;
        this.sender =s;
    }

    public Model getModel() {
        return model;
    }

    public Student getSender() {
        return sender;
    }
}
