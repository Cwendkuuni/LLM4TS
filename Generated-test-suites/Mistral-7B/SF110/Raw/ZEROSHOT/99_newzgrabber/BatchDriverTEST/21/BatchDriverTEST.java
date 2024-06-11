package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini = "path/to/newzini.ini";
 private String batchfile = "path/to/batchfile.ini";

 @Before
 public void setUp() throws IOException {
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() {
 batchDriver = null;
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
 String server = "news.example.com";
 String group = "news.example.com.group1";
 String directory = "/path/to/directory";
 int firstId = 1;
 int lastId = 10;
 BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstId, lastId);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
 assertEquals(expectedBatchJob.getGroup(), actualBatchJob.getGroup());
 assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
 assertEquals(expectedBatchJob.getFirstId(), actualBatchJob.getFirstId());
 assertEquals(expectedBatchJob.getLastId(), actualBatchJob.getLastId());
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 assertNull(batchDriver.getSubjectLines());
 }

 @Test
 public void testGetSubjectLinesNonEmpty() {
 String[] expectedSubjectLines = {"Subject line 1", "Subject line 2"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}