package Newzgrabber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.cli.Ini;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class BatchDriverTest {

    private BatchDriver batchDriver;
    private Ini mockGroupsIni;
    private Ini mockBatchIni;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockGroupsIni = mock(Ini.class);
        mockBatchIni = mock(Ini.class);
        batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        when(mockGroupsIni.readIni()).thenReturn(new Hashtable<>());
        when(mockBatchIni.readIni()).thenReturn(new Hashtable<>());
    }

    @Test
    public void testBatchDriverInitialization() {
        assertNotNull(batchDriver.getGroups());
        assertEquals(0, batchDriver.getGroups().length);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverInitializationWithNullNewzini() {
        new BatchDriver(null, "testBatchfile.ini");
    }

    @Test(expected = IOException.class)
    public void testBatchDriverInitializationWithInvalidNewzini() throws IOException {
        when(mockGroupsIni.readIni()).thenThrow(new IOException());
        new BatchDriver("testNewzini.ini", "testBatchfile.ini");
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverInitializationWithNullBatchfile() {
        new BatchDriver("testNewzini.ini", null);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverInitializationWithInvalidBatchfile() throws IOException {
        when(mockBatchIni.readIni()).thenThrow(new IOException());
        new BatchDriver("testNewzini.ini", "testBatchfile.ini");
    }

    @Test
    public void testGetGroups() {
        String[] testGroups = new String[]{"testGroup1", "testGroup2", "testGroup3"};
        when(mockBatchIni.getIniVector()).thenReturn(new Vector<>(Arrays.asList(testGroups)));
        assertArrayEquals(testGroups, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        String server = "testServer.nntp";
        String group = "testGroup1";
        String directory = "testDirectory";
        int firstid = 1;
        int lastid = 10;

        when(mockGroupsIni.get(ArgumentMatchers.eq("testGroup1"))).thenReturn("testFirstId");
        when(mockBatchIni.getProperty("server")).thenReturn(server);
        when(mockBatchIni.getProperty(ArgumentMatchers.eq("Newsgroups." + group))).thenReturn(directory);
        when(mockBatchIni.getProperty("Options.threads")).thenReturn("2");
        when(mockBatchIni.getProperty("Options.timeout")).thenReturn("30000");

        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertEquals(server, batchJob.getServer());
        assertEquals(group, batchJob.getGroup());
        assertEquals(directory, batchJob.getDirectory());
        assertEquals(firstid, batchJob.getFirstId());
        assertEquals(lastid, batchJob.getLastId());
        assertEquals(2, batchJob.getNumThreads());
        assertEquals(30000, batchJob.getTimeout());
    }

    @Test
    public void testGetBatchJobWithNullOptions() {
        String server = "testServer.nntp";
        String group = "testGroup1";
        String directory = "testDirectory";
        int firstid = 1;
        int lastid = 10;

        when(mockGroupsIni.get(ArgumentMatchers.eq("testGroup1"))).thenReturn("testFirstId");
        when(mockBatchIni.getProperty("server")).thenReturn(server);
        when(mockBatchIni.getProperty(ArgumentMatchers.eq("Newsgroups." + group))).thenReturn(directory);

        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertEquals(server, batchJob.getServer());
        assertEquals(group, batchJob.getGroup());
        assertEquals(directory, batchJob.getDirectory());
        assertEquals(firstid, batchJob.getFirstId());
        assertEquals(lastid, batchJob.getLastId());
        assertEquals(1, batchJob.getNumThreads());
        assertEquals(-1, batchJob.getTimeout());
    }

    @Test
    public void testGetSubjectLines() {
        String[] testSubjectLines = new String[]{"Subject: Test Subject 1", "Subject: Test Subject 2"};
        when(mockBatchDriver.subjectlines).thenReturn(testSubjectLines);
        BatchDriver batchDriver = spy(new BatchDriver("testNewzini.ini", "testBatchfile.ini"));
        assertEquals(testSubjectLines, batchDriver.getSubjectLines());
    }
}