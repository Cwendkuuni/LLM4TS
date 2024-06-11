package Newzgrabber.test;

import org.junit.After;
import org.junit.Assert;import org.junit.Before;
import org.junit.Test;
import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;

public class BatchDriverTest
{
    private BatchDriver batchDriver;
    private Ini sampleGroupsIni;
    private Ini sampleBatchini;

    @Before
    public void setUp() throws IOException, NullPointerException
    {
        sampleGroupsIni = new Ini("sampleGroups.ini", "Messageids");
        sampleBatchini = new Ini("sampleBatchfile.ini", "");
        batchDriver = new BatchDriver("sampleGroups.ini", "sampleBatchfile.ini");
    }

    @After
    public void tearDown()
    {
        if (batchDriver != null)
        {
            batchDriver.dispose();
        }
    }

    @Test
    public void testBatchDriverConstructors() throws NullPointerException, IOException
    {
        Assert.assertNotNull(batchDriver);
    }

    @Test
    public void testGetGroups()
    {
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        Assert.assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJobNullGroup()
    {
        Assert.assertNull(batchDriver.getBatchJob(null));
    }

    @Test
    public void testGetBatchJobEmptyGroupVector()
    {
        sampleBatchini.removeSection("Newsgroups");
        batchDriver = new BatchDriver("sampleGroups.ini", "sampleBatchfile.ini");
        Assert.assertNull(batchDriver.getBatchJob(""));
    }

    @Test
    public void testGetBatchJobInvalidGroup()
    {
        sampleBatchini.setProperty("Newsgroups", "group4=server4,dir4");
        Assert.assertNull(batchDriver.getBatchJob("group4"));
    }

    @Test
    public void testGetBatchJobValidGroup()
    {
        sampleBatchini.setProperty("Newsgroups", "group1=server1,dir1;group2=server2,dir2;group3=server3,dir3");
        BatchJob expectedBatchJob = new BatchJob("server1", "group1", "dir1", 1, 10);
        BatchJob actualBatchJob = batchDriver.getBatchJob("group1");
        Assert.assertEquals(expectedBatchJob, actualBatchJob);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetSubjectLinesArrayOutOfBoundsException()
    {
        sampleBatchini.setProperty("Retrieve", "group1=1-10");
        batchDriver.getBatchJob("group1");
        Assert.assertArrayNotEquals(null, batchDriver.getSubjectLines());
        Assert.assertArrayNotEquals(0, batchDriver.getSubjectLines().length);
        int index = batchDriver.getSubjectLines().length + 1;
        String[] subjectLines = batchDriver.getSubjectLines();
        Assert.assertArrayEquals(subjectLines, batchDriver.getSubjectLines()); // Check array reference is the same
        Assert.assertArrayEquals(new String[index], batchDriver.getSubjectLines()); // Check that new array is not equal
    }
}