package Newzgrabber;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.ini4j.*;
import org.junit.Test;

public class BatchDriverTest {

 private final String newzini = "test.ini";
 private final String batchfile = "test.ini";

 @Test
 public void testBatchDriverInstance() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 } catch (NullPointerException | IOException e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver instance");
 }
 assertNotNull(batchDriver);
 }

 @Test
 public void testGetGroups() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 } catch (NullPointerException | IOException e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver instance");
 }
 String[] groups = batchDriver.getGroups();
 assertNotNull(groups);
 assertTrue(groups.length > 0);
 }

 @Test
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 } catch (NullPointerException | IOException e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver instance");
 }
 assertNull(batchDriver.getBatchJob(null));
 }

 @Test
 public void testGetBatchJobEmptyGroup() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 } catch (NullPointerException | IOException e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver instance");
 }
 String group = "";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNull(batchJob);
 }

 @Test
 public void testGetBatchJobValidGroup() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 } catch (NullPointerException | IOException e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver instance");
 }
 String group = "testgroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobInvalidIndex() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 } catch (NullPointerException | IOException e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver instance");
 }
 String group = "testgroup";
 BatchJob[] batchJobs = batchDriver.getGroups();
 BatchJob batchJob = batchDriver.getBatchJob(group);
 int index = batchJobs.length;
 batchJob = batchDriver.getBatchJob(index);
 }

 @Test
 public void testGetSubjectLines() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 } catch (NullPointerException | IOException e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver instance");
 }
 BatchJob batchJob = batchDriver.getBatchJob("testgroup");
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertTrue(subjectLines.length > 0);
 assertEquals(subjectLines.length, batchJob.getNumMessages());
 }

 @Test
 public void testGetSubjectLinesEmptyBatch() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 } catch (NullPointerException | IOException e) {
 e.printStackTrace();
 fail("Failed to initialize BatchDriver instance");
 }
 BatchJob batchJob = batchDriver.getBatchJob("testgroup");
 batchJob.setNumMessages(0);
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNull(subjectLines);
 }
}