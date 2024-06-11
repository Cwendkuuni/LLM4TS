package Newzgrabber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.cli.Ini;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class BatchDriverTest {

    private static final String NEWZINI = "src/test/resources/newzini.ini";
    private static final String BATCHFILE = "src/test/resources/batchfile.ini";
    private static final String GROUP = "test.group";
    private static final String SERVER = "test.server";
    private static final String DIRECTORY = "test.directory";
    private static final int FIRSTID = 1;
    private static final int LASTID = 10;
    private static final String USERNAME = "test.username";
    private static final String PASSWORD = "test.password";
    private static final int NTHREADS = 2;
    private static final int TIMEOUT = 1000;

    private Ini mocksNewzini;
    private Ini mocksBatchfile;
    private BatchDriver batchDriver;
    private NNTP mocksNews;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mocksNewzini = mock(Ini.class);
        mocksBatchfile = mock(Ini.class);
        mocksNews = mock(NNTP.class);

        when(mocksNewzini.readIni()).thenReturn(new Hashtable<>());
        when(mocksBatchfile.readIni()).thenAnswer(i -> new Hashtable<>());
        when(mocksBatchfile.getIniVector()).thenReturn(new Vector<>());
        when(mocksBatchfile.getProperty(ArgumentMatchers.eq("Newsgroups"))).thenReturn("test.group=" + GROUP);

        doReturn(new FileInputStream(NEWZINI)).when(mocksNewzini).getFileStream(ArgumentMatchers.eq("Messageids"));
        doReturn(new FileInputStream(BATCHFILE)).when(mocksBatchfile).getFileStream(ArgumentMatchers.eq(""));

        doThrow(IOException.class).when(mocksBatchfile).getFileStream(ArgumentMatchers.anyString());

        doReturn(mocksNewzini).when(batchDriver).new Ini(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        doReturn(mocksBatchfile).when(batchDriver).new Ini(ArgumentMatchers.anyString(), ArgumentMatchers.eq(""));

        when(mocksNews.genericCommand(ArgumentMatchers.anyString())).thenAnswer(i -> new String[]{"", ""});
        when(mocksNews.getNewsSocket(SERVER)).thenReturn(mocksNews);

        batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
    }

    @After
    public void tearDown() throws Exception {
        reset(mocksNewzini, mocksBatchfile, mocksNews);
    }

    @Test
    public void testBatchDriverConstruction() {
        assertNotNull(batchDriver);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverConstructionWithNullNewzini() {
        new BatchDriver(null, BATCHFILE);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverConstructionWithNullBatchfile() {
        new BatchDriver(NEWZINI, null);
    }

    @Test
    public void testGetGroups() throws Exception {
        final String[] expectedGroups = {"test.group1", "test.group2"};
        when(mocksBatchfile.getIniVector()).thenReturn(new Vector<>(Arrays.asList(expectedGroups)));
        final String[] actualGroups = batchDriver.getGroups();
        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroupsWithEmptyVector() throws Exception {
        when(mocksBatchfile.getIniVector()).thenReturn(new Vector<>());
        batchDriver.getGroups();
    }

    @Test
    public void testGetBatchJob() throws Exception {
        final String server = SERVER;
        final String group = GROUP;
        final String directory = DIRECTORY;
        final int firstid = FIRSTID;
        final int lastid = LASTID;
        final String username = USERNAME;
        final String password = PASSWORD;       
        when(mocksBatchFile.get(GROUP)).thenReturn(directory);
        when(mocksBatchFile.get("Options")).thenReturn(new Ini("", ""));
        when(mocksBatchFile.get("Options.threads")).thenReturn("2");
        when(mocksBatchFile.get("Options.timeout")).thenReturn("1000");
        when(mocksBatchFile.get("Options.server")).thenReturn(server);
        when(mocksBatchFile.get("Options.username")).thenReturn(username);
        when(mocksBatchFile.get("Options.password")).thenReturn(password);

        final BatchJob actualBatchJob = batchDriver.getBatchJob(group);
        final BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstid, lastid);
        expectedBatchJob.setUsername(username);
        expectedBatchJob.setPassword(password);

        assertEquals(expectedBatchJob, actualBatchJob);
    }

    @Test
    public void testGetBatchJobWithNullGroup() {
        assertNull(batchDriver.getBatchJob(null));
    }

    @Test
    public void testGetBatchJobWithNonExistingGroup() throws Exception {
        batchDriver.getBatchJob("nonexistinggroup");
        verify(mocksNews, never()).quit();
    }

    @Test
    public void testGetSubjectLines() throws Exception {
        final String[] expectedSubjectLines = {"Subject 1", "Subject 2"};
        when(mocksNews.genericCommand("xpat Subject " + FIRSTID + "-" + LASTID + " *")).thenReturn(expectedSubjectLines);
        final String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }

    @Test
    public void testGetSubjectLinesWithEmptyArray() throws Exception {
        when(mocksNews.genericCommand("xpat Subject " + FIRSTID + "-" + LASTID + " *")).thenReturn(new String[0]);
        final String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertArrayEquals(new String[0], actualSubjectLines);
    }

    @Test
    public void testGetSubjectLinesWithNullNews() throws Exception {
        when(mocksNews.genericCommand("xpat Subject " + FIRSTID + "-" + LASTID + " *")).thenReturn(new String[0]);
        batchDriver.setNews(null);
        final String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertArrayEquals(new String[0], actualSubjectLines);
    }
}