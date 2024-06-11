package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest
{
    private BatchDriver batchDriver;
    private String newziniFilePath = "testData/newzini.ini";
    private String batchFileFilePath = "testData/batchfile.ini";

    @Before
    public void setUp() throws IOException, NullPointerException
    {
        File newziniFile = new File(newziniFilePath);
        File batchFile = new File(batchFileFilePath);
        // Assuming testData folder is in the same directory as this test file
        assert newziniFile.exists();
        assert batchFile.exists();

        batchDriver = new BatchDriver(newziniFilePath, batchFileFilePath);
    }

    @After
    public void tearDown()
    {
        batchDriver = null;
    }

    @Test
    public void testGetGroups()
    {
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJob()
    {
        String groupName = "group1";
        BatchJob expectedBatchJob = new BatchJob("server1", groupName, "/path/to/directory1", 1, 10);
        BatchJob actualBatchJob = batchDriver.getBatchJob(groupName);

        assertNotNull(actualBatchJob);
        assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
        assertEquals(expectedBatchJob.getGroupName(), actualBatchJob.getGroupName());
        assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
        assertEquals(expectedBatchJob.getFirstId(), actualBatchJob.getFirstId());
        assertEquals(expectedBatchJob.getLastId(), actualBatchJob.getLastId());
    }

    @Test
    public void testGetSubjectLines()
    {
        String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
        String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    @Test
    public void testGetBatchJobNullGroup()
    {
        String groupName = "nonExistentGroup";
        BatchJob actualBatchJob = batchDriver.getBatchJob(groupName);
        assertNull(actualBatchJob);
    }
}