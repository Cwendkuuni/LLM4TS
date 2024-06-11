package NewzgrabberTests;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import Newzgrabber.Newzgrabber;
import Newzgrabber.NewsFactory;
import Newzgrabber.OptionsPanel;
import Newzgrabber.GroupsPanel;
import Newzgrabber.SearchPanel;
import Newzgrabber.TransferPanel;
import Newzgrabber.BatchDriver;
import Newzgrabber.LineData;
import Newzgrabber.SongList;
import Newzgrabber.SongInfo;
import Newzgrabber.Downloader;
import Newzgrabber.BatchJob;
import Newzgrabber.Ini;

public class NewzgrabberTest {

 private static final String TEST_INI_FILE = "test.ini";
 private static final String TEST_BATCH_FILE = "test.batch";
 private static final String TEST_DATA_DIR = "test_data";
 private Newzgrabber newzgrabber;
 private NewsFactory nf;

 @Before
 public void setUp() {
 newzgrabber = new Newzgrabber("Newzgrabber");
 newzgrabber.IsBatch = false;
 Newzgrabber.Newzdirectory = TEST_DATA_DIR;
 nf = new NewsFactory();
 }

 @Test
 public void testSetupBasicSystem() {
 Newzgrabber.setupBasicSystem();
 assertTrue(Newzgrabber.Newzdirectory != null);
 assertFalse(Newzgrabber.IsBatch);
 Newzgrabber.nf = nf;
 }

 @Test
 public void testFindDirectory() {
 final String classfile = Newzgrabber.class.getName().replaceAll("\\.", "/");
 final String expectedDirectory = TEST_DATA_DIR;
 assertEquals(expectedDirectory, Newzgrabber.findDirectory(classfile));
 }

 @Test
 public void testUpdateIni() {
 Newzgrabber.updateIni(TEST_INI_FILE, "TestSection", "TestKey", "TestValue");
 Ini ini = new Ini(TEST_INI_FILE, "TestSection");
 String value = ini.getValue("TestKey");
 assertEquals("TestValue", value);
 }

 @Test
 public void testNewzgrabberConstructor() {
 Newzgrabber frame = new Newzgrabber("Newzgrabber");
 assertNotNull(frame);
 }

 @Test
 public void testWindowListenerMethods() {
 Newzgrabber frame = new Newzgrabber("Newzgrabber");
 frame.addWindowListener(new WindowAdapter() {
 @Override
 public void windowClosing(WindowEvent e) {
 fail("windowClosing should not be called");
 }
 });
 frame.setVisible(true);
 frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
 }

 @Test
 public void testBatchMode() {
 Newzgrabber.IsBatch = true;
 Newzgrabber newzgrabberBatch = new Newzgrabber("Newzgrabber");
 newzgrabberBatch.setBatchMode();
 assertFalse(newzgrabberBatch.IsBatch());
 }

 @Test
 public void testGetBatchJob() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.IsBatch = true;
 BatchDriver bd = new BatchDriver(TEST_INI_FILE, TEST_BATCH_FILE);
 BatchJob bj = newzgrabber.getBatchJob("TestGroup");
 assertNotNull(bj);
 }

 @Test
 public void testGetLineData() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.IsBatch = true;
 LineData ld = newzgrabber.getLineData("TestServer", new String[]{"TestGroup"});
 assertNotNull(ld);
 }

 @Test
 public void testGetSubjectLines() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.IsBatch = true;
 BatchDriver bd = new BatchDriver(TEST_INI_FILE, TEST_BATCH_FILE);
 String[] subjectlines = newzgrabber.getSubjectLines();
 assertNotNull(subjectlines);
 }

 @Test
 public void testSongList() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.IsBatch = true;
 BatchDriver bd = new BatchDriver(TEST_INI_FILE, TEST_BATCH_FILE);
 String[] subjectlines = newzgrabber.getSubjectLines();
 SongList sl = new SongList(subjectlines);
 assertNotNull(sl);
 assertNotNull(sl.getCompleteSongs());
 }

 @Test
 public void testDownload() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.IsBatch = false;
 Downloader downloader = new Downloader();
 downloader.setDownloadDirectory(new File("download_test"));
 downloader.setServer("TestServer");
 downloader.setGroup("TestGroup");
 downloader.startDownload();
 }

 @Test
 public void testGetOrderedIds() {
 SongInfo si = new SongInfo();
 si.setId(1);
 Hashtable<Integer, SongInfo> tmp = new Hashtable<>();
 tmp.put(1, si);
 long[] OrderedIds = SongInfo.getOrderedIds(tmp);
 assertNotNull(OrderedIds);
 }

 @Test
 public void testBatchDownload() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.IsBatch = true;
 BatchDriver bd = new BatchDriver(TEST_INI_FILE, TEST_BATCH_FILE);
 String[] BatchGroups = bd.getGroups();
 for (String group : BatchGroups) {
 BatchJob bj = newzgrabber.getBatchJob(group);
 assertNotNull(bj);
 }
 }

}