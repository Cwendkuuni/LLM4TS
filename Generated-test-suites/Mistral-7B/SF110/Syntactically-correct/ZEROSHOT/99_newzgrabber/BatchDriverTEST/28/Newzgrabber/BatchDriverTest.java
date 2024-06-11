package Newzgrabber;

import org.junit.Assert;import org.junit.Test;import org.junit.runner.RunWith;import org.mockito.Mock;import org.mockito.Mockito;import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;import java.util.ArrayList;import java.util.HashMap;import java.util.HashSet;import java.util.Ini;import java.util.List;import java.util.Map;import java.util.Set;

@RunWith(MockitoJUnitRunner.class) public class BatchDriverTest {

 private BatchDriver batchDriver;
 @Mock private Ini newzini; @Mock private Ini batchfile; @Mock private Ini batchgroups; @Mock private Status status; @Mock private NewsFactory nf; @Mock private NNTP news; @Mock private Newzbatch newzbatch;

 @Before public void setUp() {
 Newzgrabber.verbose = true;
 Newzgrabber.IsBatch = true;
 Newzgrabber.nf = Mockito.mock(NewsFactory.class);
 Newzgrabber.nf.setThreadLimit(1);
 Mockito.when(newzini.readIni()).thenReturn(new HashMap<>());
 Mockito.when(newzfile.readIni()).thenReturn(new HashMap<>());
 Mockito.when(batchfile.readIni()).thenReturn(new HashMap<>());
 Mockito.when(batchfile.getIniVector()).thenReturn(new ArrayList<>());
 Mockito.when(batchgroups.readIni()).thenAnswer(invocation -> new HashMap<>());
 Mockito.when(batchgroups.getProperty("Newsgroups")).thenReturn("testGroup");
 Mockito.when(batchgroups.getProperty("Retrieve")).thenReturn("testRetrieve");
 Mockito.when(batchgroups.getProperty("Options")).thenReturn("testOptions");
 Mockito.when(news.getNewsSocket("testServer")).thenReturn(news);
 Mockito.when(news.authInfo(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
 Mockito.when(news.genericCommand(Mockito.anyString())).thenAnswer(invocation -> new String[]{"testResult"});
 Mockito.when(status.statusOK()).thenReturn(true);
 batchDriver = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
 }

 @Test public void testGetGroups() {
 final String[] expectedGroups = new String[]{"testGroup"};
 final String[] actualGroups = batchDriver.getGroups();
 Assert.assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test public void testGetBatchJobNullGroup() {
 final String group = null;
 final BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 Assert.assertNull(actualBatchJob);
 }

 @Test public void testGetBatchJobValidGroup() {
 final String group = "testGroup";
 final String server = "testServer";
 final String directory = "testDirectory";
 final int firstId = 0;
 final int lastId = 1;
 final BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstId, lastId);
 final BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 Assert.assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test public void testGetBatchJobInvalidGroup() {
 final String group = "invalidGroup";
 final BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 Assert.assertNull(actualBatchJob);
 }

 @Test public void testGetSubjectLines() {
 final String[] expectedSubjectLines = new String[]{"testSubjectLine1", "testSubjectLine2"};
 Mockito.when(news.genericCommand("xpat Subject 0-1 *")).thenReturn(expectedSubjectLines);
 final String[] actualSubjectLines = batchDriver.getBatchJob("testGroup").getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test public void testGetSubjectLinesEmpty() {
 Mockito.when(news.genericCommand("xpat Subject 0-0 *")).thenReturn(new String[]{});
 final String[] actualSubjectLines = batchDriver.getBatchJob("testGroup").getSubjectLines();
 Assert.assertArrayEquals(new String[0], actualSubjectLines);
 }
}