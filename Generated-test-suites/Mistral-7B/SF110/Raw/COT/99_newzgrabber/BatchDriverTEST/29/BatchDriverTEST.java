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

public class BatchDriverTest {

 private final String newzini = "test.ini";
 private final String batchfile = "test.ini";

 @Test
 public void testBatchDriverConstruction() {
 BatchDriver batchDriver = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 assertTrue(batchDriver != null);
 }
 catch (NullPointerException | IOException e) {
 fail("BatchDriver construction failed: " + e.getMessage());
 }
 }

 @Test
 public void testGetGroups() {
 BatchDriver batchDriver = null;
 String[] groups = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 groups = batchDriver.getGroups();
 assertEquals(3, groups.length);
 }
 catch (ArrayIndexOutOfBoundsException e) {
 fail("getGroups() failed: " + e.getMessage());
 }
 }

 @Test
 public void testGetBatchJobNullGroup() {
 BatchDriver batchDriver = null;
 String group = null;
 BatchJob batchJob = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchJob = batchDriver.getBatchJob(group);
 assertNull(batchJob);
 }
 catch (NullPointerException | IOException e) {
 fail("getBatchJob() failed for null group: " + e.getMessage());
 }
 }

 @Test
 public void testGetBatchJobValidGroup() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 BatchJob batchJob = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 assertEquals(group, batchJob.getGroupName());
 }
 catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
 fail("getBatchJob() failed for valid group: " + e.getMessage());
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobEmptyVector() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchDriver.BatchgroupsVector.clear();
 batchDriver.BatchgroupsVector.add(new String[]{"test.group1", "test.server1", "test.directory1"});
 batchDriver.BatchgroupsVector.add(new String[]{"test.group2", "test.server2", "test.directory2"});
 batchDriver.BatchgroupsVector.remove(0);
 batchJob = batchDriver.getBatchJob(group);
 fail("getBatchJob() should have thrown ArrayIndexOutOfBoundsException for empty vector");
 }
 catch (NullPointerException | IOException e) {
 fail("getBatchJob() failed for empty vector: " + e.getMessage());
 }
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 BatchDriver batchDriver = null;
 String group = "test.nonExistentGroup";
 BatchJob batchJob = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchJob = batchDriver.getBatchJob(group);
 assertNull(batchJob);
 }
 catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGetSubjectLinesNullBatchJob() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 BatchJob batchJob = null;
 String[] subjectLines = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchJob = batchDriver.getBatchJob(group);
 subjectLines = batchJob.getSubjectLines();
 batchJob = null;
 assertNull(subjectLines);
 }
 catch (NullPointerException | IOException e) {
 fail("getSubjectLines() failed for null BatchJob: " + e.getMessage());
 }
 }

 @Test
 public void testGetSubjectLinesValidBatchJob() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 BatchJob batchJob = null;
 String[] subjectLines = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchJob = batchDriver.getBatchJob(group);
 subjectLines = batchJob.getSubjectLines();
 assertEquals(3, subjectLines.length);
 }
 catch (NullPointerException | IOException | ArrayIndexOutOfBoundsException e) {
 fail("getSubjectLines() failed for valid BatchJob: " + e.getMessage());
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetSubjectLinesEmptyArray() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 BatchJob batchJob = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchJob = batchDriver.getBatchJob(group);
 batchJob.subjectlines = new String[0];
 subjectLines = batchJob.getSubjectLines();
 fail("getSubjectLines() should have thrown ArrayIndexOutOfBoundsException for empty array");
 }
 catch (NullPointerException | IOException e) {
 fail("getSubjectLines() failed for empty array: " + e.getMessage());
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetSubjectLinesOutOfBounds() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 BatchJob batchJob = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchJob = batchDriver.getBatchJob(group);
 batchJob.subjectlines = new String[]{"Subject 1", "Subject 2", "Subject 3", "Subject 4"};
 subjectLines = batchJob.getSubjectLines();
 subjectLines = new String[3];
 System.arraycopy(batchJob.subjectlines, 3, subjectLines, 0, 3);
 fail("getSubjectLines() should have thrown ArrayIndexOutOfBoundsException for out-of-bounds index");
 }
 catch (NullPointerException | IOException e) {
 fail("getSubjectLines() failed for out-of-bounds index: " + e.getMessage());
 }
 }

 @Test
 public void testGetSubjectLinesNullSubjectLines() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 BatchJob batchJob = null;
 try {
 batchDriver = new BatchDriver(newzini, batchfile);
 batchJob = batchDriver.getBatchJob(group);
 batchJob.subjectlines = null;
 String[] subjectLines = batchJob.getSubjectLines();
 assertNull(subjectLines);
 }
 catch (NullPointerException | IOException e) {
 fail("getSubjectLines() failed for null subjectLines: " + e.getMessage());
 }
 }

 @Test
 public void testGetSubjectLinesEmptyIniFile() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 File newziniFile = new File(newzini);
 try {
 newziniFile.createNewFile();
 batchDriver = new BatchDriver(newzini, batchfile);
 batchDriver.getBatchJob(group);
 fail("getSubjectLines() should have thrown IOException for empty IniFile");
 }
 catch (NullPointerException | IOException e) {
 // expected
 }
 finally {
 newziniFile.delete();
 }
 }

 @Test
 public void testGetSubjectLinesMissingGroupIni() {
 BatchDriver batchDriver = null;
 String group = "test.nonExistentGroup";
 File newziniFile = new File(newzini);
 try {
 newziniFile.createNewFile();
 batchDriver = new BatchDriver(newzini, batchfile);
 batchDriver.writeIni(newzini, "Messageids", "test.group", "1");
 batchDriver.getBatchJob(group);
 fail("getSubjectLines() should have thrown IOException for missing group Ini");
 }
 catch (NullPointerException | IOException e) {
 // expected
 }
 finally {
 newziniFile.delete();
 }
 }

 @Test
 public void testGetSubjectLinesMissingRetrieveIni() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 File newziniFile = new File(newzini);
 try {
 newziniFile.createNewFile();
 batchDriver = new BatchDriver(newzini, batchfile);
 batchDriver.writeIni(newzini, "Messageids", "test.group", "1");
 batchDriver.writeIni(newzini, "Retrieve", "test.group", "");
 batchDriver.getBatchJob(group);
 fail("getSubjectLines() should have thrown IOException for missing retrieve Ini");
 }
 catch (NullPointerException | IOException e) {
 // expected
 }
 finally {
 newziniFile.delete();
 }
 }

 @Test
 public void testGetSubjectLinesMissingOptionsIni() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 File newziniFile = new File(newzini);
 try {
 newziniFile.createNewFile();
 batchDriver = new BatchDriver(newzini, batchfile);
 batchDriver.writeIni(newzini, "Messageids", "test.group", "1");
 batchDriver.writeIni(newzini, "Retrieve", "test.group", "server,directory");
 batchDriver.getBatchJob(group);
 fail("getSubjectLines() should have thrown IOException for missing options Ini");
 }
 catch (NullPointerException | IOException e) {
 // expected
 }
 finally {
 newziniFile.delete();
 }
 }

 @Test
 public void testGetSubjectLinesInvalidServer() {
 BatchDriver batchDriver = null;
 String group = "test.group";
 String invalidServer = "invalidServer";
 File newziniFile = new File(newzini);
 try {
 newziniFile.createNewFile();
 batchDriver = new BatchDriver(newzini, batchfile);
 batchDriver.writeIni(newzini, "Messageids", "test.group", "1");
 batchDriver.writeIni(newzini, "Retrieve", "test.group", "server,directory");
 batchDriver.writeIni(newzini, "Options", "test.group", "server=" + invalidServer);
 BatchJob batchJob = batchDriver.getBatchJob(group);
 fail("getSubjectLines() should have thrown IOException for invalid server");
 }
 catch (NullPointerException | IOException e) {
 // expected
 }
 finally {
 newziniFile.delete();
 }
 }
}