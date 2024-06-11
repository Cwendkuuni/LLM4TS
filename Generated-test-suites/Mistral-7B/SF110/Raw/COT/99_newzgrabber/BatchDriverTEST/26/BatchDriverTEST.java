import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;

@RunWith(JUnit4.class)
public class BatchDriverTest {

 private BatchDriver batchDriver;

 @Test
 public void testBatchDriverConstructor() throws IOException, NullPointerException {
 final String newzini = "test.ini";
 final String batchfile = "test.ini";
 batchDriver = new BatchDriver(newzini, batchfile);
 Assert.assertNotNull(batchDriver.getBatchgroupsVector());
 }

 @Test
 public void testGetGroups() {
 try {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 String[] groups = batchDriver.getGroups();
 Assert.assertEquals("Size of groups array should match vector size", batchDriver.getBatchgroupsVector().size(), groups.length);
 for (int i = 0; i < groups.length; i++) {
 Assert.assertNotNull("Group at index " + i + " should not be null", groups[i]);
 }
 } catch (IOException | NullPointerException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testGetBatchJobNullGroup() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 Assert.assertNull("getBatchJob should return null when group is null", batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 Assert.assertNull("getBatchJob should return null when group does not exist", batchDriver.getBatchJob("InvalidGroup"));
 }

 @Test
 public void testGetBatchJobValidGroup() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 String group = "ValidGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 Assert.assertNotNull("getBatchJob should not return null when group is valid", batchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetSubjectLinesArrayIndexOutOfBounds() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 batchDriver.getSubjectLines();
 batchDriver.getSubjectLines();
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 String[] subjectLines = batchDriver.getSubjectLines();
 Assert.assertNotNull("getSubjectLines should not return null", subjectLines);
 Assert.assertEquals("Size of subjectLines array should be zero when there are no subjects", 0, subjectLines.length);
 }

 @Test
 public void testGetSubjectLinesWithSubjects() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 String[] subjectLines = batchDriver.getSubjectLines();
 Assert.assertNotNull("getSubjectLines should not return null", subjectLines);
 Assert.assertArrayNotEquals("Size of subjectLines array should be greater than zero when there are subjects", 0, subjectLines.length);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullOptionsHash() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 batchDriver.OptionsHash = null;
 String group = "ValidGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobIOException() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 batchDriver.BatchretrieveHash = null;
 String group = "ValidGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidNumberOfThreads() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 String options = "threads=abc";
 batchDriver.OptionsHash = new Hashtable<>();
 batchDriver.OptionsHash.put("threads", options);
 String group = "ValidGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidTimeout() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 String options = "timeout=-5";
 batchDriver.OptionsHash = new Hashtable<>();
 batchDriver.OptionsHash.put("timeout", options);
 String group = "ValidGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBatchJobNegativeNumberOfThreads() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 String options = "threads=-5";
 batchDriver.OptionsHash = new Hashtable<>();
 batchDriver.OptionsHash.put("threads", options);
 String group = "ValidGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBatchJobNegativeTimeout() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 String options = "timeout=-5";
 batchDriver.OptionsHash = new Hashtable<>();
 batchDriver.OptionsHash.put("timeout", options);
 String group = "ValidGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test
 public void testGetBatchJobValidOptions() {
 batchDriver = new BatchDriver("test.ini", "test.ini");
 String options = "server=test.nntp.com&directory=test&username=test&password=test&threads=5&timeout=10";
 batchDriver.OptionsHash = new Hashtable<>();
 batchDriver.OptionsHash.put("server", "test.nntp.com");
 batchDriver.OptionsHash.put("directory", "test");
 batchDriver.OptionsHash.put("username", "test");
 batchDriver.OptionsHash.put("password", "test");
 batchDriver.OptionsHash.put("threads", "5");
 batchDriver.OptionsHash.put("timeout", "10");
 String group = "ValidGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 Assert.assertNotNull("getBatchJob should not return null when options are valid", batchJob);
 }

}