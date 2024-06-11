package Newzgrabber.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util. Vector;

import org.ini4j.Ini;
import org.ini4j.WiniFile;
import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Newzbatch;
import Newzgrabber.Status;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTP;

public class BatchDriverTest {

 private String newziniFile = "test.ini";
 private String batchFile = "test.ini";
 private BatchDriver batchDriver = null;

 @Before
 public void setUp() throws IOException {
 new File(newziniFile).createNewFile();
 new File(batchFile).createNewFile();

 final Ini newzini = new WiniFile(newziniFile);
 newzini.put("Messageids", "Group1", "1");
 newzini.put("Messageids", "Group2", "2");

 final Ini batchgroups = new WiniFile(batchFile);
 batchgroups.put("Newsgroups", "Group1");
 batchgroups.put("Newsgroups", "Group2");
 batchgroups.put("Retrieve", "Group1", "server1,directory1,username1,password1,threads=5,timeout=1000");
 batchgroups.put("Retrieve", "Group2", "server2,directory2,username2,password2,threads=2,timeout=500");
 batchgroups.put("Options", "Newsgroups", "server,directory,username,password,threads=1,timeout=-1");

 batchDriver = new BatchDriver(newziniFile, batchFile);
 }

 @Test
 public void testConstructor() throws IOException {
 assertNotNull(batchDriver);
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"Group1", "Group2"};
 assertArrayEquals(expectedGroups, batchDriver.getGroups());
 }

 @Test
 public void testGetBatchJobNullGroup() {
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 assertNull(batchDriver.getBatchJob("InvalidGroup"));
 }

 @Test
 public void testGetBatchJobSuccess() {
 String server = "server";
 String directory = "directory";
 String username = "username";
 String password = "password";
 int nThreads = 2;
 int timeout = 500;
 String group = "Group1";

 BatchJob expectedBatchJob = new BatchJob(server, group, directory, 1, 1);
 expectedBatchJob.setUsername(username);
 expectedBatchJob.setPassword(password);
 expectedBatchJob.setnThreads(nThreads);
 expectedBatchJob.setTimeout(timeout);

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);

 assertTrue(actualBatchJob != null);
 assertBatchJobsEqual(expectedBatchJob, actualBatchJob);
 }

 @Test
 public void testGetBatchJobServerNotFound() {
 String group = "Group1";
 String server = "nonexistentserver";
 String directory = "directory";
 String username = "username";
 String password = "password";
 int nThreads = 2;
 int timeout = 500;

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);

 assertNull(actualBatchJob);
 }

 @Test
 public void testGetBatchJobDirectoryNotFound() {
 String group = "Group1";
 String server = "server";
 String directory = "nonexistentdirectory";
 String username = "username";
 String password = "password";
 int nThreads = 2;
 int timeout = 500;

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);

 assertNull(actualBatchJob);
 }

 @Test
 public void testGetBatchJobUserNameNotFound() {
 String group = "Group1";
 String server = "server";
 String directory = "directory";
 String username = null;
 String password = "password";
 int nThreads = 2;
 int timeout = 500;

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);

 assertNull(actualBatchJob);
 }

 @Test
 public void testGetBatchJobPasswordNotFound() {
 String group = "Group1";
 String server = "server";
 String directory = "directory";
 String username = "username";
 String password = null;
 int nThreads = 2;
 int timeout = 500;

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);

 assertNull(actualBatchJob);
 }

 @Test
 public void testGetBatchJobThreadsNotFound() {
 String group = "Group1";
 String server = "server";
 String directory = "directory";
 String username = "username";
 String password = "password";
 int nThreads = 0;
 int timeout = 500;

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);

 assertNotNull(actualBatchJob);
 assertEquals(1, actualBatchJob.getnThreads());
 }

 @Test
 public void testGetBatchJobTimeoutNotFound() {
 String group = "Group1";
 String server = "server";
 String directory = "directory";
 String username = "username";
 String password = "password";
 int nThreads = 2;
 int timeout = 0;

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);

 assertNotNull(actualBatchJob);
 assertFalse(actualBatchJob.isTimeoutSet());
 }

 @Test
 public void testGetBatchJobThreadsGreaterThanZero() {
 String group = "Group1";
 String server = "server";
 String directory = "directory";
 String username = "username";
 String password = "password";
 int nThreads = 5;
 int timeout = 500;

 BatchJob expectedBatchJob = new BatchJob(server, group, directory, 1, 1);
 expectedBatchJob.setUsername(username);
 expectedBatchJob.setPassword(password);
 expectedBatchJob.setnThreads(nThreads);
 expectedBatchJob.setTimeout(timeout);

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);

 assertTrue(actualBatchJob != null);
 assertBatchJobsEqual(expectedBatchJob, actualBatchJob);
 }

 @Test
 public void testGetBatchJobTimeoutGreaterThanZero() {
 String group = "Group1";
 String server = "server";
 String directory = "directory";
 String username = "username";
 String password = "password";
 int nThreads = 2;
 int timeout = 1000;

 BatchJob expectedBatchJob = new BatchJob(server, group, directory, 1, 1);
 expectedBatchJob.setUsername(username);
 expectedBatchJob.setPassword(password);
 expectedBatchJob.setnThreads(nThreads);
 expectedBatchJob.setTimeout(timeout);
 Newzbatch.TIMEOUT = true;
 Newzbatch.TIMEOUTMAX = timeout;

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);

 assertTrue(actualBatchJob != null);
 assertBatchJobsEqual(expectedBatchJob, actualBatchJob);
 }

 @Test
 public void testGetSubjectLines() {
 String[] expectedSubjectLines = {"Subject1", "Subject2"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();

 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 String[] expectedSubjectLines = {};
 String[] actualSubjectLines = batchDriver.getSubjectLines();

 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 private void assertBatchJobsEqual(BatchJob expectedBatchJob, BatchJob actualBatchJob) {
 assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
 assertEquals(expectedBatchJob.getGroup(), actualBatchJob.getGroup());
 assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
 assertEquals(expectedBatchJob.getFirstID(), actualBatchJob.getFirstID());
 assertEquals(expectedBatchJob.getLastID(), actualBatchJob.getLastID());
 assertEquals(expectedBatchJob.getUsername(), actualBatchJob.getUsername());
 assertEquals(expectedBatchJob.getPassword(), actualBatchJob.getPassword());
 assertEquals(expectedBatchJob.getnThreads(), actualBatchJob.getnThreads());
 assertEquals(expectedBatchJob.isTimeoutSet(), actualBatchJob.isTimeoutSet());
 }
}