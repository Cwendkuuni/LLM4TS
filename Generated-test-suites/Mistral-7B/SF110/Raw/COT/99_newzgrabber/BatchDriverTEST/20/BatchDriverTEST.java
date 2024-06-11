package Newzgrabber.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

 private static final String NEWZINI = "newzini.ini";
 private static final String BATCHFILE = "batchfile.ini";

 @Test
 public void testBatchDriverConstructor() throws IOException, NullPointerException {
  new BatchDriver(NEWZINI, BATCHFILE);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructorNullNewzini() {
  new BatchDriver(null, BATCHFILE);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructorNullBatchfile() {
  new BatchDriver(NEWZINI, null);
 }

 @Test
 public void testGetGroups() {
  final BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
  final String[] groups = batchDriver.getGroups();
  assertEquals("Size of groups array should match size of BatchgroupsVector", batchDriver.BatchgroupsVector.size(), groups.length);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsArrayIndexOutOfBounds() {
  final BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
  batchDriver.getGroups();
  batchDriver.BatchgroupsVector.remove(0);
  batchDriver.getGroups();
 }

 @Test
 public void testGetBatchJob() throws IOException, NullPointerException {
  final BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
  final String group = "test.group";
  final BatchJob batchJob = batchDriver.getBatchJob(group);
  assertNotNull("BatchJob should not be null", batchJob);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullGroup() {
  final BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
  batchDriver.getBatchJob(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobEmptyBatchgroupsVector() {
  final BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
  batchDriver.BatchgroupsVector.clear();
  batchDriver.getBatchJob("test.group");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBatchJobNonExistentGroup() {
  final BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
  batchDriver.getBatchJob("non.existent.group");
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobIOException() throws IOException {
  final BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
  final String group = "test.group";
  final File iniFile = new File(BATCHFILE);
  iniFile.delete();
  final BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test
 public void testGetSubjectLines() {
  final BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
  final String group = "test.group";
  final BatchJob batchJob = batchDriver.getBatchJob(group);
  batchDriver.getBatchJob(group); // Call the method again to ensure it doesn't throw an exception and sets the subjectlines property
  assertNotNull("Subjectlines should not be null", batchJob.getSubjectLines());
  assertTrue("Size of subjectlines array should be greater than 0", batchJob.getSubjectLines().length > 0);
 }

}