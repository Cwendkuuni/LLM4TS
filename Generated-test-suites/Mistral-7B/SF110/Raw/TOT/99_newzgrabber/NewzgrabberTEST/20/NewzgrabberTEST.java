import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {

 private static final String TEST_DATA_DIRECTORY = "test_data";
 private static final String TEST_INI_FILE = TEST_DATA_DIRECTORY + File.separator + "test.ini";
 private static final String TEST_BATCH_FILE = TEST_DATA_DIRECTORY + File.separator + "test_batch.ini";

 @Test
 public void testSetupBasicSystem() throws Exception {
 setupBasicSystem();
 assertEquals(TEST_DATA_DIRECTORY, Newzdirectory);
 }

 @Test
 public void testFindDirectory() throws Exception {
 final String classfile = Newzgrabber.class.getName().replace(".", "/") + ".class";
 final String expected = TEST_DATA_DIRECTORY + File.separator;
 final String actual = findDirectory(classfile);
 assertEquals(expected, actual);
 }

 @Test
 public void testUpdateIni() throws Exception {
 final Ini ini = new Ini(TEST_INI_FILE, "Messageids");
 final Vector Newids = ini.getIniVector();
 final String key = "SampleKey";
 final String value = "SampleValue";
 final Object[] expected = { key, value };
 updateIni(TEST_INI_FILE, "Messageids", key, value);
 final Ini updatedIni = new Ini(TEST_INI_FILE, "Messageids");
 final Vector updatedNewids = updatedIni.getIniVector();
 assertEquals(expected[0], updatedNewids.elementAt(0)[0]);
 assertEquals(expected[0], updatedNewids.elementAt(updatedNewids.size() - 1)[0]);
 assertEquals(expected[1], updatedNewids.elementAt(updatedNewids.size() - 1)[1]);
 }

 @Test
 public void testWindowClosing() {
 final Newzgrabber n = new Newzgrabber("TestNewzgrabber");
 n.windowClosing(new WindowEvent(n, WindowEvent.WINDOW_CLOSING));
 }

 @Test
 public void testBatchMode() throws Exception {
 Newzgrabber.IsBatch = true;
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 try {
 n.testBatchMode();
 fail("Batch mode test should have thrown an exception");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testValidBatchMode() throws Exception {
 Newzgrabber.IsBatch = true;
 final String batchfile = TEST_DATA_DIR + File.separator + "valid_batch.ini";
 String[] groups = {"group1", "group2"};
 BatchDriver bd = new BatchDriver(TEST_INI_FILE, batchfile);
 assertEquals(groups.length, bd.getGroups().length);
 for (int i = 0; i < groups.length; i++) {
 assertEquals(groups[i], bd.getGroups()[i]);
 }
 }

 @Test(expected = Exception.class)
 public void testInvalidBatchMode() throws Exception {
 Newzgrabber.IsBatch = true;
 final String batchfile = TEST_DATA_DIR + File.separator + "invalid_batch.ini";
 new BatchDriver(TEST_INI_FILE, batchfile);
 }
}