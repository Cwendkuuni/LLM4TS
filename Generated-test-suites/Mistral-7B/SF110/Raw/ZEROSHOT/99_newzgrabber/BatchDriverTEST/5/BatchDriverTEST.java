package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Test;

public class BatchDriverTest {

 private static final String NEWZINI = "test.ini";
 private static final String BATCHFILE = "test.ini";

 @Test
 public void testBatchDriverConstruction() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 assertNotNull(batchDriver);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructionNullNewzini() {
 BatchDriver batchDriver = new BatchDriver(null, BATCHFILE);
 fail("Should have thrown NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructionNullBatchfile() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, null);
 fail("Should have thrown NullPointerException");
 }

 @Test
 public void testGetGroups() throws ArrayIndexOutOfBoundsException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] groups = batchDriver.getGroups();
 assertEquals(3, groups.length);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsEmptyVector() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.BatchgroupsVector.clear();
 String[] groups = batchDriver.getGroups();
 fail("Should have thrown ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testGetBatchJob() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String group = "test.group";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob(null);
 fail("Should have thrown ArrayIndexOutOfBoundsException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullServer() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertNull(batchJob.getServer());
 }

 @Test
 public void testGetBatchJobServer() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertNotNull(batchJob.getServer());
 assertEquals("test.server", batchJob.getServer());
 }

 @Test
 public void testGetBatchJobDirectory() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertNotNull(batchJob.getDirectory());
 assertEquals("test.directory", batchJob.getDirectory());
 }

 @Test
 public void testGetBatchJobFirstId() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertEquals(1, batchJob.getFirstId());
 }

 @Test
 public void testGetBatchJobLastId() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertEquals(10, batchJob.getLastId());
 }

 @Test
 public void testGetBatchJobUsername() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertNull(batchJob.getUsername());
 }

 @Test
 public void testGetBatchJobPassword() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = batchDriver.getBatchJob("test.group");
 assertNull(batchJob.getPassword());
 }

 @Test
 public void testGetSubjectLines() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals(5, subjectLines.length);
 }

 @Test
 public void testGetSubjectLinesEmpty() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.subjectlines = new String[0];
 String[] subjectLines = batchDriver.getSubjectLines();
 assertEquals(0, subjectLines.length);
 }

 @Test
 public void testGetSubjectLinesNull() {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.subjectlines = null;
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNull(subjectLines);
 }

 @Test
 public void testNewsgroupsHash() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 Hashtable<String, Integer> newsgroupsHash = batchDriver.NewsgroupsHash;
 File iniFile = new File(NEWZINI);
 Ini ini = new Ini(iniFile, "Messageids");
 Hashtable<String, Integer> expectedNewsgroupsHash = ini.readIni();
 assertEquals(expectedNewsgroupsHash, newsgroupsHash);
 }

 @Test
 public void testBatchgroupsHash() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 Hashtable<String, String> batchgroupsHash = batchDriver.BatchgroupsHash;
 File iniFile = new File(BATCHFILE);
 Ini ini = new Ini(iniFile, "");
 Hashtable<String, String> expectedBatchgroupsHash = ini.readIni();
 assertEquals(expectedBatchgroupsHash, batchgroupsHash);
 }

 @Test
 public void testBatchretrieveHash() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 Hashtable<String, String> batchretrieveHash = batchDriver.BatchretrieveHash;
 File iniFile = new File(BATCHFILE);
 Ini ini = new Ini(iniFile, "");
 Hashtable<String, String> expectedBatchretrieveHash = ini.getProperty("Retrieve");
 assertEquals(expectedBatchretrieveHash, batchretrieveHash);
 }

 @Test
 public void testOptionsHash() throws IOException {
 BatchDriver batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 Hashtable<String, String> optionsHash = batchDriver.OptionsHash;
 File iniFile = new File(BATCHFILE);
 Ini ini = new Ini(iniFile, "");
 Hashtable<String, String> expectedOptionsHash = ini.getProperty("Options");
 assertEquals(expectedOptionsHash, optionsHash);
 }
}