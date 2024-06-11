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
 private String newziniFile = "testNewzini.ini";
 private String batchFile = "testBatchfile.ini";

 @Before
 public void setUp() throws IOException {
 File newziniFile = new File(this.newziniFile);
 newziniFile.createNewFile();
 Wini newzini = new Ini(newziniFile);
 newzini.put("Messageids", "group1", "1");
 newzini.save();

 File batchFile = new File(this.batchFile);
 batchFile.createNewFile();
 Wini batch = new Ini(batchFile);
 batch.put("", "Newsgroups", "group1");
 batch.put("Retrieve", "group1", "server1:/path/to/directory");
 batch.put("Options", "server1", "server1");
 batch.put("Options", "username1", "username1");
 batch.put("Options", "password1", "password1");
 batch.put("Options", "threads", "2");
 batch.put("Options", "timeout", "30000");
 batch.put("", "group2", "group2");
 batch.save();

 batchDriver = new BatchDriver(newziniFile, batchFile);
 }

 @Test
 public void testGetGroups() {
 String[] groups = batchDriver.getGroups();
 assertEquals(2, groups.length);
 assertEquals("group1", groups[0]);
 assertEquals("group2", groups[1]);
 }

 @Test
 public void testGetBatchJob() {
 String server = "server1";
 String group = "group1";
 String directory = "server1:/path/to/directory";
 int firstId = 1;
 int lastId = 1;
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertEquals(server, batchJob.getServer());
 assertEquals(group, batchJob.getGroupName());
 assertEquals(directory, batchJob.getDirectory());
 assertEquals(firstId, batchJob.getFirstId());
 assertEquals(lastId, batchJob.getLastId());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() {
 String group = "nonexistentgroup";
 batchDriver.getBatchJob(group);
 }

 @Test
 public void testGetSubjectLines() {
 String[] subjectLines = batchDriver.getSubjectLines();
 assertTrue(subjectLines.length > 0);
 }
}