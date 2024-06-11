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
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

    private BatchDriver batchDriver;
    private Ini groupsiniMock, batchiniMock;
    private NNTP newsMock;

    @Before
    public void setup() throws IOException, NullPointerException {
        groupsiniMock = mock(Ini.class);
        batchiniMock = mock(Ini.class);
        newsMock = mock(NNTP.class);
        when(groupsiniMock.readIni()).thenReturn(new Properties());
        when(batchiniMock.readIni()).thenReturn(new Properties());
        when(batchiniMock.getIniVector()).thenReturn(new Vector<>());
        when(batchiniMock.getProperty("Retrieve")).thenReturn("Retrieve");
        when(batchiniMock.getProperty("Options")).thenReturn("Options");
        when(batchiniMock.getProperty("Newsgroups")).thenReturn("Newsgroups");

        batchDriver = new BatchDriver("groups.ini", "batch.ini");
        batchDriver.setGroupsini(groupsiniMock);
        batchDriver.setBatchini(batchiniMock);
        batchDriver.setNews(newsMock);
    }

    @Test
    public void testBatchDriverCreation() {
        assertNull(batchDriver.getGroups());
        assertNull(batchDriver.getBatchJob(null));
        assertNull(batchDriver.getSubjectLines());
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverCreationWithNullNewzini() {
        new BatchDriver(null, "batch.ini");
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverCreationWithNullBatchfile() {
        new BatchDriver("groups.ini", null);
    }

    @Test
    public void testGetGroups() {
        String[] groups = {"group1", "group2"};
        when(batchiniMock.getIniVector()).thenReturn(new Vector<>(Arrays.asList(groups)));
        assertArrayEquals(groups, batchDriver.getGroups());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroupsWithEmptyVector() {
        when(batchiniMock.getIniVector()).thenReturn(new Vector<>());
        batchDriver.getGroups();
    }

    @Test(expected = NullPointerException.class)
    public void testGetBatchJobWithNullGroupName() {
        batchDriver.getBatchJob(null);
    }

    @Test
    public void testGetBatchJob() {
        String server = "server1";
        String directory = "directory1";
        String group = "group1";
        int firstid = 1;
        int lastid = 10;
        String[] groupinfo = {"group1", "12345", "13567", "14568"};
        when(batchiniMock.getProperty("server")).thenReturn(server);
        when(batchiniMock.getProperty(group)).thenReturn(directory.trim());
        when(newsMock.genericCommand("group " + group)).thenReturn(groupinfo);
        when(groupsiniMock.get(group)).thenReturn(String.valueOf(firstid));

        BatchJob bj = batchDriver.getBatchJob(group);
        assertEquals(server, bj.getServer());
        assertEquals(group, bj.getGroup());
        assertEquals(directory, bj.getDirectory());
        assertEquals(firstid, bj.getFirstid());
        assertEquals(lastid, bj.getLastid());
        assertNull(bj.getUsername());
        assertNull(bj.getPassword());
    }

    @Test
    public void testGetBatchJobWithNoGroupInfo() {
        when(newsMock.genericCommand("group " + "group1")).thenReturn(new String[0]);
        assertNull(batchDriver.getBatchJob("group1"));
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = {"Subject: Test1", "Subject: Test2"};
        when(newsMock.genericCommand("xpat Subject *")).thenReturn(subjectLines);
        assertArrayEquals(subjectLines, batchDriver.getSubjectLines());
    }

    @Test
    public void testGetSubjectLinesWithEmptyResponse() {
        when(newsMock.genericCommand("xpat Subject *")).thenReturn(new String[0]);
        assertArrayEquals(new String[0], batchDriver.getSubjectLines());
    }
}