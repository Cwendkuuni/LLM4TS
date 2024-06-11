package test;

import static org.junit.Assert.*;
import Newzgrabber.Newzgrabber;
import Newzgrabber.BatchDriver;
import Newzgrabber.LineData;
import Newzgrabber.SongList;
import Newzgrabber.SongInfo;
import Newzgrabber.Downloader;

import java.io.*;
import java.util.*;

public class NewzgrabberTest {

 private static final String NEWZGRABBER_DIR = System.getProperty("user.dir") + File.separator + "src" + File.separator + "Newzgrabber";
 private static final String NEWZ_DIRECTORY = NEWZGRABBER_DIR + File.separator + "Newzgrabber";
 private static final String JAR_FILENAME = "Newz.jar";
 private static final String BATCH_FILENAME = "batch.ini";
 private static final String NEWZINI_FILENAME = "Newzgrabber.ini";
 private static final String GROUP = "TestGroup";

 @BeforeClass
 public static void setup() {
 Newzgrabber.setupBasicSystem();
 Newzgrabber.JarFilename = JAR_FILENAME;
 Newzgrabber.Newzdirectory = NEW_DIRECTORY;
 Newzgrabber.BatchFilename = BATCH_FILENAME;
 Newzgrabber.NewzInifilename = NEWZINI_FILENAME;
 }

 @AfterClass
 public static void teardown() {
 // Perform any necessary teardown here
 }

 @Test
 public void testSetupBasicSystem() {
 assertNotNull(Newzgrabber.nf);
 }

 @Test
 public void testFindDirectory() {
 File expected = new File(NEWZGRABBER_DIR);
 File actual = Newzgrabber.findDirectory(JAR_FILENAME);
 assertEquals(expected, actual);
 }

 @Test
 public void testWindowClosing() {
 Newzgrabber window = new Newzgrabber("Newzgrabber");
 window.windowClosing(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
 }

 @Test
 public void testBatchMode() {
 Newzgrabber.IsBatch = true;
 Newzgrabber n = null;
try {
 n = new Newzgrabber(null);
 fail("Expected exception when running in batch mode without providing a config file");
} catch (Exception e) {
 // Expected
}

 Newzgrabber.IsBatch = false;
 Newzgrabber.verbose = true;
 Newzgrabber nb = new Newzbatch();
 BatchDriver bd = nb.new BatchDriver(NEWZINI_FILENAME, BATCH_FILENAME);
 String[] groups = bd.getGroups();
 assertNotNull(groups);
 String group = groups[0];
 BatchJob bj = bd.getBatchJob(group);
 assertNotNull(bj);
 LineData ld = new LineData(bj.getServer(), new String[] { group });
 String[] subjectLines = bd.getSubjectLines();
 assertNotNull(subjectLines);
 SongList sl = new SongList(subjectLines);
 Object[] firstSongInfo = sl.getSongs().toArray();
 assertNotNull(firstSongInfo);
 SongInfo firstSong = (SongInfo)firstSongInfo[0];
 Long id = firstSong.getId();
 Long[] ids = { id };
 Long totalLines = ld.getTotalLines(group, ids);
 assertEquals(1, totalLines.longValue());
 }

 @Test
 public void testDownload() {
 Newzgrabber.IsBatch = false;
 Newzgrabber.verbose = true;
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 Downloader d = new Downloader();
 d.setDownloadDirectory(new File(NEW_DIRECTORY + File.separator + "downloads"));
 d.setServer("test.server.com");
 d.setGroup(GROUP);
 d.setIds(new long[] { 1L });
 d.setTotalLines(1L);
 d.startDownload();
 File downloadedFile = new File(d.getDownloadDirectory(), "test.server.com_" + GROUP + "_1.nzb");
 assertTrue(downloadedFile.exists());
 }
}