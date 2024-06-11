package Newzgrabber.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;
import Newzgrabber.Newzbatch;
import Newzgrabber.NewsFactory;
import Newzgrabber.Options;
import Newzgrabber.Status;

public class BatchDriverTest {

 private static final String NEWZINI = "test.ini";
 private static final String BATCHFILE = "test.ini";

 @Test
 public void testBatchDriverConstruction() throws IOException, NullPointerException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNotNull(batchDriver.BatchgroupsVector);
 assertNotNull(batchDriver.NewsgroupsHash);
 assertNotNull(batchDriver.BatchgroupsHash);
 assertNotNull(batchDriver.BatchretrieveHash);
 assertNotNull(batchDriver.OptionsHash);
 }

 @Test
 public void testGetGroups() throws ArrayIndexOutOfBoundsException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 assertNotNull(groups);
 assertEquals(2, groups.length);
 }

 @Test
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobEmptyGroupVector() throws IOException, NullPointerException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 Ini batchgroups = new Ini(BATCHFILE, "");
 batchgroups.deleteSection("Newsgroups");
 batchDriver.BatchgroupsVector = new Vector();
 assertNull(batchDriver.getBatchJob("emptygroup"));
 }

 @Test
 public void testGetBatchJobValidGroup() throws IOException, NullPointerException, Exception {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 NewsFactory nf = Newzgrabber.nf;
 NNTP news = nf.getNewsSocket("testserver.com");
 Ini batchgroups = new Ini(BATCHFILE, "");
 String server = "testserver.com";
 String group = "test.group";
 String directory = "/test/directory";
 int firstid = 1;
 int lastid = 10;
 String username = "testuser";
 String password = "testpassword";
 batchgroups.setProperty("Newsgroups." + group, server + ":" + directory);
 batchgroups.setProperty("Retrieve." + group, "true");
 batchgroups.setProperty("Options.username", username);
 batchgroups.setProperty("Options.password", password);
 batchDriver.BatchgroupsVector.addElement(new String[] { group, server, directory, Integer.toString(firstid), Integer.toString(lastid) });
 batchDriver.BatchgroupsHash = batchgroups.readIni();
 BatchJob bj = batchDriver.getBatchJob(group);
 assertNotNull(bj);
 assertEquals(server, bj.getServer());
 assertEquals(group, bj.getGroup());
 assertEquals(directory, bj.getDirectory());
 assertEquals(firstid, bj.getFirstid());
 assertEquals(lastid, bj.getLastid());
 assertEquals(username, bj.getUsername());
 assertEquals(password, bj.getPassword());
 news.quit();
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidIndex() throws IOException, NullPointerException, Exception {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 NewsFactory nf = Newzgrabber.nf;
 NNTP news = nf.getNewsSocket("testserver.com");
 Ini batchgroups = new Ini(BATCHFILE, "");
 String server = "testserver.com";
 String group1 = "test.group1";
 String group2 = "test.group2";
 String directory = "/test/directory";
 int firstid1 = 1;
 int lastid1 = 10;
 int firstid2 = 11;
 int lastid2 = 20;
 String username = "testuser";
 String password = "testpassword";
 batchgroups.setProperty("Newsgroups." + group1, server + ":" + directory);
 batchgroups.setProperty("Retrieve." + group1, "true");
 batchgroups.setProperty("Options.username", username);
 batchgroups.setProperty("Options.password", password);
 batchgroups.setProperty("Newsgroups." + group2, server + ":" + directory);
 batchgroups.setProperty("Retrieve." + group2, "true");
 batchgroups.setProperty("Options.username", username);
 batchgroups.setProperty("Options.password", password);
 batchDriver.BatchgroupsVector.addElement(new String[] { group1, server, directory, Integer.toString(firstid1), Integer.toString(lastid1) });
 batchDriver.BatchgroupsVector.addElement(new String[] { group2, server, directory, Integer.toString(firstid2), Integer.toString(lastid2) });
 batchDriver.BatchgroupsHash = batchgroups.readIni();
 BatchJob bj1 = batchDriver.getBatchJob(group1);
 BatchJob bj2 = batchDriver.getBatchJob(2);
 news.quit();
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobInvalidFile() throws IOException, NullPointerException, Exception {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 NewsFactory nf = Newzgrabber.nf;
 NNTP news = nf.getNewsSocket("testserver.com");
 Ini batchgroups = new Ini(BATCHFILE, "");
 String server = "testserver.com";
 String group = "test.group";
 String directory = "/test/directory";
 int firstid = 1;
 int lastid = 10;
 batchgroups.setProperty("Newsgroups." + group, server + ":" + directory);
 batchgroups.setProperty("Retrieve." + group, "true");
 batchDriver.BatchgroupsVector.addElement(new String[] { group, server, directory, Integer.toString(firstid), Integer.toString(lastid) });
 batchDriver.BatchgroupsHash = batchgroups.readIni();
 batchDriver.BatchretrieveHash = new Hashtable();
 batchDriver.OptionsHash = new Hashtable();
 File batchfile = new File("nonexistentfile.ini");
 assertNull(batchDriver.getBatchJob(group));
 news.quit();
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullBatchgroupsHash() throws IOException, NullPointerException, Exception {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 NewsFactory nf = Newzgrabber.nf;
 NNTP news = nf.getNewsSocket("testserver.com");
 Ini batchgroups = new Ini(BATCHFILE, "");
 String server = "testserver.com";
 String group = "test.group";
 String directory = "/test/directory";
 int firstid = 1;
 int lastid = 10;
 batchgroups.setProperty("Newsgroups." + group, server + ":" + directory);
 batchgroups.setProperty("Retrieve." + group, "true");
 batchDriver.BatchgroupsVector.addElement(new String[] { group, server, directory, Integer.toString(firstid), Integer.toString(lastid) });
 batchDriver.BatchgroupsHash = null;
 assertNull(batchDriver.getBatchJob(group));
 news.quit();
 }

 @Test
 public void testGetSubjectLines() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] subjectlines = batchDriver.getSubjectLines();
 assertNotNull(subjectlines);
 assertEquals(3, subjectlines.length);
 }
}