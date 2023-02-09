package bgu.spl.mics.application.objects;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Output {
    private LinkedList<Student> slist;
    private Student[] students;
    private LinkedList<ConfrenceInformation> clist;
    private ConfrenceInformation[] confrences;
    private AtomicInteger gpuUsed;
    private AtomicInteger cpuUsed;
    private AtomicInteger batchesProcessed;
    private static class singeltonHolder{
        private static Output instance = new Output();
    }
    private Output(){
        slist = new LinkedList<Student>();
        clist = new LinkedList<ConfrenceInformation>();
        gpuUsed = new AtomicInteger();
        cpuUsed = new AtomicInteger();
        batchesProcessed = new AtomicInteger();
    }
    public static Output getInstance() {
        return Output.singeltonHolder.instance;
    }

    public void setBatchesProcessed() {
        this.batchesProcessed.incrementAndGet();
    }

    public synchronized void setConfrences(ConfrenceInformation confrence) {
        clist.add(confrence);
    }

    public void setCpuTimeUsed() {
        this.cpuUsed.incrementAndGet();
    }

    public void setGpuTimeUsed() {
        this.gpuUsed.incrementAndGet();
    }

    public synchronized void setStudents(Student student) {
        slist.add(student);
    }
    public void createStuarray(){
        students = new Student[slist.size()];
        for (int i = 0; i < students.length; i++) {
            students[i] = slist.get(i);
        }
    }
    public void createConarray(){
        confrences = new ConfrenceInformation[clist.size()];
        for (int i = 0; i < confrences.length; i++) {
            confrences[i] = clist.get(i);
        }
    }

    public Student[] getStudents() {
        return students;
    }

    public int getBatchesProcessed() {
        return batchesProcessed.get();
    }

    public int getCpuTimeUsed() {
        return cpuUsed.get();
    }

    public ConfrenceInformation[] getConfrences() {
        return confrences;
    }

    public int getGpuTimeUsed() {
        return gpuUsed.get();
    }
}