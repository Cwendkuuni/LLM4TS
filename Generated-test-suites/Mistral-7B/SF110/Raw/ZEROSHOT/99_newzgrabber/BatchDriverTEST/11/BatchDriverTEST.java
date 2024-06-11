package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newziniFile = "testNewzini.ini";
 private String batchFile = "testBatch.ini";

 @Before
 public void setUp() throws IOException {
 File newziniFile = new File(this.newziniFile);
 File batchFile = new File(this.batchFile);
 newziniFile.createNewFile();
 batchFile.createNewFile();

 Ini groupsini = new Ini(newziniFile.getAbsolutePath(), "Messageids");
 groupsini.put("Newsgroups", "group1");
 groupsini.save();

 Ini batchgroups = new Ini(batchFile.getAbsolutePath(), "");
 batchgroups.put("Newsgroups", "group1");
 batchgroups.put("Retrieve", "1");
 batchgroups.save();

 batchDriver = new BatchDriver(newziniFile.getAbsolutePath(), batchFile.getAbsolutePath());
 }

 @After
 public void tearDown() throws IOException {
 batchDriver = null;
 new File(this.newziniFile).delete();
 new File(this.batchFile).delete();
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"group1"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJob() {
 String group = "group1";
 BatchJob expectedBatchJob = new BatchJob("server1", group, "/path/to/group", 1, 10);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() {
 String group = "nonExistentGroup";
 batchDriver.getBatchJob(group);
 }

 @Test
 public void testGetSubjectLines() {
 String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}