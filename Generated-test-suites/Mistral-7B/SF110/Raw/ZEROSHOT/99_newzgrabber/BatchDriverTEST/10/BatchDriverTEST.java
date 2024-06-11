package Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.ini4j.Ini;
import org.ini4j.Wini;
import org.ini4j.config.FileConfigSource;
import org.ini4j.exceptions.IniFormatException;
import org.junit.Test;

public class BatchDriverTest {

 private static final String NEWZINI_FILE = "path/to/newzini.ini";
 private static final String BATCH_FILE = "path/to/batchfile.ini";

 @Test
 public void testBatchDriverInstance() throws IOException, NullPointerException {
 // Given
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 // Then
 assertNotNull(batchDriver);
 }

 @Test
 public void testGetGroups() throws ArrayIndexOutOfBoundsException {
 // Given
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 // When
 String[] groups = batchDriver.getGroups();
 // Then
 assertEquals(2, groups.length);
 }

 @Test
 public void testGetBatchJob() {
 // Given
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 // When
 BatchJob batchJob = batchDriver.getBatchJob("alt.test.group1");
 // Then
 assertNotNull(batchJob);
 }

 @Test
 public void testGetBatchJobWithInvalidGroup() {
 // Given
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 // When
 BatchJob batchJob = batchDriver.getBatchJob("invalid.group");
 // Then
 assertNull(batchJob);
 }

 @Test
 public void testGetSubjectLines() {
 // Given
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 // When
 String[] subjectLines = batchDriver.getSubjectLines();
 // Then
 assertNotNull(subjectLines);
 assertEquals(5, subjectLines.length);
 }

 @Test
 public void testGetSubjectLinesWithEmptyIniFile() throws IOException, IniFormatException {
 // Given
 FileConfigSource configSource = new FileConfigSource(new File("src/test/resources/empty.ini"));
 Ini ini = new Ini(configSource);
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 // When
 Wini batchGroups = ini.get("Batchgroups");
 batchGroups.remove("Retrieve");
 batchDriver.BatchgroupsHash = batchGroups.readIni();
 String[] subjectLines = batchDriver.getSubjectLines();
 // Then
 assertNull(subjectLines);
 }

 @Test (expected = ArrayIndexOutOfBoundsException.class)
 public void testGetSubjectLinesWithEmptySubjects() {
 // Given
 BatchDriver batchDriver = new BatchDriver(NEWZINI_FILE, BATCH_FILE);
 // When
 String[] subjectLines = batchDriver.getSubjectLines();
 // Then
 assertNull(subjectLines);
 }
}