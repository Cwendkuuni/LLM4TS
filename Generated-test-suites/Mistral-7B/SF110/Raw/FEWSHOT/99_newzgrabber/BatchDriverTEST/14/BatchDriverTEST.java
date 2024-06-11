package Newzgrabber;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInstance() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";
        BatchDriver bd = null;
        try {
            bd = new BatchDriver(newzini, batchfile);
        } catch (NullPointerException | IOException e) {
            fail("BatchDriver initialization failed: " + e.getMessage());
        }
        assertNotNull(bd);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String[] groups = bd.getGroups();
        assertEquals(3, groups.length);
        assertTrue(Arrays.asList(groups).contains("test.group1"));
        assertTrue(Arrays.asList(groups).contains("test.group2"));
        assertTrue(Arrays.asList(groups).contains("test.group3"));
    }

    @Test
    public void testGetBatchJob() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);

        BatchJob bj1 = bd.getBatchJob("test.group1");
        assertNull(bj1);

        BatchJob bj2 = bd.getBatchJob("test.group2");
        assertNotNull(bj2);
        assertEquals("nntp.server.1", bj2.getServer());
        assertEquals("path/to/directory/2", bj2.getDirectory());
        assertEquals(1, bj2.getFirstId());
        assertEquals(10, bj2.getLastId());
        assertNull(bj2.getUsername());
        assertNull(bj2.getPassword());

        BatchJob bj3 = bd.getBatchJob("test.group3");
        assertNull(bj3);
    }

    @Test
    public void testGetSubjectLines() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String[] expectedSubjectLines = {"Subject: Test Article 1", "Subject: Test Article 2", "Subject: Test Article 3"};
        String[] subjectLines = bd.getSubjectLines();
        assertNotNull(subjectLines);
        assertArrayEquals(expectedSubjectLines, subjectLines);
    }

    @Test
    public void testGetSubjectLinesEmpty() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile_empty.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String[] subjectLines = bd.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(0, subjectLines.length);
    }

    @Test
    public void testNewsgroupsHash() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        Hashtable newsgroupsHash = bd.NewsgroupsHash;
        assertEquals("test.group1", newsgroupsHash.keySet().toArray()[0]);
        assertEquals("1", newsgroupsHash.get("test.group1"));
        assertEquals("test.group2", newsgroupsHash.keySet().toArray()[1]);
        assertEquals("2", newsgroupsHash.get("test.group2"));
        assertEquals("test.group3", newsgroupsHash.keySet().toArray()[2]);
        assertEquals("3", newsgroupsHash.get("test.group3"));
    }

    @Test
    public void testBatchGroupsHash() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        Hashtable batchgroupsHash = bd.BatchgroupsHash;
        assertEquals("test.group1", batchgroupsHash.keySet().toArray()[0]);
        assertEquals("server1,path/to/directory1", batchgroupsHash.get("test.group1"));
        assertEquals("test.group2", batchgroupsHash.keySet().toArray()[1]);
        assertEquals("server2,path/to/directory2", batchgroupsHash.get("test.group2"));
        assertEquals("test.group3", batchgroupsHash.keySet().toArray()[2]);
        assertEquals("server3,path/to/directory3", batchgroupsHash.get("test.group3"));
    }

    @Test
    public void testBatchRetrieveHash() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        Hashtable batchretrieveHash = bd.BatchretrieveHash;
        assertEquals("test.group1", batchretrieveHash.keySet().toArray()[0]);
        assertArrayEquals(new int[] {1, 2}, ((Integer[])batchretrieveHash.get("test.group1")));
        assertEquals("test.group2", batchretrieveHash.keySet().toArray()[1]);
        assertArrayEquals(new int[] {3, 4}, ((Integer[])batchretrieveHash.get("test.group2")));
        assertEquals("test.group3", batchretrieveHash.keySet().toArray()[2]);
        assertArrayEquals(new int[] {5, 6}, ((Integer[])batchretrieveHash.get("test.group3")));
    }

    @Test
    public void testOptionsHash() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        Hashtable optionsHash = bd.OptionsHash;
        assertEquals("threads", optionsHash.keySet().toArray()[0]);
        assertEquals("2", optionsHash.get("threads"));
        assertEquals("timeout", optionsHash.keySet().toArray()[1]);
        assertEquals("120", optionsHash.get("timeout"));
        assertEquals("server", optionsHash.keySet().toArray()[2]);
        assertEquals("nntp.server.1", optionsHash.get("server"));
        assertEquals("username", optionsHash.keySet().toArray()[3]);
        assertNull(optionsHash.get("username"));
        assertEquals("password", optionsHash.keySet().toArray()[4]);
        assertNull(optionsHash.get("password"));
    }

    @Test
    public void testBatchGroupsVector() {
        String newzini = "testdata/newzini.ini";
        String batchfile = "testdata/batchfile.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        Vector batchgroupsVector = bd.BatchgroupsVector;
        assertEquals(3, batchgroupsVector.size());
        assertEquals(Arrays.asList(new String[]{"test.group1", "server1,path/to/directory1"}), batchgroupsVector.get(0));
        assertEquals(Arrays.asList(new String[]{"test.group2", "server2,path/to/directory2"}), batchgroupsVector.get(1));
        assertEquals(Arrays.asList(new String[]{"test.group3", "server3,path/to/directory3"}), batchgroupsVector.get(2));
    }
}