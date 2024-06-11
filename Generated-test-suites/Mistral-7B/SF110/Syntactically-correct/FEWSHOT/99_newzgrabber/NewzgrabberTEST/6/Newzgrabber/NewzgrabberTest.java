package NewzgrabberTest;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import Newzgrabber.*;

@RunWith(PowerMockRunner.class)
public class NewzgrabberTest {

 private static final String TEST_NEWZDIRECTORY = "testNewzDirectory";
 private static final String TEST_BATCHFILENAME = "testBatchFilename.ini";
 private static final String TEST_NEWZINIFILENAME = "testNewzgrabber.ini";
 private static final String TEST_JARFILENAME = "testJarFilename.jar";
 private static final String TEST_GROUP = "testGroup";
 private static final String TEST_LASTID = "testLastid";
 private static final String TEST_KEY = "testKey";
 private static final String TEST_VALUE = "testValue";

 @Test
 public void testFindDirectory() {
 PowerMockito.mockStatic(System.class);
 PowerMockito.when(System.getProperty("java.class.path")).thenReturn("testClassPath");
 PowerMockito.when(System.getProperty("path.separator")).thenReturn(File.pathSeparator);
 PowerMockito.when(System.getProperty("os.name")).thenReturn("Windows XP");

 File result = Newzgrabber.findDirectory(TEST_JARFILENAME);
 assertTrue(result != null);
 PowerMockito.verifyStatic(Times.once());
 System.setProperty("java.library.path", TEST_NEWZDIRECTORY + File.pathSeparator + result.getAbsolutePath());
 }

 @Test
 public void testUpdateIni() {
 final Ini iniMock = Mockito.mock(Ini.class);
 final LinkedList<Object[]> listMock = Mockito.mock(LinkedList.class);
 final String prop = "Messageids";
 final String key = "testKey";
 final String value = "testValue";

 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.updateIni(TEST_NEWZINIFILENAME, prop, key, value);

 PowerMockito.verifyStatic(Times.once());
 iniMock.writeIni(listMock);
 }

 @Test
 public void testWindowClosing() {
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.windowClosing(Mockito.mock(WindowEvent.class));

 Mockito.verify(Newzgrabber.optionspanel).updateIni();
 Mockito.verify(Newzgrabber.groupspanel).updateIni();
 Mockito.verify(Newzgrabber.transferpanel).updateIni();
 Mockito.verifyNoMoreInteractions(Newzgrabber.optionspanel);
 Mockito.verifyNoMoreInteractions(Newzgrabber.groupspanel);
 Mockito.verifyNoMoreInteractions(Newzgrabber.transferpanel);
 Mockito.verify(Newzgrabber.jtp).setSelectedIndex(0);
 PowerMockito.verifyStatic(Times.once());
 System.exit(0);
 }

 @Test
 public void testMain() {
 String[] args = new String[]{"-verbose", "-batch"};

 PowerMockito.mockStatic(System.class);
 PowerMockito.when(System.getProperty("user.dir")).thenReturn(TEST_NEWZDIRECTORY);
 PowerMockito.when(System.getProperty("java.class.path")).thenReturn("testClassPath");

 Newzgrabber.main(args);

 PowerMockito.verifyStatic(Times.once());
 Newzgrabber n = new Newzgrabber("Newzgrabber");
 PowerMockito.verifyStatic(Times.once());
 Newzbatch nb = new Newzbatch();
 PowerMockito.verifyStatic(Times.once());
 Newzgrabber.IsBatch = true;
 }

 @Test
 public void testSetupBasicSystem() {
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.setupBasicSystem();

 assertEquals(TEST_NEWZDIRECTORY, Newzgrabber.Newzdirectory);
 }

 @Test
 public void testNewzgrabberConstructor() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");

