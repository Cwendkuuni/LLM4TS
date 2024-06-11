import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {

 private static final String TEST_INI_FILE = "test.ini";
 private static final String TEST_BATCH_FILE = "test.bat";
 private static final String TEST_DATA_DIRECTORY = "test_data";

 @Test
 public void testNewzgrabberConstructorWithNoArguments() {
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 assertNotNull(n);
 }

 @Test
 public void testNewzgrabberConstructorWithArguments() {
 Newzgrabber n = new Newzgrabber("Newzgrabber", TEST_DATA_DIRECTORY);
 assertNotNull(n);
 }

 @Test
 public void testSetupBasicSystem() {
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 assertNotNull(n.Newzdirectory);
 }

 @Test
 public void testFindDirectory() {
 String classfile = Newzgrabber.class.getName().replace('.', '/') + ".class";
 String expected = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes";
 String actual = Newzgrabber.findDirectory(classfile);
 assertEquals(expected, actual);
 }

 @Test
 public void testUpdateIni() {
 try {
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 Ini ini = new Ini(TEST_INI_FILE, "Messageids");
 Vector Newids = ini.getIniVector();
 Newzgrabber.updateIni(TEST_INI_FILE, "Messageids", "TestKey1", "TestValue1");
 Newids = ini.getIniVector();
 String[] expected = {"TestKey1", "TestValue1"};
 String[] actual = null;
 if (Newids.size() > 0) {
 actual = Newids.elementAt(0);
 }
 assertArrayEquals(expected, actual);
 } catch (Exception e) {
 fail("Failed to update INI file: " + e.getMessage());
 }
 }

 @Test
 public void testBatchModeWithValidInput() {
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 try {
 final String batchfile = TEST_DATA_DIRECTORY + File.separator + TEST_BATCH_FILE;
 final String newzini = TEST_DATA_DIRECTORY + File.separator + TEST_INI_FILE;
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final String[] BatchGroups = bd.getGroups();
 assertEquals(2, BatchGroups.length);
 bd.close();
 } catch (Exception e) {
 fail("Failed to run batch mode: " + e.getMessage());
 }
 }

 @Test(expected = Exception.class)
 public void testBatchModeWithInvalidBatchFile() {
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 final String batchfile = TEST_DATA_DIRECTORY + File.separator + "non-existent.bat";
 try {
 final String newzini = TEST_DATA_DIRECTORY + File.separator + TEST_INI_FILE;
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 fail("Expected an exception when the batch file does not exist");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test(expected = Exception.class)
 public void testBatchModeWithInvalidIniFile() {
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 final String batchfile = TEST_DATA_DIRECTORY + File.separator + TEST_BATCH_FILE;
 try {
 final String newzini = TEST_DATA_DIRECTORY + File.separator + "non-existent.ini";
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 fail("Expected an exception when the INI file does not exist");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testMainMethodWithNoArguments() {
 Newzgrabber.main(new String[0]);
 }

 @Test
 public void testMainMethodWithVerboseArgument() {
 Newzgrabber.main(new String[]{"-verbose"});
 }

 @Test
 public void testMainMethodWithBatchArgument() {
 Newzgrabber.main(new String[]{"-batch"});
 }

 @Test
 public void testMainMethodWithDebugArgument() {
 Newzgrabber.main(new String[]{"-debug"});
 }

}