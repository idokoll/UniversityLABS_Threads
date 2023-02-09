package bgu.spl.mics.application.objects;

public class Univ {
    private Student[] Students;
    private GPU.Type[] GPUS;
    private int[] CPUS;
    private ConfrenceInformation[] Conferences;
    private int TickTime;
    private int Duration;

    public Univ(Student[] Students, GPU.Type[] GPUS, int[] CPUS, ConfrenceInformation[] Conferences, int TickTime, int Duration){
        this.Students=Students;
        this.GPUS=GPUS;
        this.CPUS=CPUS;
        this.Conferences=Conferences;
        this.TickTime=TickTime;
        this.Duration=Duration;
    }
    public GPU[] createGPUS(){
        GPU[] realGPUS = new GPU[GPUS.length];
        for (int i = 0; i < realGPUS.length; i++) {
            realGPUS[i] = new GPU(GPUS[i], i);
        }
        return realGPUS;
    }
    public CPU[] createCPUS(){
        CPU[] realCPUS = new CPU[CPUS.length];
        for (int i = 0; i < realCPUS.length; i++) {
            realCPUS[i] = new CPU(CPUS[i]);
        }
        return realCPUS;
    }
    public Student[] getStudents() {
        return Students;
    }

    public int[] getCPUS() {
        return CPUS;
    }

    public GPU.Type[] getGPUS() {
        return GPUS;
    }

    public int getTickTime() {
        return TickTime;
    }

    public int getDuration() {
        return Duration;
    }

    public ConfrenceInformation[] getConferences() {
        return Conferences;
    }
}
