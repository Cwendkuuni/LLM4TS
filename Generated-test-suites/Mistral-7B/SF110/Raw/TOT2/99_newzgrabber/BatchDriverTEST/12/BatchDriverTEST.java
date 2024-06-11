package Newzgrabber.BatchDriverTest;

import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.BatchDriver;

public class BatchDriverTest {
    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] expectedGroups = {"group1", "group2", "group3"}; // Replace with actual expected groups
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetGroupsException() {
        BatchDriver batchDriver = new BatchDriver("emptyini.ini", "emptyBatchfile.ini");
        try {
            batchDriver.getGroups();
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetBatchJobNormal() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String group = "testgroup";
        BatchJob expectedBatchJob = new BatchJob("testserver", group, "testdirectory", 1, 10);
        BatchJob actualBatchJob = batchDriver.getBatchJob(group);
        assertEquals(expectedBatchJob, actualBatchJob);
    }

    @Test
    public void testGetBatchJobNullServer() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String group = "testgroup";
        BatchJob expectedBatchJob = null;
        BatchJob actualBatchJob = batchDriver.getBatchJob(group);
        assertNull(actualBatchJob);
    }

    @Test
    public void testGetBatchJobInvalidGroup() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String group = "invalidgroup";
        BatchJob expectedBatchJob = null;
        try {
            batchDriver.getBatchJob(group);
            fail("Expected NullPointerException or IOException");
        } catch (NullPointerException | IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] expectedSubjectLines = {"Subject1", "Subject2"};
        String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    @Test
    public void testGetSubjectLinesNull() {
        BatchDriver batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] expectedSubjectLines = null;
        String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertNull(actualSubjectLines);
    }

}