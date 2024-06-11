package Newzgrabber.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.ini4j.Ini;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Newzgrabber;

public class BatchDriverTest {

 private String newziniPath = "testData/newzini.ini";
 private String batchfilePath = "testData/batch.ini";

 @Test
 public void testBatchDriverConstruction() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 assertNotNull(batchDriver);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverNullPointerException() {
 new BatchDriver(null, batchfilePath);
 }

 @Test(expected = IOException.class)
 public void testBatchDriverIOException() {
 new BatchDriver(newziniPath, null);
 }

 @Test
 public void testGetGroups() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 String[] groups = batchDriver.getGroups();
 assertNotNull(groups);
 assertEquals(3, groups.length);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsArrayIndexOutOfBoundsException() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 batchDriver.getGroups(); // call the method to initialize the groups
 String[] groups = batchDriver.getGroups();
 assertNotNull(groups);
 assertEquals(3, groups.length);
 int index = 3;
 assertArrayBounds(index, groups);
 }

 @Test
 public void testGetBatchJob() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertNotNull(batchJob);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 batchDriver.getBatchJob(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobEmptyGroupVector() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 batchDriver.getBatchJob("");
 }

 @Test
 public void testGetBatchJobValidGroup() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertNotNull(batchJob);
 assertEquals("test.server", batchJob.getServer());
 assertEquals("test.directory", batchJob.getDirectory());
 assertEquals(1, batchJob.getFirstId());
 assertEquals(10, batchJob.getLastId());
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobInvalidGroup() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 BatchJob batchJob = batchDriver.getBatchJob("nonExistentGroup");
 assertNull(batchJob);
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals(0, subjectLines.length);
 }

 @Test
 public void testGetSubjectLinesValid() {
 BatchDriver batchDriver = new BatchDriver(newziniPath, batchfilePath);
 batchDriver.getBatchJob("test.group"); // call the getBatchJob method to initialize the subjectLines
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals(5, subjectLines.length);
 }

 private void assertArrayBounds(int index, String[] array) {
 try {
 array[index];
 fail("ArrayIndexOutOfBoundsException expected but not thrown");
 }
 catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
}

}