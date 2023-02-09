package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<Model> {
    private Model model;
    private String name;
    public TrainModelEvent(Model model){
        this.model = model;
        name = "TrainModelEvent " + model.getName();
    }

    public Model getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

}
