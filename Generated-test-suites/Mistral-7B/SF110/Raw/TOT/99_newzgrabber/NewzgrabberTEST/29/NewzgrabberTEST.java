import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {

 private static final String TEST_DIRECTORY = System.getProperty("user.dir") + File.separator + "test-data";
 static {
 Newzgrabber.Newzdirectory = TEST_DIRECTORY;
 Newzgrabber.IsBatch = false;
 Newzgrabber.verbose = true;
 Newzgrabber.setupBasicSystem();
 }

 @Test
 public void testNewzgrabberConstructor() {
 Newzgrabber ng = new Newzgrabber("Newzgrabber");
 assertEquals("Newzgrabber", ng.getTitle());
 }

 @Test
 public void testNewzgrabberBatchConstructor() {
 Newzgrabber ng = new Newzgrabber("Newzgrabber");
 ng.setBatchMode();
 assertFalse(ng.isSearchPanelShowing());
 }

 @Test
 public void testFindDirectory() {
 File jarFile = new File(Newzgrabber.JarFilename);
 assertEquals(Newzgrabber.Newzdirectory, Newzgrabber.findDirectory(jarFile.getAbsolutePath()));
 }

 @Test
 public void testSetupBasicSystem() {
 assertNotNull(Newzgrabber.Newzdirectory);
 assertNotNull(Newzgrabber.nf);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNewzDirectory() {
 Newzgrabber.Newzdirectory = null;
 try {
 Newzgrabber.setupBasicSystem();
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testInvalidIniFile() throws IOException {
 Ini ini = new Ini(Newzgrabber.Newzdirectory + File.separator + "invalid.ini", "Messageids");
 fail("IOException expected");
 }

 @Test
 public void testUpdateIni() {
 Ini ini = new Ini(Newzgrabber.Newzdirectory + File.separator + "test.ini", "Messageids");
 Newzgrabber.updateIni(Newzgrabber.Newzdirectory + File.separator + "test.ini", "Messageids", "Group1", "Value1");
 assertEquals("Value1", ini.getIniVector().get(0)[1]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullProp() {
 Newzgrabber.updateIni(Newzgrabber.Newzdirectory + File.separator + "test.ini", null, "Group1", "Value1");
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() {
 Newzgrabber.updateIni(Newzgrabber.Newzdirectory + File.separator + "test.ini", "Messageids", null, "Value1");
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullValue() {
 Newzgrabber.updateIni(Newzgrabber.Newzdirectory + File.separator + "test.ini", "Messageids", "Group1", null);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testWindowEventHandling() {
 Newzgrabber frame = new Newzgrabber("Newzgrabber");
 assertFalse(frame.isVisible());
 frame.windowClosing(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
 assertTrue(frame.isHidden());
 }

}