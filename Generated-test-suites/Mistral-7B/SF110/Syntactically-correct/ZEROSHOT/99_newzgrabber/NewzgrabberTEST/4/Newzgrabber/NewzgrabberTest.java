package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Newzgrabber.*;

public class NewzgrabberTest {

 private static final String NEWZGRABBER_DIR = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes" + File.separator + "Newzgrabber";
 private static final String JAR_FILENAME = "Newz.jar";
 private static final String INI_FILENAME = "Newzgrabber.ini";
 private static final String BATCH_FILENAME = "batch.ini";
 private static final String GROUPS_FILENAME = "groups.txt";
 private static final String TEST_GROUP = "test_group";
 private static final long[] TEST_IDS = { 1, 2, 3, 4, 5 };
 private static final String[] TEST_SUBJECTLINES = { "Test article 1", "Test article 2", "Test article 3", "Test article 4", "Test article 5" };
 private static final String[] TEST_COMPLETE_SONGS = { "test_article_1.nzb", "test_article_2.nzb", "test_article_3.nzb", "test_article_4.nzb", "test_article_5.nzb" };
 private static final long[] TEST_ORDERED_IDS = { 1, 2, 3, 4, 5 };
 private static final String[] TEST_GROUPS = { TEST_GROUP };
 private static final String TEST_SERVER = "test_server";
 private static final String TEST_DIRECTORY = "test_directory";
 private Newzgrabber newzgrabber;

 @Before
 public void setUp() {
 newzgrabber = new Newzgrabber();
 Newzgrabber.Newzdirectory = NEWZGRABBER_DIR;
 Newzgrabber.JarFilename = JAR_FILENAME;
 Newzgrabber.NewzInifilename = INI_FILENAME;
 Newzgrabber.BatchFilename = BATCH_FILENAME;
 Newzgrabber.verbose = false;
 Newzgrabber.debug = false;
 Newzgrabber.batch = false;
 Newzgrabber.IsBatch = false;
 setupMocks();
 }

 @After
 public void tearDown() {
 Newzgrabber.Newzdirectory = null;
 Newzgrabber.JarFilename = null;
 Newzgrabber.NewzInifilename = null;
 Newzgrabber.BatchFilename = null;
 Newzgrabber.verbose = false;
 Newzgrabber.debug = false;
 Newzgrabber.batch = false;
 Newzgrabber.IsBatch = false;
 Newzgrabber = null;
 tearDownMocks();
 }

