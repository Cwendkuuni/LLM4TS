package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {
    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException, IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] groups = batchDriver.getGroups();
        assertEquals(2, groups.length);
        assertEquals("group1", groups[0]);
        assertEquals("group2", groups[1]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroupsArrayOutOfBounds() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] groups = batchDriver.getGroups();
        groups = new String[groups.length + 1];
        groups[groups.length - 1] = "newGroup";
        batchDriver.getGroups();
    }

    @Test
    public void testGetBatchJobGroupExists() throws IOException, ArrayIndexOutOfBoundsException, NullPointerException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob bj = batchDriver.getBatchJob(group);
        assertNotNull(bj);
        assertEquals("server1", bj.getServer());
        assertEquals("group1", bj.getGroup());
        assertEquals("/path/to/directory1", bj.getDirectory());
        assertEquals(1, bj.getFirstId());
        assertEquals(10, bj.getLastId());
        assertNull(bj.getUsername());
        assertNull(bj.getPassword());
    }

    @Test
    public void testGetBatchJobGroupDoesNotExist() throws IOException, ArrayIndexOutOfBoundsException, NullPointerException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "nonexistentGroup";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob bj = batchDriver.getBatchJob(group);
        assertNull(bj);
    }

    @Test
    public void testGetSubjectLines() throws IOException, NullPointerException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(5, subjectLines.length);
        assertEquals("Subject 1", subjectLines[0]);
        assertEquals("Subject 2", subjectLines[1]);
        assertEquals("Subject 3", subjectLines[2]);
        assertEquals("Subject 4", subjectLines[3]);
        assertEquals("Subject 5", subjectLines[4]);
    }

    @Test
    public void testGetSubjectLinesEmpty() throws IOException, NullPointerException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(0, subjectLines.length);
    }
}