 assertEquals(false, Newzgrabber.IsBatch);
 assertEquals("Newzgrabber", newzgrabber.getTitle());
 }

 @Test
 public void testBatchConstructor() {
 Newzbatch newzbatch = new Newzbatch();

 assertEquals(true, Newzgrabber.IsBatch);
 assertNull(newzbatch.getTitle());
 }

 @Test
 public void testConstructorWithArguments() {
 Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
 Newzgrabber.optionspanel.setVisible(true);
 Newzgrabber.groupspanel.setVisible(true);
 Newzgrabber.transferpanel.setVisible(true);

 assertEquals(false, Newzgrabber.IsBatch);
 assertNotNull(newzgrabber.getTitle());
 assertTrue(Newzgrabber.optionspanel.isVisible());
 assertTrue(Newzgrabber.groupspanel.isVisible());
 assertTrue(Newzgrabber.transferpanel.isVisible());
 }

 @Test
 public void testWindowListenerMethods() {
 Newzgrabber newzgrabber = Mockito.spy(new Newzgrabber("Newzgrabber"));

 newzgrabber.windowOpened(Mockito.mock(WindowEvent.class));
 newzgrabber.windowActivated(Mockito.mock(WindowEvent.class));
 newzgrabber.windowDeactivated(Mockito.mock(WindowEvent.class));
 newzgrabber.windowIconified(Mockito.mock(WindowEvent.class));
 newzgrabber.windowDeiconified(Mockito.mock(WindowEvent.class));
 newzgrabber.windowClosing(Mockito.mock(WindowEvent.class));

 Mockito.verifyNoMoreInteractions(newzgrabber);
 }

 @Test
 public void testGetBatchJob() {
 PowerMockito.mockStatic(System.class);
 PowerMockito.when(System.getProperty("file.separator")).thenReturn("/");
 PowerMockito.when(System.getProperty("java.io.tmpdir")).thenReturn(TEST_NEWZDIRECTORY);
 BatchDriver batchDriverMock = Mockito.mock(BatchDriver.class);
 PowerMockito.when(batchDriverMock.getGroups()).thenReturn(new String[]{"Group1"});
 PowerMockito.when(batchDriverMock.getBatchJob("Group1")).thenReturn(new BatchJob("Group1", "Server1", "Directory1"));

 Newzgrabber newzgrabber = new Newzgrabber();
 Newzgrabber.Newzdirectory = TEST_NEWZDIRECTORY;
 Newzgrabber.BatchFilename = TEST_BATCHFILENAME;
 Newzgrabber.NewzInifilename = TEST_NEWZINIFILENAME;
 Newzgrabber.nf = Mockito.mock(NewsFactory.class);

 Newzgrabber.Newzgrabber().setBatch(true);
 Newzgrabber.Newzgrabber().setNewzdirectory(TEST_NEWZDIRECTORY);
 Newzgrabber.Newzgrabber().setBatchfilename(TEST_BATCHFILENAME);
 Newzgrabber.Newzgrabber().setNewzinifilename(TEST_NEWZINIFILENAME);
 Newzgrabber.Newzgrabber().setNewsfactory(Mockito.mock(NewsFactory.class));

 PowerMockito.whenNew(BatchDriver.class).withArguments(TEST_NEWZINIFILENAME, TEST_BATCHFILENAME).thenReturn(batchDriverMock);

 Object[] result = Newzgrabber.Newzgrabber().getBatchJob(0);
 assertEquals("Group1", result[0]);
 assertEquals("Server1", result[1].getServer());
 assertEquals("Directory1", result[1].getDirectory());
 }

 @Test
 public void testGetOrderedIds() {
 final SongInfo si1 = new SongInfo(1, "Title1", "Artist1");
 final SongInfo si2 = new SongInfo(2, "Title2", "Artist2");
 final SongInfo si3 = new SongInfo(3, "Title3", "Artist3");
 final Hashtable<Integer, SongInfo> ht = new Hashtable<>();
 ht.put(1, si1);
 ht.put(2, si2);
 ht.put(3, si3);
 final Object[] va = new Object[]{new Integer(1), si1};
 final long[] ids = SongInfo.getOrderedIds(ht);
 assertEquals(ids[0], si1.getId());
 assertEquals(ids[1], si2.getId());
 }

 @Test
 public void testGetTotalLines() {
 final LineData ld = Mockito.mock(LineData.class);
 PowerMockito.when(ld.getTotalLines(TEST_GROUP, new long[] {1})).thenReturn(100L);
 PowerMockito.when(ld.getTotalLines(TEST_GROUP, new long[] {2})).thenReturn(200L);
 PowerMockito.when(ld.getTotalLines(TEST_GROUP, new long[] {3})).thenReturn(-1L);

 long result1 = Newzgrabber.Newzgrabber().getTotalLines(TEST_GROUP, new long[] {1});
 assertEquals(100L, result1);

 long result2 = Newzgrabber.Newzgrabber().getTotalLines(TEST_GROUP, new long[] {2});
 assertEquals(200L, result2);

 long result3 = Newzgrabber.Newzgrabber().getTotalLines(TEST_GROUP, new long[] {3});
 assertEquals(-1L, result3);
 }

 @Test
 public void testGetGroupDownloadCount() {
 final Object[][] testData = new Object[][] {
 {"Group1", new Integer(3)},
 {"Group2", new Integer(5)},
 {"Group3", new Integer(2)}
 };

 Vector<Object[]> result = Newzgrabber.Newzgrabber().getGroupDownloadCount();
 assertEquals(testData.length, result.size());
 for (int i = 0; i < testData.length; i++) {
 Object[] testObject = testData[i];
 Object[] resultObject = result.elementAt(i);
 assertEquals(testObject[0], resultObject[0]);
 assertEquals(testObject[1].intValue(), resultObject[1].intValue());
 }
 }

}