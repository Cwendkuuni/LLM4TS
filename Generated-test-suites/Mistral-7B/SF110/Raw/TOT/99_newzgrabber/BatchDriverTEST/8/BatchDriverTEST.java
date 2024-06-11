import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BatchDriverTest {

    @Test
    public void testGetGroups() throws IOException, ArrayIndexOutOfBoundsException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";

        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();

        Assert.assertArrayEquals(expectedGroups, actualGroups);

        MockitoAnnotations.initMocks(this);
        Ini mockGroupsini = mock(Ini.class);
        Ini mockBatchgroups = mock(Ini.class);

        when(mockGroupsini.readIni()).thenReturn(new Hashtable<String, String>() {{
            put("group1", "value1");
            put("group2", "value2");
            put("group3", "value3");
        }});

        when(mockBatchgroups.readIni()).thenReturn(new Hashtable<String, String>() {{
            putAll(batchDriver.NewsgroupsHash);
            put("Retrieve", "");
            put("Retrieve.group1", "value1");
            put("Retrieve.group2", "value2");
            put("Retrieve.group3", "value3");
        }});

        when(mockBatchgroups.getIniVector()).thenReturn(new Vector<String[]>() {{
            add(new String[]{"group1", "value1"});
            add(new String[]{"group2", "value2"});
            add(new String[]{"group3", "value3"});
        }});

        when(mockBatchgroups.getProperty("Newsgroups")).thenReturn("Newsgroups");

        BatchDriver mockBatchDriver = new BatchDriver(newzini, batchfile) {
            @Override
            Ini getGroupsini() {
                return mockGroupsini;
            }

            @Override
            Ini getBatchgroups() {
                return mockBatchgroups;
            }
        };

        String[] actualGroupsMock = mockBatchDriver.getGroups();

        Assert.assertArrayEquals(expectedGroups, actualGroupsMock);
    }

    @Test
    public void testGetBatchJob_validCase() throws IOException, ArrayIndexOutOfBoundsException, NumberFormatException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";

        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        String server = "test.server.com";
        String directory = "test.directory";
        int firstId = 1;
        int lastId = 10;
        String expectedUsername = "test.username";
        String expectedPassword = "test.password";

        BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstId, lastId);
        if (expectedUsername != null) {
            expectedBatchJob.setUsername(expectedUsername);
        }
        if (expectedPassword != null) {
            expectedBatchJob.setPassword(expectedPassword);
        }

        String serverMock = "test.server.com";
        String directoryMock = "test.directory";
        int firstIdMock = 1;
        int lastIdMock = 10;
        String groupMock = "group1";
        String usernameMock = "test.username";
        String passwordMock = "test.password";

        when(batchDriver.OptionsHash.get("server")).thenReturn(serverMock);
        when(batchDriver.BatchgroupsHash.get(groupMock)).thenReturn(directoryMock);
        when(batchDriver.BatchretrieveHash.get(groupMock)).thenReturn(Integer.toString(firstIdMock));

        when(batchDriver.Groupsini.get("group1")).thenReturn("server=test.server.com\ndirectory=test.directory");
        when(batchDriver.OptionsHash.get("username")).thenReturn(usernameMock);
        when(batchDriver.OptionsHash.get("password")).thenReturn(passwordMock);
        when(batchDriver.OptionsHash.get("threads")).thenReturn("2");
        when(batchDriver.OptionsHash.get("timeout")).thenReturn("30000");

        BatchJob actualBatchJob = batchDriver.getBatchJob(groupMock);

        Assert.assertEquals(expectedBatchJob, actualBatchJob);
    }

    @Test
    public void testGetBatchJob_invalidGroup() throws IOException, ArrayIndexOutOfBoundsException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "non-existent-group";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        Assert.assertNull(batchDriver.getBatchJob(group));
    }

    @Test
    public void testGetBatchJob_missingServer() throws IOException, ArrayIndexOutOfBoundsException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        batchDriver.OptionsHash.remove("server");
        Assert.assertNull(batchDriver.getBatchJob(group));
    }

    @Test
    public void testGetBatchJob_missingDirectory() throws IOException, ArrayIndexOutOfBoundsException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        batchDriver.BatchgroupsHash.remove(group);
        Assert.assertNull(batchDriver.getBatchJob(group));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetBatchJob_missingThreads() throws IOException, ArrayIndexOutOfBoundsException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        batchDriver.OptionsHash.remove("threads");
        batchDriver.getBatchJob(group);
    }

    @Test(expected = NumberFormatException.class)
    public void testGetBatchJob_invalidThreads() throws IOException, ArrayIndexOutOfBoundsException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        batchDriver.OptionsHash.put("threads", "invalidThreads");
        batchDriver.getBatchJob(group);
    }

    @Test(expected = NumberFormatException.class)
    public void testGetBatchJob_invalidTimeout() throws IOException, ArrayIndexOutOfBoundsException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        batchDriver.OptionsHash.put("timeout", "invalidTimeout");
        batchDriver.getBatchJob(group);
    }
}