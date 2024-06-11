import org.junit.Assert;
import org.junit.Test;
import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.Status;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTP;

public class BatchDriverTest {

 private String newzini = "test.ini";
 private String batchfile = "test.ini";

 @Test
 public void testGetGroups() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String[] expectedGroups = {"group1", "group2", "group3"};
 String[] actualGroups = batchDriver.getGroups();
 Assert.assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJobWithValidGroup() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 Assert.assertNotNull(batchJob);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobWithNullGroup() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = null;
 BatchJob batchJob = batchDriver.getBatchJob(group);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroupIndex() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "group1";
 int index = batchDriver.BatchgroupsVector.size() + 1;
 BatchJob batchJob = batchDriver.getBatchJob(group + ":" + index);
 }

 @Test
 public void testGetBatchJobWithEmptyOptions() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 Assert.assertNull(batchJob.getUsername());
 Assert.assertNull(batchJob.getPassword());
 }

 @Test
 public void testGetBatchJobWithValidOptions() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "group1";
 batchDriver.OptionsHash.put("server", "test.server.com");
 batchDriver.OptionsHash.put("directory", "/test/directory");
 batchDriver.OptionsHash.put("username", "testuser");
 batchDriver.OptionsHash.put("password", "testpassword");
 BatchJob batchJob = batchDriver.getBatchJob(group);
 Assert.assertEquals("test.server.com", batchJob.getServer());
 Assert.assertEquals("/test/directory", batchJob.getDirectory());
 Assert.assertEquals("testuser", batchJob.getUsername());
 Assert.assertEquals("testpassword", batchJob.getPassword());
 }

 @Test
 public void testGetSubjectLinesWithValidBatchJob() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 NewsFactory newsFactory = Newzgrabber.nf;
 NNTP news = null;
 try {
 news = newsFactory.getNewsSocket(batchJob.getServer());
 } catch (IOException e) {
 e.printStackTrace();
 }
 batchDriver.subjectlines = news.genericCommand("xpat Subject " + batchJob.getFirstId() + "-" + batchJob.getLastId() + " *");
 Assert.assertNotNull(batchDriver.subjectlines);
 }

 @Test(expected = IOException.class)
 public void testGetSubjectLinesWithNullNews() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 NewsFactory newsFactory = Newzgrabber.nf;
 NNTP news = null;
 batchDriver.subjectlines = news.genericCommand("xpat Subject " + batchJob.getFirstId() + "-" + batchJob.getLastId() + " *");
 }

 @Test
 public void testGetSubjectLinesWithEmptySubjects() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 String[] expectedSubjectLines = {};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 @Test
 public void testGetSubjectLinesWithValidSubjects() {
 BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
 String group = "group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 batchDriver.subjectlines = new String[]{"Subject 1", "Subject 2", "Subject 3"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }
}