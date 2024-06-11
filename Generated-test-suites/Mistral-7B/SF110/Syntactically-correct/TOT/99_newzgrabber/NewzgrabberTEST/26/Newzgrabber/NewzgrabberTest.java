import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import Newzgrabber.*;
import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

 private static final String TEST_INI_FILE = "test.ini";
 private static final String TEST_BATCH_FILE = "test.batch";
 private Newzgrabber newzgrabber;

 @Before
 public void setUp() throws Exception {
 newzgrabber = new Newzgrabber("Test Newzgrabber");
 Newzgrabber.setupBasicSystem();
 Newzgrabber.Newzdirectory = new File("test_dir").getAbsolutePath();
 Newzgrabber.BatchFilename = TEST_BATCH_FILE;
 Newzgrabber.NewzInifilename = TEST_INI_FILE;
 Newzgrabber.verbose = true; // enable verbose logging for testing
 }

 @After
 public void tearDown() throws Exception {
 newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
 new File(Newzgrabber.Newzdirectory + File.separator + TEST_BATCH_FILE).delete();
 new File(Newzgrabber.Newzdirectory + File.separator + TEST_INI_FILE).delete();
 }

 @Test
 public void testNewzgrabberConstruction() {
 Newzgrabber newInstance = new Newzgrabber("Test Newzgrabber");
 assertNotNull(newInstance);
 }

 @Test
 public void testNewzgrabberBatchMode() throws Exception {
 Newzgrabber.IsBatch = true;
 final String batchfile = Newzgrabber.Newzdirectory + File.separator + Newzgrabber.BatchFilename;
 final String newzini = Newzgrabber.Newzdirectory + File.separator + Newzgrabber.NewzInifilename;
 final String[] groups = {"group1", "group2"};
 Newzgrabber newzgrabber = new Newzgrabber("");
 newzgrabber.setupBasicSystem();
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final String[] batchGroups = bd.getGroups();
 assertEquals(groups.length, batchGroups.length);
 for (int i = 0; i < groups.length; ++i) {
 final String group = groups[i];
 final BatchJob bj = bd.getBatchJob(group);
 assertNotNull(bj);
 assertEquals(group, bj.getGroup());
 }
 }

 @Test
 public void testNewzgrabberVerboseMode() throws Exception {
 Newzgrabber.verbose = true;
 final Newzgrabber newzgrabber = new Newzgrabber("Test Newzgrabber");
 newzgrabber.setupBasicSystem();
 final String batchfile = Newzgrabber.Newzdirectory + File.separator + Newzgrabber.BatchFilename;
 final String newzini = Newzgrabber.Newzdirectory + File.separator + Newzgrabber.NewzInifilename;
 final BatchDriver bd = new BatchDriver(newzini, batchfile);
 final String[] batchGroups = bd.getGroups();
 for (int i = 0; i < batchGroups.length; ++i) {
 final String group = batchGroups[i];
 final BatchJob bj = bd.getBatchJob(group);
 assertNotNull(bj);
 System.out.println("Group: " + group);
 assertEquals(group, bj.getGroup());
 }
 }

 @Test
 public void testNewzgrabberInvalidBatchFile() throws Exception {
 Newzgrabber.IsBatch = true;
 final String batchfile = Newzgrabber.Newzdirectory + File.separator + "non_existent.ini";
 Newzgrabber newzgrabber = new Newzgrabber("");
 newzgrabber.setupBasicSystem();
 try {
 final BatchDriver bd = new BatchDriver(Newzgrabber.Newzdirectory + File.separator + Newzgrabber.NewzInifilename, batchfile);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNewzgrabberUpdateIniFile() throws Exception {
 final String inifile = Newzgrabber.Newzdirectory + File.separator + TEST_INI_FILE;
 newzgrabber.updateIni(inifile, "Messageids", "test", "testvalue");
 Newzgrabber ini = new Newzgrabber("");
 assertEquals("testvalue", ini.readIni(inifile, "Messageids", "test")[1]);
 }
}