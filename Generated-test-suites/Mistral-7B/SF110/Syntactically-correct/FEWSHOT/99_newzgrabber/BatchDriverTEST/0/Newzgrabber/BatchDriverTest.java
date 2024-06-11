package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Ini;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

    private BatchDriver batchDriver;
    private Ini groupsini;
    private Ini batchini;
    private Hashtable<String, String> NewsgroupsHash;
    private Hashtable<String, String> BatchgroupsHash;
    private Hashtable<String, String> BatchretrieveHash;
    private Hashtable<String, String> OptionsHash;
    private Vector<String[]> BatchgroupsVector;
    private String[] subjectlines;

    @Before
    public void setup() throws IOException {
        String newziniFilePath = "src/test/resources/newzini.ini";
        String batchfilePath = "src/test/resources/batchfile.ini";
        this.groupsini = new Ini(newziniFilePath, "Messageids");
        this.batchini = new Ini(batchfilePath, "");
        this.NewsgroupsHash = groupsini.readIni();
        this.BatchgroupsHash = new Hashtable<>();
        this.BatchgroupsVector = new Vector<>();
        this.BatchgroupsHash.put("group1", "[group1]\nretrieve = true");
        this.BatchgroupsVector.add(new String[]{"group1", "retrieve=true"});
        this.BatchgroupsHash.put("group2", "[group2]\nretrieve = false");
        this.BatchgroupsVector.add(new String[]{"group2", "retrieve=false"});
        this.BatchretrieveHash = new Hashtable<>();
        this.OptionsHash = new Hashtable<>();
        this.OptionsHash.put("server", "test.server.com");
        this.OptionsHash.put("threads", "5");
        this.OptionsHash.put("timeout", "30000");

        this.batchDriver = new BatchDriver(newziniFilePath, batchfilePath);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        String[] groups = batchDriver.getGroups();
        assertEquals(2, groups.length);
        assertEquals("group1", groups[0]);
        assertEquals("group2", groups[1]);
    }

    @Test
    public void testGetBatchJobForExistingGroup() {
        BatchJob bj = batchDriver.getBatchJob("group1");
        assertNull(bj);
    }

    @Test
    public void testGetBatchJobForNonExistingGroup() {
        BatchJob bj = batchDriver.getBatchJob("group3");
        assertNull(bj);
    }

    @Test
    public void testGetBatchJobForValidGroup() {
        BatchJob bj = batchDriver.getBatchJob("group1");
        assertNull(bj.getUsername());
        assertNull(bj.getPassword());
        assertEquals("test.server.com", bj.getServer());
        assertEquals("group1", bj.getGroup());
        assertEquals("", bj.getDirectory());
        assertEquals(0, bj.getFirstId());
        assertEquals(-1, bj.getLastId());
    }

    @Test
    public void testGetBatchJobForValidGroupWithUsernameAndPassword() {
        this.OptionsHash.put("username", "test_user");
        this.OptionsHash.put("password", "test_password");
        BatchJob bj = batchDriver.getBatchJob("group1");
        assertEquals("test_user", bj.getUsername());
        assertEquals("test_password", bj.getPassword());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBatchJobForInvalidGroupIndex() {
        batchDriver.getGroups();
        batchDriver.getBatchJob(2);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expectedSubjectLines = new String[]{"Subject: Test 1", "Subject: Test 2"};
        when(news.genericCommand("xpat Subject *")).thenReturn(expectedSubjectLines);

        batchDriver.getBatchJob("group1");

        String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    @Test
    public void testGetSubjectLinesWhenNoSubjects() {
        when(news.genericCommand("xpat Subject *")).thenReturn(new String[0]);

        batchDriver.getBatchJob("group1");

        assertNull(batchDriver.getSubjectLines());
    }

    @Test
    public void testGetSubjectLinesWhenExceptionOccurs() {
        when(news.genericCommand("xpat Subject *")).thenThrow(new IOException("Exception"));

        batchDriver.getBatchJob("group1");

        assertNull(batchDriver.getSubjectLines());
    }
}

class TestNews implements News {
    private final News news;

    public TestNews(News news) {
        this.news = news;
    }

    @Override
    public String genericCommand(String command) throws IOException {
        return news.genericCommand(command);
    }
}

class MockedNNTP implements News {
    private String server;

    public MockedNNTP(String server) {
        this.server = server;
    }

    @Override
    public NewsFactory getNewsSocket(String server) throws IOException {
        return null;
    }

    @Override
    public String genericCommand(String command) throws IOException {
        return new String[]{""};
    }

    @Override
    public void quit() {
    }
}