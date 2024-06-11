package NewzgrabberTests;

import static org.junit.Assert.*;
import Newzgrabber.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class NewzgrabberTest {

 private static Newzgrabber newzgrabber;
 private static String iniFile = "test.ini";

 @BeforeClass
 public static void setup() {
 Newzgrabber.Newzdirectory = "test_directory";
 Newzgrabber.JarFilename = "Newz.jar";
 Newzgrabber.BatchFilename = "batch.ini";
 Newzgrabber.NewzInifilename = iniFile;
 newzgrabber = new Newzgrabber("Newzgrabber");
 }

 @Test
 public void testSetupBasicSystem() {
 setupBasicSystem();
 assertTrue(Newzgrabber.Newzdirectory != null);
 }

 @Test
 public void testFindDirectory() {
 assertEquals("test_directory", findDirectory("Newz.jar"));
 }

 @Test(expected = Exception.class)
 public void testWindowClosingException() {
 newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
 }

 @Test
 public void testUpdateIni() {
 updateIni(iniFile, "TestProp", "TestKey", "TestValue");
 Ini ini = new Ini(iniFile, "TestProp");
 String value = ini.read(0, "TestKey");
 assertEquals("TestValue", value);
 }

 @Test
 public void testMain() {
 Newzgrabber.main(new String[]{"-verbose"});
 Newzgrabber.verbose = true;
 assertTrue(Newzgrabber.verbose);
 }

 @Test
 public void testBatchMode() {
 Newzgrabber.IsBatch = true;
 Newzbatch newzbatch = new Newzbatch();
 assertNotNull(newzbatch);
 }
}