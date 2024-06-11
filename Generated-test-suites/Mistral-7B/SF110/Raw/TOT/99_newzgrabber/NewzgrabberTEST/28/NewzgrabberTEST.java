import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.*;

public class NewzgrabberTest {

 private static final String TEST_DATA_DIR = "testData";
 private static final String TEST_BATCH_FILE = TEST_DATA_DIR + File.separator + "testBatch.ini";
 private static final String TEST_NEWZINI_FILE = TEST_DATA_DIR + File.separator + "testNewzgrabber.ini";
 private static final String TEST_JAR_FILENAME = "testNewzgrabber.jar";
 private static final String TEST_BATCH_FILENAME = "testBatch.ini";
 private static final String TEST_NEWZDIRECTORY = System.getProperty("user.dir") + File.separator + TEST_DATA_DIR;

 @Before
 public void setup() {
 Newzgrabber.setupBasicSystem();
 System.setProperty("java.library.path", Newzgrabber.Newzdirectory + File.separator + System.getProperty("java.library.path"));
 Newzgrabber.Newzdirectory = TEST_NEWZDIRECTORY;
 Newzgrabber.JarFilename = TEST_JAR_FILENAME;
 Newzgrabber.BatchFilename = TEST_BATCH_FILENAME;
 Newzgrabber.NewzInifilename = TEST_NEWZINI_FILE;
 }

 @Test
 public void testNewzgrabber() {
 Newzgrabber n = new Newzgrabber("Newzgrabber for testing");
 try {
 n.Newzgrabber();
 }
 catch (Exception e) {
 fail("Exception thrown during test: " + e.getMessage());
 }
 }

 @Test
 public void testBatchJob() {
 try {
 final BatchDriver bd = new BatchDriver(TEST_NEWZINI_FILE, TEST_BATCH_FILE);
 final String[] groups = bd.getGroups();
 assertEquals("Number of groups", 2, groups.length);
 final BatchJob bj = bd.getBatchJob(groups[0]);
 assertNotNull("Batch job object for group 0", bj);
 }
 catch (Exception e) {
 fail("Exception thrown during test: " + e.getMessage());
 }
 }

 @Test
 public void testGetTotalLines() {
 try {
 final LineData ld = new LineData("testServer", new String[]{"testGroup"});
 final long totalLines = ld.getTotalLines(groups[0], new long[0]);
 assertEquals("Total lines for empty id array", 0, totalLines);
 }
 catch (Exception e) {
 fail("Exception thrown during test: " + e.getMessage());
 }
 }

 @Test
 public void testGetOrderedIds() {
 try {
 final Hashtable tmp = new Hashtable();
 final SongInfo si = new SongInfo(1, "testTitle", "testArtist", "testGenre", "testUrl");
 tmp.put(new Integer(1), si);
 final long[] orderedIds = SongInfo.getOrderedIds(tmp);
 assertNotNull("Ordered ids array for single song", orderedIds);
 }
 catch (Exception e) {
 fail("Exception thrown during test: " + e.getMessage());
 }
 }

 @Test
 public void testDownload() {
 try {
 final Downloader d = new Downloader();
 final long[] ids = {1};
 d.setIds(ids);
 d.startDownload();
 Thread.sleep(1000); // give the downloader some time to start downloading
 final File downloadedFile = new File("testDownloadedFile.nzb");
 assertTrue("Downloaded file exists", downloadedFile.exists());
 }
 catch (Exception e) {
 fail("Exception thrown during test: " + e.getMessage());
 }
 }

 @Test
 public void testUpdateIni() {
 try {
 Newzgrabber n = new Newzgrabber("Newzgrabber for testing");
 n.updateIni(TEST_NEWZINI_FILE, "Messageids", "testGroup", "testLastid");
 final Ini ini = new Ini(TEST_NEWZINI_FILE, "Messageids");
 final Vector<String[]> iniVector = ini.getIniVector();
 final String[] lastEntry = iniVector.get(iniVector.size() - 1);
 assertEquals("Last entry in ini file", "testGroup", lastEntry[0]);
 assertEquals("Last value in ini file", "testLastid", lastEntry[1]);
 }
 catch (Exception e) {
 fail("Exception thrown during test: " + e.getMessage());
 }
 }
}