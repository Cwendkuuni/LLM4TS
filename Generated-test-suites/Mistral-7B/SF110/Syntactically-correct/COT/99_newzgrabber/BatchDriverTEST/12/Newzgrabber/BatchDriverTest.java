package Newzgrabber.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

 private BatchDriver batchDriver;

 @Before
 public void setUp() throws IOException, NullPointerException {
 String newzini = "testdata/newzini.ini";
 String batchfile = "testdata/batchfile.ini";
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 // Test case for constructor
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullNewzini() {
 batchDriver = new BatchDriver(null, batchfile);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullBatchfile() {
 batchDriver = new BatchDriver(newzini, null);
 }

 @Test
 public void testGetGroups() {
 String[] groups = batchDriver.getGroups();
 assertEquals(3, groups.length);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsArrayIndexOutOfBounds() {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchDriver.NewsgroupsHash = null;
 String[] groups = batchDriver.getGroups();
 groups[10] = "test";
 }

 @Test
 public void testGetBatchJob() {
 String group = "test.group";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullServer() {
 BatchJob batchJob = batchDriver.getBatchJob(null);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullGroup() {
 BatchJob batchJob = batchDriver.getBatchJob(null);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobMissingGroupInfo() {
 batchDriver.BatchgroupsHash = new Hashtable<>();
 String group = "test.group";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidThreads() {
 batchDriver.OptionsHash = new Hashtable<>();
 batchDriver.OptionsHash.put("threads", "abc");
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidTimeout() {
 batchDriver.OptionsHash = new Hashtable<>();
 batchDriver.OptionsHash.put("timeout", "abc");
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 }

 @Test
 public void testGetSubjectLines() {
 assertNotNull(batchDriver.getSubjectLines());
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 String[] subjectLines = batchDriver.getSubjectLines();
 assertEquals(0, subjectLines.length);
 }

}