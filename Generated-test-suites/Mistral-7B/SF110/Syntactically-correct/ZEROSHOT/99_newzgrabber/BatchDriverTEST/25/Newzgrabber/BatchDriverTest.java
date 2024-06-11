package Newzgrabber;

import static org.junit.Assert.*;

import org.junit.Test;

public class BatchDriverTest {

 private static final String NEWZINI = "path/to/newzini.ini";
 private static final String BATCHFILE = "path/to/batchfile.ini";

 @Test
 public void testBatchDriverInstance() throws IOException, NullPointerException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNotNull(batchDriver.getBatchJob("testGroup"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullNewzini() {
 BatchDriver batchDriver = new BatchDriver(null, BATCHFILE);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBatchFile() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, null);
 }

 @Test(expected = IOException.class)
 public void testInvalidNewzini() throws IOException {
 BatchDriver batchDriver = new BatchDriver("invalid.ini", BATCHFILE);
 }

 @Test(expected = IOException.class)
 public void testInvalidBatchFile() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, "invalid.ini");
 }

 @Test
 public void testGetGroups() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 assertNotNull(groups);
 assertFalse(groups.length == 0);
 }

 @Test
 public void testGetBatchJob() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("testGroup");
 assertNotNull(batchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidGroup() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.getBatchJob("invalidGroup");
 }

 @Test(expected = NullPointerException.class)
 public void testNullServer() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.getBatchJob("testGroup").setServer(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullDirectory() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.getBatchJob("testGroup").setDirectory(null);
 }

 @Test
 public void testGetSubjectLines() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertFalse(subjectLines.length == 0);
 }
}