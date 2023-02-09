package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CPUTest {
    CPU testcp;
    Cluster testcl;
    ArrayList<DataBatch> testd;
    Data data;
    DataBatch db;
    GPU g;
    @BeforeEach
    void setUp() {
        testcp = new CPU(32);
        testcl = Cluster.getInstance();
        testd = new ArrayList<DataBatch>();
        data = new Data(Data.Type.Images, 10000);
        g = new GPU(GPU.Type.RTX3090, 1);
        testcl.registerGPU(g);
        db = new DataBatch(1,data);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void takeBatch() throws InterruptedException {
        testcl.getDisk().add(testd);
        testcp.takeBatch();
        assertEquals(testcp.getData(),testd);
    }

    @Test
    void process() throws InterruptedException {
        testd.add(db);
        testcl.getDisk().add(testd);
        testcp.takeBatch();
        assertTrue(testcp.process(4));
    }

}