package test;

import static org.junit.Assert.*;
import Newzgrabber.*;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

 private static final String TEST_NEWZDIRECTORY = "target/test-classes/testNewzDirectory";
 private static final String TEST_JARFILENAME = "Newz.jar";
 private static final String TEST_BATCHFILENAME = "batch.ini";
 private static final String TEST_NEWZINIFILENAME = "Newzgrabber.ini";

 @Test
 public void testNewzgrabberConstructionWithArguments() {
     String[] args = {"-verbose", "-batch"};
     Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
     assertTrue(newzgrabber.IsBatch());
     assertTrue(Newzgrabber.verbose);
     assertFalse(Newzgrabber.debug);
     assertFalse(Newzgrabber.batch);
     assertEquals(TEST_JARFILENAME, Newzgrabber.JarFilename);
     assertEquals(TEST_BATCHFILENAME, Newzgrabber.BatchFilename);
     assertEquals(TEST_NEWZINIFILENAME, Newzgrabber.NewzInifilename);
 }

 @Test
 public void testNewzgrabberConstructionWithoutArguments() {
     Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
     assertFalse(newzgrabber.IsBatch());
     assertFalse(Newzgrabber.verbose);
     assertFalse(Newzgrabber.debug);
     assertFalse(Newzgrabber.batch);
     assertEquals(TEST_JARFILENAME, Newzgrabber.JarFilename);
     assertEquals(TEST_BATCHFILENAME, Newzgrabber.BatchFilename);
     assertEquals(TEST_NEWZINIFILENAME, Newzgrabber.NewzInifilename);
 }

 @Test
 public void testSetupBasicSystem() {
     Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
     assertEquals(TEST_NEWZDIRECTORY, Newzgrabber.Newzdirectory);
 }

 @Test
 public void testFindDirectory() {
     String classfile = "Newz.jar";
     String expectedNewzdirectory = TEST_NEWZDIRECTORY;
     String actualNewzdirectory = findDirectory(classfile);
     assertEquals(expectedNewzdirectory, actualNewzdirectory);
 }

 @Test
 public void testUpdateIni() {
     String inifile = TEST_NEWZINIFILENAME;
     String prop = "Messageids";
     String key = "testKey";
     String value = "testValue";
     Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
     newzgrabber.updateIni(inifile, prop, key, value);
     Vector<String[]> iniValues = newzgrabber.readIni(inifile, prop);
     String actualValue = null;
     for (String[] stringArr : iniValues) {
         if (stringArr[0].equals(key)) {
             actualValue = stringArr[1];
             break;
         }
     }
     assertEquals(value, actualValue);
 }

 @Test
 public void testWindowEvents() {
     Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
     WindowEvent we = new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING);
     newzgrabber.windowClosing(we);
     assertTrue(Newzgrabber.IsBatch());
     assertTrue(System.exit(0) == 0);
 }

 @Test
 public void testMain() {
     String[] args = {"-verbose", "-batch"};
     Newzgrabber newzgrabber = null;
     Newzbatch newzbatch = null;
     try {
         Newzgrabber.main(args);
         newzgrabber = new Newzgrabber("Newzgrabber");
         newzbatch = new Newzbatch();
         assertTrue(newzgrabber.IsBatch());
         assertTrue(Newzgrabber.verbose);
         assertTrue(Newzgrabber.batch);
         assertTrue(newzbatch != null);
     } catch (Exception e) {
         fail("Exception thrown during main method execution: " + e.getMessage());
     }
 }

 @AfterClass
 public static void cleanup() {
     Newzgrabber.verbose = false;
     Newzgrabber.debug = false;
     Newzgrabber.batch = false;
     Newzgrabber.IsBatch = false;
     Newzgrabber.JarFilename = "Newz.jar";
     Newzgrabber.BatchFilename = "batch.ini";
     Newzgrabber.NewzInifilename = "Newzgrabber.ini";
     File testDirectory = new File(TEST_NEWZDIRECTORY);
     File batchFile = new File(TEST_NEWZDIRECTORY, TEST_BATCHFILENAME);
     File iniFile = new File(TEST_NEWZDIRECTORY, TEST_NEWZINIFILENAME);
     if (testDirectory.exists()) {
         testDirectory.delete();
     }
     if (batchFile.exists()) {
         batchFile.delete();
     }
     if (iniFile.exists()) {
         iniFile.delete();
     }
 }
}