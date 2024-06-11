package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

    private BatchDriver batchDriver;
    private Ini groupsini, batchgroups;
    private static final String NEWZINI = "testNewzini.ini";
    private static final String BATCHFILE = "testBatchfile.ini";

    @Before
    public void setUp() throws IOException {
        groupsini = new Ini(NEWZINI, "Messageids");
        batchgroups = new Ini(BATCHFILE, "");

        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        String[] expectedGroups = {"group1", "group2", "group3"};
        groupsini.setProperty("group1", "value1");
        groupsini.setProperty("group2", "value2");
        groupsini.setProperty("group3", "value3");

        batchgroups.setProperty("Newsgroups", "group1,group2,group3");

        String[] actualGroups = batchDriver.getGroups();

        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJobNullGroup() {
        String server = "test.server.com";
        String directory = "/test/directory";
        Hashtable optionsHash = new Hashtable();
        optionsHash.put("server", server);
        optionsHash.put("directory", directory);
        optionsHash.put("threads", "2");

        batchDriver.OptionsHash = optionsHash;

        assertNull(batchDriver.getBatchJob(null));
    }

    @Test
    public void testGetBatchJobEmptyGroupVector() {
        String server = "test.server.com";
        String directory = "/test/directory";
        Hashtable optionsHash = new Hashtable();
        optionsHash.put("server", server);
        optionsHash.put("directory", directory);
        optionsHash.put("threads", "2");

        batchDriver.OptionsHash = optionsHash;

        batchgroups.setProperty("Newsgroups", "");

        assertNull(batchDriver.getBatchJob(""));
    }

    @Test
    public void testGetBatchJobValidGroup() throws IOException {
        String server = "test.server.com";
        String directory = "/test/directory";
        String group = "testGroup";
        Hashtable optionsHash = new Hashtable();
        optionsHash.put("server", server);
        optionsHash.put("directory", directory);
        optionsHash.put("threads", "2");

        batchDriver.OptionsHash = optionsHash;

        when(groupsini.get(group)).thenReturn("3 5");
        when(batchgroups.get(group)).thenReturn("server testServer server2 server3 options username password");

        BatchJob expectedBatchJob = new BatchJob(server, group, directory, 3, 5, "username", "password");
        BatchJob actualBatchJob = batchDriver.getBatchJob(group);

        assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
        assertEquals(expectedBatchJob.getGroup(), actualBatchJob.getGroup());
        assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
        assertEquals(expectedBatchJob.getFirstId(), actualBatchJob.getFirstId());
        assertEquals(expectedBatchJob.getLastId(), actualBatchJob.getLastId());
        assertEquals(expectedBatchJob.getUsername(), actualBatchJob.getUsername());
        assertEquals(expectedBatchJob.getPassword(), actualBatchJob.getPassword());
    }

    @Test
    public void testGetSubjectLines() {
        NewsFactory nf = mock(NewsFactory.class);
        NNTP news = mock(NNTP.class);
        when(nf.getNewsSocket(server)).thenReturn(news);
        when(news.genericCommand("xpat Subject 0-100 *")).thenReturn(new String[]{"Subject: Test 1\r\n", "Subject: Test 2\r\n"});

        batchDriver.subjectlines = batchDriver.getSubjectLines();

        String[] expectedSubjectLines = {"Test 1", "Test 2"};
        assertArrayEquals(expectedSubjectLines, batchDriver.subjectlines);
    }
}