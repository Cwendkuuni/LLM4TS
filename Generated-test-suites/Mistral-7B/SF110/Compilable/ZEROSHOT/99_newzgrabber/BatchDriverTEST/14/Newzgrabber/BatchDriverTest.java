package Newzgrabber;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class BatchDriverTest
{
    private BatchDriver batchDriver;
    private String newzini;
    private String batchfile;

    @Before
    public void setUp() throws Exception
    {
        newzini = "test.ini"; // Replace with the path to your test INI file for groups
        batchfile = "test.ini"; // Replace with the path to your test INI file for batches
        batchDriver = new BatchDriver(newzini, batchfile);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException
    {
        String[] expectedGroups = {"group1", "group2", "group3"}; // Replace with the expected groups
        String[] actualGroups = batchDriver.getGroups();

        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJob_ValidGroup()
    {
        String group = "group1";
        BatchJob expectedBatchJob = new BatchJob("server1", group, "/path/to/directory1", 1, 10); // Replace with the expected values
        BatchJob actualBatchJob = batchDriver.getBatchJob(group);

        assertEquals(expectedBatchJob, actualBatchJob);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBatchJob_InvalidGroup()
    {
        String group = "nonexistent_group";
        batchDriver.getBatchJob(group);
    }

    @Test
    public void testGetSubjectLines()
    {
        String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"}; // Replace with the expected subject lines
        String[] actualSubjectLines = batchDriver.getSubjectLines();

        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }
}