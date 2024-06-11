package Newzgrabber.test;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class BatchDriverTest {

 private final String newzini = "test.ini";
 private final String batchfile = "test.ini";

 @Test
 public void testGetGroups() throws IOException {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String[] expectedGroups = {"group1", "group2", "group3"};
 String[] actualGroups = batchDriver.getGroups();
 Assert.assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetGroupsWithEmptyVector() {
 BatchDriver batchDriver = new BatchDriver(null, null);
 Assert.assertNull(batchDriver.getGroups());
 }

 @Test
 public void testGetBatchJobWithValidGroup() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 Assert.assertNotNull(batchJob);
 }

 @Test
 public void testGetBatchJobWithInvalidGroup() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "invalidGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 Assert.assertNull(batchJob);
 }

 @Test
 public void testGetBatchJobWithEmptyOptionsHash() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 batchDriver.OptionsHash = new Hashtable();
 BatchJob batchJob = batchDriver.getBatchJob("validGroup");
 Assert.assertNull(batchJob.getUsername());
 Assert.assertNull(batchJob.getPassword());
 }

 @Test
 public void testGetBatchJobWithValidOptionsHash() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 Properties optionsHash = new Properties();
 optionsHash.setProperty("server", "test.server.com");
 optionsHash.setProperty("username", "testUser");
 optionsHash.setProperty("password", "testPass");
 batchDriver.OptionsHash = optionsHash;
 String group = "validGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 Assert.assertEquals("test.server.com", batchJob.getServer());
 Assert.assertEquals("testUser", batchJob.getUsername());
 Assert.assertEquals("testPass", batchJob.getPassword());
 }

 @Test (expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsWithEmptyBatchgroupsVector() {
 BatchDriver batchDriver = new BatchDriver(null, null);
 batchDriver.BatchgroupsVector = new Vector();
 batchDriver.getGroups();
 }

 @Test
 public void testGetSubjectLinesWithValidBatchJob() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "validGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 String[] expectedSubjectLines = {"Subject line 1", "Subject line 2", "Subject line 3"};
 String[] actualSubjectLines = batchJob.getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesWithEmptySubjectLinesArray() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 BatchJob batchJob = new BatchJob("server", "group", "directory", 0, 0);
 batchJob.subjectlines = new String[0];
 String[] actualSubjectLines = batchJob.getSubjectLines();
 Assert.assertArrayEquals(new String[0], actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesWithValidSubjectLinesArray() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "validGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 String[] expectedSubjectLines = {"Subject line 1", "Subject line 2", "Subject line 3"};
 String[] actualSubjectLines = batchJob.getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesWithNullSubjectLinesArray() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 BatchJob batchJob = new BatchJob("server", "group", "directory", 0, 0);
 batchJob.subjectlines = null;
 String[] actualSubjectLines = batchJob.getSubjectLines();
 Assert.assertArrayEquals(new String[0], actualSubjectLines);
 }
}