 @Test
 public void testNewzgrabber() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 assertNotNull(newzgrabber);
 }

 @Test
 public void testSetupBasicSystem() {
 assertTrue(Newzgrabber.setupBasicSystem());
 assertEquals(NEWZGRABBER_DIR, Newzgrabber.Newzdirectory);
 }

 @Test
 public void testFindDirectory() {
 String classfile = Newzgrabber.class.getName().replace(".", "/") + ".class";
 String expectedDirectory = NEWZGRABBER_DIR;
 String actualDirectory = Newzgrabber.findDirectory(classfile);
 assertEquals(expectedDirectory, actualDirectory);
 }

 @Test
 public void testWindowListenerMethods() {
 Newzgrabber newzgrabber = spy(Newzgrabber.class);
 doNothing().when(newzgrabber).windowClosed(any(WindowEvent.class));
 doNothing().when(newzgrabber).windowOpened(any(WindowEvent.class));
 doNothing().when(newzgrabber).windowActivated(any(WindowEvent.class));
 doNothing().when(newzgrabber).windowDeactivated(any(WindowEvent.class));
 doNothing().when(newzgrabber).windowIconified(any(WindowEvent.class));
 doNothing().when(newzgrabber).windowDeiconified(any(WindowEvent.class));
 doNothing().when(newzgrabber).windowClosing(any(WindowEvent.class));
 Newzgrabber newzgrabberInstance = mock(Newzgrabber.class);
 when(newzgrabberInstance.getContentPane()).thenReturn(mock(Container.class));
 when(newzgrabberInstance.jtp).thenReturn(mock(JTabbedPane.class));
 when(newzgrabberInstance.searchpanel).thenReturn(mock(SearchPanel.class));
 when(newzgrabberInstance.optionspanel).thenReturn(mock(OptionsPanel.class));
 when(newzgrabberInstance.groupspanel).thenReturn(mock(GroupsPanel.class));
 when(newzgrabberInstance.transferpanel).thenReturn(mock(TransferPanel.class));
 when(newzgrabberInstance.getWidth()).thenReturn(600);
 when(newzgrabberInstance.getHeight()).thenReturn(500);
 newzgrabber.windowOpened(mock(WindowEvent.class));
 verify(newzgrabber).windowOpened(any(WindowEvent.class));
 }

 @Test
 public void testBatchMode() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.IsBatch = true;
 Newzgrabber.verbose = false;
 Newzgrabber.debug = false;
 Newzgrabber.batch = false;
 Newzgrabber.Newzdirectory = NEWZGRABBER_DIR;
 Newzgrabber.JarFilename = JAR_FILENAME;
 Newzgrabber.NewzInifilename = INI_FILENAME;
 Newzgrabber.BatchFilename = BATCH_FILENAME;
 Newzgrabber.setupBasicSystem();
 Newzgrabber nf = Newzgrabber.nf;
 BatchDriver bd = new BatchDriver(INI_FILENAME, BATCH_FILENAME);
 assertNotNull(bd);
 String[] BatchGroups = bd.getGroups();
 assertEquals(1, BatchGroups.length);
 assertEquals(TEST_GROUP, BatchGroups[0]);
 BatchJob bj = bd.getBatchJob(TEST_GROUP);
 assertNotNull(bj);
 assertEquals(TEST_SERVER, bj.getServer());
 String[] subjectlines = bd.getSubjectLines();
 assertEquals(5, subjectlines.length);
 for (int i = 0; i < 5; i++) {
 assertEquals(TEST_SUBJECTLINES[i], subjectlines[i]);
 }
 SongList sl = new SongList(subjectlines);
 assertNotNull(sl);
 String[] complete = sl.getCompleteSongs();
 assertEquals(5, complete.length);
 for (int i = 0; i < 5; i++) {
 assertEquals(TEST_COMPLETE_SONGS[i], complete[i]);
 }
 Vector GroupDownloadCount = new Vector();
 for (int i = 0; i < 5; i++) {
 Object[] rowinfo = { TEST_COMPLETE_SONGS[i], TEST_GROUP, new Long(100), TEST_ORDERED_IDS };
 GroupDownloadCount.add(rowinfo);
 }
 for (int i = 0; i < 5; i++) {
 Object[] objarr = GroupDownloadCount.elementAt(i);
 Downloader d = new Downloader();
 d.setDownloadDirectory(new File(TEST_DIRECTORY));
 d.setServer(TEST_SERVER);
 d.setGroup(TEST_GROUP);
 d.setIds(TEST_ORDERED_IDS);
 d.setTotalLines(new Long(100));
 d.startDownload();
 }
 Object[] DownloadData = { TEST_GROUP, new Integer(5) };
 GroupDownloadCount.add(DownloadData);
 Vector expectedDownloadData = new Vector();
 expectedDownloadData.add(DownloadData);
 assertEquals(expectedDownloadData, GroupDownloadCount);
 }

 @Test(expected = Exception.class)
 public void testBatchModeException() {
 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.IsBatch = true;
 Newzgrabber.verbose = false;
 Newzgrabber.debug = false;
 Newzgrabber.batch = false;
 Newzgrabber.Newzdirectory = NEWZGRABBER_DIR;
 Newzgrabber.JarFilename = JAR_FILENAME;
 Newzgrabber.NewzInifilename = INI_FILENAME;
 Newzgrabber.BatchFilename = BATCH_FILENAME;
 Newzgrabber.setupBasicSystem();
 Newzgrabber nf = Newzgrabber.nf;
 BatchDriver bd = new BatchDriver(INI_FILENAME, BATCH_FILENAME);
 assertNotNull(bd);
 Newzgrabber.verbose = true;
 String[] BatchGroups = bd.getGroups();
 assertEquals(1, BatchGroups.length);
 assertEquals(TEST_GROUP, BatchGroups[0]);
 BatchJob bj = bd.getBatchJob(TEST_GROUP);
 assertNotNull(bj);
 assertEquals(TEST_SERVER, bj.getServer());
 String[] subjectlines = bd.getSubjectLines();
 assertEquals(5, subjectlines.length);
 for (int i = 0; i < 5; i++) {
 assertEquals(TEST_SUBJECTLINES[i], subjectlines[i]);
 }
 SongList sl = new SongList(subjectlines);
 assertNotNull(sl);
 String[] complete = sl.getCompleteSongs();
 assertEquals(5, complete.length);
 for (int i = 0; i < 5; i++) {
 assertEquals(TEST_COMPLETE_SONGS[i], complete[i]);
 }
 Vector GroupDownloadCount = new Vector();
 for (int i = 0; i < 5; i++) {
 Object[] rowinfo = { TEST_COMPLETE_SONGS[i], TEST_GROUP, new Long(100), TEST_ORDERED_IDS };
 GroupDownloadCount.add(rowinfo);
 }
 for (int i = 0; i < 5; i++) {
 Object[] objarr = GroupDownloadCount.elementAt(i);
 Downloader d = new Downloader();
 d.setDownloadDirectory(new File(TEST_DIRECTORY));
 d.setServer(TEST_SERVER);
 d.setGroup(TEST_GROUP);
 d.setIds(TEST_ORDERED_IDS);
 d.setTotalLines(new Long(100));
 try {
 d.startDownload();
 fail();
 }
 catch (Exception e) {
 // Expected exception
 }
 }
 Object[] DownloadData = { TEST_GROUP, new Integer(5) };
 GroupDownloadCount.add(DownloadData);
 Vector expectedDownloadData = new Vector();
 expectedDownloadData.add(DownloadData);
 assertEquals(expectedDownloadData, GroupDownloadCount);
 }

 private void setupMocks() {
 // Set up mocks here
 }

 private void tearDownMocks() {
 // Tear down mocks here
 }
}