package Newzgrabber.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {
    @Test
    public void testGetGroups() throws IOException {
        BatchDriver driver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = driver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetGroupsEdgeCase() throws IOException {
        BatchDriver driver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] expectedGroups = {};
        String[] actualGroups = driver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJobHappyPath() throws IOException, ArrayIndexOutOfBoundsException {
        BatchDriver driver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String group = "group1";
        BatchJob expectedBatchJob = new BatchJob("testServer", "group1", "testDirectory", 1, 10);
        BatchJob actualBatchJob = driver.getBatchJob(group);
        assertEquals(expectedBatchJob, actualBatchJob);
    }

    @Test
    public void testGetBatchJobExceptionHandling() throws IOException {
        BatchDriver driver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String group = "nonExistentGroup";
        driver.getBatchJob(group);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver driver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] expectedSubjectLines = {"Subject1", "Subject2", "Subject3"};
        String[] actualSubjectLines = driver.getSubjectLines();
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }
}