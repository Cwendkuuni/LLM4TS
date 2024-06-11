package test.Newzgrabber;

import static org.junit.Assert.*;
import Newzgrabber.*;
import java.io.*;
import java.util.*;

public class NewzgrabberTest {

 private static final String TEST_NEWZDIR = "test_newzdir";
 private static final String TEST_BATCHFILE = "test_batchfile.ini";
 private static final String TEST_NEWZINIFILE = "test_newzgrabber.ini";
 private static final String TEST_JARFILENAME = "test_Newz.jar";

 @BeforeClass
 public static void setUpClass() {
 System.setProperty("java.io.tmpdir", TEST_NEWZDIR);
 File newzdir = new File(TEST_NEWZDIR);
 File jarFile = new File(TEST_JARFILENAME);
 File batchFile = new File(TEST_BATCHFILE);
 File newziniFile = new File(TEST_NEWZINIFILE);
 Newzgrabber.Newzdirectory = newzdir.getAbsolutePath();
 Newzgrabber.JarFilename = jarFile.getAbsolutePath();
 Newzgrabber.BatchFilename = batchFile.getAbsolutePath();
 Newzgrabber.NewzInifilename = newziniFile.getAbsolutePath();
 }

 @AfterClass
 public static void tearDownClass() {
 File newzdir = new File(TEST_NEWZDIR);
 File.deleteRecursively(newzdir);
 }

 @Test
 public void testSetupBasicSystem() {
 Newzgrabber n = null;
 assertFalse(Newzgrabber.IsBatch);
 try {
 n = new Newzgrabber("Newzgrabber");
 } catch (Exception e) {
 fail("Failed to initialize Newzgrabber: " + e.getMessage());
 }
 assertTrue(Newzgrabber.Newzdirectory.endsWith(File.separator));
 assertFalse(System.getProperty("os.name").toLowerCase().indexOf("windows") == -1);
 try {
 System.setProperty("java.library.path", Newzgrabber.Newzdirectory + File.separator + System.getProperty("java.library.path"));
 } catch (Exception e) {
 fail("Failed to set java.library.path: " + e.getMessage());
 }
 assertTrue(Newzgrabber.verbose || Newzgrabber.debug || Newzgrabber.batch);
 Newzgrabber.setupBasicSystem();
 assertEquals(Newzgrabber.Newzdirectory, Newzgrabber.nf.getNewzDirectory());
 }

 @Test
 public void testWindowEvents() {
 Newzgrabber n = null;
 try {
 n = new Newzgrabber("Newzgrabber");
 } catch (Exception e) {
 fail("Failed to initialize Newzgrabber: " + e.getMessage());
 }
 Newzgrabber.windowClosed(new WindowEvent(n, WindowEvent.WINDOW_CLOSED));
 Newzgrabber.windowClosing(new WindowEvent(n, WindowEvent.WINDOW_CLOSING));
 Newzgrabber.windowDeactivated(new WindowEvent(n, WindowEvent.WINDOW_DEACTIVATED));
 Newzgrabber.windowIconified(new WindowEvent(n, WindowEvent.WINDOW_ICONIFIED));
 Newzgrabber.windowDeiconified(new WindowEvent(n, WindowEvent.WINDOW_DEICONIFIED));
 Newzgrabber.windowActivated(new WindowEvent(n, WindowEvent.WINDOW_ACTIVATED));
 Newzgrabber.windowOpened(new WindowEvent(n, WindowEvent.WINDOW_OPENED));
 }

 @Test
 public void testFindDirectory() {
 File jarFile = new File(TEST_JARFILENAME);
 String result = Newzgrabber.findDirectory(TEST_JARFILENAME);
 assertTrue("Failed to find directory for JarFile: " + jarFile.getAbsolutePath(), result.equals(newzdir.getAbsolutePath()));
 }

 @Test(expected = Exception.class)
 public void testFindDirectoryException() {
 String invalidFile = "nonexistentfile.txt";
 String result = Newzgrabber.findDirectory(invalidFile);
 fail("Expected Exception for invalid file: " + invalidFile);
 }

 @Test
 public void testUpdateIni() {
 Newzgrabber n = null;
 try {
 n = new Newzgrabber("Newzgrabber");
 } catch (Exception e) {
 fail("Failed to initialize Newzgrabber: " + e.getMessage());
 }
 Ini ini = new Ini(Newzgrabber.NewzInifilename, "TestSection");
 LinkedList<String[]> iniVector = ini.getIniVector();
 String[] initialData = iniVector.get(0);
 String key = "TestKey";
 String value = "TestValue";
 Newzgrabber.updateIni(Newzgrabber.NewzInifilename, "TestSection", key, value);
 String[] updatedData = iniVector.get(0);
 assertEquals(initialData[0], key);
 assertEquals(initialData[1], updatedData[1]);
 assertEquals(value, updatedData[1]);
 }

 @Test
 public void testMain() {
 String[] args = {"-verbose"};
 try {
 Newzgrabber.main(args);
 assertTrue(Newzgrabber.verbose);
 args = new String[]{"-batch"};
 Newzgrabber.main(args);
 assertTrue(Newzgrabber.IsBatch);
 args = new String[]{};
 Newzgrabber.main(args);
 assertFalse(Newzgrabber.IsBatch);
 } catch (Exception e) {
 fail("Failed to run Newzgrabber: " + e.getMessage());
 }
 }

}