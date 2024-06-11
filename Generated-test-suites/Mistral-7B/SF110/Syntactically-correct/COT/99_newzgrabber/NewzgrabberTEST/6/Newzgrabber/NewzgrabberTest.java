import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {

 private static final String NEWZDIR = "testNewzDir";
 private static final String JARFILENAME = "testJarFilename.jar";
 private static final String BATCHFILENAME = "testBatchFilename.ini";
 private static final String NEWZINIFILENAME = "testNewzIniFilename.ini";
 private static final boolean VERBOSE = true;
 private static final boolean DEBUG = true;
 private static final boolean BATCH = true;
 private static final boolean ISBATCH = true;

 @Test
 public void testNewzgrabberConstructor() {
 Newzgrabber newzgrabber = new Newzgrabber("Test Newzgrabber");
 assertEquals("Test Newzgrabber", newzgrabber.getTitle());
 }

 @Test
 public void testNewzgrabberBatchConstructor() {
 Newzgrabber newzgrabber = new Newzgrabber(BATCHFILENAME);
 assertNotNull(newzgrabber.getJtp());
 }

 @Test
 public void testSetupBasicSystem() {
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.setupBasicSystem();
 assertEquals(NEWZDIR, Newzgrabber.Newzdirectory);
 }

 @Test(expected = Exception.class)
 public void testFindDirectoryFailure() {
 String classfile = "NonExistentClassFile.class";
 assertNull(Newzgrabber.findDirectory(classfile));
 }

 @Test
 public void testFindDirectorySuccess() {
 String classfile = "Newzgrabber.class";
 assertEquals(NEWZDIR, Newzgrabber.findDirectory(classfile));
 }

 @Test
 public void testUpdateIni() {
 String inifile = "test.ini";
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.updateIni(inifile, "TestProp", "TestKey", "TestValue");
 String[] keys = newzgrabber.getIniKeys(inifile, "TestProp");
 String value = newzgrabber.getIniValue(inifile, "TestProp", "TestKey");
 assertEquals("TestValue", value);
 assertEquals(2, keys.length);
 assertEquals("TestKey", keys[0]);
 assertEquals("TestProp", keys[1]);
 }

 @Test(expected = Exception.class)
 public void testUpdateIniFailure() {
 String inifile = "test.ini";
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.updateIni(inifile, "TestProp", "TestKey", "TestValue"); // Already set
 newzgrabber.updateIni(inifile, "TestProp", "TestKey", "TestValue2"); // Failure
 }

 @Test
 public void testWindowListeners() {
 Newzgrabber newzgrabber = new Newzgrabber("Test Newzgrabber");
 newzgrabber.addWindowListener(e -> {});
 }

 @Test
 public void testWindowClosing() {
 Newzgrabber newzgrabber = spy(new Newzgrabber("Test Newzgrabber"));
 doCallRealMethod().when(newzgrabber).windowClosing(any(WindowEvent.class));
 newzgrabber.windowClosing(mock(WindowEvent.class));
 verify(newzgrabber, times(1)).windowClosing(any(WindowEvent.class));
 }

}