package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.ini4j.Ini;
import org.ini4j.Wini;
import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini = "test.ini";
 private String batchfile = "test.ini";

 @Before
 public void setup() throws IOException {
 // Initialize test ini files
 writeTestIniFiles();

 // Initialize BatchDriver with test ini files
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @Test
 public void testBatchDriverCreation() {
 // Test that BatchDriver is created successfully
 assertNotNull(batchDriver);
 }

 @Test
 public void testGetGroups() throws ArrayIndexOutOfBoundsException {
 // Test getting groups from BatchDriver
 String[] groups = batchDriver.getGroups();
 assertEquals("Size of groups array should be 2", 2, groups.length);
 }

 @Test
 public void testGetBatchJob() {
 // Test getting BatchJob for valid group
 String group = "test.group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull("BatchJob should not be null for valid group", batchJob);

 // Test getting BatchJob for invalid group
 group = "test.group3";
 batchJob = batchDriver.getBatchJob(group);
 assertNull("BatchJob should be null for invalid group", batchJob);
 }

 @Test
 public void testGetSubjectLines() {
 // Test getting subject lines from BatchDriver
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull("Subject lines should not be null", subjectLines);
 assertEquals("Size of subject lines array should be 3", 3, subjectLines.length);
 }

 @Test
 public void testGetBatchJobWithUsernameAndPassword() {
 // Test getting BatchJob with username and password
 String group = "test.group1";
 String username = "test.user1";
 String password = "test.pass1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 batchJob.setUsername(username);
 batchJob.setPassword(password);
 assertNotNull("BatchJob should not be null for valid group", batchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidIndex() {
 // Test getting BatchJob with invalid index
 String group = "test.group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 batchJob = batchDriver.getBatchJob(group); // should throw ArrayIndexOutOfBoundsException
 fail("Should throw ArrayIndexOutOfBoundsException for invalid index");
 }

 @Test(expected = NullPointerException.class)
 public void testNullNewzini() {
 // Test NullPointerException when newzini is null
 batchDriver = new BatchDriver(null, batchfile);
 fail("Should throw NullPointerException when newzini is null");
 }

 @Test(expected = IOException.class)
 public void testIOExceptionWhenOpeningNewzini() throws IOException {
 // Test IOException when opening newzini file
 File newziniFile = new File(newzini);
 newziniFile.delete();
 batchDriver = new BatchDriver(newzini, batchfile);
 fail("Should throw IOException when opening newzini file that does not exist");
 }

 @Test(expected = IOException.class)
 public void testIOExceptionWhenOpeningBatchfile() throws IOException {
 // Test IOException when opening batchfile
 File batchfile = new File(this.batchfile);
 batchfile.delete();
 batchDriver = new BatchDriver(newzini, batchfile);
 fail("Should throw IOException when opening batchfile that does not exist");
 }

 @Test
 public void teardown() {
 // Cleanup test ini files
 deleteTestIniFiles();
 }

 private void writeTestIniFiles() throws IOException {
 Wini ini = new Ini();
 // Write Newzini file
 ini.put("Messageids", "test.group1", "12345");
 ini.put("Messageids", "test.group2", "67890");
 ini.put("Messageids", "test.group3", "23456");
 File newziniFile = new File(newzini);
 FileUtils.writeStringToFile(newziniFile, ini.toFormat(4));

 // Write Batchfile
 Ini batchgroups = new Ini();
 batchgroups.put("Newsgroups", "test.group1", "server1.com:/path1/");
 batchgroups.put("Newsgroups", "test.group2", "server2.com:/path2/");
 batchgroups.put("Newsgroups", "test.group3", "server3.com:/path3/");
 batchgroups.put("Retrieve", "test.group1", "true");
 batchgroups.put("Retrieve", "test.group2", "false");
 batchgroups.put("Retrieve", "test.group3", "true");
 batchgroups.put("Options", "threads", "2");
 batchgroups.put("Options", "timeout", "120");
 batchgroups.put("Options", "server", "server1.com");
 File batchfile = new File(batchfile);
 FileUtils.writeStringToFile(batchfile, batchgroups.toFormat(4));
 }

 private void deleteTestIniFiles() {
 File newziniFile = new File(newzini);
 File batchfile = new File(batchfile);
 if (newziniFile.exists()) {
 newziniFile.delete();
 }
 if (batchfile.exists()) {
 batchfile.delete();
 }
}
}