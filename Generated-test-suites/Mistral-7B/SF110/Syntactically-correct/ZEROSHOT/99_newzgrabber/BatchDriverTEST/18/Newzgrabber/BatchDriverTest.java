package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.ini4j.Ini;
import org.ini4j.WiniFile;
import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newziniFile = "testNewzini.ini";
 private String batchFile = "testBatchfile.ini";

 @Before
 public void setUp() {
 try {
 // Create test ini files
 writeTestIniFiles();

 // Initialize BatchDriver
 batchDriver = new BatchDriver(newziniFile, batchFile);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver in setUp()");
 }
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"group1", "group2", "group3"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJob() {
 String group = "group1";
 BatchJob expectedBatchJob = new BatchJob("server1", group, "/path/to/directory1", 1, 10);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() {
 String group = "nonExistentGroup";
 batchDriver.getBatchJob(group);
 fail("Should have thrown ArrayIndexOutOfBoundsException with message: Index 1 out of range (size=2)");
 }

 @Test
 public void testGetSubjectLines() {
 String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesWithEmptySubjectLines() {
 String[] expectedSubjectLines = {};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testCleanUp() {
 File newziniFile = new File(this.newziniFile);
 File batchFile = new File(this.batchFile);
 assertTrue(newziniFile.exists());
 assertTrue(batchFile.exists());

 // Clean up test ini files
 newziniFile.delete();
 batchFile.delete();

 File newziniFileAfterDelete = new File(this.newziniFile);
 File batchFileAfterDelete = new File(this.batchFile);
 assertFalse(newziniFileAfterDelete.exists());
 assertFalse(batchFileAfterDelete.exists());
 }

 private void writeTestIniFiles() {
 WiniFile newzini = new WiniFile(newziniFile);
 newzini.put("Messageids", "Newsgroups", "group1", "1");
 newzini.put("Messageids", "Newsgroups", "group2", "2");
 newzini.put("Messageids", "Newsgroups", "group3", "3");
 newzini.save();

 WiniFile batch = new WiniFile(batchFile);
 batch.put("", "Newsgroups", "group1");
 batch.put("", "Retrieve", "true");
 batch.put("", "Options", "server1");
 batch.put("", "Options", "threads=2");
 batch.put("", "Options", "timeout=3000");
 batch.put("", "Options", "username=testUser");
 batch.put("", "Options", "password=testPassword");
 batch.put("", "Options", "server2");
 batch.save();
 }

}