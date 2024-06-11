package Newzgrabber.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.NewsFactory;
import Newzgrabber.NFException;
import Newzgrabber.NntpClient;
import Newzgrabber.Options;
import Newzgrabber.Status;

public class BatchDriverTest
{
    private BatchDriver batchDriver;
    private String newzini = "test.ini";
    private String batchfile = "test.ini";

    @Before
    public void setUp() throws Exception
    {
        batchDriver = new BatchDriver(newzini, batchfile);
    }

    @After
    public void tearDown() throws Exception
    {
        if (batchDriver != null)
        {
            batchDriver.dispose();
        }
    }

    @Test
    public void testBatchDriverConstructor()
    {
        assertNull(batchDriver);
        batchDriver = new BatchDriver(newzini, batchfile);
        assertTrue(batchDriver instanceof BatchDriver);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverConstructorWithNullNewzini()
    {
        new BatchDriver(null, batchfile);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverConstructorWithNullBatchfile()
    {
        new BatchDriver(newzini, null);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverConstructorWithInvalidNewzini() throws IOException
    {
        final File invalidIniFile = new File("invalid.ini");
        new BatchDriver(invalidIniFile.getAbsolutePath(), batchfile);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverConstructorWithInvalidBatchfile() throws IOException
    {
        final File invalidBatchFile = new File("invalid.ini");
        new BatchDriver(newzini, invalidBatchFile.getAbsolutePath());
    }

    @Test
    public void testGetGroups()
    {
        String[] groups = batchDriver.getGroups();
        assertArrayEquals(new String[]{"Group1", "Group2", "Group3"}, groups);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroupsWithEmptyVector()
    {
        batchDriver = new BatchDriver("empty.ini", "batch.ini");
        batchDriver.getGroups();
    }

    @Test
    public void testGetBatchJob() throws Exception
    {
        String group = "Group1";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
        assertTrue(batchJob instanceof BatchJob);
        assertEquals(group, batchJob.getGroup());
    }

    @Test
    public void testGetBatchJobWithInvalidGroup()
    {
        String group = "InvalidGroup";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines()
    {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertTrue(subjectLines.length > 0);
    }

    @Test
    public void testGetBatchJobWithEmptySubjectLines()
    {
        batchDriver = new BatchDriver("test.ini", "test.ini");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertTrue(subjectLines.length == 0);
    }
}