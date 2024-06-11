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
import Newzgrabber.Newzbatch;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTPCustom;
import Newzgrabber.Status;

public class BatchDriverTest {

 private static final String NEWZINI_FILE = "testNewzini.ini";
 private static final String BATCH_FILE = "testBatch.ini";

 @Test
 public void testBatchDriverConstruction() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 assertNotNull(batchDriver.getGroups());
 assertFalse(batchDriver.getGroups().length == 0);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNewzini() {
 BatchDriver batchDriver = new BatchDriver(null, BATCH_FILE);
 fail("Should have thrown a NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullBatchFile() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, null);
 fail("Should have thrown a NullPointerException");
 }

 @Test(expected = IOException.class)
 public void testInvalidNewzini() throws IOException {
 File invalidNewziniFile = new File("invalidNewzini.ini");
 BatchDriver batchDriver = new BatchDriver(invalidNewziniFile.getAbsolutePath(), BATCH_FILE);
 fail("Should have thrown an IOException");
 }

 @Test(expected = IOException.class)
 public void testInvalidBatchFile() throws IOException {
 File invalidBatchFile = new File("invalidBatchFile.ini");
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, invalidBatchFile.getAbsolutePath());
 fail("Should have thrown an IOException");
 }

 @Test
 public void testGetGroups() {
 String[] groups = new BatchDriver(NEWZINI_FILE, BATCH_FILE).getGroups();
 assertArrayEquals(new String[]{"group1", "group2", "group3"}, groups);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsEmptyVector() {
 new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 fail("Should have thrown an ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testGetBatchJob() throws Exception {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 BatchJob batchJob = batchDriver.getBatchJob("group1");
 assertNotNull(batchJob);
 assertEquals("group1", batchJob.getGroup());
 assertNotNull(batchJob.getServer());
 assertNotNull(batchJob.getDirectory());
 assertEquals(1, batchJob.getFirstId());
 assertEquals(10, batchJob.getLastId());
 assertEquals("username", batchJob.getUsername());
 assertEquals("password", batchJob.getPassword());
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 BatchJob batchJob = batchDriver.getBatchJob(null);
 fail("Should have thrown a NullPointerException");
 }

 @Test(expected = Exception.class)
 public void testGetBatchJobInvalidGroup() throws Exception {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 BatchJob batchJob = batchDriver.getBatchJob("invalidGroup");
 fail("Should have thrown an Exception");
 }

 @Test
 public void testGetSubjectLines() {
 String[] subjectLines = new BatchDriver(NEWZINI_FILE, BATCH_FILE).getBatchJob("group1").getSubjectLines();
 assertNotNull(subjectLines);
 assertFalse(subjectLines.length == 0);
 }

 @Test
 public void testGetSubjectLinesEmptyBatchJob() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 BatchJob batchJob = batchDriver.getBatchJob("emptyGroup");
 String[] subjectLines = batchJob.getSubjectLines();
 assertNotNull(subjectLines);
 assertFalse(subjectLines.length > 0);
 }

 @Test
 public void testGetSubjectLinesNullBatchJob() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 String[] subjectLines = null;
 assertArrayEquals(new String[0], subjectLines);
 }
}