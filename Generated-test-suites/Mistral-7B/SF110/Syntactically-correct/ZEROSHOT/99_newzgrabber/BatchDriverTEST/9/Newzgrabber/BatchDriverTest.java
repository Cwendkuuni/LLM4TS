package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.ini4j.Ini;
import org.junit.Test;

public class BatchDriverTest {

 private final String newzini = "test.ini";
 private final String batchfile = "test.ini";

 @Test
 public void testBatchDriver() throws IOException, NullPointerException {
  // Create test data files
  writeTestDataFiles(newzini, batchfile);

  // Create instance of BatchDriver under test
  BatchDriver batchDriver = new BatchDriver(newzini, batchfile);

  // Test getGroups method
  String[] expectedGroups = {"group1", "group2", "group3"};
  String[] actualGroups = batchDriver.getGroups();
  assertArrayEquals(expectedGroups, actualGroups);

  // Test getBatchJob method
  String group = "group1";
  BatchJob expectedBatchJob = new BatchJob("server1", group, "directory1", 1, 10);
  BatchJob actualBatchJob = batchDriver.getBatchJob(group);
  assertEquals(expectedBatchJob, actualBatchJob);

  // Test getSubjectLines method
  String[] expectedSubjectLines = new String[]{"Subject 1", "Subject 2", "Subject 3"};
  String[] actualSubjectLines = batchDriver.getSubjectLines();
  assertArrayEquals(expectedSubjectLines, actualSubjectLines);

  // Clean up test data files
  File newziniFile = new File(newzini);
  newziniFile.delete();
  File batchfileFile = new File(batchfile);
  batchfileFile.delete();
 }

 private void writeTestDataFiles(String newzini, String batchfile) throws IOException {
  Ini newziniIni = new Ini(newzini, "Messageids");
  Ini batchini = new Ini(batchfile, "");

  newziniIni.add("Messageids", "group1", "1");
  newziniIni.add("Messageids", "group2", "11");
  newziniIni.add("Messageids", "group3", "21");

  batchini.add("", "Newsgroups", "group1=server1,directory1");
  batchini.add("", "Newsgroups", "group2=server2,directory2");
  batchini.add("", "Newsgroups", "group3=server3,directory3");

  batchini.add("group1", "Retrieve", "true");
  batchini.add("group1", "Options", "server=server1");

  batchini.add("group2", "Retrieve", "false");
  batchini.add("group2", "Options", "server=server2");

  batchini.add("group3", "Retrieve", "true");
  batchini.add("group3", "Options", "server=server3,threads=2,timeout=60");

  newziniIni.save(new File(newzini));
  batchini.save(new File(batchfile));
 }

}