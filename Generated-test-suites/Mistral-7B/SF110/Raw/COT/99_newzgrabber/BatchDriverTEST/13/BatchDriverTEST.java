package Newzgrabber.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchDriverTestData;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.Options;
import Newzgrabber.Status;

public class BatchDriverTest
{
    private BatchDriver batchDriver;

    @Before
    public void setup() throws IOException, NullPointerException
    {
        batchDriver = new BatchDriver("test.ini", "test.ini");
    }

    @Test
    public void testGetGroups()
    {
        String[] expectedGroups = BatchDriverTestData.expectedGroups;
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJobNullGroup()
    {
        String group = null;
        BatchJob actualBatchJob = batchDriver.getBatchJob(group);
        assertNull(actualBatchJob);
    }

    @Test
    public void testGetBatchJobEmptyGroup()
    {
        String group = "";
        BatchJob actualBatchJob = batchDriver.getBatchJob(group);
        assertNull(actualBatchJob);
    }

    @Test
    public void testGetBatchJobValidGroup()
    {
        String group = "test.group";
        BatchJob actualBatchJob = batchDriver.getBatchJob(group);
        BatchJob expectedBatchJob = new BatchJob("testServer", group, "testDirectory", 1, 10);
        assertBatchJobsEqual(expectedBatchJob, actualBatchJob);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBatchJobInvalidIndex()
    {
        String group = "test.group";
        batchDriver.getGroups();
        String invalidIndex = "12";
        BatchJob actualBatchJob = batchDriver.getBatchJob(invalidIndex);
        assertNull(actualBatchJob);
    }

    @Test
    public void testGetSubjectLinesNullBatchJob()
    {
        BatchJob batchJob = null;
        String[] expectedSubjectLines = new String[0];
        String[] actualSubjectLines = batchDriver.getSubjectLines(batchJob);
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    @Test
    public void testGetSubjectLinesEmptyBatchJob()
    {
        BatchJob batchJob = new BatchJob("testServer", "test.group", "testDirectory", 1, 10);
        batchJob.setSubjectLines(new String[0]);
        String[] expectedSubjectLines = new String[0];
        String[] actualSubjectLines = batchDriver.getSubjectLines(batchJob);
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    @Test
    public void testGetSubjectLinesValidBatchJob()
    {
        BatchJob batchJob = new BatchJob("testServer", "test.group", "testDirectory", 1, 10);
        batchJob.setSubjectLines(new String[]{"Subject 1", "Subject 2", "Subject 3"});
        String[] expectedSubjectLines = new String[]{"Subject 1", "Subject 2", "Subject 3"};
        String[] actualSubjectLines = batchDriver.getSubjectLines(batchJob);
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSubjectLinesNullBatchJobSubjectLines()
    {
        BatchJob batchJob = new BatchJob("testServer", "test.group", "testDirectory", 1, 10);
        batchJob.setSubjectLines(null);
        String[] actualSubjectLines = batchDriver.getSubjectLines(batchJob);
        assertNull(actualSubjectLines);
    }

    @Test
    public void testGetSubjectLinesEmptyBatchJobSubjectLines()
    {
        BatchJob batchJob = new BatchJob("testServer", "test.group", "testDirectory", 1, 10);
        batchJob.setSubjectLines(new String[0]);
        String[] actualSubjectLines = batchDriver.getSubjectLines(batchJob);
        String[] expectedSubjectLines = new String[0];
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    @Test
    public void testGetSubjectLinesValidBatchJobSubjectLines()
    {
        BatchJob batchJob = new BatchJob("testServer", "test.group", "testDirectory", 1, 10);
        batchJob.setSubjectLines(new String[]{"Subject 1", "Subject 2", "Subject 3"});
        String[] expectedSubjectLines = new String[]{"Subject 1", "Subject 2", "Subject 3"};
        String[] actualSubjectLines = batchDriver.getSubjectLines(batchJob);
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    private void assertBatchJobsEqual(BatchJob a, BatchJob b)
    {
        assertTrue(a.equals(b));
    }

    @Test(expected = IOException.class)
    public void testBatchDriverConstructorNullNewzini()
    {
        String newzini = null;
        String batchfile = "test.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverConstructorNullBatchfile()
    {
        String newzini = "test.ini";
        String batchfile = null;
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverConstructorInvalidNewzini()
    {
        String newzini = "invalid.ini";
        String batchfile = "test.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverConstructorInvalidBatchfile()
    {
        String newzini = "test.ini";
        String batchfile = "invalid.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
    }
}