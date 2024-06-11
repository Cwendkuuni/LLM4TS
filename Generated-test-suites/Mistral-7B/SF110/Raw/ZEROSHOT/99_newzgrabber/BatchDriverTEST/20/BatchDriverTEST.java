package Newzgrabber;

import static org.junit.Assert.*;

import org.ini4j.Ini;
import org.junit.Test;

public class BatchDriverTest {

 private BatchDriver batchDriver;

 @Test
 public void testBatchDriverInstance() throws IOException, NullPointerException {
 final String newzini = "test.ini";
 final String batchfile = "test.ini";
 batchDriver = new BatchDriver(newzini, batchfile);
 assertNotNull(batchDriver);
 }

 @Test
 public void testGetGroups() throws ArrayIndexOutOfBoundsException {
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
 assertEquals("directory1", batchJob.getDirectory());
 assertEquals(1, batchJob.getFirstId());
 assertEquals(10, batchJob.getLastId());
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullGroup() {
 batchDriver.getBatchJob(null);
 }

 @Test
 public void testGetSubjectLines() {
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals(3, subjectLines.length);
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 Ini ini = new Ini("test.ini", "Messageids");
 ini.add("group1", "FirstId=1;LastId=10;Retrieve=true;Options=server1;");
 batchDriver = new BatchDriver(ini.getAbsolutePath(), "test.ini");
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals(0, subjectLines.length);
 }
}