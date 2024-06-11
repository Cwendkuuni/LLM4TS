package Newzgrabber;

import static org.junit.Assert.*;

import org.ini4j.Ini;
import org.junit.Test;

public class BatchDriverTest {

 private static final String NEWZINI = "test.ini";
 private static final String BATCHFILE = "test.ini";

 @Test
 public void testBatchDriverConstruction() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNotNull(batchDriver);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructionNullNewzini() {
 BatchDriver batchDriver = new BatchDriver(null, BATCHFILE);
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructionNullBatchfile() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetGroups() throws ArrayIndexOutOfBoundsException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 assertNotNull(groups);
 assertEquals(1, groups.length);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsArrayIndexOutOfBounds() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.getGroups();
 batchDriver.BatchgroupsVector.remove(0);
 String[] groups = batchDriver.getGroups();
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testGetBatchJob() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String group = "test.group";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobArrayIndexOutOfBounds() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String group = "test.group";
 batchDriver.BatchgroupsVector.remove(0);
 BatchJob batchJob = batchDriver.getBatchJob(group);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testGetSubjectLines() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals(1, subjectLines.length);
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] subjectLines = batchDriver.getSubjectLines();
 assertEquals(0, subjectLines.length);
 }
}