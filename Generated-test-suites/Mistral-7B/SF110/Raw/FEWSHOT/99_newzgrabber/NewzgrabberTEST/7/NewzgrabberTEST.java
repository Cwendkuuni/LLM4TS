package Newzgrabber;import static org.junit.Assert.*; import org.junit.Test;

public class NewzgrabberTest {

 // Test constructor
 @Test
 public void testNewzgrabber() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 assertNotNull(newzgrabber);
 }

 // Test findDirectory method
 @Test
 public void testFindDirectory() {
 String classfile = "Newz.jar";
 String expectedDirectory = System.getProperty("user.dir") + File.separator + "bin"; // Assuming the test is run from bin directory
 String actualDirectory = Newzgrabber.findDirectory(classfile);
 assertEquals(expectedDirectory, actualDirectory);
 }

 // Test updateIni method
 @Test
 public void testUpdateIni() {
 String inifile = "test.ini";
 String prop = "config";
 String key = "testKey";
 String value = "testValue";
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.updateIni(inifile, prop, key, value);

 // Add test to check if key-value pair is added to the ini file
 // You would need to read the ini file and check the value of the specific key

 }

 // Test windowClosing method
 @Test
 public void testWindowClosing() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 newzgrabber.addWindowListener(new WindowAdapter() {
 @Override
 public void windowClosing(WindowEvent e) {
 fail("windowClosing was not called");
 }
 });
 newzgrabber.setVisible(true);
 newzgrabber.dispatchEvent(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
 }

 // Test other methods if necessary
 // ...
}