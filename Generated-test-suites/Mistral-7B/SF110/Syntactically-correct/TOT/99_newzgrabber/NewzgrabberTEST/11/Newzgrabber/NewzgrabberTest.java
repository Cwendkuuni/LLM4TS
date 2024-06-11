package NewzgrabberTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Downloader;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.SongInfo;
import Newzgrabber.SongList;

public class NewzgrabberTest {

 private Newzgrabber newzgrabber;
 private BatchDriver batchDriverMock;
 private NewsFactory newsFactoryMock;

 @Before
 public void setUp() {
 newzgrabber = new Newzgrabber();
 batchDriverMock = mock(BatchDriver.class);
 newsFactoryMock = mock(NewsFactory.class);
 newzgrabber.nf = newsFactoryMock;
 }

 @Test
 public void testNewzgrabberWithValidBatchFile() {
 // Given
 String newzdirectory = "test_newz_directory";
 String batchfilename = "test_batch_file.ini";
 String newzinifilename = "test_newz_ini_file.ini";
 String group1 = "group1";
 String group2 = "group2";
 String[] batchGroups = { group1, group2 };
 BatchJob[] batchJobs = new BatchJob[2];
 for (int i = 0; i < 2; i++) {
 batchJobs[i] = mock(BatchJob.class);
 when(batchJobs[i].getGroup()).thenReturn(batchGroups[i]);
 when(batchJobs[i].getServer()).thenReturn("test_server");
 when(batchJobs[i].getDirectory()).thenReturn("test_directory");
 when(batchDriverMock.getBatchJobs(eq(group1))).thenReturn(batchJobs);
 when(batchDriverMock.getBatchJobs(eq(group2))).thenReturn(batchJobs);
 }
 LineData lineDataMock = mock(LineData.class);
 when(lineDataMock.getTotalLines(eq(group1), anyLongArray())).thenReturn(100L);
 when(lineDataMock.getTotalLines(eq(group2), anyLongArray())).thenReturn(200L);
 when(newsFactoryMock.newLineData(anyString(), any(String[].class))).thenReturn(lineDataMock);

 // When
 newzgrabber.Newzdirectory = newzdirectory;
 newzgrabber.BatchFilename = batchfilename;
 newzgrabber.NewzInifilename = newzinifilename;
 newzgrabber.nf = newsFactoryMock;
 Newzgrabber.IsBatch = true;
 Newzgrabber b = new Newzgrabber();
 b.setupBasicSystem();
 b.setBatchDriver(batchDriverMock);
 b.init();

 // Then
 assertEquals(2, b.getBatchGroups().length);
 for (int i = 0; i < 2; i++) {
 assertEquals(batchGroups[i], b.getBatchGroups()[i]);
 }

 for (int i = 0; i < 2; i++) {
 BatchJob batchJob = b.getBatchJobs(batchGroups[i]);
 assertEquals(batchGroups[i], batchJob.getGroup());
 assertEquals("test_server", batchJob.getServer());
 assertEquals("test_directory", batchJob.getDirectory());
 }
 }

 @Test
 public void testNewzgrabberWithNullBatchJob() {
 // Given
 String newzdirectory = "test_newz_directory";
 String batchfilename = "test_batch_file.ini";
 String newzinifilename = "test_newz_ini_file.ini";
 String group1 = "group1";
 String[] batchGroups = { group1 };
 BatchJob nullBatchJob = null;
 when(batchDriverMock.getBatchJobs(eq(group1))).thenReturn(new BatchJob[0]);
 when(batchDriverMock.getBatchJob(eq(group1), anyInt())).thenReturn(nullBatchJob);

 // When
 newzgrabber.Newzdirectory = newzdirectory;
 newzgrabber.BatchFilename = batchfilename;
 newzgrabber.NewzInifilename = newzinifilename;
 newzgrabber.nf = newsFactoryMock;
 Newzgrabber.IsBatch = true;
 Newzgrabber b = new Newzgrabber();
 b.setupBasicSystem();
 b.setBatchDriver(batchDriverMock);
 b.init();

 // Then
 assertNull(b.getBatchJobs(group1)[0]);
 }

 @Test(expected = NullPointerException.class)
 public void testNewzgrabberWithNullLineData() {
 // Given
 String newzdirectory = "test_newz_directory";
 String batchfilename = "test_batch_file.ini";
 String newzinifilename = "test_newz_ini_file.ini";
 String group1 = "group1";
 String[] batchGroups = { group1 };
 LineData nullLineData = null;
 when(newsFactoryMock.newLineData(anyString(), any(String[].class))).thenReturn(nullLineData);

 // When & Then
 try {
 Newzgrabber newzgrabber = new Newzgrabber();
 newzgrabber.Newzdirectory = newzdirectory;
 newzgrabber.BatchFilename = batchfilename;
 newzgrabber.NewzInifilename = newzinifilename;
 newzgrabber.nf = newsFactoryMock;
 newzgrabber.init();
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testUpdateIniFile() {
 // Given
 String inifile = "test_ini_file.ini";
 String prop = "Messageids";
 String key = "test_key";
 String value = "test_value";

 // When
 newzgrabber.updateIni(inifile, prop, key, value);

 // Then
 File iniFile = new File(inifile);
 String content = readFile(iniFile);
 String expectedContent = "Messageids=\n" +
                         key + "=\t" + value + "\n";
 assertEquals(expectedContent, content);
 }

 private String readFile(File file) {
 String content = "";
 try (BufferedReader br = new BufferedReader(new FileReader(file))) {
 String line;
 while ((line = br.readLine()) != null) {
 content += line;
 content += System.lineSeparator();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 return content;
 }
}