package Newzgrabber;

import static org.junit.Assert.*; // for assertions
import static org.mockito.Mockito.mock; // for creating mocks
import static org.mockito.Mockito.when; // for setting up mocks

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Ini;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {

 private BatchDriver batchDriver;
 private String newzini = "test.ini";
 private String batchfile = "test.ini";

 @Before
 public void setup() throws IOException, NullPointerException {
 final Ini groupsini = mock(Ini.class);
 final Ini batchgroups = mock(Ini.class);
 when(groupsini.readIni()).thenReturn(new Hashtable<String, String>() {{
 put("test", "test1");
 put("test2", "test3");
 }});
 when(batchgroups.readIni()).thenReturn(new Hashtable<String, String>() {{
 put("test", "server1 directory1");
 put("test2", "server2 directory2");
 put("Retrieve", "true");
 put("Options", "threads=2 timeout=1000");
 }});
 when(batchgroups.getIniVector()).thenReturn(new Vector<Object>() {{
 add(new String[]{"test", "test1"});
 add(new String[]{"test2", "test2"});
 }});

 batchDriver = new BatchDriver(newzini, batchfile);
 }

 @Test
 public void testGetGroups() {
 final String[] groups = batchDriver.getGroups();
 assertEquals(2, groups.length);
 assertArrayEquals(new String[]{"test1", "test2"}, groups);
 }

 @Test
 public void testGetBatchJobNullGroup() {
 final String group = null;
 final BatchJob bj = batchDriver.getBatchJob(group);
 assertNull(bj);
 }

 @Test
 public void testGetBatchJobValidGroup() {
 final String group = "test";
 final BatchJob bj = batchDriver.getBatchJob(group);
 assertNotNull(bj);
 }

 @Test
 public void testGetBatchJobInvalidGroup() {
 final String group = "invalidGroup";
 final BatchJob bj = batchDriver.getBatchJob(group);
 assertNull(bj);
 }

 @Test
 public void testGetSubjectLines() {
 final String[] expectedSubjectLines = new String[]{"Expected subject line 1", "Expected subject line 2"};
 when(batchDriver.getBatchJob("test").getSubjectLines()).thenReturn(expectedSubjectLines);
 final String[] actualSubjectLines = batchDriver.getBatchJob("test").getSubjectLines();
 assertArrayEquals(expectedSubjectLines, actualSubjectLines);
 }

}