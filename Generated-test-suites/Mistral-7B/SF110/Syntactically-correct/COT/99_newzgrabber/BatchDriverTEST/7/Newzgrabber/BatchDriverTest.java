package Newzgrabber.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.Newzbatch;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTP;
import Newzgrabber.Status;

public class BatchDriverTest {

 private BatchDriver batchDriver;

 @Before
 public void setUp() throws IOException, NullPointerException {
 String newzini = "testdata/newzini.ini";
 String batchfile = "testdata/batchfile.ini";
 batchDriver = new BatchDriver(newzini, batchfile);
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
 public void testGetBatchJobInvalidGroup() {
 assertNull(batchDriver.getBatchJob("InvalidGroup"));
 }

 @Test
 public void testGetBatchJobValidGroup() {
 String server = "example.nntp.server";
 String group = "group1";
 String directory = "/path/to/group/directory";
 int firstid = 1;
 int lastid = 10;
 BatchJob expectedBatchJob = new BatchJob(server, group, directory, firstid, lastid);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobEmptyVector() {
 batchDriver = new BatchDriver(null, null);
 assertNull(batchDriver.getBatchJob("ValidGroup"));
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobInvalidFile() {
 batchDriver = new BatchDriver("testdata/invalidfile.ini", "testdata/batchfile.ini");
 assertNull(batchDriver.getBatchJob("ValidGroup"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullINI() {
 batchDriver = new BatchDriver(null, "testdata/batchfile.ini");
 assertNull(batchDriver.getBatchJob("ValidGroup"));
 }

 @Test
 public void testGetSubjectLinesNullBatchJob() {
 assertNull(batchDriver.getSubjectLines());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetSubjectLinesEmptyBatchJob() {
 NewsFactory nf = Newzgrabber.nf;
 NNTP news = nf.getNewsSocket("example.nntp.server");
 BatchJob batchJob = new BatchJob("example.nntp.server", "ValidGroup", null, 0, 0);
 batchDriver = new BatchDriver("testdata/newzini.ini", "testdata/batchfile.ini");
 batchDriver.setBatchJob(batchJob);
 assertNull(batchDriver.getSubjectLines());
 news.quit();
 }

 @Test
 public void testGetSubjectLinesValidBatchJob() {
 String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesInvalidBatchJob() {
 batchDriver = new BatchDriver("testdata/newzini.ini", "testdata/batchfile.ini");
 assertNull(batchDriver.getSubjectLines());
 }
}