package Newzgrabber;

import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {

    private static BatchDriver batchDriver;

    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            batchDriver = new BatchDriver("test.ini", "test.ini");
        } catch (NullPointerException | IOException e) {
            fail("Failed to initialize the BatchDriver instance: " + e.getMessage());
        }
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJob() {
        String server = "server1";
        String group = "group1";
        String directory = "/path/to/directory1";
        int firstId = 1;
        int lastId = 10;
        String username = "testUser";
        String password = "testPassword";
        BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstId, lastId, username, password);
        BatchJob actualBatchJob = batchDriver.getBatchJob(group);
        assertEquals(expectedBatchJob, actualBatchJob);
    }

    // Add more test cases for other methods as needed

    @AfterClass
    public static void tearDownAfterClass() {
        batchDriver = null;
    }
}