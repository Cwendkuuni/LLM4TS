package Newzgrabber.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;

public class BatchDriverTest {

 private BatchDriver batchDriver;

 @Before
 public void setUp() throws IOException {
 String newziniPath = "src/test/resources/Newzgrabber/newzini.ini";
 String batchFile = "src/test/resources/Newzgrabber/batch.ini";
 batchDriver = new BatchDriver(newziniPath, batchFile);
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = new String[]{"news.group1", "news.group2", "news.group3"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsArrayIndexOutOfBoundsException() {
 batchDriver = new BatchDriver(new File("src/test/resources/Newzgrabber/emptyBatchFile.ini").getAbsolutePath());
 batchDriver.getGroups();
 }

 @Test
 public void testGetBatchJobNullGroup() {
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobEmptyOptionsHash() {
 Hashtable<String, String> optionsHash = new Hashtable<>();
 BatchDriver emptyOptionsBatchDriver = new BatchDriver("newzini.ini", "batch.ini", null, optionsHash, null, null, null, null);
 assertNull(emptyOptionsBatchDriver.getBatchJob("news.group1"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBatchJobNullServer() {
 BatchDriver nullServerBatchDriver = new BatchDriver("newzini.ini", "batch.ini", null, null, null, null, null, null);
 assertNull(nullServerBatchDriver.getBatchJob("news.group1"));
 }

 @Test
 public void testGetBatchJobValidArguments() {
 String server = "news.server.com";
 String group = "news.group1";
 BatchJob expectedBatchJob = new BatchJob(server, group, "/path/to/directory", 1, 10);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertNotNull(actualBatchJob);
 assertNotNull(actualBatchJob.getServer());
 assertNotNull(actualBatchJob.getDirectory());
 assertNotNull(actualBatchJob.getFirstId());
 assertNotNull(actualBatchJob.getLastId());
 assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
 assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
 assertEquals(expectedBatchJob.getFirstId(), actualBatchJob.getFirstId());
 assertEquals(expectedBatchJob.getLastId(), actualBatchJob.getLastId());
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobIOException() {
 batchDriver = new BatchDriver("newzini.ini", "batch.ini", null, null, null, null, null, null);
 assertNull(batchDriver.getBatchJob("news.group1"));
 }

 @Test
 public void testGetSubjectLinesNull() {
 assertNull(batchDriver.getSubjectLines());
 }

 @Test
 public void testGetSubjectLinesValidArguments() {
 String[] expectedSubjectLines = new String[]{"Subject 1", "Subject 2", "Subject 3"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}