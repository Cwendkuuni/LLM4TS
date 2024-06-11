package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
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
 File iniFile = new File(newzini);
 File batchFile = new File(batchfile);
 iniFile.createNewFile();
 batchFile.createNewFile();
 try (PrintWriter newzWriter = new PrintWriter(iniFile);
 PrintWriter batchWriter = new PrintWriter(batchFile)) {
 newzWriter.println("[Messageids]");
 batchWriter.println("[Newsgroups]");
 batchWriter.println("group1=group1");
 batchWriter.println("group2=group2");
 batchWriter.println("[Retrieve]");
 batchWriter.println("group1=server1,directory1");
 batchWriter.println("group2=server2,directory2");
 batchWriter.println("[Options]");
 batchWriter.println("server=server");
 batchWriter.println("threads=5");
 batchWriter.println("timeout=30");
 }
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() throws Exception {
 File iniFile = new File(newzini);
 File batchFile = new File(batchfile);
 if (iniFile.exists()) {
 iniFile.delete();
 }
 if (batchFile.exists()) {
 batchFile.delete();
 }
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"group1", "group2"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJobNullGroup() {
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 assertNull(batchDriver.getBatchJob("invalidGroup"));
 }

 @Test
 public void testGetBatchJob() {
 String expectedServer = "server";
 String expectedDirectory = "directory1";
 String expectedGroup = "group1";
 BatchJob expectedBatchJob = new BatchJob(expectedServer, expectedGroup, "directory1", 0, 0);
 expectedBatchJob.setUsername("username");
 expectedBatchJob.setPassword("password");
 BatchJob actualBatchJob = batchDriver.getBatchJob("group1");
 assertEquals(expectedBatchJob.getServer(), actualBatchJob.getServer());
 assertEquals(expectedBatchJob.getDirectory(), actualBatchJob.getDirectory());
 assertEquals(expectedBatchJob.getGroup(), actualBatchJob.getGroup());
 assertEquals(expectedBatchJob.getFirstId(), actualBatchJob.getFirstId());
 assertEquals(expectedBatchJob.getLastId(), actualBatchJob.getLastId());
 assertEquals(expectedBatchJob.getUsername(), actualBatchJob.getUsername());
 assertEquals(expectedBatchJob.getPassword(), actualBatchJob.getPassword());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidIndex() {
 batchDriver.getBatchJob(1);
 }

 @Test
 public void testGetSubjectLines() {
 String[] expectedSubjectLines = {"Subject: Test Subject 1", "Subject: Test Subject 2"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 String[] expectedSubjectLines = new String[0];
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}