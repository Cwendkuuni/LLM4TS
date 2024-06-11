package NewzgrabberTest;

import junit.framework.Assert;
import junit.framework.TestCase;
import Newzgrabber.BatchDriver;
import Newzgrabber.LineData;
import Newzgrabber.SongList;

public class NewzgrabberTest extends TestCase {

 private BatchDriver batchDriver;
 private LineData lineData;
 private SongList songList;

 @Before
 public void setUp() throws Exception {
 batchDriver = new BatchDriver("test.ini", "test.batch");
 lineData = new LineData("testServer", new String[]{"testGroup1"});
 songList = new SongList(new String[]{"testSubjectLine1"});
 }

 @After
 public void tearDown() throws Exception {
 batchDriver.close();
 }

 public void testGetGroups() {
 String[] expectedGroups = {"testGroup1"};
 String[] actualGroups = batchDriver.getGroups();
 Assert.assertArrayEquals(expectedGroups, actualGroups);
 }

 public void testGetBatchJob() {
 String group = "testGroup1";
 BatchJob expectedBatchJob = new BatchJob("testServer", group, "testDirectory", "testUsername", "testPassword", null);
 BatchJob actualBatchJob = batchDriver.getBatchJob(group);
 Assert.assertEquals(expectedBatchJob, actualBatchJob);
 }

 public void testGetSubjectLines() {
 String[] expectedSubjectLines = {"testSubjectLine1"};
 String[] actualSubjectLines = batchDriver.getSubjectLines();
 Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

 public void testGetSongList() {
 SongList expectedSongList = new SongList(new String[]{"testSubjectLine1"});
 SongList actualSongList = batchDriver.getSongList();
 Assert.assertEquals(expectedSongList, actualSongList);
 }

 public void testGetCompleteSongs() {
 String[] expectedCompleteSongs = {"testSubjectLine1"};
 String[] actualCompleteSongs = batchDriver.getCompleteSongs();
 Assert.assertArrayEquals(expectedCompleteSongs, actualCompleteSongs);
 }

 public void testGetOrderedIds() {
 int id = 1;
 SongInfo expectedSongInfo = new SongInfo(id, "testGroup1", "testTitle", "testArtist", "testSize", "testDate");
 Hashtable expectedHashtable = new Hashtable();
 expectedHashtable.put(new Integer(1), expectedSongInfo);
 long[] expectedOrderedIds = SongInfo.getOrderedIds(expectedHashtable);
 long[] actualOrderedIds = lineData.getOrderedIds("testGroup1", new long[]{id});
 Assert.assertArrayEquals(expectedOrderedIds, actualOrderedIds);
 }

 public void testGetTotalLines() {
 long expectedTotalLines = 100;
 long actualTotalLines = lineData.getTotalLines("testGroup1", new long[]{1});
 Assert.assertEquals(expectedTotalLines, actualTotalLines);
 }

 public void testDownload() {
 String group = "testGroup1";
 long[] ids = {1};
 long totalLines = 100;
 Downloader downloader = new Downloader();
 downloader.setDownloadDirectory(new File("testDirectory"));
 downloader.setServer("testServer");
 downloader.setGroup(group);
 downloader.setIds(ids);
 downloader.setTotalLines(totalLines);
 downloader.startDownload();
 // Verify that download has started
 // Add assertions to check downloaded file size and content
 }

 public void testUpdateIni() {
 String inifile = "test.ini";
 String prop = "Messageids";
 String key = "testGroup1";
 String value = "12345";
 Newzgrabber.updateIni(inifile, prop, key, value);
 // Verify that ini file has been updated with the new value
 }
}