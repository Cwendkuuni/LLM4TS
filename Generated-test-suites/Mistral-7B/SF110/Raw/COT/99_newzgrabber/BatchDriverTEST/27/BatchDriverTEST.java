package Newzgrabber.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Newzbatch;
import Newzgrabber.NewsFactory;
import Newzgrabber.Status;

public class BatchDriverTest {

 private String newzini = "test.ini";
 private String batchfile = "test.ini";

 @Test
 public void testBatchDriverConstruction() throws IOException, NullPointerException {
 BatchDriver bd = new BatchDriver(newzini, batchfile);
 assertNotNull(bd.getGroups());
 assertNotNull(bd.getBatchGroupsVector());
 }

 @Test
 public void testGetGroups() throws ArrayIndexOutOfBoundsException {
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final String[] groups = bd.getGroups();
 assertNotNull(groups);
 assertFalse(groups.length == 0);
 }

 @Test
 public void testGetBatchJob() throws IOException, ArrayIndexOutOfBoundsException, InterruptedException, NullPointerException {
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final String group = "test.group";
 final String server = "test.server";
 final String directory = "test.directory";
 final int firstid = 1;
 final int lastid = 10;
 final BatchJob bj = bd.getBatchJob(group);
 assertNotNull(bj);
 assertEquals(server, bj.getServer());
 assertEquals(group, bj.getGroup());
 assertEquals(directory, bj.getDirectory());
 assertEquals(firstid, bj.getFirstid());
 assertEquals(lastid, bj.getLastid());
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullGroup() throws IOException, NullPointerException {
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final BatchJob bj = bd.getBatchJob(null);
 fail("Expected NullPointerException, but got " + bj);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobGroupOutOfBounds() throws IOException, ArrayIndexOutOfBoundsException, InterruptedException, NullPointerException {
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final String group = "100";
 final BatchJob bj = bd.getBatchJob(group);
 fail("Expected ArrayIndexOutOfBoundsException, but got " + bj);
 }

 @Test
 public void testGetSubjectLinesNull() {
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 assertNull(bd.getSubjectLines());
 }

 @Test
 public void testGetSubjectLinesNotNull() throws IOException, InterruptedException {
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final String group = "test.group";
 final BatchJob bj = bd.getBatchJob(group);
 final String[] subjectlines = bj.getSubjectLines();
 assertNotNull(subjectlines);
 assertFalse(subjectlines.length == 0);
 }

 @Test(expected = NullPointerException.class)
 public void testGetSubjectLinesNullBatchJob() {
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final String[] subjectlines = bd.getSubjectLines();
 fail("Expected NullPointerException, but got " + subjectlines);
 }

 @Test
 public void testNewzbatchTimeout() throws IOException, InterruptedException {
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final String group = "test.group";
 final BatchJob bj = bd.getBatchJob(group);
 NewsFactory.setThreadLimit(5);
 final Newzbatch nb = new Newzbatch();
 nb.addJob(bj);
 Newzbatch.TIMEOUT = true;
 nb.start();
 Newzbatch.TIMEOUT = false;
 nb.stop();
 assertTrue(Newzbatch.isStopped());
 }

 @Test(expected = IOException.class)
 public void testGetBatchJobFileNotFound() throws IOException, ArrayIndexOutOfBoundsException, InterruptedException, NullPointerException {
 File batchfile = new File("nonexistentfile.ini");
 try {
 final BatchDriver bd = new BatchDriver(newzini, batchfile.getAbsolutePath());
 final String group = "test.group";
 final BatchJob bj = bd.getBatchJob(group);
 fail("Expected IOException, but got " + bj);
 }
 catch (IOException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullNewzini() throws IOException, ArrayIndexOutOfBoundsException, InterruptedException, NullPointerException {
 try {
 final BatchDriver bd = new BatchDriver(null, batchfile);
 final String group = "test.group";
 final BatchJob bj = bd.getBatchJob(group);
 fail("Expected NullPointerException, but got " + bj);
 }
 catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobNullBatchfile() throws IOException, ArrayIndexOutOfBoundsException, InterruptedException, NullPointerException {
 try {
 final BatchDriver bd = new BatchDriver(newzini, null);
 final String group = "test.group";
 final BatchJob bj = bd.getBatchJob(group);
 fail("Expected NullPointerException, but got " + bj);
 }
 catch (NullPointerException e) {
 // expected
 }
 }
}