import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private CpioArchiveOutputStream cpioStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        out = new FileOutputStream("test.cpio"); // this should never be executed in a real test
        cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioStream != null) {
            cpioStream.close();
        }
        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntry() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Hello World!";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testContent.length());
        cpioStream.putNextEntry(entry);

        byte[] testBytes = testContent.getBytes(StandardCharsets.UTF_8);
        cpioStream.write(testBytes, 0, testBytes.length);
        cpioStream.closeArchiveEntry();

        byte[] expectedCpioHeader = new byte[17];
        CpioUtil.long2byteArray(CpioConstants.MAGIC_NEW, 0, 4, false);
        System.arraycopy(CpioUtil.long2byteArray(1, 0, 8, false), 4, expectedCpioHeader, 4, 8);
        System.arraycopy(CpioUtil.long2byteArray(CpioConstants.PERMISSIONS_READABLE, 0, 8, false), 12, expectedCpioHeader, 12, 8);
        System.arraycopy(CpioUtil.long2byteArray(1001, 0, 8, false), 20, expectedCpioHeader, 20, 8);
        System.arraycopy(CpioUtil.long2byteArray(1001, 0, 8, false), 28, expectedCpioHeader, 28, 8);
        System.arraycopy(CpioUtil.long2byteArray(1, 0, 8, false), 36, expectedCpioHeader, 36, 8);
        System.arraycopy(CpioUtil.long2byteArray(System.currentTimeMillis(), 0, 8, false), 44, expectedCpioHeader, 44, 8);
        System.arraycopy(CpioUtil.long2byteArray(testContent.length(), 0, 8, false), 52, expectedCpioHeader, 52, 8);
        System.arraycopy(CpioUtil.long2byteArray(0, 0, 4, false), 60, expectedCpioHeader, 60, 4);

        assertEquals("CPIO header is not correct", expectedCpioHeader, byteArrayOutputStream.toByteArray());

        Map<String, CpioArchiveEntry> entries = cpioStream.getEntries();
        assertNotNull("No entry found for testFile.txt", entries.get(testFileName));
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        cpioStream = new CpioArchiveOutputStream(out, (short) 0xFF);
        assertThrows(IllegalArgumentException.class, () -> cpioStream.putNextEntry(new CpioArchiveEntry()));
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Hello World!";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testContent.length());
        cpioStream.putNextEntry(entry);

        byte[] testBytes = testContent.getBytes(StandardCharsets.UTF_8);
        cpioStream.write(testBytes, 0, testBytes.length);
        cpioStream.closeArchiveEntry();

        assertThrows(IOException.class, () -> cpioStream.putNextEntry(entry));
    }

    @Test
    public void testPutNextEntryWithNullName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        assertThrows(NullPointerException.class, () -> cpioStream.putNextEntry(entry));
    }

    @Test
    public void testPutNextEntryWithEmptyName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("");
        assertThrows(IllegalArgumentException.class, () -> cpioStream.putNextEntry(entry));
    }

    @Test
    public void testPutNextEntryWithNegativeSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testFile.txt");
        entry.setSize(-1);
        assertThrows(IllegalArgumentException.class, () -> cpioStream.putNextEntry(entry));
    }

    @Test
    public void testFinish() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Hello World!";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testContent.length());
        cpioStream.putNextEntry(entry);

        byte[] testBytes = testContent.getBytes(StandardCharsets.UTF_8);
        cpioStream.write(testBytes, 0, testBytes.length);

        cpioStream.finish();
        byte[] expectedTrailer = new byte[16];
        System.arraycopy(CpioUtil.long2byteArray(CpioConstants.MAGIC_NEW_CRC, 0, 4, false), 0, expectedTrailer, 0, 4);

        assertEquals("Trailer is not correct", expectedTrailer, byteArrayOutputStream.toByteArray().subArray(0, 16));
        assertNull("Entry should not be present after finish", cpioStream.getNextEntry());
    }

    @Test
    public void testWrite() throws IOException {
        String testFileName = "testFile.txt";
        String testContent = "Test";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setSize(testContent.length());
        cpioStream.putNextEntry(entry);

        byte[] testBytes = testContent.getBytes(StandardCharsets.UTF_8);
        cpioStream.write(testBytes, 0, testBytes.length);

        byte[] expectedContent = new byte[testBytes.length];
        System.arraycopy(testBytes, 0, expectedContent, 0, testBytes.length);

        byte[] output = byteArrayOutputStream.toByteArray();
        for (int i = 0; i < output.length; i++) {
            assertEquals("Unexpected byte at position [" + i + "]", expectedContent[i % testBytes.length], output[i]);
        }
    }
}