package Newzgrabber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class BatchDriverTest {

    private BatchDriver batchDriver;
    private Ini mockGroupsini;
    private Ini mockBatchgroups;
    private Hashtable mockNewsgroupsHash;
    private Hashtable mockBatchgroupsHash;
    private Hashtable mockBatchretrieveHash;
    private Hashtable mockOptionsHash;
    private Vector mockBatchgroupsVector;
    private String[] mockSubjectlines;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockGroupsini = mock(Ini.class);
        mockBatchgroups = mock(Ini.class);
        mockNewsgroupsHash = mock(Hashtable.class);
        mockBatchgroupsHash = mock(Hashtable.class);
        mockBatchretrieveHash = mock(Hashtable.class);
        mockOptionsHash = mock(Hashtable.class);
        mockBatchgroupsVector = mock(Vector.class);
        mockSubjectlines = new String[5];

        when(mockGroupsini.readIni()).thenReturn(mockNewsgroupsHash);
        when(mockBatchgroups.readIni()).thenReturn(mockBatchgroupsHash);
        when(mockBatchgroups.getIniVector()).thenReturn(mockBatchgroupsVector);
        when(mockBatchgroups.readIni()).thenReturn(mockBatchretrieveHash);
        when(mockBatchgroups.readIni()).thenReturn(mockOptionsHash);

        batchDriver = new BatchDriver("test.ini", "test.ini");
        batchDriver.setGroupsini(mockGroupsini);
        batchDriver.setBatchgroups(mockBatchgroups);
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};
        when(mockBatchgroupsVector.toArray(new String[0])).thenReturn(expectedGroups);

        String[] actualGroups = batchDriver.getGroups();

        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJob_ValidGroup() throws Exception {
        String server = "test.server.com";
        String directory = "test/directory";
        String group = "test.group";
        int firstId = 1;
        int lastId = 5;
        String username = "testuser";
        String password = "testpassword";

        when(mockBatchgroupsHash.get(group)).thenReturn("test.directory");
        when(mockOptionsHash.get("server")).thenReturn(server);
        when(mockOptionsHash.get("username")).thenReturn(username);
        when(mockOptionsHash.get("password")).thenReturn(password);
        when(mockOptionsHash.get("threads")).thenReturn("2");
        when(mockOptionsHash.get("timeout")).thenReturn("30000");

        BatchJob actualBatchJob = batchDriver.getBatchJob(group);
        BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstId, lastId);
        expectedBatchJob.setUsername(username);
        expectedBatchJob.setPassword(password);

        assertEquals(expectedBatchJob, actualBatchJob);
    }

    @Test
    public void testGetBatchJob_InvalidGroup() {
        when(mockBatchgroupsHash.get("invalidGroup")).thenThrow(new Exception("Invalid group"));

        BatchJob actualBatchJob = batchDriver.getBatchJob("invalidGroup");

        assertNull(actualBatchJob);
    }

    @Test
    public void testGetBatchJob_NoServer() {
        when(mockOptionsHash.get("server")).thenReturn(null);

        BatchJob actualBatchJob = batchDriver.getBatchJob("test.group");

        assertNull(actualBatchJob);
    }

    @Test
    public void testGetBatchJob_NoDirectory() {
        String server = "test.server.com";
        when(mockOptionsHash.get("server")).thenReturn(server);
        when(mockBatchgroupsHash.get("test.group")).thenReturn("");

        BatchJob actualBatchJob = batchDriver.getBatchJob("test.group");

        assertNull(actualBatchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
        when(mockBatchDriver.getNews("test.server.com")).thenReturn(mockNewsSocket);
        when(mockNewsSocket.genericCommand("xpat Subject 1-5 *")).thenReturn(mockSubjectlines);

        String[] actualSubjectLines = batchDriver.getSubjectLines();

        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }
}