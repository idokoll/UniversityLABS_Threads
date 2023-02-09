package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch {
    private Integer sender;
    private Data data;
    private int start_index;
    public DataBatch(Integer sender,Data data){
        this.sender = sender;
        this.data = data;
    }
    public Integer getSender() {
        return sender;
    }
    public Data getData() {
        return data;
    }

}
