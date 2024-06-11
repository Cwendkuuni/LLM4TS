package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.ini4j.Ini;
import org.ini4j.WiniFile;
import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private Ini newzini;
 private Ini batchfile;

 @Before
 public void setUp() throws IOException {
 newzini = new WiniFile(new File("test.ini"));
 batchfile = new WiniFile(new File("test_batchfile.ini"));
 batchDriver = new BatchDriver("test.ini", "test_batchfile.ini");
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"group1", "group2", "group3"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJobNullGroup() {
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobValidGroup() {
 String server = "test_server";
 String directory = "test_directory";
 String group = "group1";
 BatchJob expectedBatchJob = new BatchJob(server, group, directory, 1, 10);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertTrue(expectedBatchJob.compareTo(actualBatchJob) == 0);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidGroup() {
 batchDriver.getBatchJob("invalid_group");
 }

 @Test
 public void testGetSubjectLines() {
 String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}