package bgu.spl.mics.application.objects;

import java.util.LinkedList;

public class JsonOutput {
    private class newStudent{
        private String name;
        private String department;
        private Student.Degree status;
        private int publications;
        private int papersRead;
        private newModel[] trainedModels;
        public newStudent(Student s){
            name = s.getName();
            department = s.getDepartment();
            status = s.getStatus();
            publications = s.getPublications();
            papersRead = s.getPapersRead();
            LinkedList<newModel> mods = new LinkedList<newModel>();
            for (Model m: s.getRealModels()) {
                if(m.getStatus() == Model.Status.Tested){
                    mods.add(new newModel(m));
                }
            }
            trainedModels = new newModel[mods.size()];
            for (int i = 0; i < trainedModels.length; i++) {
                trainedModels[i] = mods.get(i);
            }
        }

    }
    private class  newModel{
        private String name;
        private newData data;
        private Model.Status status;
        private Model.Result results;
        public newModel(Model m){
            name = m.getName();
            data = new newData(m.getData());
            status = m.getStatus();
            if(m.getResult()) {
                results = Model.Result.Good;
            }
            else{
                results = Model.Result.Bad;
            }
        }
    }
    private class newData{
        private Data.Type type;
        private int size;
        public newData(Data d){
            type = d.getType();
            size = d.getSize();
        }
    }
    private class newConference{
        private String name;
        private int date;
        private newModel[] publications;
        public newConference(ConfrenceInformation c){
            name = c.getName();
            date = c.getDate();
            publications = new newModel[c.getPublications().length];
            for (int i = 0; i < publications.length; i++) {
                publications[i] = new newModel(c.getPublications()[i]);
            }
        }
    }
    private newStudent[] students;
    private newConference[] conferences;
    private int cpuTimeUsed;
    private int gpuTimeUsed;
    private int batchesProcessed;
    public JsonOutput(Output o){
        students = new newStudent[o.getStudents().length];
        for (int i = 0; i < students.length; i++) {
            students[i] = new newStudent(o.getStudents()[i]);
        }
        conferences = new newConference[o.getConfrences().length];
        for (int i = 0; i < conferences.length; i++) {
            conferences[i] = new newConference((o.getConfrences()[i]));
        }
        cpuTimeUsed = o.getCpuTimeUsed();
        gpuTimeUsed = o.getGpuTimeUsed();
        batchesProcessed = o.getBatchesProcessed();
    }
}
