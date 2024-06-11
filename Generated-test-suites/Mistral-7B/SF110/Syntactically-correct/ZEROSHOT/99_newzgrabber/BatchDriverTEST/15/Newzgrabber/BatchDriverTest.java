package Newzgrabber;

import org.junit.After;
import org.junit.Assert;import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini = "test.ini";
 private String batchfile = "test.ini";

 @Before
 public void setUp() throws IOException {
 // Initialize the test file with some data
 writeTestFile(newzini);
 writeTestFile(batchfile);

 // Initialize the BatchDriver object
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() {
 // Delete the test file after the test is finished
 File testFile = new File(newzini);
 testFile.delete();
 testFile = new File(batchfile);
 testFile.delete();
 }

 @Test
 public void testGetGroups() {
 final String[] expectedGroups = {"group1", "group2", "group3"};
 final String[] actualGroups = batchDriver.getGroups();
 Assert.assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJobWithValidGroup() {
 final String group = "group1";
 final String server = "test.server.com";
 final String directory = "/path/to/directory";
 final int firstId = 1;
 final int lastId = 10;
 final BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstId, lastId);
 final BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 Assert.assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() {
 final String group = "nonExistentGroup";
 batchDriver.getBatchJob(group);
 }

 @Test
 public void testGetSubjectLines() {
 final String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 final String[] actualSubjectLines = batchDriver.getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetBatchJobWithNullGroup() {
 batchDriver.getBatchJob(null);
 }

 private void writeTestFile(String fileName) throws IOException {
 final PrintWriter writer = new PrintWriter(fileName);
 writer.println("; Newzini File");
 writer.println("[Messageids]");
 writer.println("group1=10");
 writer.println("group2=20");
 writer.println("group3=30");
 writer.println("; Batch File");
 writer.println("[Newsgroups]");
 writer.println("group1=group.server.com.test.group1");
 writer.println("group2=group.server.com.test.group2");
 writer.println("group3=group.server.com.test.group3");
 writer.println("[Retrieve]");
 writer.println("server=test.server.com");
 writer.println("[Options]");
 writer.println("threads=2");
 writer.println("timeout=10000");
 writer.close();
 }
}