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
 public void setUp() throws IOException, NullPointerException {
 final File newziniFile = new File(newzini);
 final File batchfileFile = new File(batchfile);
 final Ini groupsini = new Ini(newziniFile, "Messageids");
 final Ini batchgroups = new Ini(batchfileFile, "");
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() {
 batchDriver = null;
 }

 @Test
 public void testGetGroups() {
 final String[] expectedGroups = {"group1", "group2", "group3"};
 final String[] actualGroups = batchDriver.getGroups();
 Assert.assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetGroupsEmptyVector() {
 batchDriver = new BatchDriver("empty.ini", "batchfile.ini");
 final String[] actualGroups = batchDriver.getGroups();
 Assert.assertArrayEquals(new String[0], actualGroups);
 }

 @Test
 public void testGetBatchJob() {
 final String group = "group1";
 final BatchJob expectedBatchJob = new BatchJob("server1", group, "/path/to/directory1", 1, 10);
 final BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 Assert.assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBatchJobNullGroup() {
 batchDriver.getBatchJob(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBatchJobNonExistentGroup() {
 batchDriver.getBatchJob("nonExistentGroup");
 }

 @Test
 public void testGetSubjectLines() {
 final String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 final String[] actualSubjectLines = batchDriver.getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesEmptyArray() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 final String[] actualSubjectLines = batchDriver.getSubjectLines();
 Assert.assertArrayEquals(new String[0], actualSubjectLines);
 }
}