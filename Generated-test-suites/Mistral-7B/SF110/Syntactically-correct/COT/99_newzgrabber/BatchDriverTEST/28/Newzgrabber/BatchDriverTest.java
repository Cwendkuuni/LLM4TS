import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import Newzgrabber.BatchDriver;
import Newzgrabber.Ini;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTP;
import Newzgrabber.Status;
import Newzgrabber.BatchJob;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private Ini groupsini;
 private Ini batchini;

 @Before
 public void setUp() throws IOException {
 groupsini = new Ini("src/test/resources/Newzgrabber/groups.ini", "Messageids");
 batchini = new Ini("src/test/resources/Newzgrabber/batchfile.ini", "");
 batchDriver = new BatchDriver("src/test/resources/Newzgrabber/groups.ini", "src/test/resources/Newzgrabber/batchfile.ini");
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"group1", "group2", "group3"};
 String[] actualGroups = batchDriver.getGroups();
 Assert.assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJob_ValidGroup() {
 String group = "group1";
 BatchJob expectedBatchJob = new BatchJob("server1", group, "directory1", 1, 10);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 Assert.assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJob_InvalidGroup() {
 String group = "nonExistentGroup";
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 fail("Expected ArrayIndexOutOfBoundsException but got " + actualBatchJob);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructor_NullNewzini() {
 try {
 new BatchDriver(null, "batchfile.ini");
 fail("Expected NullPointerException but got null");
 }
 catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructor_NullBatchfile() {
 try {
 new BatchDriver("Newzgrabber/groups.ini", null);
 fail("Expected NullPointerException but got null");
 }
 catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testBatchDriverConstructor_InvalidNewzini() {
 try {
 new BatchDriver("InvalidNewzini.ini", "batchfile.ini");
 fail("Expected IOException but got null");
 }
 catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testBatchDriverConstructor_InvalidBatchfile() {
 try {
 new BatchDriver("Newzgrabber/groups.ini", "InvalidBatchfile.ini");
 fail("Expected IOException but got null");
 }
 catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testGetSubjectLines_ValidBatchJob() {
 String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 String[] actualSubjectLines = batchDriver.getBatchJob("group1").getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLines_EmptyBatchJob() {
 String[] expectedSubjectLines = {};
 String[] actualSubjectLines = batchDriver.getBatchJob("nonExistentGroup").getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSubjectLines_NullBatchJob() {
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 fail("Expected NullPointerException but got " + actualSubjectLines);
 }
}