package Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini = "test.ini";
 private String batchfile = "test.ini";

 @Before
 public void setUp() throws IOException {
 File iniFile = new File(newzini);
 Ini.load(iniFile);
 Ini groupsIni = new Ini(newzini, "Messageids");
 groupsIni.add("Newsgroups", "news.google.com.comp.sys.mac.osx", "1");
 groupsIni.add("Newsgroups", "news.google.com.comp.sys.mac.osx.misc", "2");
 Ini batchIni = new Ini(batchfile, "");
 batchIni.add("Retrieve", "news.google.com.comp.sys.mac.osx");
 batchIni.add("Retrieve", "news.google.com.comp.sys.mac.osx.misc");
 batchIni.add("Options", "server=news.google.com");
 batchIni.add("Options", "username=testuser");
 batchIni.add("Options", "password=testpassword");
 batchIni.add("Options", "threads=2");
 batchIni.add("Options", "timeout=10000");
 this.batchDriver = new BatchDriver(newzini, batchfile);
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"news.google.com.comp.sys.mac.osx", "news.google.com.comp.sys.mac.osx.misc"};
 String[] groups = this.batchDriver.getGroups();
 assertArrayEquals(expectedGroups, groups);
 }

 @Test
 public void testGetNullBatchJob() {
 String group = "non-existent-group";
 BatchJob batchJob = this.batchDriver.getBatchJob(group);
 assertNull(batchJob);
 }

 @Test
 public void testGetBatchJob() {
 String group = "news.google.com.comp.sys.mac.osx";
 BatchJob batchJob = this.batchDriver.getBatchJob(group);
 String server = batchJob.getServer();
 String directory = batchJob.getDirectory();
 int firstId = batchJob.getFirstId();
 int lastId = batchJob.getLastId();
 String username = batchJob.getUsername();
 String password = batchJob.getPassword();

 assertTrue(server.equals("news.google.com"));
 assertTrue(directory.isEmpty());
 assertTrue(firstId == 1);
 assertTrue(lastId > firstId);
 assertTrue(username.equals("testuser"));
 assertTrue(password.equals("testpassword"));
 }

 @Test
 public void testGetSubjectLines() {
 String[] expectedSubjectLines = {"Subject: Test subject line 1", "Subject: Test subject line 2"};
 String[] subjectLines = this.batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, subjectLines);
 }
}