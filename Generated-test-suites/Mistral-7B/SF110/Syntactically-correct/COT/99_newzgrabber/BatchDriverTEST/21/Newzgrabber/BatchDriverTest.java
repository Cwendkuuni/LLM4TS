package Newzgrabber.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini = "testData/test.ini";
 private String batchfile = "testData/test.ini";

 @Before
 public void setUp() throws IOException {
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 // Test for constructor
 @Test(expected = NullPointerException.class)
 public void testConstructorNullNewzini() {
 batchDriver = new BatchDriver(null, batchfile);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullBatchfile() {
 batchDriver = new BatchDriver(newzini, null);
 }

 @Test(expected = IOException.class)
 public void testConstructorInvalidFiles() {
 batchDriver = new BatchDriver("invalidfile.ini", batchfile);
 }

 // Test for getGroups()
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsEmptyVector() {
 batchDriver = new BatchDriver("", "");
 String[] groups = batchDriver.getGroups();
 assertArrayEquals(new String[0], groups);
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"news.groups.group1", "news.groups.group2", "news.groups.group3"};
 String[] groups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, groups);
 }

 // Test for getBatchJob() with valid group
 @Test
 public void testGetBatchJobValidGroup() {
 String group = "news.groups.group1";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNotNull(batchJob);
 }

 // Test for getBatchJob() with invalid group
 @Test
 public void testGetBatchJobInvalidGroup() {
 String group = "news.groups.nonExistentGroup";
 BatchJob batchJob = batchDriver.getBatchJob(group);
 assertNull(batchJob);
 }

 // Test for getSubjectLines()
 @Test
 public void testGetSubjectLinesEmpty() {
 String[] expectedSubjectLines = new String[0];
 String[] subjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, subjectLines);
 }

 @Test
 public void testGetSubjectLinesNonEmpty() {
 String[] expectedSubjectLines = {"Subject: Test Subject 1", "Subject: Test Subject 2"};
 String[] subjectLines = batchDriver.getSubjectLines();
 assertArrayEquals(expectedSubjectLines, subjectLines);
 }

}