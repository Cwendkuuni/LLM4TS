package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

@RunWith(MockitoJUnitRunner.class)
public class BatchDriverTest {

    @Test
    public void testGetGroups() {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";

        final Ini groupsiniMock = Mockito.mock(Ini.class);
        final Ini batchgroupsMock = Mockito.mock(Ini.class);

        Mockito.when(groupsiniMock.readIni()).thenReturn(NewsgroupsHash);
        Mockito.when(groupsiniMock.setProperty("Messageids")).thenReturn(groupsiniMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchgroupsHash);
        Mockito.when(batchgroupsMock.setProperty("")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.getIniVector()).thenReturn(BatchgroupsVector);
        Mockito.when(batchgroupsMock.setProperty("Retrieve")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchretrieveHash);
        Mockito.when(batchgroupsMock.setProperty("Options")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(OptionsHash);

        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);

        final String[] expectedGroups = {"group1", "group2", "group3"};
        assertArrayEquals(expectedGroups, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJobNullGroup() {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = null;

        final Ini groupsiniMock = Mockito.mock(Ini.class);
        final Ini batchgroupsMock = Mockito.mock(Ini.class);

        Mockito.when(groupsiniMock.readIni()).thenReturn(NewsgroupsHash);
        Mockito.when(groupsiniMock.setProperty("Messageids")).thenReturn(groupsiniMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchgroupsHash);
        Mockito.when(batchgroupsMock.setProperty("Newsgroups")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchretrieveHash);
        Mockito.when(batchgroupsMock.setProperty("Options")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(OptionsHash);

        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        assertNull(batchDriver.getBatchJob(group));
    }

    @Test
    public void testGetBatchJobValidGroup() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";

        final Ini groupsiniMock = Mockito.mock(Ini.class);
        final Ini batchgroupsMock = Mockito.mock(Ini.class);
        final NewsSocket newsSocketMock = Mockito.mock(NewsSocket.class);

        Mockito.when(groupsiniMock.readIni()).thenReturn(NewsgroupsHash);
        Mockito.when(groupsiniMock.setProperty("Messageids")).thenReturn(groupsiniMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchgroupsHash);
        Mockito.when(batchgroupsMock.setProperty("Newsgroups")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchretrieveHash);
        Mockito.when(batchgroupsMock.setProperty("Options")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(OptionsHash);
        Mockito.when(batchgroupsMock.getIniVector()).thenReturn(BatchgroupsVector);

        Mockito.when(Newzgrabber.nf.getNewsSocket(Mockito.anyString())).thenReturn(newsSocketMock);
        Mockito.when(newsSocketMock.authInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(newsSocketMock.genericCommand(Mockito.anyString())).thenAnswer(invocation -> {
            final String command = invocation.callRealMethod().toString();
            if (command.startsWith("group")) {
                return new String[]{"group status: 200 OK\n", "[newsgroups]"};
            } else if (command.startsWith("xpat")) {
                return new String[]{"Subject: Subject 1\n", "Subject: Subject 2\n", "Subject: Subject 3\n"};
            } else {
                throw new IOException("Invalid command: " + command);
            }
        });

        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        final BatchJob batchJob = batchDriver.getBatchJob(group);
        assertEquals(group, batchJob.getGroup());
        assertEquals("server1", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals(1, batchJob.getFirstId());
        assertEquals(10, batchJob.getLastId());
        assertEquals("username", batchJob.getUsername());
        assertEquals("password", batchJob.getPassword());
        assertArrayEquals(new String[]{"Subject: Subject 1\n", "Subject: Subject 2\n", "Subject: Subject 3\n"}, batchJob.getSubjectLines());
    }

    @Test
    public void testGetBatchJobInvalidGroup() {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "non_existent_group";

        final Ini groupsiniMock = Mockito.mock(Ini.class);
        final Ini batchgroupsMock = Mockito.mock(Ini.class);

        Mockito.when(groupsiniMock.readIni()).thenReturn(NewsgroupsHash);
        Mockito.when(groupsiniMock.setProperty("Messageids")).thenReturn(groupsiniMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchgroupsHash);
        Mockito.when(batchgroupsMock.setProperty("Newsgroups")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchretrieveHash);
        Mockito.when(batchgroupsMock.setProperty("Options")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(OptionsHash);

        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        assertNull(batchDriver.getBatchJob(group));
    }

    @Test
    public void testGetSubjectLinesEmpty() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";

        final Ini groupsiniMock = Mockito.mock(Ini.class);
        final Ini batchgroupsMock = Mockito.mock(Ini.class);
        final NewsSocket newsSocketMock = Mockito.mock(NewsSocket.class);

        Mockito.when(groupsiniMock.readIni()).thenReturn(NewsgroupsHash);
        Mockito.when(groupsiniMock.setProperty("Messageids")).thenReturn(groupsiniMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchgroupsHash);
        Mockito.when(batchgroupsMock.setProperty("Newsgroups")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchretrieveHash);
        Mockito.when(batchgroupsMock.setProperty("Options")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(OptionsHash);
        Mockito.when(batchgroupsMock.getIniVector()).thenReturn(BatchgroupsVector);
        Mockito.when(Newzgrabber.nf.getNewsSocket(Mockito.anyString())).thenReturn(newsSocketMock);
        Mockito.when(newsSocketMock.authInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(newsSocketMock.genericCommand(Mockito.anyString())).thenAnswer(invocation -> {
            final String command = invocation.callRealMethod().toString();
            if (command.startsWith("xpat")) {
                return new String[0];
            } else {
                throw new IOException("Invalid command: " + command);
            }
        });

        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        assertNull(batchDriver.getBatchJob(group));
        assertNull(batchDriver.getSubjectLines());
    }

    @Test
    public void testGetSubjectLinesValid() throws IOException {
        final String newzini = "test.ini";
        final String batchfile = "test.ini";
        final String group = "group1";

        final Ini groupsiniMock = Mockito.mock(Ini.class);
        final Ini batchgroupsMock = Mockito.mock(Ini.class);
        final NewsSocket newsSocketMock = Mockito.mock(NewsSocket.class);

        Mockito.when(groupsiniMock.readIni()).thenReturn(NewsgroupsHash);
        Mockito.when(groupsiniMock.setProperty("Messageids")).thenReturn(groupsiniMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchgroupsHash);
        Mockito.when(batchgroupsMock.setProperty("Newsgroups")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(BatchretrieveHash);
        Mockito.when(batchgroupsMock.setProperty("Options")).thenReturn(batchgroupsMock);
        Mockito.when(batchgroupsMock.readIni()).thenReturn(OptionsHash);

        Mockito.when(Newzgrabber.nf.getNewsSocket(Mockito.anyString())).thenReturn(newsSocketMock);
        Mockito.when(newsSocketMock.authInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Mockito.when(newsSocketMock.genericCommand(Mockito.anyString())).thenAnswer(invocation -> {
            final String command = invocation.callRealMethod().toString();
            if (command.startsWith("xpat")) {
                return new String[]{"Subject: Subject 1\n", "Subject: Subject 2\n", "Subject: Subject 3\n"};
            } else {
                throw new IOException("Invalid command: " + command);
            }
        });

        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        final BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
        final String[] expectedSubjectLines = {"Subject: Subject 1\n", "Subject: Subject 2\n", "Subject: Subject 3\n"};
        assertArrayEquals(expectedSubjectLines, batchDriver.getSubjectLines());
    }
}