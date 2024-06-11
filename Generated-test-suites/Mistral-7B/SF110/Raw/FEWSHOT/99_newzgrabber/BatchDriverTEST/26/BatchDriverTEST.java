package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInstance() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            assertNotNull(bd);
        } catch (NullPointerException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetGroups() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            String[] groups = bd.getGroups();
            assertNotNull(groups);
            assertEquals(3, groups.length);
            assertTrue(Arrays.asList(groups).containsAll(Arrays.asList("group1", "group2", "group3")));
        } catch (NullPointerException | ArrayIndexOutOfBoundsException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatchJobNullGroup() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            assertNull(bd.getBatchJob(null));
        } catch (NullPointerException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatchJobEmptyGroup() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            assertNull(bd.getBatchJob(""));
        } catch (NullPointerException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatchJobInvalidGroup() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            assertNull(bd.getBatchJob("invalidGroup"));
        } catch (NullPointerException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatchJobValidGroup() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            BatchJob bj = bd.getBatchJob("group1");
            assertNotNull(bj);
            assertEquals("server1", bj.getServer());
            assertEquals("directory1", bj.getDirectory());
            assertEquals(1, bj.getFirstId());
            assertEquals(10, bj.getLastId());
            assertNull(bj.getUsername());
            assertNull(bj.getPassword());
        } catch (NullPointerException | IOException | NumberFormatException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatchJobWithUsernameAndPassword() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            BatchJob bj = bd.getBatchJob("group2");
            assertNotNull(bj);
            assertEquals("server2", bj.getServer());
            assertEquals("directory2", bj.getDirectory());
            assertEquals(11, bj.getFirstId());
            assertEquals(15, bj.getLastId());
            assertEquals("username2", bj.getUsername());
            assertEquals("password2", bj.getPassword());
        } catch (NullPointerException | IOException | NumberFormatException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSubjectLinesNull() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            assertNull(bd.getSubjectLines());
        } catch (NullPointerException | IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSubjectLinesEmpty() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            String[] subjectLines = bd.getBatchJob("group3").getSubjectLines();
            assertNotNull(subjectLines);
            assertTrue(Arrays.asList(subjectLines).isEmpty());
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSubjectLinesValid() {
        try {
            final BatchDriver bd = new BatchDriver("test.ini", "test.ini");
            BatchJob bj = bd.getBatchJob("group1");
            String[] subjectLines = bj.getSubjectLines();
            assertNotNull(subjectLines);
            assertTrue(Arrays.asList(subjectLines).size() > 0);
            for (String subjectLine : subjectLines) {
                assertNotNull(subjectLine);
            }
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}