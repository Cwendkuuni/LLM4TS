package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini;
 private String batchfile;

 @Before
 public void setUp() throws IOException {
 newzini = new File("testNewzini.ini").getAbsolutePath();
 batchfile = new File("testBatchfile.ini").getAbsolutePath();
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() {
 // Add tear down code here, if needed.
 }

 @Test
 public void testGetGroups() {
 final String[] expectedGroups = new String[]{"Group1", "Group2", "Group3"};
 final String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJobNullGroup() {
 final String group = null;
 final BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertNull(actualBatchJob);
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 final String group = "InvalidGroup";
 final BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertNull(actualBatchJob);
 }

 @Test
 public void testGetBatchJobValidGroup() throws Exception {
 final String group = "Group1";
 final BatchJob expectedBatchJob = new BatchJob("server1", group, "/path/to/directory1", 1, 10);
 final BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
 assertEquals(expectedBatchJob.getGroup(), actualBatchJob.getGroup());
 assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
 assertEquals(expectedBatchJob.getFirstId(), actualBatchJob.getFirstId());
 assertEquals(expectedBatchJob.getLastId(), actualBatchJob.getLastId());
 }

 @Test
 public void testGetSubjectLines() {
 final String[] expectedSubjectLines = new String[]{"Subject1", "Subject2", "Subject3"};
 final String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}