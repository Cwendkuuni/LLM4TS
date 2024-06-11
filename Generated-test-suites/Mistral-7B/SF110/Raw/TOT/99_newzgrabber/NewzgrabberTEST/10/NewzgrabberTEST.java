package NewzgrabberTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Downloader;
import Newzgrabber.GroupDownloadCount;
import Newzgrabber.Ini;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.SongList;
import Newzgrabber.SongInfo;

public class NewzgrabberTest {

 private Newzgrabber newzgrabber;
 private BatchDriver mockBatchDriver;
 private Ini mockIni;

 @Before
 public void setup() {
 newzgrabber = new Newzgrabber("Newzgrabber");
 mockBatchDriver = mock(BatchDriver.class);
 mockIni = mock(Ini.class);
 }

 @Test
 public void testBatchModeWithValidInput() {
 // Given
 when(mockBatchDriver.getGroups()).thenReturn(new String[]{"Group1", "Group2"});
 when(mockBatchDriver.getBatchJob("Group1")).thenReturn(new BatchJob("Group1", "Server1", "Directory1", "Username1", "Password1"));
 when(mockBatchDriver.getSubjectLines()).thenReturn(new String[]{"SubjectLine1"});
 when(new SongList(new String[]{"SubjectLine1"}).getCompleteSongs()).thenReturn(new String[]{"Article1", "Article2"});
 when(mockIni.getIniVector()).thenReturn(new Vector<String[]>(){
 {
 add(new String[]{"Prop1", "Value1"});
 add(new String[]{"Group1", "Lastid1"});
 }
 });

 // When
 newzgrabber.setupBasicSystem();
 newzgrabber.setBatch(true);
 newzgrabber.runBatch();

 // Then
 assertEquals("Group1", newzgrabber.getSelectedGroup());
 assertEquals(2, newzgrabber.getDownloadCountForGroup("Group1"));
 verify(mockBatchDriver).getGroups();
 verify(mockBatchDriver).getBatchJob("Group1");
 verify(mockIni).writeIni(anyListOf(String[].class));
 }

 @Test
 public void testBatchModeWithNullBatchJob() {
 // Given
 when(mockBatchDriver.getGroups()).thenReturn(new String[]{"Group1"});
 when(mockBatchDriver.getBatchJob("Group1")).thenReturn(null);

 // When
 newzgrabber.setupBasicSystem();
 newzgrabber.setBatch(true);
 newzgrabber.runBatch();

 // Then
 assertNull(newzgrabber.getSelectedGroup());
 verify(mockBatchDriver, never()).getBatchJob("Group1");
 }

 @Test
 public void testVerboseMode() {
 // Given
 System.setProperty("Newzgrabber.verbose", "true");

 // When
 new Newzgrabber("Newzgrabber");

 // Then
 assertTrue(Newzgrabber.verbose);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentForBatch() {
 // Given
 newzgrabber.setBatch(-1);

 // When
 newzgrabber.runBatch();

 // Then
 fail("Should have thrown an IllegalArgumentException");
 }
}