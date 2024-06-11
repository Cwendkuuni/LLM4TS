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
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInstance() {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        assertNull(bd.getGroups());
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverInstanceNull() {
        final String newzini = null;
        final String batchfile = "test.ini";
        new BatchDriver(newzini, batchfile);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverInstanceIO() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = new File("non-existent-file.ini").getAbsolutePath();
        new BatchDriver(newzini, batchfile);
    }

    @Test
    public void testGetGroups() {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final Ini groupsini = mock(Ini.class);
        final Ini batchgroups = mock(Ini.class);
        final Vector batchgroupsVector = new Vector();
        batchgroupsVector.add(new String[]{"group1", "group1-server", "group1-directory"});
        when(groupsini.readIni()).thenReturn(new Hashtable<>());
        when(batchgroups.readIni()).thenReturn(new Hashtable<>());
        when(batchgroups.getIniVector()).thenReturn(batchgroupsVector);
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String[] groups = bd.getGroups();
        assertArrayEquals(new String[]{"group1"}, groups);
    }

    @Test
    public void testGetBatchJobNullGroup() {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = null;
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        assertNull(bd.getBatchJob(group));
    }

    @Test
    public void testGetBatchJobWrongGroup() {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final Ini groupsini = mock(Ini.class);
        final Ini batchgroups = mock(Ini.class);
        final Hashtable newsgroupsHash = new Hashtable<>();
        newsgroupsHash.put("group1", "1");
        when(groupsini.readIni()).thenReturn(newsgroupsHash);
        when(batchgroups.readIni()).thenReturn(new Hashtable<>());
        when(batchgroups.getIniVector()).thenReturn(new Vector<>());
        when(batchgroups.getProperty("Newsgroups")).thenReturn("group2");
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        assertNull(bd.getBatchJob("group1"));
    }

    @Test
    public void testGetBatchJobSuccess() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final Ini groupsini = mock(Ini.class);
        final Ini batchgroups = mock(Ini.class);
        final Hashtable newsgroupsHash = new Hashtable<>();
        newsgroupsHash.put("group1", "1");
        when(groupsini.readIni()).thenReturn(newsgroupsHash);
        when(batchgroups.readIni()).thenReturn(new Hashtable<>());
        when(batchgroups.getIniVector()).thenReturn(new Vector<>());
        when(batchgroups.getProperty("Newsgroups")).thenReturn("group1");
        when(batchgroups.getProperty("Retrieve")).thenReturn("retrieve-group1");
        when(batchgroups.getProperty("Options")).thenReturn("server1 server2 username password threads 5 timeout 30000");
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        BatchJob bj = bd.getBatchJob("group1");
        assertEquals("server1 server2", bj.getServer());
        assertEquals("group1", bj.getGroup());
        assertEquals("retrieve-group1", bj.getDirectory());
        assertEquals(5, bj.getThreads());
        assertEquals(30000, bj.getTimeout());
        assertNull(bj.getUsername());
        assertNull(bj.getPassword());
    }

    @Test
    public void testGetBatchJobSuccessWithCredentials() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final Ini groupsini = mock(Ini.class);
        final Ini batchgroups = mock(Ini.class);
        final Hashtable newsgroupsHash = new Hashtable<>();
        newsgroupsHash.put("group1", "1");
        when(groupsini.readIni()).thenReturn(newsgroupsHash);
        when(batchgroups.readIni()).thenReturn(new Hashtable<>());
        when(batchgroups.getIniVector()).thenReturn(new Vector<>());
        when(batchgroups.getProperty("Newsgroups")).thenReturn("group1");
        when(batchgroups.getProperty("Retrieve")).thenReturn("retrieve-group1");
        when(batchgroups.getProperty("Options")).thenReturn("server1 server2 username1 password1 threads 5 timeout 30000");
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        BatchJob bj = bd.getBatchJob("group1");
        assertEquals("server1 server2", bj.getServer());
        assertEquals("group1", bj.getGroup());
        assertEquals("retrieve-group1", bj.getDirectory());
        assertEquals(5, bj.getThreads());
        assertEquals(30000, bj.getTimeout());
        assertEquals("username1", bj.getUsername());
        assertEquals("password1", bj.getPassword());
    }

    @Test
    public void testGetSubjectLinesEmpty() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final Ini groupsini = mock(Ini.class);
        final Ini batchgroups = mock(Ini.class);
        final NewsFactory nf = mock(NewsFactory.class);
        final NNTP news = mock(NNTP.class);
        when(groupsini.readIni()).thenReturn(new Hashtable<>());
        when(batchgroups.readIni()).thenReturn(new Hashtable<>());
        when(batchgroups.getIniVector()).thenReturn(new Vector<>());
        when(batchgroups.getProperty("Newsgroups")).thenReturn("group1");
        when(batchgroups.getProperty("Retrieve")).thenReturn("retrieve-group1");
        when(batchgroups.getProperty("Options")).thenReturn("server1 server2 username password threads 5 timeout 30000");
        when(nf.getNewsSocket("server1")).thenReturn(news);
        when(news.genericCommand("group group1")).thenReturn(new String[]{"Group: group1\n"});
        when(news.genericCommand("xpat Subject * *")).thenReturn(new String[0]);
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String[] subjectLines = bd.getSubjectLines();
        assertArrayEquals(new String[0], subjectLines);
    }

    @Test
    public void testGetSubjectLinesSuccess() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final Ini groupsini = mock(Ini.class);
        final Ini batchgroups = mock(Ini.class);
        final NewsFactory nf = mock(NewsFactory.class);
        final NNTP news = mock(NNTP.class);
        final List<String> subjectLinesList = new ArrayList<>();
        subjectLinesList.add("Subject: Test subject line 1");
        subjectLinesList.add("Subject: Test subject line 2");
        when(groupsini.readIni()).thenReturn(new Hashtable<>());
        when(batchgroups.readIni()).thenReturn(new Hashtable<>());
        when(batchgroups.getIniVector()).thenReturn(new Vector<>());
        when(batchgroups.getProperty("Newsgroups")).thenReturn("group1");
        when(batchgroups.getProperty("Retrieve")).thenReturn("retrieve-group1");
        when(batchgroups.getProperty("Options")).thenReturn("server1 server2 username password threads 5 timeout 30000");
        when(nf.getNewsSocket("server1")).thenReturn(news);
        when(news.genericCommand("group group1")).thenReturn(new String[]{"Group: group1\n"});
        when(news.genericCommand("xpat Subject * *")).thenReturn(subjectLinesList.toArray(new String[0]));
        BatchDriver bd = new BatchDriver(newzini, batchfile);
        String[] subjectLines = bd.getSubjectLines();
        assertArrayEquals(new String[]{"Test subject line 1", "Test subject line 2"}, subjectLines);
    }
}