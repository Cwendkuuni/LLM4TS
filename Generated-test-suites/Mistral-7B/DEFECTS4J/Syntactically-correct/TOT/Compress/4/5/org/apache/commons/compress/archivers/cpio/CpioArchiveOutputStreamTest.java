import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtils;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "test.cpio";
    private static final String TEST_ENTRY_NAME = "test.txt";
    private static final String TEST_CONTENTS = "Test Contents";
    private static final int TEST_FILE_SIZE = TEST_CONTENTS.getBytes().length;

    private CpioArchiveOutputStream out;
    private File testFile;

    @Before
    public void setUp() throws IOException {
        testFile = new File(TEST_FILE_NAME);
        out = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (out != null) {
            out.finish();
            out.close();
            testFile.delete();
        }
    }

    @Test
    public void testWriteFile() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        entry.setSize(TEST_FILE_SIZE);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        out.write(TEST_CONTENTS.getBytes());
        out.closeArchiveEntry();
        byte[] testData = byteArrayOS.toByteArray();
        assertTrue(testData.length == TEST_FILE_SIZE);
        File testFile = new File(TEST_FILE_NAME);
        byte[] fileData = IOUtils.toByteArray(testFile);
        assertEquals(testData, fileData);
    }

    @Test(expected = IOException.class)
    public void testWriteMultipleEntriesWithSameName() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(TEST_ENTRY_NAME);
        entry1.setSize(TEST_FILE_SIZE);
        entry1.setFormat(CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(entry1);
        ByteArrayOutputStream byteArrayOS1 = new ByteArrayOutputStream();
        out.write(TEST_CONTENTS.getBytes());
        out.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(TEST_ENTRY_NAME);
        entry2.setSize(TEST_FILE_SIZE);
        entry2.setFormat(CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(entry2);

        ByteArrayOutputStream byteArrayOS2 = new ByteArrayOutputStream();
        out.write(TEST_CONTENTS.getBytes());
        out.closeArchiveEntry();

        out.finish();
        out.close();
    }

    @Test(expected = IOException.class)
    public void testWriteFileWithInvalidEntryFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        entry.setSize(TEST_FILE_SIZE);
        entry.setFormat((short) 123);
        out.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteFileWithNullName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(null);
        entry.setSize(TEST_FILE_SIZE);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteFileWithNegativeSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        entry.setSize(-1);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteFileWithWrongMagicNumber() throws IOException {
        File testFile = new File(TEST_FILE_NAME + "_invalid");
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
        byte[] invalidMagicNumber = {(byte) 0xFA, (byte) 0xBE};
        out.write(invalidMagicNumber);
        out.finish();
        out.close();
    }

    @Test(expected = IOException.class)
    public void testWriteFileWithWrongFormat() throws IOException {
        File testFile = new File(TEST_FILE_NAME + "_invalid");
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(testFile), (short) 123);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        entry.setSize(TEST_FILE_SIZE);
        entry.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        out.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        out.write(TEST_CONTENTS.getBytes());
        out.closeArchiveEntry();
        out.finish();
        out.close();
    }

    @Test(expected = IOException.class)
    public void testWriteFileWithWrongChecksum() throws IOException {
        File testFile = new File(TEST_FILE_NAME + "_invalid");
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(testFile), CpioConstants.FORMAT_NEW_CRC);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        entry.setSize(TEST_FILE_SIZE);
        entry.setFormat(CpioConstants.FORMAT_NEW);
        out.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        out.write(TEST_CONTENTS.getBytes());
        byte[] testData = byteArrayOS.toByteArray();
        out.closeArchiveEntry();
        out.setCrc(0);
        out.finish();
        out.close();
        File testFile2 = new File(TEST_FILE_NAME + "_invalid2");
        CpioArchiveOutputStream out2 = new CpioArchiveOutputStream(
                new FileOutputStream(testFile2), CpioConstants.FORMAT_NEW_CRC);
        out2.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOS2 = new ByteArrayOutputStream();
        out2.write(testData);
        byte[] testData2 = byteArrayOS2.toByteArray();
        out2.closeArchiveEntry();
        out2.finish();
        out2.close();
        File testFile3 = new File(TEST_FILE_NAME + "_invalid3");
        ByteArrayOutputStream byteArrayOS3 = new ByteArrayOutputStream();
        byte[] invalidChecksum = {(byte) 0xFA, (byte) 0xBE};
        byteArrayOS3.write(invalidChecksum);
        byte[] testData3 = byteArrayOS3.toByteArray();
        CpioArchiveOutputStream out3 = new CpioArchiveOutputStream(
                new FileOutputStream(testFile3), CpioConstants.FORMAT_NEW_CRC);
        out3.putNextEntry(entry);
        out3.write(testData3);
        out3.finish();
        out3.close();
    }

    @Test
    public void testWriteFileWithDifferentFormats() throws IOException {
        File testFile = new File(TEST_FILE_NAME + "_different_formats");
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(
                new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(TEST_ENTRY_NAME);
        entry.setSize(TEST_FILE_SIZE);
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        out.putArchiveEntry(entry);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        out.write(TEST_CONTENTS.getBytes());
        out.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(TEST_ENTRY_NAME + "_2");
        entry2.setSize(TEST_FILE_SIZE);
        entry2.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        out.putArchiveEntry(entry2);

        ByteArrayOutputStream byteArrayOS2 = new ByteArrayOutputStream();
        out.write(TEST_CONTENTS.getBytes());
        out.closeArchiveEntry();

        out.finish();
        out.close();

        File testFile2 = new File(TEST_FILE_NAME + "_different_formats_2");
        CpioArchiveOutputStream out2 = new CpioArchiveOutputStream(
                new FileOutputStream(testFile2), CpioConstants.FORMAT_OLD_ASCII);
        CpioArchiveEntry entry3 = new CpioArchiveEntry();
        entry3.setName(TEST_ENTRY_NAME + "_3");
        entry3.setSize(TEST_FILE_SIZE);
        entry3.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        out2.putArchiveEntry(entry3);
        ByteArrayOutputStream byteArrayOS3 = new ByteArrayOutputStream();
        out2.write(TEST_CONTENTS.getBytes());
        out2.closeArchiveEntry();

        out2.finish();
        out2.close();

        Map<String, CpioArchiveEntry> entries = out.getEntryMap();
        assertEquals(2, entries.size());
        CpioArchiveEntry entry4 = entries.get(TEST_ENTRY_NAME);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, entry4.getFormat());
        CpioArchiveEntry entry5 = entries.get(TEST_ENTRY_NAME + "_2");
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, entry5.getFormat());

        File testFile3 = new File(TEST_FILE_NAME + "_different_formats_3");
        CpioArchiveOutputStream out3 = new CpioArchiveOutputStream(
                new FileOutputStream(testFile3), CpioConstants.FORMAT_OLD_BINARY);
        CpioArchiveEntry entry6 = new CpioArchiveEntry();
        entry6.setName(TEST_ENTRY_NAME + "_4");
        entry6.setSize(TEST_FILE_SIZE);
        entry6.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        out3.putArchiveEntry(entry6);
        ByteArrayOutputStream byteArrayOS4 = new ByteArrayOutputStream();
        out3.write(TEST_CONTENTS.getBytes());
        out3.closeArchiveEntry();

        out3.finish();
        out3.close();

        Map<String, CpioArchiveEntry> entries3 = out3.getEntryMap();
        assertEquals(2, entries3.size());
        CpioArchiveEntry entry7 = entries3.get(TEST_ENTRY_NAME + "_3");
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, entry7.getFormat());
        CpioArchiveEntry entry8 = entries3.get(TEST_ENTRY_NAME + "_4");
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, entry8.getFormat());
    }
}