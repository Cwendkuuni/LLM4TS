package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class NewzgrabberTest {

 // Test for the static block
 @Test
 public void testStaticBlock() {
 assertEquals(false, Newzgrabber.IsBatch);
 assertEquals(false, Newzgrabber.verbose);
 assertEquals(false, Newzgrabber.debug);
 assertEquals(false, Newzgrabber.batch);
 assertEquals("Newz.jar", Newzgrabber.JarFilename);
 assertEquals("batch.ini", Newzgrabber.BatchFilename);
 assertEquals("Newzgrabber.ini", Newzgrabber.NewzInifilename);
 }

 // Test for the findDirectory method
 @Test
 public void testFindDirectory() throws Exception {
 final String classfile = "Newz.jar";
 final String expectedNewzdirectory = System.getProperty("user.dir") + File.separator + "bin"; // assuming the test is run from the bin directory
 String actualNewzdirectory = Newzgrabber.findDirectory(classfile);
 assertEquals(expectedNewzdirectory, actualNewzdirectory);
 }

 // Test for the constructor when not in batch mode
 @Test
 public void testNewzgrabberConstructor() throws Exception {
 final String title = "Newzgrabber";
 Newzgrabber newzgrabber = new Newzgrabber(title);
 assertNotNull(newzgrabber.jtp);
 assertNotNull(newzgrabber.optionspanel);
 assertNotNull(newzgrabber.groupspanel);
 assertNotNull(newzgrabber.searchpanel);
 assertNotNull(newzgrabber.transferpanel);
 }

 // Test for the windowClosing method
 @Test
 public void testWindowClosing() {
 final Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 assertFalse(newzgrabber.isDisplayable());
 newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
 assertTrue(newzgrabber.isDisposed());
 }

 // Test for the updateIni method
 @Test
 public void testUpdateIni() throws Exception {
 final String inifile = "test.ini";
 Newzgrabber.updateIni(inifile, "Group", "TestGroup", "TestValue");
 final Ini ini = new Ini(inifile, "Group");
 final Vector Newids = ini.getIniVector();
 final String[] expected = {"Group", "TestValue"};
 assertEquals(expected[0], Newids.elementAt(0)[0]);
 assertEquals(expected[1], Newids.elementAt(0)[1]);
 }

 // Test for the main method
 @Test
 public void testMain() throws Exception {
 final String[] args = {"-verbose"};
 Newzgrabber.main(args);
 assertTrue(Newzgrabber.verbose);
 }

 // Test for the exception handling in the main method
 @Test(expected = Exception.class)
 public void testMainException() {
 Newzgrabber.main(new String[0]);
 }
}