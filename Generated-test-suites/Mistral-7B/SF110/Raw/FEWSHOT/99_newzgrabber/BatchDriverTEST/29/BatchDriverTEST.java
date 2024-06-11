package Newzgrabber;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;
import org.junit.Test;

public class BatchDriverTest {

 Ini testGroupsini;
 Ini testBatchini;
 BatchDriver testBatchDriver;

 @Test
 public void testBatchDriverInstance() throws IOException {
 testGroupsini = new Ini(new File("test_groups.ini"), "Messageids");
 testBatchini = new Ini(new File("test_batchfile.ini"), "");
 testBatchDriver = new BatchDriver(testGroupsini.getFilename(), testBatchini.getFilename());
 assertNotNull(testBatchDriver);
 }

 @Test
 public void testGetGroups() {
 String[] testGroups = testBatchDriver.getGroups();
 assertEquals(testGroups.length, testBatchGroupsVector.size());
 for (int i = 0; i < testGroups.length; i++) {
 assertEquals(testGroups[i], testBatchGroupsVector.elementAt(i)[0]);
 }
 }

 @Test
 public void testGetBatchJob() throws IOException, ArrayIndexOutOfBoundsException {
 String testGroup = "test_group";
 BatchJob testBatchJob = testBatchDriver.getBatchJob(testGroup);
 assertNotNull(testBatchJob);
 String testServer = "test_server";
 String testDirectory = "test_directory";
 int testFirstId = 1;
 int testLastId = 10;
 assertEquals(testServer, testBatchJob.getServer());
 assertEquals(testGroup, testBatchJob.getGroupName());
 assertEquals(testDirectory, testBatchJob.getDirectory());
 assertEquals(testFirstId, testBatchJob.getFirstId());
 assertEquals(testLastId, testBatchJob.getLastId());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithInvalidGroup() {
 String testInvalidGroup = "test_invalid_group";
 BatchJob testBatchJob = testBatchDriver.getBatchJob(testInvalidGroup);
 assertNull(testBatchJob);
 }

 @Test
 public void testGetSubjectLines() {
 String[] testSubjectLines = testBatchDriver.getSubjectLines();
 assertNotNull(testSubjectLines);
 assertEquals(testSubjectLines.length, 10);
 }

 @Test
 public void testNIOSocketException() {
 String testServer = "test_server";
 try {
 testBatchDriver.getNewsSocket(testServer);
 fail("NIOSocketException should be thrown");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testAuthenticationFailure() {
 String testServer = "test_server";
 String testInvalidUsername = "test_invalid_username";
 String testInvalidPassword = "test_invalid_password";
 try {
 testBatchDriver.getNewsSocket(testServer);
 testBatchDriver.authInfo(testInvalidUsername, testInvalidPassword);
 fail("AuthenticationFailedException should be thrown");
 } catch (Exception e) {
 if (!(e instanceof AuthenticationFailedException)) {
 throw e;
 }
 }
 }

 @Test
 public void testGettingSubjectsSuccess() {
 String testServer = "test_server";
 String testGroup = "test_group";
 int testFirstId = 1;
 int testLastId = 10;
 BatchJob testBatchJob = testBatchDriver.getBatchJob(testGroup);
 String[] testSubjectLines = testBatchJob.getSubjectLines();
 assertNotNull(testSubjectLines);
 assertEquals(testSubjectLines.length, 10);
 }

 @Test
 public void testGettingSubjectsFailure() {
 String testServer = "test_server";
 String testGroup = "test_group";
 int testFirstId = 1;
 int testLastId = 10;
 testBatchDriver.setNewsSocket(new FakeNNTP());
 BatchJob testBatchJob = testBatchDriver.getBatchJob(testGroup);
 assertNull(testBatchJob);
 }

 @Test
 public void testQuitingNewsSocket() {
 String testServer = "test_server";
 testBatchDriver.setNewsSocket(new FakeNNTP());
 testBatchDriver.quitNewsSocket();
 }

}