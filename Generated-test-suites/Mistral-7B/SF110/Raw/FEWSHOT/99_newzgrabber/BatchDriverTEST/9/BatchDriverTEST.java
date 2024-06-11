package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;
import java.util.StringTokenizer;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testGetGroups() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] groups = batchDriver.getGroups();
        assertEquals(2, groups.length);
        assertEquals("group1", groups[0]);
        assertEquals("group2", groups[1]);
    }

    @Test
    public void testGetBatchJob_validGroup() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String server = "test.server.com";
        final String group = "group1";
        final String directory = "test/directory";
        final int firstid = 1;
        final int lastid = 10;
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);

        final Ini mockGroupsini = mock(Ini.class);
        when(mockGroupsini.readIni()).thenReturn(new Hashtable() {{
            put("group1", "server1,directory1,1,10");
            put("group2", "server2,directory2,2,20");
        }});

        final Ini mockBatchgroups = mock(Ini.class);
        when(mockBatchgroups.readIni()).thenReturn(new Hashtable() {{
            put("Newsgroups", mockGroupsini);
            put("Retrieve", new Hashtable() {{
                put(group, "");
            }});
            put("Options", new Hashtable() {{
                put("server", server);
                put("threads", "2");
                put("timeout", "30000");
            }});
        }});

        when(mockBatchgroups.getIniVector()).thenReturn(new Vector() {{
            add(new String[] {"group1", "server1,directory1,1,10"});
            add(new String[] {"group2", "server2,directory2,2,20"});
        }});

        final BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
        assertEquals(server, batchJob.getServer());
        assertEquals(group, batchJob.getGroup());
        assertEquals(directory, batchJob.getDirectory());
        assertEquals(firstid, batchJob.getFirstId());
        assertEquals(lastid, batchJob.getLastId());
        assertEquals(2, batchDriver.getThreadLimit());
        assertEquals(30000, batchDriver.getTimeout());
    }

    @Test
    public void testGetBatchJob_invalidGroup() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group3";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        assertNull(batchDriver.getBatchJob(group));
    }

    @Test
    public void testGetBatchJob_noServerOrDirectory() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        final BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNull(batchJob.getUsername());
        assertNull(batchJob.getPassword());
    }

    @Test
    public void testGetBatchJob_withUsernameAndPassword() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String server = "test.server.com";
        final String group = "group1";
        final String directory = "test/directory";
        final String username = "testuser";
        final String password = "testpassword";
        final int firstid = 1;
        final int lastid = 10;
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);

        final Ini mockGroupsini = mock(Ini.class);
        when(mockGroupsini.readIni()).thenReturn(new Hashtable() {{
            put("group1", "server1,directory1,1,10");
            put("group2", "server2,directory2,2,20");
        }});

        final Ini mockBatchgroups = mock(Ini.class);
        when(mockBatchgroups.readIni()).thenReturn(new Hashtable() {{
            put("Newsgroups", mockGroupsini);
            put("Retrieve", new Hashtable() {{
                put(group, "");
            }});
            put("Options", new Hashtable() {{
                put("server", server);
                put("threads", "2");
                put("timeout", "30000");
                put("username", username);
                put("password", password);
            }});
        }});

        when(mockBatchgroups.getIniVector()).thenReturn(new Vector() {{
            add(new String[] {"group1", "server1,directory1,1,10"});
            add(new String[] {"group2", "server2,directory2,2,20"});
        }});

        final BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
        assertEquals(server, batchJob.getServer());
        assertEquals(group, batchJob.getGroup());
        assertEquals(directory, batchJob.getDirectory());
        assertEquals(firstid, batchJob.getFirstId());
        assertEquals(lastid, batchJob.getLastId());
        assertEquals(username, batchJob.getUsername());
        assertEquals(password, batchJob.getPassword());
        assertEquals(2, batchDriver.getThreadLimit());
        assertEquals(30000, batchDriver.getTimeout());
    }

    @Test
    public void testGetSubjectLines() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        final String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(3, subjectLines.length);
        assertEquals("Subject 1-1", subjectLines[0]);
        assertEquals("Subject 2-2", subjectLines[1]);
        assertEquals("Subject 3-3", subjectLines[2]);
    }
}