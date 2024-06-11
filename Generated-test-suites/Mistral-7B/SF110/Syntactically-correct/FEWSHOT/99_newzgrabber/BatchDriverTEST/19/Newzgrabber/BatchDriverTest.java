package Newzgrabber;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class BatchDriverTest {

 private Ini mockGroupsIni;
 private Ini mockBatchini;
 private BatchDriver batchDriver;

 @Test
 public void testBatchDriverInitialization() {
 mockGroupsIni = mock(Ini.class);
 mockBatchini = mock(Ini.class);

 when(mockGroupsIni.readIni()).thenReturn(new Hashtable() {{
 put("group1", "1");
 put("group2", "2");
 }});

 when(mockBatchini.readIni()).thenReturn(new Hashtable() {{
 put("Retrieve", "group1,group2");
 put("Options", "server=test.server.com,threads=5,timeout=120");
 }});

 when(mockBatchini.getIniVector()).thenReturn(new Vector() {{
 add(new String[]{"group1", "NewzGrabber"});
 add(new String[]{"group2", "NewzGrabber"});
 }});

 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 assertEquals(2, batchDriver.BatchgroupsVector.size());
 assertEquals("group1", batchDriver.BatchgroupsVector.get(0));
 assertEquals("group2", batchDriver.BatchgroupsVector.get(1));

 verify(mockGroupsIni, times(1)).readIni();
 verify(mockBatchini, times(3)).readIni();
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverInitializationWithNullNewzini() {
 batchDriver = new BatchDriver(null, "batchfile.ini");
 fail("Should throw NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testBatchDriverInitializationWithNullBatchfile() {
 batchDriver = new BatchDriver("newzini.ini", null);
 fail("Should throw NullPointerException");
 }

 @Test(expected = IOException.class)
 public void testBatchDriverInitializationWithInvalidNewzini() throws IOException {
 mockGroupsIni = mock(Ini.class);
 mockBatchini = mock(Ini.class);
 when(mockGroupsIni.readIni()).thenThrow(new IOException("Invalid newzini file"));

 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 fail("Should throw IOException");
 }

 @Test(expected = IOException.class)
 public void testBatchDriverInitializationWithInvalidBatchfile() throws IOException {
 mockGroupsIni = mock(Ini.class);
 mockBatchini = mock(Ini.class);
 when(mockBatchini.readIni()).thenThrow(new IOException("Invalid batchfile"));

 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 fail("Should throw IOException");
 }

 @Test
 public void testGetGroups() {
 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 String[] groups = batchDriver.getGroups();
 assertEquals(2, groups.length);
 assertEquals("group1", groups[0]);
 assertEquals("group2", groups[1]);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetGroupsWithEmptyVector() {
 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 batchDriver.BatchgroupsVector.clear();
 batchDriver.getGroups();
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testGetBatchJob() {
 mockGroupsIni = mock(Ini.class);
 mockBatchini = mock(Ini.class);
 NewsSocket newsSocket = mock(NewsSocket.class);
 when(mockGroupsIni.readIni()).thenReturn(new Hashtable() {{
 put("group1", "server=test.group1.server.com,directory=test.group1.directory");
 put("group2", "server=test.group2.server.com,directory=test.group2.directory");
 }});
 when(mockBatchini.readIni()).thenReturn(new Hashtable() {{
 put("Retrieve", "group1,group2");
 put("Options", "server=test.server.com,username=test,password=test,threads=5,timeout=120");
 }});
 when(mockBatchini.getIniVector()).thenReturn(new Vector() {{
 add(new String[]{"group1", "NewzGrabber"});
 add(new String[]{"group2", "NewzGrabber"});
 }});
 when(newsSocket.authInfo(anyString(), anyString())).thenReturn(true);
 when(newsSocket.genericCommand(anyString())).thenAnswer(i -> new String[]{"Status: 200 OK", "1 test message"});
 when(newsSocket.quit()).thenReturn(true);

 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 BatchJob bj1 = batchDriver.getBatchJob("group1");
 assertEquals("test.group1.server.com", bj1.getServer());
 assertEquals("test.group1.directory", bj1.getDirectory());
 assertEquals(1, bj1.getFirstId());
 assertEquals(1, bj1.getLastId());
 assertEquals("test", bj1.getUsername());
 assertEquals("test", bj1.getPassword());
 assertEquals(5, bj1.getnThreads());
 assertEquals(-1, bj1.getTimeout());

 BatchJob bj2 = batchDriver.getBatchJob("group2");
 assertEquals("test.group2.server.com", bj2.getServer());
 assertEquals("test.group2.directory", bj2.getDirectory());
 assertEquals(0, bj2.getFirstId()); // first id is not set if not in NewsgroupsHash
 assertEquals(1, bj2.getLastId());
 assertEquals(null, bj2.getUsername());
 assertEquals(null, bj2.getPassword());
 assertEquals(5, bj2.getnThreads());
 assertEquals(-1, bj2.getTimeout());
 }

 @Test(expected = NullPointerException.class)
 public void testGetBatchJobWithNullGroup() {
 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 batchDriver.getBatchJob(null);
 fail("Should throw NullPointerException");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testGetBatchJobWithNonExistentGroup() {
 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 batchDriver.getBatchJob("nonExistentGroup");
 fail("Should throw ArrayIndexOutOfBoundsException");
 }

 @Test
 public void testGetSubjectLines() {
 mockGroupsIni = mock(Ini.class);
 mockBatchini = mock(Ini.class);
 NewsSocket newsSocket = mock(NewsSocket.class);
 when(mockGroupsIni.readIni()).thenReturn(new Hashtable() {{
 put("group1", "server=test.group1.server.com,directory=test.group1.directory");
 put("group2", "server=test.group2.server.com,directory=test.group2.directory");
 }});
 when(mockBatchini.readIni()).thenReturn(new Hashtable() {{
 put("Retrieve", "group1,group2");
 put("Options", "server=test.server.com,username=test,password=test,threads=5,timeout=120");
 }});
 when(mockBatchini.getIniVector()).thenReturn(new Vector() {{
 add(new String[]{"group1", "NewzGrabber"});
 add(new String[]{"group2", "NewzGrabber"});
 }});
 when(newsSocket.authInfo(anyString(), anyString())).thenReturn(true);
 when(newsSocket.genericCommand(anyString())).thenAnswer(i -> new String[]{"Status: 200 OK", ""});
 when(newsSocket.quit()).thenReturn(true);

 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals(0, subjectLines.length);
 }

 @Test
 public void testGetSubjectLinesWithSubjects() {
 mockGroupsIni = mock(Ini.class);
 mockBatchini = mock(Ini.class);
 NewsSocket newsSocket = mock(NewsSocket.class);
 when(mockGroupsIni.readIni()).thenReturn(new Hashtable() {{
 put("group1", "server=test.group1.server.com,directory=test.group1.directory");
 put("group2", "server=test.group2.server.com,directory=test.group2.directory");
 }});
 when(mockBatchini.readIni()).thenReturn(new Hashtable() {{
 put("Retrieve", "group1,group2");
 put("Options", "server=test.server.com,username=test,password=test,threads=5,timeout=120");
 }});
 when(mockBatchini.getIniVector()).thenReturn(new Vector() {{
 add(new String[]{"group1", "NewzGrabber"});
 add(new String[]{"group2", "NewzGrabber"});
 }});
 when(newsSocket.authInfo(anyString(), anyString())).thenReturn(true);
 when(newsSocket.genericCommand(anyString())).thenAnswer(i -> new String[]{"Status: 200 OK", "Subject: test1", "Subject: test2"});
 when(newsSocket.quit()).thenReturn(true);

 batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
 String[] subjectLines = batchDriver.getSubjectLines();
 assertNotNull(subjectLines);
 assertEquals(2, subjectLines.length);
 assertEquals("test1", subjectLines[0]);
 assertEquals("test2", subjectLines[1]);
 }
}