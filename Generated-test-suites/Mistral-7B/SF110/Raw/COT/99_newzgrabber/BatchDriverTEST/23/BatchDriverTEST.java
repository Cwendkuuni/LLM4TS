package Newzgrabber.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Newzbatch;
import Newzgrabber.NewsgroupsHash;
import Newzgrabber.OptionsHash;
import Newzgrabber.TestUtils;

public class BatchDriverTest {

 private static final String NEWZINI = "test.ini";
 private static final String BATCHFILE = "test.ini";

 @Test
 public void testBatchDriverConstruction() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 } catch (NullPointerException | IOException e) {
 fail("BatchDriver construction should not throw exception: " + e.getMessage());
 }
 assertNotNull(batchDriver);
 }

 @Test
 public void testGetGroups() {
 BatchDriver batchDriver = null;
 String[] expectedGroups = new String[]{"group1", "group2", "group3"};
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals("Expected and actual groups array should be equal", expectedGroups, actualGroups);
 } catch (ArrayIndexOutOfBoundsException e) {
 fail("getGroups should not throw ArrayIndexOutOfBoundsException: " + e.getMessage());
 }
 }

 @Test
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = null;
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 BatchDriver batchDriver = null;
 assertNull(batchDriver.getBatchJob("InvalidGroup"));
 }

 @Test
 public void testGetBatchJobValidGroup() {
 BatchDriver batchDriver = null;
 String group = "group1";
 BatchJob expectedBatchJob = new BatchJob("server1", group, "directory1", 1, 10);
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals("Expected and actual BatchJobs should be equal", expectedBatchJob, actualBatchJob);
 } catch (Exception e) {
 fail("getBatchJob should not throw exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetSubjectLinesNullBatchJob() {
 BatchDriver batchDriver = null;
 assertNull(batchDriver.getSubjectLines());
 }

 @Test
 public void testGetSubjectLinesEmptyBatchJob() {
 BatchDriver batchDriver = null;
 String[] expectedSubjectLines = new String[0];
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = new BatchJob("server1", "group1", "directory1", 1, 1);
 batchDriver.setBatchJob(batchJob);
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals("Expected and actual subject lines array should be equal", expectedSubjectLines, actualSubjectLines);
 } catch (Exception e) {
 fail("getSubjectLines should not throw exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetSubjectLinesNonEmptyBatchJob() throws IOException {
 BatchDriver batchDriver = null;
 String[] expectedSubjectLines = new String[]{"Subject 1", "Subject 2", "Subject 3"};
 try {
 File newziniFile = new File(NEWZINI);
 newziniFile.createNewFile();
 Ini newzini = new Ini(NEWZINI, "Messageids");
 newzini.put("Messageids", "group1", "0");
 newzini.put("Messageids", "group1", "10");
 newzini.save(NEWZINI);
 File batchfile = new File(BATCHFILE);
 batchfile.createNewFile();
 Ini batchini = new Ini(BATCHFILE, "");
 batchini.put("Retrieve", "group1", "directory1");
 batchini.save(BATCHFILE);
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob batchJob = new BatchJob("server1", "group1", "directory1", 1, 10);
 batchDriver.setBatchJob(batchJob);
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 assertArrayEquals("Expected and actual subject lines array should be equal", expectedSubjectLines, actualSubjectLines);
 } catch (Exception e) {
 fail("getSubjectLines should not throw exception: " + e.getMessage());
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobInvalidServer() {
 BatchDriver batchDriver = null;
 String group = "group1";
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 fail("getBatchJob should throw IOException when invalid server provided");
 } catch (IOException e) {
 // Expected
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobInvalidDirectory() {
 BatchDriver batchDriver = null;
 String group = "group1";
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.setBatchJob(new BatchJob("server1", group, null, 1, 10));
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 fail("getSubjectLines should throw IOException when invalid directory provided");
 } catch (IOException e) {
 // Expected
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobInvalidFirstId() {
 BatchDriver batchDriver = null;
 String group = "group1";
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.setBatchJob(new BatchJob("server1", group, "directory1", -1, 10));
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 fail("getSubjectLines should throw IOException when invalid first id provided");
 } catch (IOException e) {
 // Expected
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobInvalidLastId() {
 BatchDriver batchDriver = null;
 String group = "group1";
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.setBatchJob(new BatchJob("server1", group, "directory1", 1, -1));
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 fail("getSubjectLines should throw IOException when invalid last id provided");
 } catch (IOException e) {
 // Expected
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobTimeout() {
 BatchDriver batchDriver = null;
 String group = "group1";
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.setBatchJob(new BatchJob("server1", group, "directory1", 1, 10));
 Newzbatch.TIMEOUT = true;
 Newzbatch.TIMEOUTMAX = 1;
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 fail("getSubjectLines should throw IOException when timeout occurs");
 } catch (IOException e) {
 // Expected
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullOptionsHash() {
 BatchDriver batchDriver = null;
 String group = "group1";
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.setBatchJob(new BatchJob("server1", group, "directory1", 1, 10));
 batchDriver.setOptionsHash(null);
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 fail("getSubjectLines should throw NullPointerException when options hash is null");
 } catch (NullPointerException e) {
 // Expected
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidThreads() {
 BatchDriver batchDriver = null;
 String group = "group1";
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.setBatchJob(new BatchJob("server1", group, "directory1", 1, 10));
 batchDriver.setOptionsHash(new OptionsHash());
 batchDriver.getOptionsHash().put("threads", "invalidThreads");
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 fail("getSubjectLines should throw NumberFormatException when invalid threads provided");
 } catch (NumberFormatException e) {
 // Expected
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testGetBatchJobInvalidTimeout() {
 BatchDriver batchDriver = null;
 String group = "group1";
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.setBatchJob(new BatchJob("server1", group, "directory1", 1, 10));
 batchDriver.setOptionsHash(new OptionsHash());
 batchDriver.getOptionsHash().put("timeout", "invalidTimeout");
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 fail("getSubjectLines should throw NumberFormatException when invalid timeout provided");
 } catch (NumberFormatException e) {
 // Expected
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }

 @Test
 public void testGetBatchJobValidOptions() {
 BatchDriver batchDriver = null;
 String group = "group1";
 int threads = 3;
 int timeout = 100;
 try {
 batchDriver = new BatchDriver(NEWZINI, BATCHFILE);
 batchDriver.setBatchJob(new BatchJob("server1", group, "directory1", 1, 10));
 batchDriver.setOptionsHash(new OptionsHash());
 batchDriver.getOptionsHash().put("threads", String.valueOf(threads));
 batchDriver.getOptionsHash().put("timeout", String.valueOf(timeout));
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 fail("getSubjectLines should throw exception when valid options provided");
 } catch (Exception e) {
 // Should not reach here
 } finally {
 TestUtils.deleteFiles(NEWZINI, BATCHFILE);
 }
 }
}