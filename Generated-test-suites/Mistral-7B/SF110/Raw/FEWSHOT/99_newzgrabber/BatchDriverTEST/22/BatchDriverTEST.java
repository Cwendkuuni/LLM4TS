package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Ini;
import java.util.StringTokenizer;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

    private static final String NEWZINI = "src/test/resources/newzini.ini";
    private static final String BATCHFILE = "src/test/resources/batchfile.ini";

    private BatchDriver batchDriver;
    private Ini groupsini;
    private Ini batchgroups;

    @Before
    public void setup() throws IOException {
        groupsini = new Ini(NEWZINI, "Messageids");
        batchgroups = new Ini(BATCHFILE, "");
    }

    @Test
    public void testBatchDriverConstruction() {
        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
        assertNull(batchDriver.getGroups());
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverConstructionWithNullNewzini() {
        batchDriver = new BatchDriver(null, BATCHFILE);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverConstructionWithInvalidNewzini() {
        batchDriver = new BatchDriver("invalid.ini", BATCHFILE);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverConstructionWithNullBatchfile() {
        batchDriver = new BatchDriver(NEWZINI, null);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverConstructionWithInvalidBatchfile() {
        batchDriver = new BatchDriver(NEWZINI, "invalid.ini");
    }

    @Test
    public void testGetGroups() {
        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
        String[] groups = batchDriver.getGroups();
        assertArrayEquals(new String[]{"group1", "group2"}, groups);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroupsWithEmptyVector() {
        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
        batchDriver.BatchgroupsVector.clear();
        batchDriver.getGroups();
    }

    @Test
    public void testGetBatchJob() {
        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
        String server = "test.server.com";
        String group = "group1";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNull(batchJob);

        batchDriver.OptionsHash.put("server", server);
        batchDriver.BatchgroupsHash.put(group, "directory1");
        batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
        assertEquals(server, batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals("group1", batchJob.getGroup());
    }

    @Test
    public void testGetBatchJobWithInvalidGroup() {
        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
        assertNull(batchDriver.getBatchJob("invalidGroup"));
    }

    @Test
    public void testGetSubjectLines() {
        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);

        batchDriver.subjectlines = new String[]{"Subject 1", "Subject 2"};
        subjectLines = batchDriver.getSubjectLines();
        assertArrayEquals(new String[]{"Subject 1", "Subject 2"}, subjectLines);
    }

    @Test
    public void testGetSubjectLinesWithEmptyStringArray() {
        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
        batchDriver.subjectlines = null;
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }
}