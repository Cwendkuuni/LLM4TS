package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

 Ini mockGroupsini;
 Ini mockBatchgroups;
 NewsFactory mockNewzfactory;
 NNTP mockNews;
 Status mockStatus;
 BatchJob mockBatchJob;

 @Before
 public void setup() {
 mockGroupsini = mock(Ini.class);
 mockBatchgroups = mock(Ini.class);
 mockNewzfactory = mock(NewsFactory.class);
 mockNews = mock(NNTP.class);
 mockStatus = mock(Status.class);
 }

 @Test
 public void testBatchDriverConstructor() {
 assertNull(new BatchDriver(null, null));

 String newzini = "test.ini";
 String batchfile = "test.ini";

 try {
 new BatchDriver(newzini, batchfile);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }

 assertEquals(mockGroupsini, BatchDriver.this.groupsini);
 assertEquals(mockBatchgroups, BatchDriver.this.batchgroups);
 }

 @Test
 public void testGetGroups() {
 String[] groups = {"group1", "group2", "group3"};
 when(mockBatchgroups.getIniVector()).thenReturn(groups);

 String[] actualGroups = new BatchDriver("test.ini", "test.ini").getGroups();
 assertArrayEquals(groups, actualGroups);
 }

 @Test
 public void testGetBatchJob() throws IOException {
 String server = "example.news.server";
 String group = "group1";
 String directory = "/path/to/directory";
 int firstid = 1;
 int lastid = 10;
 String username = "testuser";
 String password = "testpassword";
 int nThreads = 5;
 int timeout = 30000;

 when(mockNewzfactory.getNewsSocket(server)).thenReturn(mockNews);
 when(mockStatus.statusOK()).thenReturn(true);
 when(mockBatchgroups.get(group)).thenReturn("server=example.news.server\ndirectory=/path/to/directory");
 when(mockOptionsHash.get("server")).thenReturn(server);
 when(mockOptionsHash.get("directory")).thenReturn(directory);
 when(mockOptionsHash.get("username")).thenReturn(username);
 when(mockOptionsHash.get("password")).thenReturn(password);
 when(mockOptionsHash.get("threads")).thenReturn("5");
 when(mockOptionsHash.get("timeout")).thenReturn("30000");

 BatchDriver batchDriver = new BatchDriver("test.ini", "test.ini");
 BatchJob batchJob = batchDriver.getBatchJob(group);

 assertEquals(server, batchJob.getServer());
 assertEquals(group, batchJob.getGroup());
 assertEquals(directory, batchJob.getDirectory());
 assertEquals(firstid, batchJob.getFirstId());
 assertEquals(lastid, batchJob.getLastId());
 assertEquals(username, batchJob.getUsername());
 assertEquals(password, batchJob.getPassword());
 assertEquals(nThreads, batchDriver.getOptionsHash().size());
 assertEquals(timeout, batchDriver.getOptionsHash().getInt("timeout"));
 }

 @Test
 public void testGetBatchJobNoServerOrDirectory() {
 String group = "group1";

 BatchDriver batchDriver = new BatchDriver("test.ini", "test.ini");
 BatchJob batchJob = batchDriver.getBatchJob(group);

 assertNull(batchJob);
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 String group = "groupX";

 BatchDriver batchDriver = new BatchDriver("test.ini", "test.ini");
 BatchJob batchJob = batchDriver.getBatchJob(group);

 assertNull(batchJob);
 }

 @Test
 public void testGetSubjectLines() {
 String[] expectedSubjectLines = new String[]{"Subject 1", "Subject 2", "Subject 3"};
 when(mockNews.genericCommand("xpat Subject 1-10 *")).thenReturn(expectedSubjectLines);

 BatchDriver batchDriver = new BatchDriver("test.ini", "test.ini");
 String[] actualSubjectLines = batchDriver.getSubjectLines();

 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesEmptyResponse() {
 String[] expectedSubjectLines = new String[0];
 when(mockNews.genericCommand("xpat Subject 1-10 *")).thenReturn(new String[0]);

 BatchDriver batchDriver = new BatchDriver("test.ini", "test.ini");
 String[] actualSubjectLines = batchDriver.getSubjectLines();

 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}