package Newzgrabber;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInstance() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver bd = new BatchDriver(newzini, batchfile);
            assertNotNull(bd.getGroups());
            assertNotNull(bd.getBatchJob("testgroup"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException_newzini() {
        new BatchDriver(null, "testdata/batchfile.ini");
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException_batchfile() {
        String newzini = "testdata/newzini.ini";
        new BatchDriver(newzini, null);
    }

    @Test(expected = IOException.class)
    public void testIOException_newzini() throws IOException {
        String newzini = "non_existent_file.ini";
        new BatchDriver(newzini, "testdata/batchfile.ini");
    }

    @Test(expected = IOException.class)
    public void testIOException_batchfile() throws IOException {
        String newzini = "testdata/newzini.ini";
        String batchfile = "non_existent_file.ini";
        new BatchDriver(newzini, batchfile);
    }

    @Test
    public void testGetGroups() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver bd = new BatchDriver(newzini, batchfile);
            String[] groups = bd.getGroups();
            assertEquals(3, groups.length);
            assertTrue(groups[0].equals("testgroup1"));
            assertTrue(groups[1].equals("testgroup2"));
            assertTrue(groups[2].equals("testgroup3"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatchJob() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver bd = new BatchDriver(newzini, batchfile);
            BatchJob bj = bd.getBatchJob("testgroup1");
            assertEquals("nntp.example.com", bj.getServer());
            assertEquals("testdir1", bj.getDirectory());
            assertEquals(1, bj.getFirstId());
            assertEquals(10, bj.getLastId());
            assertTrue(bj.getUsername() == null);
            assertTrue(bj.getPassword() == null);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatchJob_username_password() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile_username_password.ini";

        try {
            BatchDriver bd = new BatchDriver(newzini, batchfile);
            BatchJob bj = bd.getBatchJob("testgroup1");
            assertEquals("nntp.example.com", bj.getServer());
            assertEquals("testdir1", bj.getDirectory());
            assertEquals(1, bj.getFirstId());
            assertEquals(10, bj.getLastId());
            assertEquals("testuser", bj.getUsername());
            assertEquals("testpassword", bj.getPassword());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSubjectLines() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver bd = new BatchDriver(newzini, batchfile);
            String[] subjectLines = bd.getSubjectLines();
            assertNotNull(subjectLines);
            assertEquals(5, subjectLines.length);
            for (int i = 0; i < subjectLines.length; i++) {
                String subjectLine = subjectLines[i];
                assertTrue(subjectLine.length() > 0);
            }
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNoSubjectLines() throws IOException {
        String newzini = "testdata/newzini.ini";
        String batchfile = new File("testdata/batchfile_no_subjects.ini").getAbsolutePath();

        try {
            BatchDriver bd = new BatchDriver(newzini, batchfile);
            String[] subjectLines = bd.getSubjectLines();
            assertNotNull(subjectLines);
            assertEquals(0, subjectLines.length);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}