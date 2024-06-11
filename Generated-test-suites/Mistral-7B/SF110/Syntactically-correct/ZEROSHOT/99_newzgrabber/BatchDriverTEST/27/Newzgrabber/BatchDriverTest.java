package Newzgrabber;

import static org.junit.Assert.*;

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
 File newzfile = new File(newzini);
 File batchfile = new File(this.batchfile);
 newzfile.createNewFile();
 batchfile.createNewFile();

 Ini newzini = new Ini(newzini, "Messageids");
 Ini batchgroups = new Ini(batchfile, "");

 newzini.writeIni("group1", "1");
 newzini.writeIni("group2", "10");
 batchgroups.writeIni("group1", "server1,Retrieve1,Options1,Threads1,Timeout1");
 batchgroups.writeIni("group2", "server2,Retrieve2,Options2,Threads2,Timeout2");

 batchDriver = new BatchDriver(newzini.getFileName(), batchgroups.getFileName());
 }

 @Test
 public void testGetGroups() {
 String[] groups = batchDriver.getGroups();
 assertEquals(2, groups.length);
 assertArrayEquals(new String[]{"group1", "group2"}, groups);
 }

 @Test
 public void testGetBatchJob() {
 String group = "group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 assertEquals("server1", batchJob.getServer());
 assertEquals("Retrieve1", batchJob.getRetrieve());
 assertEquals("Options1", batchJob.getOptions());
 assertEquals(1, batchJob.getThreads());
 assertEquals(-1, batchJob.getTimeout());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidGroup() {
 String group = "group3";
 batchDriver.getBatchJob(group);
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testGetSubjectLines() {
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertFalse(subjectLines.length == 0);
 }
}