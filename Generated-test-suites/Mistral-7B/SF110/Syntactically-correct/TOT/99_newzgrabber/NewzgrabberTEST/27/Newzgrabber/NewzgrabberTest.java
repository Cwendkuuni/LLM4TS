import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {

 private static final String TEST_INIDIR = "testIniFile.ini";
 private static final String TEST_BATCHFILE = "testBatchFile.ini";
 private static final String TEST_DIR = "testOutputDir";

 @Test
 public void testConstructorWithName() {
 Newzgrabber newzgrabber = new Newzgrabber("Test Newzgrabber");
 assertEquals("Test Newzgrabber", newzgrabber.getTitle());
 }

 @Test
 public void testConstructorWithEmptyArgs() {
 Newzgrabber newzgrabber = new Newzgrabber();
 assertNotNull(newzgrabber);
 }

 @Test
 public void testSetupBasicSystem() {
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.setupBasicSystem();
 assertNotNull(Newzgrabber.Newzdirectory);
 }

 @Test
 public void testFindDirectory() {
 String classfile = Newzgrabber.class.getName().replaceAll("\\.", "/") + ".class";
 String dir = Newzgrabber.findDirectory(classfile);
 assertNotNull(dir);
 }

 @Test
 public void testUpdateIni() {
 try {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.setupBasicSystem();
 newzgrabber.updateIni(TEST_INIDIR, "Messageids", "testGroup", "testValue");
 File iniFile = new File(TEST_INIDIR);
 Ini ini = new Ini(iniFile, "Messageids");
 assertEquals("testValue", ini.getIniVector().get(0)[1]);
 } catch (Exception e) {
 fail("Exception during test: " + e.getMessage());
 }
 }

 @Test
 public void testBatchModeWithValidArgs() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.IsBatch = true;
 newzgrabber.setArgs(new String[]{"-verbose", "-batch"});
 Newzgrabber newzbatch = null;
 try {
 newzbatch = new Newzbatch();
 newzbatch.init(newzgrabber.getArgs());
 } catch (Exception e) {
 fail("Exception during test: " + e.getMessage());
 }
 Newzgrabber.setupBasicSystem();
 assertNotNull(Newzbatch.BatchFilename);
 File batchFile = new File(Newzbatch.BatchFilename);
 assertTrue(batchFile.exists());
 }

 @Test
 public void testBatchModeWithInvalidArgs() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.IsBatch = true;
 newzgrabber.setArgs(new String[]{"-verbose", "-invalid"});
 try {
 Newzbatch newzbatch = new Newzbatch();
 newzbatch.init(newzgrabber.getArgs());
 fail("Expected exception during test");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @Test
 public void testVerboseMode() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.verbose = true;
 newzgrabber.setupBasicSystem();
 assertTrue(Newzgrabber.verbose);
 }

 @Test
 public void testNonVerboseMode() {
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.setupBasicSystem();
 assertFalse(Newzgrabber.verbose);
 }

 @Test
 public void testBatchDownload() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.IsBatch = true;
 newzgrabber.setArgs(new String[]{"-verbose", "-batch"});
 Newzbatch newzbatch = null;
 try {
 newzbatch = new Newzbatch();
 newzbatch.init(newzgrabber.getArgs());
 newzbatch.downloadArticles();
 } catch (Exception e) {
 fail("Exception during test: " + e.getMessage());
 }
 File outputDir = new File(Newzbatch.directory);
 assertTrue(outputDir.exists());
 assertTrue(outputDir.isDirectory());
 }

 @Test
 public void testNormalMode() {
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.setArgs(new String[0]);
 Newzgrabber newzgui = null;
 try {
 newzgui = new Newzgrabber("Test Newzgrabber");
 newzgui.init(Newzgrabber.class.getCanonicalName());
 newzgui.jtp.setVisible(true);
 } catch (Exception e) {
 fail("Exception during test: " + e.getMessage());
 }
 assertEquals("Test Newzgrabber", newzgui.getTitle());
}

 @Test
 public void testNewzgrabberWithInvalidArgs() {
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.setArgs(new String[]{"-invalid"});
 try {
 Newzgrabber newzgui = new Newzgrabber("Test Newzgrabber");
 newzgui.init(Newzgrabber.class.getCanonicalName());
 fail("Expected exception during test");
 } catch (Exception e) {
 // Expected exception
 }
 }

 @After
 public void tearDown() {
 Newzgrabber.setupBasicSystem();
 if (Newzgrabber.Newzdirectory != null) {
 deleteRecursively(new File(Newzgrabber.Newzdirectory));
 }
 }

 private void deleteRecursively(File fileOrDirectory) {
 if (fileOrDirectory.isDirectory()) {
 for (File child : fileOrDirectory.listFiles()) {
 deleteRecursively(child);
 }
 }
 if (fileOrDirectory.exists()) {
 fileOrDirectory.delete();
 }
 }

}