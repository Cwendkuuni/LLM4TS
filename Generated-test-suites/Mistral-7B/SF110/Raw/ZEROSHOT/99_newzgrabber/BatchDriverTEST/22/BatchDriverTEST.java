package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.ini4j.Ini;
import org.junit.Test;

public class BatchDriverTest {

 private static final String NEWZINI_FILE = "path/to/newzini.ini";
 private static final String BATCH_FILE = "path/to/batchfile.ini";

 @Test
 public void testBatchDriverConstruction() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 assertNotNull(batchDriver.getGroups());
 assertFalse(batchDriver.getGroups().length == 0);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructionWithNullNewzini() {
 new BatchDriver(null, BATCH_FILE);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructionWithNullBatchFile() {
 new BatchDriver(NEWZINI_FILE, null);
 }

 @Test
 public void testGetGroups() {
 String[] groups = new BatchDriver(NEWZINI_FILE, BATCH_FILE).getGroups();
 for (String group : groups) {
 System.out.println("Group: " + group);
 }
 }

 @Test
 public void testGetBatchJob() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 String group = batchDriver.getGroups()[0];
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 String invalidGroup = "invalid_group";
 batchDriver.getBatchJob(invalidGroup);
 }

 @Test
 public void testGetSubjectLines() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 String group = batchDriver.getGroups()[0];
 BatchJob batchJob = batchDriver.getBatchJob(group);
 String[] subjectLines = batchJob.getSubjectLines();
 assertNotNull(subjectLines);
 assertFalse(subjectLines.length == 0);
 }
}