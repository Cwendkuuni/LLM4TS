package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInitialization() {
        try {
            new BatchDriver("testnewzini.ini", "testbatchfile.ini");
        } catch (NullPointerException | IOException e) {
            fail("BatchDriver initialization failed: " + e.getMessage());
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = null;
        try {
            batchDriver = new BatchDriver("testnewzini.ini", "testbatchfile.ini");
            String[] groups = batchDriver.getGroups();
            assertNotNull(groups);
            assertEquals(3, groups.length);
        } catch (NullPointerException | IOException e) {
            fail("Failed to get groups: " + e.getMessage());
        } finally {
            if (batchDriver != null) {
                batchDriver.close();
            }
        }
    }

    @Test
    public void testGetBatchJobNullGroup() {
        BatchDriver batchDriver = null;
        try {
            batchDriver = new BatchDriver("testnewzini.ini", "testbatchfile.ini");
            assertNull(batchDriver.getBatchJob(null));
        } catch (NullPointerException | IOException e) {
            fail("Failed to get batch job with null group: " + e.getMessage());
        } finally {
            if (batchDriver != null) {
                batchDriver.close();
            }
        }
    }

    @Test
    public void testGetBatchJobInvalidGroup() {
        BatchDriver batchDriver = null;
        try {
            batchDriver = new BatchDriver("testnewzini.ini", "testbatchfile.ini");
            assertNull(batchDriver.getBatchJob("InvalidGroup"));
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            fail("Failed to get batch job with invalid group: " + e.getMessage());
        } finally {
            if (batchDriver != null) {
                batchDriver.close();
            }
        }
    }

    @Test
    public void testGetBatchJobValidGroup() {
        BatchDriver batchDriver = null;
        try {
            batchDriver = new BatchDriver("testnewzini.ini", "testbatchfile.ini");
            BatchJob bj = batchDriver.getBatchJob("testgroup");
            assertNotNull(bj);
            assertEquals("testserver", bj.getServer());
            assertEquals("testdirectory", bj.getDirectory());
            assertEquals(1, bj.getFirstId());
            assertEquals(10, bj.getLastId());
            assertEquals("testusername", bj.getUsername());
            assertEquals("testpassword", bj.getPassword());
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            fail("Failed to get batch job with valid group: " + e.getMessage());
        } finally {
            if (batchDriver != null) {
                batchDriver.close();
            }
        }
    }

    @Test
    public void testGetSubjectLinesNullBatchJob() {
        BatchDriver batchDriver = null;
        try {
            batchDriver = new BatchDriver("testnewzini.ini", "testbatchfile.ini");
            assertNull(batchDriver.getBatchJob(null).getSubjectLines());
        } catch (NullPointerException | IOException e) {
            fail("Failed to get subject lines with null batch job: " + e.getMessage());
        } finally {
            if (batchDriver != null) {
                batchDriver.close();
            }
        }
    }

    @Test
    public void testGetSubjectLinesValidBatchJob() {
        BatchDriver batchDriver = null;
        try {
            batchDriver = new BatchDriver("testnewzini.ini", "testbatchfile.ini");
            BatchJob bj = batchDriver.getBatchJob("testgroup");
            String[] subjectLines = bj.getSubjectLines();
            assertNotNull(subjectLines);
            assertEquals(5, subjectLines.length);
        } catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
            fail("Failed to get subject lines with valid batch job: " + e.getMessage());
        } finally {
            if (batchDriver != null) {
                batchDriver.close();
            }
        }
    }
}