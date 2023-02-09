package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GPUTest {
    GPU testg;
    Cluster testc;
    Data testd;
    Model testm;


    @BeforeEach
    void setUp() {
        testg = new GPU(GPU.Type.RTX3090, 1);
        testc = Cluster.getInstance();
        testd = new Data(Data.Type.Images, 33000);
        testm = new Model("manyak", testd);
        testg.setModel(testm);
        testg.setModelSize(testd.getSize());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sendfirstDataColToCluster() {
        testg.sendfirstDataColToCluster();
        assertEquals(testg.getModel().getStatus(), Model.Status.Training);
        assertTrue(!testc.getDisk().isEmpty());
        assertEquals(testg.getModelSize(), 1000);
    }

    @Test
    void sendOneToCluster() {
        testg.sendOneToCluster();
        assertTrue(!testc.getDisk().isEmpty());
        assertEquals(testg.getModelSize(), 32000);
    }


    @Test
    void receiveBatch() {
        DataBatch db = new DataBatch(1,testd);
        testg.receiveBatch(db);
        assertEquals(testg.getVRAM().size(),1);
    }

    @Test
    void trainBatch() {
        DataBatch db = new DataBatch(1,testd);
        testg.receiveBatch(db);
        int prep = testg.getModel().getData().getProcessed();
        testg.trainBatch(1);
        assertEquals(testg.getModel().getData().getProcessed(),prep + 1000);
        assertEquals(testg.getVRAM().size(),0);

    }


}