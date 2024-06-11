package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
 final File newziniFile = new File(newzini);
 final File batchfileFile = new File(batchfile);
 newziniFile.createNewFile();
 batchfileFile.createNewFile();

 try (PrintWriter newziniWriter = new PrintWriter(newziniFile); PrintWriter batchfileWriter = new PrintWriter(batchfileFile)) {
 newziniWriter.println("[Messageids]");
 newziniWriter.println("Newsgroups = group1");
 newziniWriter.println("Newsgroups = group2");
 batchfileWriter.println("[ ]");
 batchfileWriter.println("Newsgroups = group1");
 batchfileWriter.println("Retrieve = true");
 batchfileWriter.println("Options = server1 user1 password1 threads=5 timeout=120");
 batchfileWriter.println("Newsgroups = group2");
 batchfileWriter.println("Retrieve = true");
 batchfileWriter.println("Options = server2 user2 password2 threads=3 timeout=-1");
 }

 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() {
 final File newziniFile = new File(newzini);
 final File batchfileFile = new File(batchfile);
 newziniFile.delete();
 batchfileFile.delete();
 }

 @Test
 public void testGetGroups() {
 final String[] expectedGroups = new String[]{"group1", "group2"};
 final String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJob() {
 final String group1 = "group1";
 final BatchJob expectedBatchJob = new BatchJob("server1", group1, "/path/to/group1", 1, 100);
 expectedBatchJob.setUsername("user1");
 expectedBatchJob.setPassword("password1");
 final BatchJob actualBatchJob = batchDriver.getBatchJob(group1);
 assertEquals(expectedBatchJob, actualBatchJob);

 final String group2 = "group2";
 final BatchJob expectedBatchJob2 = new BatchJob("server2", group2, "/path/to/group2", 1, 200);
 expectedBatchJob2.setUsername("user2");
 expectedBatchJob2.setPassword("password2");
 final BatchJob actualBatchJob2 = batchDriver.getBatchJob(group2);
 assertEquals(expectedBatchJob2, actualBatchJob2);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() {
 batchDriver.getBatchJob("invalidGroup");
 }

 @Test
 public void testGetSubjectLines() {
 final String[] expectedSubjectLines = new String[]{"Subject 1", "Subject 2", "Subject 3"};
 final String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}