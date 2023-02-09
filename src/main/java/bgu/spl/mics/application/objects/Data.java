package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {
    /**
     * Enum representing the Data type.
     */
    enum Type {
        Images, Text, Tabular
    }

    private Type type;
    private int processed;
    private int size;
    public Data(Type t, int s){
        type = t;
        size = s;
        processed = 0;

    }
    public int getProcessed(){
        return this.processed;
    }
    public int getSize() {
        return size;
    }

    public Type getType() {
        return type;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }
}
