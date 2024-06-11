package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

import java.io.File;
import java.util.Vector;

public class NewzgrabberTest {

 private static final String TEST_DATA_DIR = "test_data";
 private static final String NEWZGRABBER_JAR_FILENAME = "Newz.jar";
 private static final String NEWZGRABBER_BATCH_FILENAME = "batch.ini";
 private static final String NEWZGRABBER_INI_FILENAME = "Newzgrabber.ini";
 private static final String GROUP_NAME = "test_group";

 @Before
 public void setUp() throws Exception {
 System.setProperty("user.dir", TEST_DATA_DIR);
 System.setProperty("java.library.path", TEST_DATA_DIR + File.separator + "native");
 File newzDir = new File(TEST_DATA_DIR);
 Newzgrabber.Newzdirectory = newzDir.getAbsolutePath();
 Newzgrabber.JarFilename = NEWZGRABBER_JAR_FILENAME;
 Newzgrabber.BatchFilename = NEWZGRABBER_BATCH_FILENAME;
 Newzgrabber.NewzInifilename = NEWZGRABBER_INI_FILENAME;
 Newzgrabber.verbose = false;
 Newzgrabber.debug = false;
 Newzgrabber.batch = false;
 Newzgrabber.IsBatch = false;
 }

 @After
 public void tearDown() throws Exception {
 Newzgrabber.updateIni(Newzgrabber.NewzInifilename, "Messageids", "", "");
 }

 @Test
 public void testNewzgrabber_BatchMode() throws Exception {
 Newzgrabber newzgrabber = new Newzgrabber(null);
 newzgrabber.Newzgrabber(null);
 Newzgrabber.IsBatch = true;
 newzgrabber.main(new String[]{"-batch"});

 Vector<Object[]> expectedDownloadData = new Vector<>();
 Object[] groupDownloadData = new Object[]{GROUP_NAME, new Integer(2)};
 expectedDownloadData.add(groupDownloadData);

 assertEquals("Incorrect download data", expectedDownloadData, newzgrabber.getGroupDownloadCount());
 }

 @Test
 public void testNewzgrabber_NormalMode() throws Exception {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 newzgrabber.Newzgrabber("Newzgrabber");

 assertEquals("Incorrect number of tabs", 5, newzgrabber.jtp.getTabCount());
 }

 @Test(expected = Exception.class)
 public void testNewzgrabber_InvalidBatchFile_ThrowsException() throws Exception {
 Newzgrabber newzgrabber = new Newzgrabber(null);
 Newzgrabber.IsBatch = true;
 newzgrabber.main(new String[]{"-batch", "non_existent_file.ini"});
 fail("Expected an exception to be thrown but none was");
 }
}