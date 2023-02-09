package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class PublishConferenceBroadcast implements Broadcast {
    private ConcurrentHashMap<Student, LinkedList<Model>> confrenceMap;
    public PublishConferenceBroadcast(ConcurrentHashMap<Student, LinkedList<Model>> confrenceMap){
        this.confrenceMap = confrenceMap;
    }

    public ConcurrentHashMap<Student, LinkedList<Model>> getConfrenceMap() {
        return confrenceMap;
    }
}
