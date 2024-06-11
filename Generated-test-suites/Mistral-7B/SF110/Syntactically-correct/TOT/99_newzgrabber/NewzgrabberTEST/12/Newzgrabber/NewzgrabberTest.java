import org.junit.*;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {

 private static final String TEST_DATA_DIR = "test-data";
 private static final String TEST_INI_FILE = TEST_DATA_DIR + File.separator + "test.ini";
 private static final String TEST_BATCH_FILE = TEST_DATA_DIR + File.separator + "test.batch";
 private static final String TEST_JAR_FILE = "test-Newz.jar";

 @BeforeClass
 public static void setupClass() throws Exception {
 System.setProperty("java.library.path", Newzgrabber.Newzdirectory + File.separator + System.getProperty("path.separator") + System.getProperty("java.library.path"));
 Newzgrabber.setupBasicSystem();
 Newzgrabber.Newzdirectory = TEST_DATA_DIR;
 Newzgrabber.JarFilename = TEST_JAR_FILE;
 Newzgrabber.NewzInifilename = "Newzgrabber.ini";
 Newzgrabber.BatchFilename = TEST_BATCH_FILE;
 }

 @Before
 public void setup() throws Exception {
 Newzgrabber.verbose = false;
 Newzgrabber.debug = false;
 Newzgrabber.batch = false;
 Newzgrabber.IsBatch = false;
 }

 @Test
 public void testNewzgrabberConstruction() throws Exception {
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 assertNotNull(n);
 }

 @Test(expected = Exception.class)
 public void testNewzgrabberConstructionWithInvalidArgs() throws Exception {
 Newzgrabber n = new Newzgrabber("");
 fail("Should have thrown an exception");
 }

 @Test
 public void testSetupBasicSystem() throws Exception {
 Newzgrabber.setupBasicSystem();
 assertNotNull(Newzgrabber.Newzdirectory);
 assertNotNull(Newzgrabber.nf);
 }

 @Test
 public void testFindDirectory() throws Exception {
 String classFile = Newzgrabber.class.getName().replace(".", "/") + ".class";
 String expected = TEST_DATA_DIR + File.separator;
 String actual = Newzgrabber.findDirectory(TEST_JAR_FILE);
 assertEquals(expected, actual);
 }

 @Test(expected = Exception.class)
 public void testFindDirectoryWithInvalidJarFile() throws Exception {
 String invalidJarFile = "does_not_exist.jar";
 String actual = Newzgrabber.findDirectory(invalidJarFile);
 fail("Should have thrown an exception");
 }

 @Test
 public void testUpdateIni() throws Exception {
 String iniFile = TEST_INI_FILE;
 String key = "TestKey";
 String value = "TestValue";
 Newzgrabber n = new Newzgrabber();
 n.updateIni(iniFile, "Messageids", key, value);
 Ini ini = new Ini(iniFile);
 String actual = ini.get(key);
 assertEquals(value, actual);
 }

 @Test(expected = Exception.class)
 public void testUpdateIniWithInvalidKey() throws Exception {
 String iniFile = TEST_INI_FILE;
 String invalidKey = "123456";
 Newzgrabber n = new Newzgrabber();
 n.updateIni(iniFile, "Messageids", invalidKey, "TestValue");
 fail("Should have thrown an exception");
 }

 @After
 public void teardown() throws Exception {
 // Perform any necessary teardown here
 }

 @AfterClass
 public static void teardownClass() throws Exception {
 // Perform any necessary teardown here
 }
}