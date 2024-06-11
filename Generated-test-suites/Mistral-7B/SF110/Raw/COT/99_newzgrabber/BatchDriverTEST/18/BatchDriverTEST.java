package Newzgrabber.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.Newzgrabber;
import Newzgrabber.NewsFactory;
import Newzgrabber.Options;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private Ini newzini;
 private Ini batchfile;

 @Before
 public void setUp() {
 newzini = new Ini("test.ini", "Messageids");
 batchfile = new Ini("test.ini", "");

 try {
 batchDriver = new BatchDriver(newzini.getFileName(), batchfile.getFileName());
 }
 catch (IOException | NullPointerException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testBatchDriverConstructor() {
 assertNotNull(batchDriver);
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
 public void testGetBatchJobValidGroup() {
 BatchJob expectedBatchJob = new BatchJob("server", "group", "directory", 1, 10);
 BatchJob actualBatchJob = batchDriver.getBatchJob("group");
 assertTrue(expectedBatchJob.equals(actualBatchJob));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidIndex() {
 batchDriver.getBatchJob(1);
 }

 @Test
 public void testGetSubjectLinesNullBatchJob() {
 assertNull(batchDriver.getSubjectLines());
 }

 @Test
 public void testGetSubjectLinesValidBatchJob() {
 String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSubjectLinesNullBatchDriver() {
 BatchDriver nullBatchDriver = null;
 nullBatchDriver.getSubjectLines();
 }
}