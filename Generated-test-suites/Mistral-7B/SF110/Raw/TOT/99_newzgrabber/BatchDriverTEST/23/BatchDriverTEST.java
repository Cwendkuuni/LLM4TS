import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;

public class BatchDriverTest {
    

    @Test
    public void testGetGroups() {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] groups = batchDriver.getGroups();
        assertEquals("Size of groups array should be equal to the number of groups", 2, groups.length);
        assertArrayEquals("Groups array should contain ['group1', 'group2']", new String[]{"group1", "group2"}, groups);
    }

    @Test
    public void testGetBatchJob_ValidParameters() {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        String group = "group1";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNull("getBatchJob method should return null when group is not found", batchJob);

        String server = "test.nntp.com";
        String directory = "test";
        int firstId = 1;
        int lastId = 10;
        batchDriver.OptionsHash.put("server", server);
        batchDriver.OptionsHash.put("directory", directory);
        batchDriver.OptionsHash.put("threads", "2");
        batchDriver.OptionsHash.put("timeout", "10000");
        batchDriver.BatchgroupsHash.put(group, "server=test.nntp.com,directory=test");

        batchJob = batchDriver.getBatchJob(group);
        BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstId, lastId);
        expectedBatchJob.setUsername("testUsername");
        expectedBatchJob.setPassword("testPassword");
        assertEquals("BatchJob objects should be equal", expectedBatchJob, batchJob);
    }

    @Test
    public void testGetBatchJob_InvalidGroup() {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        String group = "nonExistentGroup";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNull("getBatchJob method should return null when group is not found", batchJob);
    }

    @Test
    public void testGetBatchJob_EmptyOptionsHash() {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        String group = "group1";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNull("getBatchJob method should return null when server and directory are not set", batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String newzini = "test.ini";
        String batchfile = "test.ini";
        String group = "group1";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        BatchJob batchJob = batchDriver.getBatchJob(group);
        String[] expectedSubjectLines = new String[]{"Subject line 1", "Subject line 2"};
        batchJob.subjectlines = expectedSubjectLines;
        String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertArrayEquals("SubjectLines array should be equal to the expected subject lines", expectedSubjectLines, actualSubjectLines);
    }

    
}