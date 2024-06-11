import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.Status;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTP;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private Ini mockGroupsIni;
 private Ini mockBatchini;

 @Before
 public void setUp() {
 mockGroupsIni = new Ini("test.ini", "Messageids");
 mockBatchini = new Ini("test.ini", "");

 try {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 } catch (IOException | NullPointerException e) {
 e.printStackTrace();
 fail("Constructor should not throw an exception");
 }
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"group1", "group2", "group3"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJobNullGroup() {
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 assertNull(batchDriver.getBatchJob("InvalidGroup"));
 }

 @Test
 public void testGetBatchJobValidGroup() {
 String server = "test.nntp.server";
 String group = "test.group";
 String directory = "test.directory";
 int firstId = 1;
 int lastId = 10;

 mockGroupsIni.setProperty("group1", "server=test.nntp.server;directory=test.directory1");
 mockBatchini.setProperty("group1", "Retrieve=1;Options=server=test.nntp.server;timeout=1000");

 mockBatchini.setProperty("group", "Retrieve=1;Options=server=" + server + ";directory=" + directory);
 mockBatchini.setProperty("Options", "threads=3;timeout=2000");

 NewsFactory mockNewsFactory = new NewsFactory() {
 @Override
 public NNTP getNewsSocket(String server) {
 NNTP newsSocket = new NNTP(server);
 return newsSocket;
 }

 @Override
 public void setThreadLimit(int limit) {
 // Do nothing
 }
 };

 Newzgrabber.nf = mockNewsFactory;

 BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstId, lastId);
 expectedBatchJob.setUsername("testUser");
 expectedBatchJob.setPassword("testPassword");

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test
 public void testGetBatchJobInvalidServer() {
 assertNull(batchDriver.getBatchJob("group"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullServerOption() {
 mockBatchini.setProperty("group", "Retrieve=1;Options=timeout=1000");
 batchDriver = new BatchDriver("test.ini", "test.ini");
 batchDriver.getBatchJob("group");
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidThreadsOption() {
 mockBatchini.setProperty("group", "Retrieve=1;Options=threads=abc");
 batchDriver = new BatchDriver("test.ini", "test.ini");
 batchDriver.getBatchJob("group");
 }

 @Test
 public void testGetBatchJobNegativeTimeout() {
 mockBatchini.setProperty("group", "Retrieve=1;Options=timeout=-1");
 batchDriver = new BatchDriver("test.ini", "test.ini");
 BatchJob batchJob = batchDriver.getBatchJob("group");
 assertNull(batchJob);
 }

 @Test
 public void testGetSubjectLinesNullBatchJob() {
 assertNull(batchDriver.getSubjectLines());
 }

 @Test
 public void testGetSubjectLinesEmptyBatchJob() {
 String[] expectedSubjectLines = {};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesValidBatchJob() {
 String[] expectedSubjectLines = {"Subject: Test 1", "Subject: Test 2"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSubjectLinesNullNews() {
 NewsFactory mockNewsFactory = new NewsFactory() {
 @Override
 public NNTP getNewsSocket(String server) {
 return null;
 }

 @Override
 public void setThreadLimit(int limit) {
 // Do nothing
 }
 };

 Newzgrabber.nf = mockNewsFactory;
 batchDriver = new BatchDriver("test.ini", "test.ini");
 batchDriver.getSubjectLines();
 }

}