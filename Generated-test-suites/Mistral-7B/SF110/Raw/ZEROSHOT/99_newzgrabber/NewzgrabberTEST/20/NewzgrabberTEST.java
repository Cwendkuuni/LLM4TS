package test.Newzgrabber;

import static org.junit.Assert.*;
import Newzgrabber.*;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

 private static final String TEST_DATA_DIR = "test_data";
 private static final String NEWZGRABBER_INI = "Newzgrabber.ini";
 private static final String BATCH_INI = "batch.ini";
 private static final String NEWZDIR = "test_newz_dir";

 @Test
 public void testSetupBasicSystem() {
  setupSystem();
  String newzdirectory = Newzgrabber.Newzdirectory;
  assertNotNull(newzdirectory);
  assertEquals(NEWZDIR, newzdirectory);
 }

 @Test
 public void testFindDirectory() {
  String classfile = "Newz.jar";
  String newzdirectory = findDirectory(classfile);
  assertNotNull(newzdirectory);
  assertEquals(NEWZDIR, newzdirectory);
 }

 @Test
 public void testCreateNewzgrabberInstance() {
  Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
  assertNotNull(newzgrabber);
 }

 @Test(expected = Exception.class)
 public void testCreateNewzgrabberInstanceWithNullArgument() {
  Newzgrabber newzgrabber = new Newzgrabber(null);
 }

 @Test
 public void testCreateNewzbatchInstance() {
  Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
  Newzbatch newzbatch = new Newzbatch();
  assertNotNull(newzbatch);
 }

 @Test
 public void testUpdateIniFile() {
  setupSystem();
  String inifile = NEWZGRABBER_INI;
  String prop = "Messageids";
  String key = "TestKey";
  String value = "TestValue";
  Newzgrabber.updateIni(inifile, prop, key, value);
  Vector<Vector<String>> iniData = readIniFile(inifile);
  Vector<String> messageids = iniData.get(0);
  for (String msgid : messageids) {
   if (msgid.equals(key)) {
    Vector<String> values = messageids.get(messageids.indexOf(msgid) + 1);
    assertEquals(value, values.get(0));
   }
  }
 }

 @Test
 public void testReadIniFile() {
  setupSystem();
  String inifile = NEWZGRABBER_INI;
  Vector<Vector<String>> iniData = readIniFile(inifile);
  // Check basic structure
  assertEquals(3, iniData.size());
  assertEquals("General", iniData.get(0).get(0));
  assertEquals("Options", iniData.get(1).get(0));
  assertEquals("Groups", iniData.get(2).get(0));
  // Check specific data
  Vector<String> groups = iniData.get(2);
  String firstGroup = groups.get(0);
  Vector<String> firstGroupValues = groups.get(groups.indexOf(firstGroup) + 1);
  assertEquals("Group1", firstGroup);
  assertEquals("Description1", firstGroupValues.get(0));
  assertEquals("URL1", firstGroupValues.get(1));
 }

 @Test
 public void testBatchMode() {
  setupBatchSystem();
  Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
  assertFalse(Newzgrabber.IsBatch);
  String[] args = {"-batch"};
  newzgrabber = new Newzgrabber("Newzgrabber");
  for (String arg : args) {
   if (arg.toLowerCase().equals("-batch")) {
    Newzgrabber.IsBatch = true;
   }
  }
  assertTrue(Newzgrabber.IsBatch);
  Newzgrabber.main(args);
 }

 @Test
 public void testVerboseMode() {
  setupSystem();
  String[] args = {"-verbose"};
  Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
  for (String arg : args) {
   if (arg.toLowerCase().equals("-verbose")) {
    Newzgrabber.verbose = true;
   }
  }
  assertTrue(Newzgrabber.verbose);
 }

 @Test
 public void testDownloadFiles() {
  setupBatchSystem();
  Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
  String[] args = {"-batch"};
  for (String arg : args) {
   if (arg.toLowerCase().equals("-batch")) {
    Newzgrabber.IsBatch = true;
   }
  }
  Newzgrabber.main(args);
  Vector<Vector<Object>> downloadData = Newzgrabber.GroupDownloadCount;
  assertEquals(1, downloadData.size());
  Vector<Object> downloadData0 = downloadData.get(0);
  assertEquals("Group1", downloadData0.get(0));
  assertEquals(1, downloadData0.get(1));
  File downloadFile = new File(NEWZDIR + File.separator + "Group1" + File.separator + "article1.nzb");
  assertTrue(downloadFile.exists());
 }

 @Test
 public void testNoArticlesToDownload() {
  setupBatchSystem();
  Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
  String[] args = {"-batch"};
  for (String arg : args) {
   if (arg.toLowerCase().equals("-batch")) {
    Newzgrabber.IsBatch = true;
   }
  }
  Newzgrabber.main(args);
  Vector<Vector<Object>> downloadData = Newzgrabber.GroupDownloadCount;
  assertEquals(0, downloadData.size());
 }

 @Test
 public void testInvalidServerCredentials() {
  setupBatchSystem();
  String[] args = {"-batch"};
  for (String arg : args) {
   if (arg.toLowerCase().equals("-batch")) {
    Newzgrabber.IsBatch = true;
   }
  }
  Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
  newzgrabber.setUsername("InvalidUsername");
  newzgrabber.setPassword("InvalidPassword");
  Newzgrabber.main(args);
  Vector<Vector<Object>> downloadData = Newzgrabber.GroupDownloadCount;
  assertEquals(0, downloadData.size());
 }

 @Test
 public void testInvalidDownloadDirectory() {
  setupBatchSystem();
  String[] args = {"-batch"};
  for (String arg : args) {
   if (arg.toLowerCase().equals("-batch")) {
    Newzgrabber.IsBatch = true;
   }
  }
  Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
  newzgrabber.setDownloadDirectory(new File("InvalidDirectory"));
  Newzgrabber.main(args);
  Vector<Vector<Object>> downloadData = Newzgrabber.GroupDownloadCount;
  assertEquals(0, downloadData.size());
 }

 private void setupSystem() {
  Newzgrabber.verbose = false;
  Newzgrabber.debug = false;
  Newzgrabber.batch = false;
  Newzgrabber.IsBatch = false;
  Newzgrabber.JarFilename = "Newz.jar";
  Newzgrabber.BatchFilename = "batch.ini";
  Newzgrabber.NewzInifilename = NEWZGRABBER_INI;
  Newzgrabber.Newzdirectory = TEST_DATA_DIR;
  NewFile(NEWZDIR).mkdirs();
 }

 private void setupBatchSystem() {
  Newzgrabber.verbose = false;
  Newzgrabber.debug = false;
  Newzgrabber.batch = true;
  Newzgrabber.IsBatch = true;
  Newzgrabber.JarFilename = "Newz.jar";
  Newzgrabber.BatchFilename = BATCH_INI;
  Newzgrabber.NewzInifilename = NEWZGRABBER_INI;
  Newzgrabber.Newzdirectory = NEWZDIR;
 }

 private Vector<Vector<String>> readIniFile(String inifile) {
  Ini ini = new Ini(inifile);
  return ini.getIniVector();
 }

 private void NewFile(String pathname) {
  File file = new File(pathname);
  file.mkfile();
 }
}