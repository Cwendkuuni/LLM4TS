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
 public void setUp() throws IOException, NullPointerException {
 File newzfile = new File(newzini);
 File batchfileObj = new File(batchfile);
 newzfile.createNewFile();
 batchfileObj.createNewFile();

 Ini newzinis = new Ini(newzini, "Messageids");
 Ini batchinis = new Ini(batchfile, "");
 newzinis.setProperty("server", "localhost");
 newzinis.setProperty("threads", "5");
 newzinis.setProperty("timeout", "30000");
 newzinis.setProperty("username", "testuser");
 newzinis.setProperty("password", "testpass");
 newzinis.writeIni();

 batchinis.setProperty("Newsgroups", "group1,group2");
 batchinis.setProperty("Retrieve", "group1:firstid1-lastid1,group2:firstid2-lastid2");
 batchinis.setProperty("Options", "server:localhost,threads:5,timeout:30000,username:testuser,password:testpass");
 batchinis.writeIni();

 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() {
 batchDriver = null;
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"group1", "group2"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJobForValidGroup() {
 String server = "localhost";
 String group = "group1";
 String directory = "testdir1";
 int firstid = 1;
 int lastid = 10;
 BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstid, lastid);
 expectedBatchJob.setUsername("testuser");
 expectedBatchJob.setPassword("testpass");

 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobForInvalidGroup() {
 String group = "nonExistentGroup";
 assertNull(batchDriver.getBatchJob(group));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetSubjectLinesWithEmptySubjectsArray() {
 String[] expectedSubjectLines = new String[0];
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesWithSubjectsArray() {
 String[] expectedSubjectLines = {"Subject 1", "Subject 2"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}