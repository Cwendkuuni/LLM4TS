package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini = "testData/newzini.ini";
 private String batchfile = "testData/batchfile.ini";

 @Before
 public void setUp() throws IOException, NullPointerException {
 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() {
 batchDriver = null;
 }

 @Test
 public void testGetGroups() {
 String[] expectedGroups = {"group1", "group2", "group3"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJob_ValidGroup() {
 String group = "group1";
 BatchJob expectedBatchJob = new BatchJob("server1", group, "/path/to/directory", 1, 10);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertTrue(expectedBatchJob.equals(actualBatchJob));
 }

 @Test
 public void testGetBatchJob_InvalidGroup() {
 String group = "nonexistentgroup";
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertNull(actualBatchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJob_EmptyVector() {
 String group = "";
 batchDriver = new BatchDriver(newzini, batchfile);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertNull(actualBatchJob);
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJob_NullOptionsHash() {
 batchDriver = new BatchDriver(newzini, batchfile) {
 @Override
 public Hashtable OptionsHash() {
 throw new NullPointerException();
 }
 };
 String group = "group1";
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertNull(actualBatchJob);
 }

 @Test(expected = IOException.class)
 public void testGetBatchJob_IOException() throws IOException {
 batchDriver = new BatchDriver(new File("wrongPathToNewzini.ini"), batchfile) {
 @Override
 public Ini getIni(final String name, final String section) throws IOException {
 throw new IOException();
 }
 };
 String group = "group1";
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertNull(actualBatchJob);
 }
}