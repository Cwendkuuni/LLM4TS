import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private Ini iniGroups, iniBatchGroups;
 private String newzini = "test.ini", batchfile = "test.ini";

 @Before
 public void setUp() throws IOException {
 batchDriver = new BatchDriver(newzini, batchfile);
 iniGroups = new Ini(newzini, "Messageids");
 iniBatchGroups = new Ini(batchfile, "");
 }

 @After
 public void tearDown() {
 batchDriver = null;
 iniGroups = null;
 iniBatchGroups = null;
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"group1", "group2", "group3"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetGroupsEmpty() {
 String[] expectedGroups = {};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJobValid() {
 String group = "group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 String group = "nonexistentgroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNull(batchJob);
 }

 @Test
 public void testGetBatchJobNullOptions() {
 String group = "group1";
 batchDriver.OptionsHash = null;
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNull(batchJob);
 }

 @Test
 public void testGetBatchJobInvalidServer() {
 String group = "group1";
 batchDriver.OptionsHash.put("server", "invalidserver");
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNull(batchJob);
 }

 @Test
 public void testGetBatchJobInvalidDirectory() {
 String group = "group1";
 batchDriver.OptionsHash.put("server", "validserver");
 batchDriver.BatchgroupsHash.put(group, "invaliddirectory");
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNull(batchJob);
 }

 @Test
 public void testGetSubjectLinesValid() {
 String[] expectedSubjectLines = {"Subject: Test 1", "Subject: Test 2"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 String[] expectedSubjectLines = {};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

}