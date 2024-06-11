package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Ini;
import java.util.List;
import java.util.Vector;

@RunWith(MockitoJUnitRunner.class)
public class BatchDriverTest {

    @InjectMocks
    private BatchDriver batchDriver;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        final Ini groupsini = Mockito.mock(Ini.class);
        final Ini batchgroups = Mockito.mock(Ini.class);
        final Vector<String[]> batchgroupsVector = new Vector<>();
        batchgroupsVector.add(new String[]{"group1", "server1", "dir1"});
        Mockito.when(batchgroups.getIniVector()).thenReturn(batchgroupsVector);
        final Hashtable<String, String> batchgroupsHash = new Hashtable<>();
        batchgroupsHash.put("group1", "server1,dir1");
        Mockito.when(batchgroups.readIni()).thenReturn(batchgroupsHash);
        final Hashtable<String, String> optionsHash = new Hashtable<>();
        optionsHash.put("server", "server1");
        optionsHash.put("directory", "dir1");
        Mockito.when(optionsHash.get("server")).thenReturn("server1");
        Mockito.when(optionsHash.get("directory")).thenReturn("dir1");
        Mockito.when(optionsHash.containsKey("username")).thenReturn(true);
        Mockito.when(optionsHash.containsKey("password")).thenReturn(true);
        Mockito.when(optionsHash.get("threads")).thenReturn("1");
        Mockito.when(optionsHash.get("timeout")).thenReturn("1000");
        Mockito.when(groupsini.readIni()).thenReturn(new Hashtable<>());
        this.batchDriver = new BatchDriver("groups.ini", "batch.ini");
    }

    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertEquals("size", 1, groups.length);
        assertEquals("first element", "group1", groups[0]);
    }

    @Test
    public void testGetBatchJobNullGroup() {
        assertNull("batchJob is null", batchDriver.getBatchJob(null));
    }

    @Test
    public void testGetBatchJobInvalidGroup() {
        assertNull("batchJob is null", batchDriver.getBatchJob("invalidGroup"));
    }

    @Test
    public void testGetBatchJobValidGroup() throws IOException {
        final String server = "server1";
        final String directory = "dir1";
        final int firstId = 1;
        final int lastId = 10;
        final String[] groupInfo = new String[]{"Group: group1"};
        Mockito.when(batchgroups.get(Mockito.anyString())).thenReturn(groupInfo);
        Mockito.when(Newzgrabber.nf.getNewsSocket(server)).thenReturn(Mockito.mock(NNTP.class));
        Mockito.when(Newzgrabber.nf.getNewsSocket(server).genericCommand(Mockito.anyString())).thenReturn(new String[]{"Subject: 1-10"});

        final BatchJob bj = batchDriver.getBatchJob("group1");
        assertEquals("server", server, bj.getServer());
        assertEquals("group", "group1", bj.getGroup());
        assertEquals("directory", "dir1", bj.getDirectory());
        assertEquals("firstId", firstId, bj.getFirstId());
        assertEquals("lastId", lastId, bj.getLastId());
        assertEquals("username", "", bj.getUsername());
        assertEquals("password", "", bj.getPassword());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBatchJobInvalidIndex() {
        batchDriver.getBatchJob(0);
    }

    @Test
    public void testGetSubjectLines() {
        final String[] expected = new String[]{"Subject: 1-10: line1", "Subject: 1-10: line2"};
        Mockito.when(batchDriver.getBatchJob("group1").getSubjectLines()).thenReturn(expected);
        assertArrayEquals("subjectLines", expected, batchDriver.getSubjectLines());
    }
}