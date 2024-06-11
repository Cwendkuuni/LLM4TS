package Newzgrabber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class BatchDriverTest {

    private static final String NEWZINI = "src/test/resources/newzini.ini";
    private static final String BATCHFILE = "src/test/resources/batchfile.ini";

    private BatchDriver batchDriver;
    private Ini mockGroupsini;
    private Ini mockBatchini;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockGroupsini = mock(Ini.class);
        mockBatchini = mock(Ini.class);

        when(mockGroupsini.readIni()).thenReturn(getTestData("Messageids", "groupsini.ini"));
        when(mockBatchini.newIni(anyString(), anyString())).thenReturn(mockBatchini);
        when(mockBatchini.readIni()).thenCallRealMethod();

        try {
            batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
        } catch (NullPointerException | IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(batchDriver);
        verify(mockGroupsini, times(1)).readIni();
        verify(mockBatchini, times(2)).newIni(eq(NEWZINI), eq(""));
        verify(mockBatchini, times(1)).readIni();
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};
        when(mockBatchini.getIniVector()).thenReturn(new Vector<>(Arrays.asList(expectedGroups)));

        String[] actualGroups = batchDriver.getGroups();

        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJob_ValidGroup() {
        String server = "news.example.com";
        String group = "group1";
        String directory = "/path/to/directory";
        int firstId = 1;
        int lastId = 10;
        String username = "testUser";
        String password = "testPassword";

        when(mockBatchini.get(eq("Newsgroups"), eq(group))).thenReturn(group);
        when(mockBatchini.get(eq("Retrieve"), eq(group))).thenReturn(directory);
        when(mockBatchini.get(eq("Options"), eq("server"))).thenReturn(server);
        when(mockBatchini.get(eq("Options"), eq("username"))).thenReturn(username);
        when(mockBatchini.get(eq("Options"), eq("password"))).thenReturn(password);
        when(mockBatchini.get(eq("Options"), eq("threads"))).thenReturn("4");
        when(mockBatchini.get(eq("Options"), eq("timeout"))).thenReturn("300");

        BatchJob actualBatchJob = batchDriver.getBatchJob(group);

        assertEquals(server, actualBatchJob.getServer());
        assertEquals(group, actualBatchJob.getGroup());
        assertEquals(directory, actualBatchJob.getDirectory());
        assertEquals(firstId, actualBatchJob.getFirstId());
        assertEquals(lastId, actualBatchJob.getLastId());
        assertEquals(username, actualBatchJob.getUsername());
        assertEquals(password, actualBatchJob.getPassword());
        assertEquals(4, actualBatchJob.getNumberOfThreads());
        assertEquals(300, actualBatchJob.getTimeout());
    }

    @Test
    public void testGetBatchJob_InvalidGroup() {
        when(mockBatchini.getIniVector()).thenReturn(new Vector<>());

        BatchJob actualBatchJob = batchDriver.getBatchJob("nonexistentGroup");

        assertNull(actualBatchJob);
    }

    @Test
    public void testGetBatchJob_MissingRequiredFields() {
        String group = "group1";

        when(mockBatchini.get(eq("Newsgroups"), eq(group))).thenReturn(group);
        when(mockBatchini.get(eq("Retrieve"), eq(group))).thenReturn("");

        BatchJob actualBatchJob = batchDriver.getBatchJob(group);

        assertNull(actualBatchJob);
    }

    @Test
    public void testGetBatchJob_MissingOptionalFields() {
        String server = "news.example.com";
        String group = "group1";
        String directory = "/path/to/directory";

        when(mockBatchini.get(eq("Newsgroups"), eq(group))).thenReturn(group);
        when(mockBatchini.get(eq("Retrieve"), eq(group))).thenReturn(directory);
        when(mockBatchini.get(eq("Options"), eq("server"))).thenReturn(null);

        BatchJob actualBatchJob = batchDriver.getBatchJob(group);

        assertEquals(server, actualBatchJob.getServer());
        assertNull(actualBatchJob.getUsername());
        assertNull(actualBatchJob.getPassword());
        assertEquals(1, actualBatchJob.getNumberOfThreads());
        assertTrue(actualBatchJob.getTimeout() < 0);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};

        when(mockBatchDriver.subjectlines).thenReturn(expectedSubjectLines);

        String[] actualSubjectLines = batchDriver.getSubjectLines();

        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    private Object[] getTestData(String sectionName, String fileName) {
        try {
            return new File(fileName).getData(File.INPUT_STREAM);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}