package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class TestModelEvent implements Event<Model> {
    private double successrate;
    private Model model;
    private String name;
    public TestModelEvent(double s, Model m){
        successrate = s;
        model = m;
        name = "TestModelEvent";
    }
    public double getSuccessrate() {
        return successrate;
    }
    public Model getModel() {
        return model;
    }
    public String getName() {
        return name;
    }
}
