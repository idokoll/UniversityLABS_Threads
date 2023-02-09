package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.PublishResultsEvent;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {
    private String name;
    private int date;
    private ConcurrentHashMap<Student, LinkedList<Model>> confrenceMap;
    private Model[] publications;
    public ConfrenceInformation(String name, int date){
        this.name = name;
        this.date = date;
        confrenceMap = new ConcurrentHashMap<Student, LinkedList<Model>>();
    }

    public String getName() {
        return name;
    }

    public int getDate() {
        return date;
    }

    public ConcurrentHashMap<Student, LinkedList<Model>> getConfrenceMap() {
        return confrenceMap;
    }

    public void addModel(PublishResultsEvent pre){
        if(confrenceMap == null)
            confrenceMap = new ConcurrentHashMap<Student, LinkedList<Model>>();
        if(confrenceMap.get(pre.getSender()) == null){
            confrenceMap.put(pre.getSender(), new LinkedList<Model>());
        }
        confrenceMap.get(pre.getSender()).add(pre.getModel());
    }
    public void createPublications(){
        LinkedList<Model> pubs = new LinkedList<Model>();
        if(confrenceMap == null){
            confrenceMap = new ConcurrentHashMap<Student, LinkedList<Model>>();
        }
        for (Student s:confrenceMap.keySet()) {
            pubs.addAll(confrenceMap.get(s));
        }
        publications = new Model[pubs.size()];
        for (int i = 0; i < publications.length; i++) {
            publications[i] = pubs.get(i);
        }
    }

    public Model[] getPublications() {
        return publications;
    }
}
