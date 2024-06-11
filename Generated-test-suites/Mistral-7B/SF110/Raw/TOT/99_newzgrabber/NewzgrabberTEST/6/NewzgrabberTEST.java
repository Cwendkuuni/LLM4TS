import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {

 private static final String TEST_INIDIR = "test.ini";
 private static final String TEST_BATCHINIDIR = "test.batch.ini";
 private static final String TEST_JARFILENAME = "TestNewzgrabber.jar";
 private static final String TEST_NEWZDIRECTORY = "target/test-classes";
 private static final String TEST_BATCHFILENAME = "test.batch";
 private static final String TEST_GROUPSFILENAME = "test.groups";

 @Before
 public void setup() {
 System.setProperty("user.dir", TEST_NEWZDIRECTORY);
 System.setProperty("java.library.path", TEST_NEWZDIRECTORY + File.pathSeparator + System.getProperty("java.library.path"));
 try {
 File f = new File(TEST_INIDIR);
 if (!f.exists()) {
 f.createNewFile();
 }
 f = new File(TEST_BATCHINIDIR);
 if (!f.exists()) {
 f.createNewFile();
 }
 } catch (Exception e) {
 e.printStackTrace();
 }
 Newzgrabber.Newzdirectory = TEST_NEWZDIRECTORY;
 Newzgrabber.JarFilename = TEST_JARFILENAME;
 Newzgrabber.BatchFilename = TEST_BATCHFILENAME;
 Newzgrabber.verbose = false;
 Newzgrabber.debug = false;
 Newzgrabber.batch = false;
 Newzgrabber.IsBatch = false;
 }

 @After
 public void teardown() {
 // Remove created files
 File iniFile = new File(TEST_INIDIR);
 if (iniFile.exists()) {
 iniFile.delete();
 }
 File batchFile = new File(TEST_BATCHFILENAME);
 if (batchFile.exists()) {
 batchFile.delete();
 }
 File groupsFile = new File(TEST_GROUPSFILENAME);
 if (groupsFile.exists()) {
 groupsFile.delete();
 }
 }

 @Test
 public void testNewzgrabberConstruction() {
 Newzgrabber ng = new Newzgrabber("Test Newzgrabber");
 assertNotNull(ng);
 }

 @Test
 public void testNewzgrabberBatchConstruction() {
 Newzgrabber ng = new Newzgrabber("Test Newzgrabber");
 ng.setBatchMode();
 assertNotNull(ng);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewzgrabberInvalidJarFilename() {
 Newzgrabber ng = new Newzgrabber("Test Newzgrabber");
 Newzgrabber.JarFilename = null;
 try {
 Newzgrabber n = new Newzgrabber();
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testNewzgrabberSetupBasicSystem() {
 Newzgrabber ng = new Newzgrabber("Test Newzgrabber");
 Newzgrabber.setupBasicSystem();
 String newzdirectory = Newzgrabber.Newzdirectory;
 String jarfilename = Newzgrabber.JarFilename;
 String newzini = Newzgrabber.Newzdirectory + File.separator + Newzgrabber.NewzInifilename;
 String batchfilename = Newzgrabber.Newzdirectory + File.separator + Newzgrabber.BatchFilename;
 assertTrue("Newz directory is not set correctly", newzdirectory != null && newzdirectory.endsWith("test-classes"));
 assertTrue("Jar filename is not set correctly", jarfilename.equals("TestNewzgrabber.jar"));
 assertTrue("Newzini file path is not set correctly", newzini.endsWith("test.ini"));
 assertTrue("Batchfilename file path is not set correctly", batchfilename.endsWith("test.batch"));
 }

 @Test
 public void testFindDirectory() {
 String classfile = Newzgrabber.class.getName().replace('.', '/') + ".class";
 String expected = TEST_NEWZDIRECTORY + File.separator + "bin" + File.separator;
 String result = Newzgrabber.findDirectory(classfile);
 assertEquals(expected, result);
 }

 @Test
 public void testUpdateIni() {
 // Testing with empty ini file
 String inifile = TEST_INIDIR;
 String prop = "Messageids";
 String key = "TestKey";
 String value = "TestValue";
 Newzgrabber.updateIni(inifile, prop, key, value);
 Ini ini = new Ini(inifile, prop);
 String[][] expected = { { key, value } };
 String[][] result = ini.getIniMatrix();
 assertArrayEquals(expected, result);

 // Testing with non-empty ini file
 String[][] initialData = { { "TestKey1", "TestValue1" }, { "TestKey2", "TestValue2" } };
 try {
 Newzgrabber.updateIni(inifile, prop, key, value);
 Newzgrabber.updateIni(inifile, prop, key, "TestValue3");
 Newzgrabber.updateIni(inifile, prop, "TestKey3", "TestValue4");
 } catch (Exception e) {
 fail("Expected no exception");
 }
 ini = new Ini(inifile, prop);
 String[][] result = ini.getIniMatrix();
 assertArrayEquals(new String[][] { { "TestKey1", "TestValue1" }, { "TestKey2", "TestValue2" }, { key, "TestValue3" }, { "TestKey3", "TestValue4" } }, result);
 }

 @Test
 public void testWindowClosing() {
 Newzgrabber ng = new Newzgrabber("Test Newzgrabber");
 ng.windowClosing(new WindowEvent(ng, WindowEvent.WINDOW_CLOSING));
 }

 @Test
 public void testWindowClosingWithIniUpdates() {
 Newzgrabber ng = new Newzgrabber("Test Newzgrabber");
 ng.setupBasicSystem();
 ng.add(new JButton("Test"));
 ng.windowClosing(new WindowEvent(ng, WindowEvent.WINDOW_CLOSING));
 Ini ini = new Ini(TEST_INIDIR, "Messageids");
 String[][] result = ini.getIniMatrix();
 String[][] expected = { { "TestButton", "Test" } };
 assertArrayEquals(expected, result);
 }
}