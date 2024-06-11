package Newzgrabber.test;

import org.junit.After;
import org.junit.Assert;import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.NewsFactory;
import Newzgrabber.Newzbatch;
import Newzgrabber.Status;
import Newzgrabber.NFConfiguration;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private Ini groupsini;
 private Ini batchini;
 private String newzini;
 private String batchfile;
 private String group;
 private String server;
 private String directory;

 @Before
 public void setUp() throws IOException {
 newzini = "test.ini";
 batchfile = "test.ini";
 groupsini = new Ini(newzini, "Messageids");
 batchini = new Ini(batchfile, "");
 batchDriver = new BatchDriver(newzini, batchfile);
 group = "test.group";
 server = "test.server";
 directory = "test.directory";
 }

 @After
 public void tearDown() {
 if (batchDriver != null) {
 batchDriver.quit();
 }
 }

 @Test
 public void testBatchDriverConstruction() {
 Assert.assertNotNull(batchDriver);
 }

 @Test
 public void testGetGroups() {
 String[] groups = batchDriver.getGroups();
 Assert.assertNotNull(groups);
 Assert.assertEquals(groups.length, batchDriver.BatchgroupsVector.size());
 }

 @Test
 public void testGetBatchJob() throws Exception {
 BatchJob batchJob = batchDriver.getBatchJob(group);
 Assert.assertNotNull(batchJob);
 Assert.assertEquals(batchJob.getServer(), server);
 Assert.assertEquals(batchJob.getGroup(), group);
 Assert.assertEquals(batchJob.getDirectory(), directory);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() throws Exception {
 batchDriver.getBatchJob("invalidGroup");
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverWithNullNewzini() {
 try {
 new BatchDriver(null, batchfile);
 fail("Should have thrown NullPointerException");
 }
 catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverWithNullBatchfile() {
 try {
 new BatchDriver(newzini, null);
 fail("Should have thrown NullPointerException");
 }
 catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testBatchDriverWithInvalidNewzini() throws IOException {
 groupsini = null;
 batchini = new Ini(batchfile, "");
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @Test(expected = IOException.class)
 public void testBatchDriverWithInvalidBatchfile() throws IOException {
 groupsini = new Ini(newzini, "Messageids");
 batchini = null;
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobWithInvalidGroupInBatchFile() throws Exception {
 batchDriver.getBatchJob("invalidGroupInBatchFile");
 }

 @Test(expected = NullPointerException.class)
 public void testGetSubjectLinesWithNullBatchJob() {
 String[] subjectLines = batchDriver.getSubjectLines();
 Assert.assertNull(subjectLines);
 }

 @Test
 public void testGetSubjectLines() {
 BatchJob batchJob = batchDriver.getBatchJob(group);
 String[] subjectLines = batchDriver.getSubjectLines();
 Assert.assertNotNull(subjectLines);
 Assert.assertEquals(subjectLines.length, batchJob.getSubjectLines().length);
 }
}