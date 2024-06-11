package NewzgrabberTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.Newzgrabber;
import Newzgrabber.OptionsPanel;
import Newzgrabber.GroupsPanel;
import Newzgrabber.SearchPanel;
import Newzgrabber.TransferPanel;

public class NewzgrabberTest {

 private static final String TEST_DIR = "test_data";
 private static final String INI_FILE = TEST_DIR + File.separator + "Newzgrabber.ini";
 private static final String BATCH_FILE = TEST_DIR + File.separator + "batch.ini";
 private static final String JAR_FILE = "Newz.jar";

 @Before
 public void setUp() {
 Newzgrabber.setupBasicSystem();
 Newzgrabber.initializeTesting(INI_FILE, BATCH_FILE, JAR_FILE);
 }

 @After
 public void tearDown() {
 Newzgrabber.cleanupTesting();
 }

 @Test
 public void testConstructor() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber Test");
 assertNotNull(newzgrabber);
 }

 @Test
 public void testSetupBasicSystem() {
 Newzgrabber newzgrabber = new Newzgrabber();
 assertNotNull(Newzgrabber.Newzdirectory);
 }

 @Test
 public void testFindDirectory() {
 String classfile = Newzgrabber.class.getProtectionDomain().getCodeSource().getLocation().getPath();
 String newzdir = Newzgrabber.findDirectory(classfile);
 assertTrue(newzdir.endsWith(File.separator));
 String testfile = newzdir + "test.txt";
 File file = new File(testfile);
 assertTrue(file.exists());
 }

 @Test
 public void testWindowListener() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber Test");
 newzgrabber.addWindowListener(new WindowAdapter() {
 @Override
 public void windowClosing(WindowEvent e) {
 fail("windowClosing should not be called");
 }
 });
 newzgrabber.setVisible(true);
 newzgrabber.dispose();
 }

 @Test
 public void testUpdateIni() {
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.updateIni(INI_FILE, "Messageids", "TestGroup", "TestValue");
 Ini ini = new Ini(INI_FILE, "Messageids");
 assertNotNull(ini.getIniVector());
 String[] testvalues = ini.getIniVector().elementAt(0);
 assertEquals("TestGroup", testvalues[0]);
 assertEquals("TestValue", testvalues[1]);
 }

 @Test
 public void testBatchMode() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.IsBatch = true;
 newzgrabber = new Newzbatch();
 assertNotNull(newzgrabber.groupspanel);
 assertNull(newzgrabber.searchpanel);
 }

 @Test
 public void testVerboseMode() {
 Newzgrabber.verbose = true;
 Newzgrabber newzgrabber = new Newzgrabber();
 assertTrue(Newzgrabber.verbose);
 }

 @Test
 public void testOptionsPanel() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.optionspanel = new OptionsPanel(newzgrabber);
 assertTrue(Newzgrabber.optionspanel instanceof OptionsPanel);
 }

 @Test
 public void testGroupsPanel() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.groupspanel = new GroupsPanel(newzgrabber);
 assertTrue(Newzgrabber.groupspanel instanceof GroupsPanel);
 }

 @Test
 public void testSearchPanel() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.searchpanel = new SearchPanel(newzgrabber);
 assertTrue(Newzgrabber.searchpanel instanceof SearchPanel);
 }

 @Test
 public void testTransferPanel() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.transferpanel = new TransferPanel(newzgrabber);
 assertTrue(Newzgrabber.transferpanel instanceof TransferPanel);
 }
}