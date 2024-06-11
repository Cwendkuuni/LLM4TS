package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ZipArchiveInputStreamTest {

    private static final String TEST_FILE = "TestFile.zip";
    private static final String TEST_FILE_CONTENTS = "Hello, World!";
    private ByteArrayOutputStream out;
    private ByteArrayOutputStream expectedDataDescriptors;
    private ByteArrayInputStream testInputStream;
    private ZipArchiveInputStream zipArchiveInputStream;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        expectedDataDescriptors = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);
        ZipArchiveEntry entry = new ZipArchiveEntry("TestFile.txt");
        entry.setSize(-1);
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write(TEST_FILE_CONTENTS.getBytes());
        zipOutputStream.closeEntry();
        zipOutputStream.finish();
        testInputStream = new ByteArrayInputStream(out.toByteArray());
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("TestFile.txt", entry.getName());
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryThrowsIOExceptionWhenNoMoreEntries() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.close();

        ZipArchiveInputStream zipArchiveInputStream2 = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        zipArchiveInputStream2.getNextZipEntry();
    }

    @Test
    public void testRead() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        byte[] buffer = new byte[TEST_FILE_CONTENTS.length()];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(TEST_FILE_CONTENTS.length(), length);
        String actualContent = new String(buffer, 0, length);
        Assert.assertEquals(TEST_FILE_CONTENTS, actualContent);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testReadThrowsIOExceptionWhenNoMoreBytes() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        byte[] buffer = new byte[TEST_FILE_CONTENTS.length()];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(TEST_FILE_CONTENTS.length(), length);
        zipArchiveInputStream.close();
        zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        buffer = new byte[1];
        length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, length);
    }

    @Test
    public void testSkip() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        long skippedBytes = zipArchiveInputStream.skip(TEST_FILE_CONTENTS.length() + 10);
        Assert.assertEquals(TEST_FILE_CONTENTS.length() + 10, skippedBytes);
        zipArchiveInputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipThrowsIllegalArgumentExceptionWhenNegativeValue() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        zipArchiveInputStream.skip(-1);
    }

    @Test
    public void testClose() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        zipArchiveInputStream.close();
        Assert.assertFalse(zipArchiveInputStream.isOpen());
    }

    @Test
    public void testReadFromDataDescriptor() throws IOException {
        // Set up an example zip file with a data descriptor
        ByteArrayOutputStream dataDescriptors = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(new ByteArrayOutputStream());
        ZipArchiveEntry entry = new ZipArchiveEntry("TestFile.txt");
        entry.setSize(-1);
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write(TEST_FILE_CONTENTS.getBytes());
        zipOutputStream.putNextEntry(new ZipArchiveEntry("TestFile.txt.data"));
        zipOutputStream.setMethod(ZipArchiveOutputStream.DEFLATED);
        zipOutputStream.write("TestData".getBytes());
        zipOutputStream.finish();

        zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(zipOutputStream.toByteArray()), ZipEncodingHelper.UTF8, false);
        byte[] buffer = new byte[TEST_FILE_CONTENTS.length()];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(TEST_FILE_CONTENTS.length(), length);
        String actualContent = new String(buffer, 0, length);
        Assert.assertEquals(TEST_FILE_CONTENTS, actualContent);
        length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals("TestData".length(), length);
        String dataDescriptorContent = new String(buffer, 0, length);
        Assert.assertEquals("TestData", dataDescriptorContent);
        zipArchiveInputStream.close();
    }

    @Test
    public void testMatchesSignature() throws IOException {
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
        System.arraycopy(ZipArchiveOutputStream.LFH_SIG, 0, signature, 0, ZipArchiveOutputStream.LFH_SIG.length);
        Assert.assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testCloseEntry() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        Assert.assertNull(entry);
        zipArchiveInputStream.close();
    }

    @Test
    public void testFill() throws IOException {
        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(testInputStream, ZipEncodingHelper.UTF8, false);
        zipArchiveInputStream.fill();
        Assert.assertTrue(zipArchiveInputStream.inf.needsInput());
        zipArchiveInputStream.close();
    }

    @Test
    public void testInvalidCRC32() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);
        ZipArchiveEntry entry = new ZipArchiveEntry("TestFile.txt");
        entry.setSize(-1);
        entry.setCrc(new CRC32().getValue(TEST_FILE_CONTENTS.getBytes()) + 1);
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write(TEST_FILE_CONTENTS.getBytes());
        zipOutputStream.finish();

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(out.toByteArray()), ZipEncodingHelper.UTF8, false);
        Assert.assertFalse(zipArchiveInputStream.getNextZipEntry() != null);
        zipArchiveInputStream.close();
    }

    @Test(expected = ZipException.class)
    public void testInvalidEndOfCentralDirectorySignature() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);
        ZipArchiveEntry endOfCentralDirectoryEntry = new ZipArchiveEntry("EndOfCentralDirectoryMarker.txt");
        endOfCentralDirectoryEntry.setSize(-1);
        endOfCentralDirectoryEntry.setMethod(ZipArchiveOutputStream.STORED);
        zipOutputStream.putNextEntry(endOfCentralDirectoryEntry);
        zipOutputStream.write(new byte[]{0x50, 0x4b, 0x03, 0x04});
        zipOutputStream.finish();

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(out.toByteArray()), ZipEncodingHelper.UTF8, false);
        Assert.assertFalse(zipArchiveInputStream.getNextZipEntry() != null);
        zipArchiveInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidLocalFileHeaderSignature() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);
        ZipArchiveEntry entry = new ZipArchiveEntry("TestFile.txt");
        entry.setSize(-1);
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write(new byte[]{0x4b, 0x50, 0x04, 0x03});
        zipOutputStream.write(TEST_FILE_CONTENTS.getBytes());
        zipOutputStream.finish();

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(out.toByteArray()), ZipEncodingHelper.UTF8, false);
        Assert.assertFalse(zipArchiveInputStream.getNextZipEntry() != null);
        zipArchiveInputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testUnexpectedEndOfStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);
        ZipArchiveEntry entry = new ZipArchiveEntry("TestFile.txt");
        entry.setSize(-1);
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write(TEST_FILE_CONTENTS.getBytes());
        zipOutputStream.finish();

        ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(out.toByteArray()), ZipEncodingHelper.UTF8, false);
        byte[] buffer = new byte[TEST_FILE_CONTENTS.length() + 1];
        int length = zipArchiveInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(TEST_FILE_CONTENTS.length(), length);
        zipArchiveInputStream.close();

        ZipArchiveInputStream zipArchiveInputStream2 = new ZipArchiveInputStream(new ByteArrayInputStream(out.toByteArray()), ZipEncodingHelper.UTF8, false);
        length = zipArchiveInputStream2.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, length);
    }
}