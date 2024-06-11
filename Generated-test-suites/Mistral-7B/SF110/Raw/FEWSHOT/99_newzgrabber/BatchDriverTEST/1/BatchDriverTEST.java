package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testBatchDriverInitialization() {
        final String newzini = "testnewzini.ini";
        final String batchfile = "testbatchfile.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        assertSame(batchDriver.BatchgroupsVector, batchDriver.getBatchgroupsVector());
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverInitializationWithNullNewzini() {
        final String batchfile = "testbatchfile.ini";
        new BatchDriver(null, batchfile);
    }

    @Test(expected = NullPointerException.class)
    public void testBatchDriverInitializationWithNullBatchfile() {
        final String newzini = "testnewzini.ini";
        new BatchDriver(newzini, null);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverInitializationWithInvalidNewzini() throws IOException {
        final String newzini = "testnewzini.txt";
        final String batchfile = "testbatchfile.ini";
        new BatchDriver(newzini, batchfile);
    }

    @Test(expected = IOException.class)
    public void testBatchDriverInitializationWithInvalidBatchfile() throws IOException {
        final String newzini = "testnewzini.ini";
        final String batchfile = "testbatchfile.txt";
        new BatchDriver(newzini, batchfile);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        final String newzini = "testnewzini.ini";
        final String batchfile = "testbatchfile.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        final String[] expectedGroups = {"group1", "group2"};
        when(batchDriver.getBatchgroupsVector()).thenReturn(new Vector(Arrays.asList(expectedGroups)));
        assertArrayEquals(expectedGroups, batchDriver.getGroups());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroupsWithEmptyVector() throws ArrayIndexOutOfBoundsException {
        final String newzini = "testnewzini.ini";
        final String batchfile = "testbatchfile.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        when(batchDriver.getBatchgroupsVector()).thenReturn(new Vector());
        batchDriver.getGroups();
    }

    @Test
    public void testGetBatchJob() throws IOException, ArrayIndexOutOfBoundsException, NullPointerException {
        final String newzini = "testnewzini.ini";
        final String batchfile = "testbatchfile.ini";
        final String group = "group1";
        final NewsFactory newsFactoryMock = mock(NewsFactory.class);
        final NNTP newsMock = mock(NNTP.class);
        final Status statusMock = mock(Status.class);
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        when(newsFactoryMock.getNewsSocket(anyString())).thenReturn(newsMock);
        when(newsMock.authInfo(anyString(), anyString())).thenReturn(true);
        when(newsMock.genericCommand(anyString())).thenReturn(new String[]{"testgroupline"});
        when(statusMock.statusOK()).thenReturn(true);
        when(batchDriver.OptionsHash).thenReturn(new Hashtable<>());
        when(batchDriver.BatchgroupsHash.get(group)).thenReturn("testdirectory");
        when(batchDriver.NewsgroupsHash.get(group)).thenReturn("1001");
        when(newsMock.genericCommand("group " + group)).thenReturn(new String[]{"testgroupline"});
        when(newsMock.quit()).thenReturn(true);
        final BatchJob batchJob = batchDriver.getBatchJob(group);
        assertSame(newsMock, batchJob.getNews());
        assertSame("testdirectory", batchJob.getDirectory());
        assertSame("group1", batchJob.getGroupName());
        assertSame(1001, batchJob.getFirstId());
        assertSame(1001, batchJob.getLastId());
        assertNull(batchJob.getUsername());
        assertNull(batchJob.getPassword());
    }

    @Test
    public void testGetBatchJobWithNullGroupName() {
        final String newzini = "testnewzini.ini";
        final String batchfile = "testbatchfile.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        assertNull(batchDriver.getBatchJob(null));
    }

    @Test
    public void testGetBatchJobWithNonExistentGroupName() {
        final String newzini = "testnewzini.ini";
        final String batchfile = "testbatchfile.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        when(batchDriver.BatchgroupsHash.get("nonExistentGroup")).thenReturn(null);
        assertNull(batchDriver.getBatchJob("nonExistentGroup"));
    }

    @Test
    public void testGetSubjectLines() {
        final String newzini = "testnewzini.ini";
        final String batchfile = "testbatchfile.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        final String[] expectedSubjectLines = {"Subject 1", "Subject 2"};
        final NewsFactory newsFactoryMock = mock(NewsFactory.class);
        final NNTP newsMock = mock(NNTP.class);
        when(newsFactoryMock.getNewsSocket(anyString())).thenReturn(newsMock);
        when(newsMock.genericCommand("xpat Subject * * * *")).thenReturn(expectedSubjectLines);
        when(batchDriver.subjectlines).thenReturn(new String[0]);
        batchDriver.setNewsFactory(newsFactoryMock);
        assertArrayEquals(expectedSubjectLines, batchDriver.getSubjectLines());
    }

    @Test
    public void testGetSubjectLinesWithEmptyStringArray() {
        final String newzini = "testnewzini.ini";
        final String batchfile = "testbatchfile.ini";
        final BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        when(batchDriver.subjectlines).thenReturn(new String[0]);
        assertSame(new String[0], batchDriver.getSubjectLines());
    }
}