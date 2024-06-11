package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInitialization() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            assertTrue(batchDriver.getGroups().length > 0);
        } catch (NullPointerException | IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetGroups() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            String[] groups = batchDriver.getGroups();
            assertArrayEquals(new String[]{"group1", "group2"}, groups);
        } catch (NullPointerException | IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_ValidGroup() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            assertNotNull(batchJob);
            assertEquals("server1", batchJob.getServer());
            assertEquals("path/to/group1", batchJob.getDirectory());
            assertEquals(1, batchJob.getFirstId());
            assertEquals(50, batchJob.getLastId());
            assertNull(batchJob.getUsername());
            assertNull(batchJob.getPassword());
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_InvalidGroup() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            BatchJob batchJob = batchDriver.getBatchJob("invalidGroup");
            assertNull(batchJob);
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetBatchJob_NoOptions() {
        String newzini = "testdata/newzini_no_options.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            assertNotNull(batchJob);
            assertNull(batchJob.getUsername());
            assertNull(batchJob.getPassword());
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithOptions() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile_with_options.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            assertNotNull(batchJob);
            assertEquals("server1", batchJob.getServer());
            assertEquals("path/to/group1", batchJob.getDirectory());
            assertEquals(1, batchJob.getFirstId());
            assertEquals(50, batchJob.getLastId());
            assertEquals("username", batchJob.getUsername());
            assertEquals("password", batchJob.getPassword());
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetSubjectLines() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            String[] subjectLines = batchDriver.getSubjectLines();
            assertNotNull(subjectLines);
            assertTrue(subjectLines.length > 0);
            for (String subjectLine : subjectLines) {
                assertFalse(subjectLine.isEmpty());
            }
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_NoSubjectLines() throws IOException {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile_no_subjectlines.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            assertNotNull(batchJob);
            assertNull(batchDriver.getSubjectLines());
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithSubjectLines() throws IOException {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile_with_subjectlines.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            assertNotNull(batchJob);
            String[] subjectLines = batchDriver.getSubjectLines();
            assertNotNull(subjectLines);
            assertTrue(subjectLines.length > 0);
            for (String subjectLine : subjectLines) {
                assertFalse(subjectLine.isEmpty());
            }
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithThreadLimit() throws IOException {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile_with_thread_limit.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            assertNotNull(batchJob);
            assertEquals(2, Newzgrabber.nf.getThreadCount());
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithoutTimeout() throws IOException {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            assertNotNull(batchJob);
            assertFalse(Newzbatch.TIMEOUT);
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithTimeout() throws IOException {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile_with_timeout.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            assertNotNull(batchJob);
            assertTrue(Newzbatch.TIMEOUT);
            assertEquals(30000, Newzbatch.TIMEOUTMAX);
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithInvalidGroup() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            assertThrows(NullPointerException.class, () -> batchDriver.getBatchJob(null));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> batchDriver.getBatchJob(""));
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> batchDriver.getBatchJob("invalidGroup"));
        } catch (NullPointerException | IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithInvalidServer() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            String invalidServer = "invalid_server";
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            batchJob.setServer(invalidServer);
            assertThrows(IOException.class, () -> batchDriver.getBatchJob("group1"));
        } catch (NullPointerException | IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithInvalidDirectory() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            String invalidDirectory = "invalid_directory";
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            batchJob.setDirectory(invalidDirectory);
            assertThrows(IOException.class, () -> batchDriver.getBatchJob("group1"));
        } catch (NullPointerException | IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithInvalidFirstId() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            int invalidFirstId = -1;
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            batchJob.setFirstId(invalidFirstId);
            assertThrows(IOException.class, () -> batchDriver.getBatchJob("group1"));
        } catch (NullPointerException | IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithInvalidLastId() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            int invalidLastId = -1;
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            batchJob.setLastId(invalidLastId);
            assertThrows(IOException.class, () -> batchDriver.getBatchJob("group1"));
        } catch (NullPointerException | IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithInvalidUsername() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            String invalidUsername = null;
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            batchJob.setUsername(invalidUsername);
            assertThrows(IOException.class, () -> batchDriver.getBatchJob("group1"));
        } catch (NullPointerException | IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetBatchJob_WithInvalidPassword() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";

        try {
            BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
            String invalidPassword = null;
            BatchJob batchJob = batchDriver.getBatchJob("group1");
            batchJob.setPassword(invalidPassword);
            assertThrows(IOException.class, () -> batchDriver.getBatchJob("group1"));
        } catch (NullPointerException | IOException e) {
            assertTrue(false);
        }
    }
}