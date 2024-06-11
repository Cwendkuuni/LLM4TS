package Newzgrabber;

import static org.junit.Assert.*;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BatchDriverTest {

 Ini groupsini;
 Ini batchini;
 Hashtable<String, String> NewsgroupsHash;
 Hashtable<String, String> BatchgroupsHash;
 Hashtable<String, String> BatchretrieveHash;
 Hashtable<String, String> OptionsHash;
 Vector<String[]> BatchgroupsVector;
 String[] subjectlines;
 String newzini = "test.ini";
 String batchfile = "test.ini";

 @Test
 public void testBatchDriverInitialization() {
 try {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 assertNotNull(bd.getGroups());
 assertNotNull(bd.getBatchJob("test"));
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver");
 }
 }

 @Test
 public void testGetGroups() {
 String[] groups = {"group1", "group2", "group3"};
 groupsini = new Ini(newzini, "Messageids");
 batchini = new Ini(batchfile, "");
 batchini.setProperty("Newsgroups", groupsini.toString());
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 assertArrayEquals(groups, bd.getGroups());
 }

 @Test
 public void testGetBatchJob() {
 String server = "test.server.com";
 String group = "group1";
 String directory = "/path/to/directory";
 int firstid = 1;
 int lastid = 10;
 String username = "test";
 String password = "test";
 Ini batchgroups = new Ini(batchfile, "");
 batchgroups.setProperty("Server", server);
 batchgroups.setProperty(group, directory);
 batchgroups.setProperty("Retrieve.username", username);
 batchgroups.setProperty("Retrieve.password", password);
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 BatchJob bj = bd.getBatchJob(group);
 assertNotNull(bj);
 assertEquals(server, bj.getServer());
 assertEquals(group, bj.getGroup());
 assertEquals(directory, bj.getDirectory());
 assertEquals(firstid, bj.getFirstId());
 assertEquals(lastid, bj.getLastId());
 assertNull(bj.getUsername());
 assertNull(bj.getPassword());
 }

 @Test
 public void testGetBatchJobWithUsernameAndPassword() {
 String server = "test.server.com";
 String group = "group1";
 String directory = "/path/to/directory";
 int firstid = 1;
 int lastid = 10;
 String username = "test";
 String password = "test";
 Ini batchgroups = new Ini(batchfile, "");
 batchgroups.setProperty("Server", server);
 batchgroups.setProperty(group, directory);
 batchgroups.setProperty("Options.username", username);
 batchgroups.setProperty("Options.password", password);
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 BatchJob bj = bd.getBatchJob(group);
 assertNotNull(bj);
 assertEquals(server, bj.getServer());
 assertEquals(group, bj.getGroup());
 assertEquals(directory, bj.getDirectory());
 assertEquals(firstid, bj.getFirstId());
 assertEquals(lastid, bj.getLastId());
 assertEquals(username, bj.getUsername().trim());
 assertEquals(password, bj.getPassword().trim());
 }

 @Test
 public void testGetSubjectLines() {
 String[] expectedSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 String[] actualSubjectLines = {"Subject 1", "Subject 2", "Subject 3"};
 try {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 bd.getBatchJob("test");
 NewsFactory nf = Newzgrabber.nf;
 NNTP news = nf.getNewsSocket("test.server.com");
 String cmd = "xpat Subject *";
 news.setTimeout(1000);
 String response = news.genericCommand(cmd);
 String[] responseLines = response.split("\n");
 subjectlines = new String[responseLines.length - 1];
 System.arraycopy(responseLines, 1, subjectlines, 0, subjectlines.length);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to get subject lines");
 }
 assertArrayEquals(expectedSubjectLines, subjectlines);
 }

 @Test
 public void testGetSubjectLinesWithEmptyBatchFile() {
 String[] expectedSubjectLines = {};
 String[] actualSubjectLines = {};
 try {
 BatchDriver bd = new BatchDriver(newzini, new File("").getAbsolutePath() + File.separator + "empty.ini");
 NewsFactory nf = Newzgrabber.nf;
 NNTP news = nf.getNewsSocket("test.server.com");
 String cmd = "xpat Subject *";
 news.setTimeout(1000);
 String response = news.genericCommand(cmd);
 String[] responseLines = response.split("\n");
 subjectlines = new String[responseLines.length - 1];
 System.arraycopy(responseLines, 1, subjectlines, 0, subjectlines.length);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to get subject lines with empty batch file");
 }
 assertArrayEquals(expectedSubjectLines, subjectlines);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsWithEmptyVector() {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 try {
 bd.getGroups();
 fail("Expected ArrayIndexOutOfBoundsException");
 } catch (ArrayIndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobWithNullGroup() {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 try {
 bd.getBatchJob(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobWithInvalidBatchFile() {
 BatchDriver bd = new BatchDriver(newzini, "invalid.ini");
 try {
 bd.getBatchJob("test");
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobWithNullBatchini() {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 Ini batchgroups = null;
 try {
 bd.getBatchJob("test", batchgroups);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBatchJobWithEmptyGroupName() {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 Ini batchgroups = new Ini(batchfile, "");
 try {
 bd.getBatchJob("", batchgroups);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobWithNullServer() {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 BatchJob bj = new BatchJob("", "", "", 0, 0);
 try {
 bj.setServer(null);
 bd.getBatchJob("test", bj);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBatchJobWithNullDirectory() {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 BatchJob bj = new BatchJob("test", null, "", 0, 0);
 try {
 bj.setDirectory(null);
 bd.getBatchJob("test", bj);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobWithInvalidFirstId() {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 BatchJob bj = new BatchJob("test", "", "", 0, 0);
 try {
 bj.setFirstId("abc");
 bd.getBatchJob("test", bj);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobWithInvalidLastId() {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 BatchJob bj = new BatchJob("test", "", "", 0, 0);
 try {
 bj.setLastId("abc");
 bd.getBatchJob("test", bj);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }
}