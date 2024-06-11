package Newzgrabber.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.ini4j.Ini;
import org.ini4j.Wini;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTP;

public class BatchDriverTest {

 private static final String NEWZINI = "src/test/resources/newzini.ini";
 private static final String BATCHFILE = "src/test/resources/batchfile.ini";

 @Test
 public void testBatchDriverConstruction() throws IOException, NullPointerException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNotNull(batchDriver.getGroups());
 assertFalse(batchDriver.getGroups().length == 0);
 }

 @Test
 public void testGetGroups() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 for (String group : groups) {
 System.out.println(group);
 }
 }

 @Test
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobValidGroup() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertNotNull(batchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidIndex() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob(0);
 fail("Expected ArrayIndexOutOfBoundsException but got " + batchJob);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullServer() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 BatchJob batchJob = batchDriver.getBatchJob(groups[0]);
 batchDriver.getBatchJob(null);
 fail("Expected NullPointerException but got " + batchJob);
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobInvalidServer() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 String invalidServer = "invalid.server";
 for (String group : groups) {
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 batchDriver.getBatchJob(invalidServer);
 fail("Expected IOException but got " + batchJob);
 }
 }

 @Test
 public void testGetBatchJobWithValidOptions() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 String server = "test.server";
 String directory = "/test/directory";
 int nThreads = 2;
 int timeout = 3000;
 String username = "test.username";
 String password = "test.password";
 NewsFactory nf = NewsFactory.getSingleton();
 NNTP news = nf.getNewsSocket(server);
 batchDriver.getOptionsHash().put("server", server);
 batchDriver.getOptionsHash().put("directory", directory);
 batchDriver.getOptionsHash().put("threads", Integer.toString(nThreads));
 batchDriver.getOptionsHash().put("timeout", Integer.toString(timeout));
 batchDriver.getOptionsHash().put("username", username);
 batchDriver.getOptionsHash().put("password", password);
 BatchJob batchJobWithOptions = batchDriver.getBatchJob("test.group");
 assertNotNull(batchJobWithOptions);
 assertEquals(server, batchJobWithOptions.getServer());
 assertEquals(directory, batchJobWithOptions.getDirectory());
 assertEquals(nThreads, batchJobWithOptions.getnThreads());
 assertEquals(timeout, batchJobWithOptions.getTimeout());
 if (username != null) {
 assertEquals(username, batchJobWithOptions.getUsername());
 }
 if (password != null) {
 assertEquals(password, batchJobWithOptions.getPassword());
 }
 news.quit();
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidNumberOfThreads() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 BatchJob batchJob = batchDriver.getBatchJob(groups[0]);
 batchDriver.getOptionsHash().put("threads", "invalidThreads");
 BatchJob batchJobWithInvalidThreads = batchDriver.getBatchJob(groups[0]);
 fail("Expected NumberFormatException but got " + batchJobWithInvalidThreads);
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidTimeout() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 BatchJob batchJob = batchDriver.getBatchJob(groups[0]);
 batchDriver.getOptionsHash().put("timeout", "invalidTimeout");
 BatchJob batchJobWithInvalidTimeout = batchDriver.getBatchJob(groups[0]);
 fail("Expected NumberFormatException but got " + batchJobWithInvalidTimeout);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullUsername() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 BatchJob batchJob = batchDriver.getBatchJob(groups[0]);
 batchDriver.getOptionsHash().put("username", null);
 BatchJob batchJobWithNullUsername = batchDriver.getBatchJob(groups[0]);
 fail("Expected NullPointerException but got " + batchJobWithNullUsername);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullPassword() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 BatchJob batchJob = batchDriver.getBatchJob(groups[0]);
 batchDriver.getOptionsHash().put("password", null);
 BatchJob batchJobWithNullPassword = batchDriver.getBatchJob(groups[0]);
 fail("Expected NullPointerException but got " + batchJobWithNullPassword);
 }

 @Test
 public void testGetSubjectLines() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 String group = groups[0];
 BatchJob batchJob = batchDriver.getBatchJob(group);
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertTrue(subjectLines.length > 0);
 for (String subjectLine : subjectLines) {
 System.out.println(subjectLine);
 }
 }

 @Test
 public void testGetSubjectLinesNullBatchJob() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNull(subjectLines);
 }

 @Test
 public void testGetSubjectLinesEmptyBatchJob() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 String group = groups[0];
 BatchJob batchJob = batchDriver.getBatchJob(group);
 batchDriver.getOptionsHash().put("server", "invalid.server");
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNull(subjectLines);
 }

 @Test
 public void testGetSubjectLinesValidBatchJob() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 String group = groups[0];
 BatchJob batchJob = batchDriver.getBatchJob(group);
 batchDriver.getOptionsHash().put("server", "test.server");
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertTrue(subjectLines.length > 0);
 for (String subjectLine : subjectLines) {
 System.out.println(subjectLine);
 }
 }
}