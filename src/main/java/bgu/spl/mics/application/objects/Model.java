package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {
    enum Status {
        PreTrained, Trained, Training, Tested
    }
    enum Result {
        None, Good, Bad
    }
    private String name;
    private Data data;
    private Status status;
    private Result result;

    public Model(String name,Data data){
        this.name=name;
        this.data=data;
        result=Result.None;
        status=Status.PreTrained;
    }

    public Status getStatus(){
        return this.status;
    }
    public Data getData(){
        return this.data;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setResult(boolean flag) {
        if (flag)
            result = Result.Good;
        else
            result = Result.Bad;
        status = Status.Tested;

    }
    public boolean getResult(){
        if (result == Result.Good)
            return true;
        else
            return false;
    }

    public String getName() {
        return name;
    }
}
