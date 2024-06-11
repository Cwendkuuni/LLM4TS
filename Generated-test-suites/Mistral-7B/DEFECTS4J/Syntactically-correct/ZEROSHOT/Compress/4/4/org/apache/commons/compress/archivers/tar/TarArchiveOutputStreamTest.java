package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

 private OutputStream outputStream;
 private ArchiveOutputStream archiveOutputStream;
 private TarArchiveOutputStream tarArchiveOutputStream;

 @Before
 public void setUp() throws IOException {
 outputStream = new ByteArrayOutputStream();
 archiveOutputStream = new ArchiveOutputStream(outputStream);
 tarArchiveOutputStream = new TarArchiveOutputStream(archiveOutputStream);
 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 }

 @After
 public void tearDown() throws IOException {
 archiveOutputStream.finish();
 archiveOutputStream.close();
 }

 @Test
 public void testConstructor() {
 // Check default constructor
 TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(new ByteArrayOutputStream());

 // Check constructor with block size and record size
 TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(),
 TarBuffer.DEFAULT_BLKSIZE, TarBuffer.DEFAULT_RCDSIZE);
 }

 @Test
 public void testSetLongFileMode() {
 TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
 assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());

 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
 assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

 tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
 assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
 }

 @Test
 public void testFinish() throws IOException {
 // Test empty archive
 archiveOutputStream.finish();
 assertTrue(outputStream.size() >= 2 * TarConstants.BLOCK_SIZE);

 // Test archive with one entry
archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test content".getBytes()));
archiveOutputStream.write("test content".getBytes());
archiveOutputStream.closeArchiveEntry();
archiveOutputStream.finish();

int expectedSize = 1 * TarConstants.BLOCK_SIZE + 2 * TarConstants.BLOCK_SIZE;
assertTrue(outputStream.size() >= expectedSize);
 }

 @Test
 public void testClose() throws IOException {
 // Test empty archive
 archiveOutputStream.close();
 assertFalse(outputStream.hasAvailableBytes());

 // Test archive with one entry
 archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt", "test content".getBytes()));
 archiveOutputStream.write("test content".getBytes());
 archiveOutputStream.closeArchiveEntry();
 archiveOutputStream.close();
 assertFalse(outputStream.hasAvailableBytes());
 }

 @Test
 public void testGetRecordSize() {
 assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
 }

 @Test
 public void testPutArchiveEntry() throws IOException {
 // Test empty archive
 assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.putArchiveEntry(null));
 assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt")));

 // Test archive with one entry
 TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes());
 archiveOutputStream.putArchiveEntry(archiveEntry);
 assertNull(tarArchiveOutputStream.getNextEntry());
 assertEquals(archiveEntry.getName(), tarArchiveOutputStream.getCurrentName());
 assertEquals(archiveEntry.getSize(), tarArchiveOutputStream.getCurrentSize());
 assertEquals(0, tarArchiveOutputStream.getCurrentBytes());

 // Test archive with multiple entries
 archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test1.txt", "test1 content".getBytes()));
 archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test2.txt", "test2 content".getBytes()));
 TarArchiveEntry nextEntry = tarArchiveOutputStream.getNextEntry();
 assertEquals("test1.txt", nextEntry.getName());
 assertEquals(0, tarArchiveOutputStream.getCurrentBytes());
 assertNull(tarArchiveOutputStream.getCurrentName());
 nextEntry = tarArchiveOutputStream.getNextEntry();
 assertEquals("test2.txt", nextEntry.getName());
 assertEquals(0, tarArchiveOutputStream.getCurrentBytes());
 assertNull(tarArchiveOutputStream.getCurrentName());
 assertNull(tarArchiveOutputStream.getNextEntry());
 }

 @Test
 public void testCloseArchiveEntry() throws IOException {
 // Test empty archive
 assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.closeArchiveEntry());

 // Test archive with one entry
 TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes());
 archiveOutputStream.putArchiveEntry(archiveEntry);
 archiveOutputStream.write("test content".getBytes());
 archiveOutputStream.closeArchiveEntry();
 assertEquals(archiveEntry.getName(), tarArchiveOutputStream.getCurrentName());
 assertEquals(archiveEntry.getSize(), tarArchiveOutputStream.getCurrentSize());
 assertEquals(-1, tarArchiveOutputStream.getCurrentBytes());
 assertNull(tarArchiveOutputStream.getNextEntry());
 }

 @Test
 public void testWrite() throws IOException {
 // Test empty archive
 assertThrows(IllegalStateException.class, () -> tarArchiveOutputStream.write(new byte[0]));

 // Test archive with one entry
 TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes());
 archiveOutputStream.putArchiveEntry(archiveEntry);
 archiveOutputStream.write("test content".getBytes());
 archiveOutputStream.closeArchiveEntry();

 ByteArrayInputStream inputStream = new ByteArrayInputStream("test content".getBytes());
 TarArchiveEntry nextEntry = tarArchiveOutputStream.getNextEntry();
 byte[] buffer = new byte[archiveEntry.getSize()];
 int numRead = -1;
 while ((numRead = inputStream.read(buffer)) != -1) {
 tarArchiveOutputStream.write(buffer, 0, numRead);
 }
 assertNull(tarArchiveOutputStream.getNextEntry());
 assertFalse(outputStream.hasAvailableBytes());
 }

 @Test
 public void testWriteWithUnclosedEntry() throws IOException {
 // Test archive with one entry
 TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt", "test content".getBytes());
 archiveOutputStream.putArchiveEntry(archiveEntry);
 archiveOutputStream.write("test content".getBytes());

 // Test write with unclosed entry
 assertThrows(IOException.class, () -> tarArchiveOutputStream.write("test content".getBytes()));
 archiveOutputStream.closeArchiveEntry();
 }
 }