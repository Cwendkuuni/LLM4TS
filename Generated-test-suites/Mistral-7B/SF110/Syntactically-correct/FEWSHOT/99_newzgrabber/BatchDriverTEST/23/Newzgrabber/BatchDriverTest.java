package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInstance() {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        assertNull(bd.getGroups());
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverInstanceNullNewzini() {
        String batchfile = "test.ini";
        BatchDriver bd = new BatchDriver(null, batchfile);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverInstanceNullBatchfile() {
        String newzini = "test.ini";
        BatchDriver bd = new BatchDriver(newzini, null);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String[] groups = bd.getGroups();
        assertArrayEquals(new String[]{"Group1", "Group2"}, groups);
    }

    @Test
    public void testGetBatchJob() {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String group = "Group1";
        BatchJob bj = bd.getBatchJob(group);
        assertNull(bj);

        newzini = "test_valid.ini";
        batchfile = "test_valid.ini";
        bd = new BatchDriver(newzini, batchfile);
        group = "Group1";
        bj = bd.getBatchJob(group);
        assertNotNull(bj);
        assertTrue(bj.getServer().equals("server1"));
        assertTrue(bj.getGroup().equals("Group1"));
        assertTrue(bj.getDirectory().equals("dir1"));
        assertTrue(bj.getFirstId() == 1);
        assertTrue(bj.getLastId() == 10);
        assertNull(bj.getUsername());
        assertNull(bj.getPassword());

        group = "Group2";
        bj = bd.getBatchJob(group);
        assertNotNull(bj);
        assertTrue(bj.getServer().equals("server2"));
        assertTrue(bj.getGroup().equals("Group2"));
        assertTrue(bj.getDirectory().equals("dir2"));
        assertTrue(bj.getFirstId() == 11);
        assertTrue(bj.getLastId() == 20);
        assertTrue(bj.getUsername().equals("user1"));
        assertTrue(bj.getPassword().equals("pass1"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBatchJobInvalidIndex() {
        String newzini = "test_valid.ini";
        String batchfile = "test_valid.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        BatchJob bj = bd.getBatchJob("Group3");
        assertNull(bj);

        int index = 3;
        String[] groups = bd.getGroups();
        BatchJob invalidBatchJob = bd.getBatchJob(groups[index]);
        assertNull(invalidBatchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String newzini = "test_valid.ini";
        String batchfile = "test_valid.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String group = "Group1";
        BatchJob bj = bd.getBatchJob(group);
        String[] subjectlines = bd.getSubjectLines();
        assertNotNull(subjectlines);
        assertArrayEquals( Arrays.copyOfRange(bj.getSubjectLines(), 0, bj.getSubjectLines().length), subjectlines );
    }
}