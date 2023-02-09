package bgu.spl.mics.application.objects;

import java.util.LinkedList;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private Project[] models;
    private Model[] realModels;
    public Student(){}
    public void createRealModels(){
        realModels = new Model[models.length];
        int counter = 0;
        for (Project p : models) {
            Data d;
            if(p.getType().equals("Images")) {
                d = new Data(Data.Type.Images, p.getSize());
            }
            else if(p.getType().equals("Text")) {
                d = new Data(Data.Type.Text, p.getSize());
            }
            else {
                d = new Data(Data.Type.Tabular, p.getSize());
            }
            realModels[counter] = new Model(p.getName(), d);
            counter++;
        }
    }

    public Model[] getRealModels() {
        return realModels;
    }
    public double getSuccsessRate(){
        if(status == Degree.MSc)
            return 0.6;
        else
            return 0.8;
    }

    public void setPapersRead(int papersRead) {
        this.papersRead += papersRead;
    }
    public void setPublishedModels(LinkedList<Model> publishedModels) {
        if(publishedModels != null) {
            for (Model m : publishedModels) {
                publications++;
            }
        }
    }

    public String getName() {
        return name;
    }

    public Degree getStatus() {
        return status;
    }

    public int getPapersRead() {
        return papersRead;
    }

    public int getPublications() {
        return publications;
    }

    public String getDepartment() {
        return department;
    }

}
