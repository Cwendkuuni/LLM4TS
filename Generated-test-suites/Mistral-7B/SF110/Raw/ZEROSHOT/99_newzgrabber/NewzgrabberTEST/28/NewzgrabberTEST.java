package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import Newzgrabber.*;

import java.io.File;
import java.nio.file.Files;
import java.util.Vector;

public class NewzgrabberTest {

    private static final String TEST_DATA_DIR = "test_data";
    private static final String INI_FILE_NAME = "test.ini";
    private static final String BATCH_FILE_NAME = "test_batch.ini";
    private static final String JAR_FILE_NAME = "Newz.jar";

    @Before
    public void setUp() throws Exception {
        // Set up test data directory
        File testDir = new File(TEST_DATA_DIR);
        if (!testDir.exists()) {
            testDir.mkdir();
        }

        // Copy test data files to test data directory
        Files.copy(new File("src/test/resources/Newzgrabber/batch.ini").toPath(),
                new File(TEST_DATA_DIR, BATCH_FILE_NAME).toPath());
        Files.copy(new File("src/test/resources/Newzgrabber/Newzgrabber.ini").toPath(),
                new File(TEST_DATA_DIR, INI_FILE_NAME).toPath());
        Files.copy(new File("src/test/resources/Newz.jar").toPath(),
                new File(TEST_DATA_DIR, JAR_FILE_NAME).toPath());
    }

    @After
    public void tearDown() throws Exception {
        // Clean up test data directory
        File testDir = new File(TEST_DATA_DIR);
        Files.deleteIfExists(new File(TEST_DATA_DIR, BATCH_FILE_NAME).toPath());
        Files.deleteIfExists(new File(TEST_DATA_DIR, INI_FILE_NAME).toPath());
        Files.deleteIfExists(new File(TEST_DATA_DIR, JAR_FILE_NAME).toPath());
        Files.deleteRecursively(testDir.toPath());
    }

    @Test
    public void testNewzgrabberBatchMode() throws Exception {
        // Set up Newzgrabber with test data
        System.setProperty("user.dir", TEST_DATA_DIR);
        String[] args = {"-batch"};
        Newzgrabber newzgrabber = new Newzgrabber(args);

        // Verify that batch mode is enabled
        assertTrue(Newzgrabber.IsBatch);

        // Verify that the correct ini files are used
        assertEquals(TEST_DATA_DIR + File.separator + "Newzgrabber.ini", Newzgrabber.NewzInifilename);
        assertEquals(TEST_DATA_DIR + File.separator + "batch.ini", Newzgrabber.BatchFilename);

        // Perform test in a separate thread to not block the test execution
        new Thread(() -> {
            try {
                newzgrabber.initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Wait for test to finish
        Thread.sleep(5000);

        // Verify that the batch job is executed correctly
        Vector<Object[]> downloadData = Newzgrabber.GroupDownloadCount;
        assertEquals(1, downloadData.size());
        Object[] downloadDataRow = downloadData.get(0);
        assertEquals("Test Group", downloadDataRow[0]);
        assertEquals(10, ((Integer) downloadDataRow[1]).intValue());

        // Verify that the ini file is updated correctly
        Ini ini = new Ini(INI_FILE_NAME, "Messageids");
        Vector<String[]> newids = ini.getIniVector();
        String[] newidsRow = newids.get(0);
        assertEquals("Test Group", newidsRow[0]);
        assertEquals("11", newidsRow[1]);
    }
}