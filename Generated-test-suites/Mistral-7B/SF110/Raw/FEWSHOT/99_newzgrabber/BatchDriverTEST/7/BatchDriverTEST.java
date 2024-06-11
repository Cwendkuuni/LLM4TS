package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInstance() {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        assertNotNull(batchDriver);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverInstanceNullNewzini() {
        String batchfile = "testfile.ini";
        BatchDriver batchDriver = new BatchDriver(null, batchfile);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverInstanceNullBatchfile() {
        String newzini = "testfile.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, null);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(3, groups.length);
    }

    @Test
    public void testGetBatchJob() {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        String group = "test.group";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
    }

    @Test
    public void testGetBatchJobNullGroup() {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob batchJob = batchDriver.getBatchJob(null);
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(2, subjectLines.length);
    }

    @Test
    public void testGetSubjectLinesEmpty() {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(0, subjectLines.length);
    }

    @Test
    public void testNewsgroupsHash() throws IOException {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        Hashtable<String, Integer> newsgroupsHash = batchDriver.NewsgroupsHash;
        assertNotNull(newsgroupsHash);
        assertEquals("group1", newsgroupsHash.keySet().toArray()[0].toString());
        assertEquals(1, newsgroupsHash.get("group1"));
    }

    @Test
    public void testBatchretrieveHash() throws IOException {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        Hashtable<String, String> batchretrieveHash = batchDriver.BatchretrieveHash;
        assertNotNull(batchretrieveHash);
        assertEquals("group1", batchretrieveHash.keySet().toArray()[0].toString());
        assertEquals("true", batchretrieveHash.get("group1"));
    }

    @Test
    public void testOptionsHash() throws IOException {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        Hashtable<String, String> optionsHash = batchDriver.OptionsHash;
        assertNotNull(optionsHash);
        assertEquals("server", optionsHash.keySet().toArray()[0].toString());
        assertEquals("localhost", optionsHash.get("server"));
    }

    @Test
    public void testBatchgroupsHash() throws IOException {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        Hashtable<String, String> batchgroupsHash = batchDriver.BatchgroupsHash;
        assertNotNull(batchgroupsHash);
        assertEquals("group1", batchgroupsHash.keySet().toArray()[0].toString());
        assertEquals("path/to/directory", batchgroupsHash.get("group1"));
    }

    @Test
    public void testBatchgroupsVector() throws IOException {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        Vector<String[]> batchgroupsVector = batchDriver.BatchgroupsVector;
        assertNotNull(batchgroupsVector);
        assertEquals(2, batchgroupsVector.size());
        String[] groupInfo = batchgroupsVector.get(0);
        assertEquals("group1", groupInfo[0]);
        assertEquals("path/to/directory1", groupInfo[1]);
        String[] groupInfo2 = batchgroupsVector.get(1);
        assertEquals("group2", groupInfo2[0]);
        assertEquals("path/to/directory2", groupInfo2[1]);
    }

    @Test
    public void testGetBatchJobWithInvalidGroup() {
        String newzini = "testfile1.ini";
        String batchfile = "testfile2.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob batchJob = batchDriver.getBatchJob("invalidGroup");
        assertNull(batchJob);
    }
}