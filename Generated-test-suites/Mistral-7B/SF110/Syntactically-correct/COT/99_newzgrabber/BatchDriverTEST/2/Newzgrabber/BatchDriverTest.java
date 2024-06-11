package Newzgrabber.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTP;
import Newzgrabber.Status;

public class BatchDriverTest
{
    @Test
    public void testBatchDriverConstruction() throws IOException, NullPointerException
    {
        final String newzini = "test/config/newzini.ini";
        final String batchfile = "test/config/batchfile.ini";
        final BatchDriver bd = new BatchDriver(newzini, batchfile);
        assertNotNull(bd.BatchgroupsVector);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException
    {
        final String newzini = "test/config/newzini.ini";
        final String batchfile = "test/config/batchfile.ini";
        final BatchDriver bd = new BatchDriver(newzini, batchfile);
        final String[] groups = bd.getGroups();
        assertNotNull(groups);
        assertEquals(3, groups.length);
        assertEquals("news.comp.sys.ibm.as400.rpg.pgm", groups[0]);
        assertEquals("news.comp.sys.ibm.as400.rpg.pgm.jcl", groups[1]);
        assertEquals("news.comp.sys.ibm.as400.rpg.pgm.jcl.misc", groups[2]);
    }

    @Test
    public void testGetBatchJobNullGroup()
    {
        final String newzini = "test/config/newzini.ini";
        final String batchfile = "test/config/batchfile.ini";
        final BatchDriver bd = new BatchDriver(newzini, batchfile);
        assertNull(bd.getBatchJob(null));
    }

    @Test
    public void testGetBatchJobEmptyGroup() throws IOException, NullPointerException
    {
        final String newzini = "test/config/newzini.ini";
        final String batchfile = "test/config/batchfile_emptygroup.ini";
        final BatchDriver bd = new BatchDriver(newzini, batchfile);
        assertNull(bd.getBatchJob("emptygroup"));
    }

    @Test
    public void testGetBatchJobValidGroup() throws IOException, NullPointerException
    {
        final String newzini = "test/config/newzini.ini";
        final String batchfile = "test/config/batchfile.ini";
        final BatchDriver bd = new BatchDriver(newzini, batchfile);
        final BatchJob bj = bd.getBatchJob("news.comp.sys.ibm.as400.rpg.pgm");
        assertNotNull(bj);
        assertEquals("localhost", bj.getServer());
        assertEquals("news.comp.sys.ibm.as400.rpg.pgm", bj.getGroup());
        assertEquals("/path/to/directory", bj.getDirectory());
        assertEquals(1, bj.getFirstId());
        assertEquals(10, bj.getLastId());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBatchJobInvalidIndex() throws IOException, NullPointerException
    {
        final String newzini = "test/config/newzini.ini";
        final String batchfile = "test/config/batchfile.ini";
        final BatchDriver bd = new BatchDriver(newzini, batchfile);
        final BatchJob[] batchjobs = bd.getBatchJobs();
        bj = batchjobs[3]; // invalid index
    }

    @Test
    public void testGetSubjectLinesNullBatchJob()
    {
        final String newzini = "test/config/newzini.ini";
        final String batchfile = "test/config/batchfile.ini";
        final BatchDriver bd = new BatchDriver(newzini, batchfile);
        assertNull(bd.getSubjectLines());
    }

    @Test
    public void testGetSubjectLinesValidBatchJob()
    {
        final String newzini = "test/config/newzini.ini";
        final String batchfile = "test/config/batchfile.ini";
        final BatchDriver bd = new BatchDriver(newzini, batchfile);
        final BatchJob bj = bd.getBatchJob("news.comp.sys.ibm.as400.rpg.pgm");
        final String[] subjectlines = bd.getSubjectLines();
        assertNotNull(subjectlines);
        assertEquals(5, subjectlines.length);
        assertEquals("Re: AS400 RPG Tip: Using the SET statement", subjectlines[0]);
        assertEquals("Re: AS400 RPG Tip: Using the SET statement", subjectlines[1]);
        // ...
    }

    @Test
    public void testGetSubjectLinesEmptyBatchJob() throws IOException, NullPointerException
    {
        final String newzini = "test/config/newzini.ini";
        final String batchfile = "test/config/batchfile_emptybatchjob.ini";
        final BatchDriver bd = new BatchDriver(newzini, batchfile);
        final BatchJob bj = bd.getBatchJob("news.comp.sys.ibm.as400.rpg.pgm");
        final String[] subjectlines = bd.getSubjectLines();
        assertNotNull(subjectlines);
        assertEquals(0, subjectlines.length);
    }
}