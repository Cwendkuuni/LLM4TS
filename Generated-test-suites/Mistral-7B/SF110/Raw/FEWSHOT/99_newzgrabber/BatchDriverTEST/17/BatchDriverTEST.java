package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

    private BatchDriver batchDriver;
    private static final String NEWZINI = "test.ini";
    private static final String BATCHFILE = "test.ini";
    private static final String GROUP = "test.group";
    private static final String SERVER = "test.server";
    private static final String DIRECTORY = "test.directory";
    private static final int FIRSTID = 1;
    private static final int LASTID = 10;
    private static final String USERNAME = "test.username";
    private static final String PASSWORD = "test.password";
    private static final int NUM_THREADS = 2;
    private static final int TIMEOUT = 1000;

    @Before
    public void setUp() throws IOException {
        Properties groupsiniProps = new Properties();
        groupsiniProps.setProperty("test.group", "test.id");
        InputStream groupsiniStream = new ByteArrayInputStream(groupsiniProps.getProperty("test.ini").getBytes());
        InputStream batchgroupsStream = new ByteArrayInputStream(("Newsgroups=" + GROUP + "\nRetrieve=" + FIRSTID + "-" + LASTID + "\nOptions=").getBytes());
        Properties batchgroupsProps = new Properties();
        batchgroupsProps.load(batchgroupsStream);
        InputStream optionsStream = new ByteArrayInputStream(("server=" + SERVER + "\nusername=" + USERNAME + "\npassword=" + PASSWORD + "\ntimeout=" + TIMEOUT + "\nthreads=" + NUM_THREADS).getBytes());
        Properties optionsProps = new Properties();
        optionsProps.load(optionsStream);

        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
        batchDriver.OptionsHash = optionsProps;
        batchDriver.BatchgroupsHash = batchgroupsProps;
    }

    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertTrue(groups.length > 0);
        assertEquals(GROUP, groups[0]);
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob(GROUP);
        assertNotNull(batchJob);
        assertEquals(SERVER, batchJob.getServer());
        assertEquals(GROUP, batchJob.getGroup());
        assertEquals(DIRECTORY, batchJob.getDirectory());
        assertEquals(FIRSTID, batchJob.getFirstId());
        assertEquals(LASTID, batchJob.getLastId());
        assertNull(batchJob.getUsername());
        assertNull(batchJob.getPassword());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBatchJobWithInvalidGroup() {
        batchDriver.getBatchJob("invalid.group");
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertArrayEquals(new String[]{}, subjectLines);
    }

    @Test
    public void testGetSubjectLinesWithData() throws IOException {
        Properties subjectsProps = new Properties();
        subjectsProps.setProperty("Subject", "test.subject1 test.subject2 test.subject3");
        InputStream subjectsStream = new ByteArrayInputStream(subjectsProps.getProperty("test.ini").getBytes());
        byte[] subjectLinesBytes = "test.subject1 test.subject2 test.subject3".getBytes();
        batchDriver.subjectlines = new String[1];
        batchDriver.subjectlines[0] = new String(subjectLinesBytes);

        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertArrayEquals(new String[]{"test.subject1", "test.subject2", "test.subject3"}, subjectLines);
    }
}