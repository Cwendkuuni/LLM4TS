package Newzgrabber.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTP;
import Newzgrabber.Status;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini = "test.ini";
 private String batchfile = "test.ini";

 @Before
 public void setUp() throws Exception {
 File iniFile = new File(newzini);
 File batchFile = new File(batchfile);
 Ini groupsini = new Ini(iniFile, "Messageids");
 Ini batchgroups = new Ini(batchFile, "");
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @Test
 public void testBatchDriverConstruction() throws Exception {
 assertNotNull(batchDriver);
 }

 @Test
 public void testGetGroups() throws Exception {
 String[] groups = batchDriver.getGroups();
 assertEquals(2, groups.length);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsArrayIndexOutOfBoundsException() throws Exception {
 batchDriver.getGroups();
 int length = batchDriver.getGroups().length + 1;
 String group = batchDriver.getGroups()[length];
 }

 @Test
 public void testGetBatchJobNullGroup() {
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobEmptyGroup() throws Exception {
 assertNull(batchDriver.getBatchJob(""));
 }

 @Test
 public void testGetBatchJobValidGroup() throws Exception {
 String server = "test.server.com";
 String group = "test.group";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 assertEquals(server, batchJob.getServer());
 assertEquals(group, batchJob.getGroup());
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullServer() {
 String group = "test.group";
 batchDriver.OptionsHash.put("server", null);
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullDirectory() {
 String group = "test.group";
 batchDriver.BatchgroupsHash.put(group, null);
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobIOException() throws Exception {
 batchDriver.OptionsHash.put("server", "test.server.com");
 batchDriver.BatchgroupsHash.put("test.group", null);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 }

 @Test
 public void testGetSubjectLinesNull() {
 assertNull(batchDriver.getSubjectLines());
 }

 @Test
 public void testGetSubjectLinesValid() {
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals(5, subjectLines.length);
 }

 @Test
 public void testSubjectLinesNullSubjectLinesArray() {
 batchDriver.subjectlines = null;
 String[] subjectLines = batchDriver.getSubjectLines();
 }

 @Test
 public void testSubjectLinesEmptySubjectLinesArray() {
 batchDriver.subjectlines = new String[0];
 String[] subjectLines = batchDriver.getSubjectLines();
 }

 @Test
 public void testSubjectLinesValidSubjectLinesArray() {
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals("Test subject line 1", subjectLines[0]);
 assertEquals("Test subject line 2", subjectLines[1]);
 }

 @Test
 public void testGetBatchJobWithUsernameAndPassword() throws Exception {
 String server = "test.server.com";
 String group = "test.group";
 String username = "test.username";
 String password = "test.password";
 batchDriver.OptionsHash.put("server", server);
 batchDriver.OptionsHash.put("username", username);
 batchDriver.OptionsHash.put("password", password);
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 assertEquals(server, batchJob.getServer());
 assertEquals(group, batchJob.getGroup());
 assertEquals(username, batchJob.getUsername());
 assertEquals(password, batchJob.getPassword());
 }

}