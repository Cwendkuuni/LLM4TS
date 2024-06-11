package Newzgrabber.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

 private static final String NEWZINI = "testData/newzini.ini";
 private static final String BATCHFILE = "testData/batchfile.ini";

 @Test
 public void testBatchDriverConstruction() throws IOException, NullPointerException {
 BatchDriver bd = new BatchDriver(NEWZINI, BATCHFILE);
 assertNotNull(bd);
 }

 @Test
 public void testGetGroups() throws ArrayIndexOutOfBoundsException {
 BatchDriver bd = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = bd.getGroups();
 assertNotNull(groups);
 assertFalse(groups.length == 0);
 }

 @Test
 public void testGetBatchJobNormalCase() {
 BatchDriver bd = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob bj = bd.getBatchJob("testgroup");
 assertNotNull(bj);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidGroup() {
 BatchDriver bd = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob bj = bd.getBatchJob("InvalidGroup");
 fail("getBatchJob should throw ArrayIndexOutOfBoundsException for invalid group");
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullGroup() {
 BatchDriver bd = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob bj = bd.getBatchJob(null);
 fail("getBatchJob should throw NullPointerException for null group");
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullOptionsHash() {
 BatchDriver bd = new BatchDriver(NEWZINI, BATCHFILE);
 bd.OptionsHash = null;
 BatchJob bj = bd.getBatchJob("testgroup");
 fail("getBatchJob should throw NullPointerException for null OptionsHash");
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobInvalidBatchFile() throws IOException {
 BatchDriver bd = new BatchDriver(NEWZINI, "testData/invalidBatchFile.ini");
 BatchJob bj = bd.getBatchJob("testgroup");
 fail("getBatchJob should throw IOException for invalid batch file");
 }

 @Test
 public void testGetSubjectLines() {
 BatchDriver bd = new BatchDriver(NEWZINI, BATCHFILE);
 String[] subjectLines = bd.getSubjectLines();
 assertNotNull(subjectLines);
 assertFalse(subjectLines.length == 0);
 }

 @Test
 public void testGetSubjectLinesEmptyBatch() {
 BatchDriver bd = new BatchDriver("testData/emptyBatchFile.ini", BATCHFILE);
 String[] subjectLines = bd.getSubjectLines();
 assertNotNull(subjectLines);
 assertArrayEqual(new String[0], subjectLines);
 }
}