package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini = "test.ini";
 private String batchfile = "test.ini";

 @Before
 public void setUp() throws IOException {
 File file = new File(newzini);
 file.createNewFile();
 file = new File(batchfile);
 file.createNewFile();

 Ini groupsini = new Ini(newzini, "Messageids");
 Ini batchgroups = new Ini(batchfile, "");

 groupsini.put("group1", "1");
 groupsini.put("group2", "3");

 batchgroups.put("group1", "server1,Retrieve1,Options1");
 batchgroups.put("group2", "server2,Retrieve2,Options2");

 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @After
 public void tearDown() throws Exception {
 File file = new File(newzini);
 file.delete();
 file = new File(batchfile);
 file.delete();
 }

 @Test
 public void testGetGroups() throws ArrayIndexOutOfBoundsException {
 String[] expectedGroups = {"group1", "group2"};
 String[] actualGroups = batchDriver.getGroups();
 assertArrayEquals(expectedGroups, actualGroups);
 }

 @Test
 public void testGetBatchJob() {
 String group = "group1";
 BatchJob expectedBatchJob = new BatchJob("server1", group, "Retrieve1", 1, 1, "username1", "password1");
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 assertEquals(expectedBatchJob, actualBatchJob);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() {
 String group = "group3";
 batchDriver.getBatchJob(group);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructorWithNullNewzini() {
 new BatchDriver(null, batchfile);
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverConstructorWithNullBatchfile() {
 new BatchDriver(newzini, null);
 }

 @Test(expected = IOException.class)
 public void testBatchDriverConstructorWithInvalidNewziniFile() {
 new BatchDriver("invalidfile.ini", batchfile);
 }

 @Test(expected = IOException.class)
 public void testBatchDriverConstructorWithInvalidBatchfile() {
 new BatchDriver(newzini, "invalidfile.ini");
 }
}