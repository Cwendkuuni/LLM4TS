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
import Newzgrabber.Status;
import Newzgrabber.Newzbatch;
import Newzgrabber.NewsFactory;

public class BatchDriverTest {

 private static final String NEWZINI = "test.ini";
 private static final String BATCHFILE = "test.ini";

 @Test
 public void testBatchDriverConstructor() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNotNull(batchDriver.BatchgroupsHash);
 assertNotNull(batchDriver.BatchgroupsVector);
 assertNotNull(batchDriver.OptionsHash);
 assertNotNull(batchDriver.getGroups());
 }

 @Test
 public void testGetGroups() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 assertNotNull(groups);
 assertFalse(groups.length == 0);
 }

 @Test
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobEmptyGroup() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNull(batchDriver.getBatchJob(""));
 }

 @Test
 public void testGetBatchJobValidGroup() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("testgroup");
 assertNotNull(batchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidIndex() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob[] batchJobs = new BatchJob[batchDriver.getGroups().length];
 System.arraycopy(batchDriver.getBatchJobs(), 0, batchJobs, 0, batchJobs.length);
 batchJob = batchJobs[-1];
 }

 @Test
 public void testGetSubjectLinesNullBatchJob() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNull(subjectLines);
 }

 @Test
 public void testGetSubjectLinesValidBatchJob() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("testgroup");
 assertNotNull(batchJob.getSubjectLines());
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchDriverNullNewzini() {
 new BatchDriver(null, BATCHFILE);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchDriverNullBatchFile() {
 new BatchDriver(NEWZINI, null);
 }

 @Test(expected = IOException.class)
 public void testGetBatchDriverInvalidFile() throws IOException {
 new BatchDriver(new File("test.txt").getAbsolutePath(), BATCHFILE);
 }

 @Test(expected = IOException.class)
 public void testGetBatchDriverInvalidGroupsSection() throws IOException {
 File iniFile = new File("test.ini");
 iniFile.createNewFile();
 Ini ini = new Ini(iniFile.getAbsolutePath(), "Messageids");
 ini.setProperty("Newsgroups", "testgroup[,testgroup2]");
 new BatchDriver(iniFile.getAbsolutePath(), BATCHFILE);
 iniFile.delete();
 }

 @Test(expected = IOException.class)
 public void testGetBatchDriverInvalidBatchSection() throws IOException {
 File iniFile = new File("test.ini");
 iniFile.createNewFile();
 Ini ini = new Ini(iniFile.getAbsolutePath(), "Messageids");
 ini.setProperty("Newsgroups", "testgroup[,testgroup2]");
 ini.setProperty("", "testgroup[,testgroup2]\nRetrieve = test\nOptions = test");
 new BatchDriver(iniFile.getAbsolutePath(), BATCHFILE);
 iniFile.delete();
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobEmptyRetrieveSection() throws IOException {
 File iniFile = new File("test.ini");
 iniFile.createNewFile();
 Ini ini = new Ini(iniFile.getAbsolutePath(), "Messageids");
 ini.setProperty("Newsgroups", "testgroup[,testgroup2]");
 ini.setProperty("", "testgroup[,testgroup2]\nRetrieve = ");
 ini.setProperty("Options", "server=testserver\ndirectory=testdirectory\ntimeout=120\nthreads=2");
 new BatchDriver(iniFile.getAbsolutePath(), BATCHFILE);
 iniFile.delete();
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobEmptyOptionsSection() throws IOException {
 File iniFile = new File("test.ini");
 iniFile.createNewFile();
 Ini ini = new Ini(iniFile.getAbsolutePath(), "Messageids");
 ini.setProperty("Newsgroups", "testgroup[,testgroup2]");
 ini.setProperty("", "testgroup[,testgroup2]\nRetrieve = testgroup\nOptions = ");
 new BatchDriver(iniFile.getAbsolutePath(), BATCHFILE);
 iniFile.delete();
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidThreadsOption() throws IOException {
 File iniFile = new File("test.ini");
 iniFile.createNewFile();
 Ini ini = new Ini(iniFile.getAbsolutePath(), "Messageids");
 ini.setProperty("Newsgroups", "testgroup[,testgroup2]");
 ini.setProperty("", "testgroup[,testgroup2]\nRetrieve = testgroup\nOptions = threads=abc");
 new BatchDriver(iniFile.getAbsolutePath(), BATCHFILE);
 iniFile.delete();
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidTimeoutOption() throws IOException {
 File iniFile = new File("test.ini");
 iniFile.createNewFile();
 Ini ini = new Ini(iniFile.getAbsolutePath(), "Messageids");
 ini.setProperty("Newsgroups", "testgroup[,testgroup2]");
 ini.setProperty("", "testgroup[,testgroup2]\nRetrieve = testgroup\nOptions = timeout=-120");
 new BatchDriver(iniFile.getAbsolutePath(), BATCHFILE);
 iniFile.delete();
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullServerOption() throws IOException {
 File iniFile = new File("test.ini");
 iniFile.createNewFile();
 Ini ini = new Ini(iniFile.getAbsolutePath(), "Messageids");
 ini.setProperty("Newsgroups", "testgroup[,testgroup2]");
 ini.setProperty("", "testgroup[,testgroup2]\nRetrieve = testgroup\nOptions = server=null directory=testdirectory timeout=120 threads=2");
 new BatchDriver(iniFile.getAbsolutePath(), BATCHFILE);
 iniFile.delete();
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullDirectoryOption() throws IOException {
 File iniFile = new File("test.ini");
 iniFile.createNewFile();
 Ini ini = new Ini(iniFile.getAbsolutePath(), "Messageids");
 ini.setProperty("Newsgroups", "testgroup[,testgroup2]");
 ini.setProperty("", "testgroup[,testgroup2]\nRetrieve = testgroup\nOptions = server=testserver directory=null timeout=120 threads=2");
 new BatchDriver(iniFile.getAbsolutePath(), BATCHFILE);
 iniFile.delete();
 }

 @Test
 public void testGetBatchJobValidOptions() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("testgroup");
 assertEquals("testserver", batchJob.getServer());
 assertEquals("testdirectory", batchJob.getDirectory());
 assertEquals(120, batchJob.getTimeout());
 assertEquals(2, batchJob.getThreads());
 }
}