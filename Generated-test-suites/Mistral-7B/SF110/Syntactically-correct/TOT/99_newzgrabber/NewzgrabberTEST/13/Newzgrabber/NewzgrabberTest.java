package NewzgrabberTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Downloader;
import Newzgrabber.Hashtable;
import Newzgrabber.Ini;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.SongInfo;
import Newzgrabber.SongList;

public class NewzgrabberTest {

 private Newzgrabber newzgrabber;
 private BatchDriver batchDriverMock;
 private BatchJob batchJobMock;
 private Downloader downloaderMock;

 @Before
 public void setUp() {
 newzgrabber = new Newzgrabber("Newzgrabber");
 batchDriverMock = mock(BatchDriver.class);
 batchJobMock = mock(BatchJob.class);
 downloaderMock = mock(Downloader.class);
 }

 @Test
 public void testNewzgrabberConstruction() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 assertNotNull(newzgrabber.jtp);
 assertNotNull(newzgrabber.optionspanel);
 assertNotNull(newzgrabber.groupspanel);
 assertNotNull(newzgrabber.searchpanel);
 assertNotNull(newzgrabber.transferpanel);
 }

 @Test
 public void testBatchModeConstruction() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber -batch");
 assertTrue(newzgrabber.IsBatch);
 assertNull(newzgrabber.searchpanel);
 assertNull(newzgrabber.optionspanel);
 assertNull(newzgrabber.groupspanel);
 assertNotNull(newzgrabber.transferpanel);
 }

 @Test
 public void testSetupBasicSystem() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 when(batchDriverMock.getGroups()).thenReturn(new String[]{"Group1", "Group2"});
 when(batchDriverMock.getBatchJob(eq("Group1"))).thenReturn(batchJobMock);
 when(batchJobMock.getServer()).thenReturn("test.server.com");
 when(batchJobMock.getDirectory()).thenReturn(new File("testDir"));
 when(batchJobMock.getUsername()).thenReturn("testUser");
 when(batchJobMock.getPassword()).thenReturn("testPass");
 when(batchJobMock.getLastid()).thenReturn("12345");
 Newzgrabber.setupBasicSystem();
 assertEquals("testDir", Newzgrabber.Newzdirectory + File.separator + "testDir");
 assertEquals("test.server.com", Newzgrabber.nf.getServer("Group1"));
 assertNull(Newzgrabber.nf.getServer("nonexistentGroup"));
 }

 @Test
 public void testGetOrderedIds() {
 SongInfo songInfo = new SongInfo(1, "title", "url");
 Hashtable hashtable = new Hashtable();
 hashtable.put(new Integer(1), songInfo);
 long[] orderedIds = SongInfo.getOrderedIds(hashtable);
 assertArrayEquals(new long[]{1}, orderedIds);
 }

 @Test
 public void testDownload() {
 when(batchJobMock.getTotalLines(eq("Group1"), anyLongArray())).thenReturn(100L);
 when(ld.getTotalLines(eq("Group1"), anyLongArray())).thenReturn(100L);
 when(downloaderMock.getTotalLines()).thenReturn(100L);

 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 newzgrabber.setupBasicSystem();
 newzgrabber.setIds(new long[]{1});
 newzgrabber.setTotalLines(100L);
 newzgrabber.startDownload();
 verify(downloaderMock).setDownloadDirectory(new File("testDir"));
 verify(downloaderMock).setServer("test.server.com");
 verify(downloaderMock).setGroup("Group1");
 verify(downloaderMock).setIds(new long[]{1});
 verify(downloaderMock).setTotalLines(100L);
 verify(downloaderMock).startDownload();
 }

 @Test
 public void testUpdateIniFile() {
 Ini ini = new Ini("test.ini", "test");
 Vector newids = new Vector();
 newids.add(new String[]{"key1", "value1"});
 newids.add(new String[]{"key2", "value2"});
 ini.writeIni(newids);

 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 newzgrabber.updateIni("test.ini", "test", "key1", "newValue1");
 LinkedList iniVector = ini.getIniVector();
 String[] newIniData = (String[]) iniVector.get(0);
 assertEquals("newValue1", newIniData[1]);
 }

}