import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.NewsFactory;
import Newzgrabber.Status;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private Ini groupsini;
 private Ini batchini;
 private String newzini = "test.ini";
 private String batchfile = "test.ini";

 @Before
 public void setUp() throws IOException {
 groupsini = new Ini(newzini, "Messageids");
 batchini = new Ini(batchfile, "");
 groupsini.addProperty("testgroup", "test.group");
 batchini.addProperty("testgroup", "test.directory");
 batchini.addProperty("testgroup", "test.server");
 batchini.addProperty("testgroup", "test.username");
 batchini.addProperty("testgroup", "test.password");
 batchini.addProperty("testgroup", "test.threads");
 batchini.addProperty("testgroup", "test.timeout");
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() {
 batchDriver = null;
 groupsini = null;
 batchini = null;
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"testgroup"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJob() {
 String group = "testgroup";
 BatchJob expectedBatchJob = new BatchJob("test.server", group, "test.directory", 1, 10);
 expectedBatchJob.setUsername("test.username");
 expectedBatchJob.setPassword("test.password");
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() {
 String invalidGroup = "";
 batchDriver.getBatchJob(invalidGroup);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverWithNullNewzini() {
 batchDriver = new BatchDriver(null, batchfile);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverWithNullBatchfile() {
 batchDriver = new BatchDriver(newzini, null);
 }

 @Test
 public void testGetSubjectLines() {
 String[] expectedSubjectLines = {"Subject: Test 1", "Subject: Test 2"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetSubjectLinesWithEmptyList() {
 batchDriver.subjectlines = new String[0];
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 }
}