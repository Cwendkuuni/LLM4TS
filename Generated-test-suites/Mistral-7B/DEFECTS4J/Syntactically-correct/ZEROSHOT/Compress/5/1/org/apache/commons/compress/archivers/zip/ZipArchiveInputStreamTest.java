import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ZipArchiveInputStreamTest {

    private static final String TEST_FILE_NAME = "TestZipArchive.zip";
    private static final String TEST_FILE_CONTENTS = "Hello World!";

    private ZipArchiveInputStream zipArchiveInputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        ZipEntry zipEntry = new ZipEntry("test.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(TEST_FILE_NAME);
        zipArchiveOutputStream = new ZipArchiveOutputStream(fileOutputStream);
        zipArchiveOutputStream.putNextEntry(zipEntry);
        zipArchiveOutputStream.write(TEST_FILE_CONTENTS.getBytes());
        zipArchiveOutputStream.finish();
        fileOutputStream.close();

        byteArrayInputStream = new ByteArrayInputStream(
                TEST_FILE_CONTENTS.getBytes());
        zipArchiveInputStream = new ZipArchiveInputStream(byteArrayInputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (zipArchiveInputStream != null) {
            zipArchiveInputStream.close();
        }
        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }
        if (byteArrayInputStream != null) {
            byteArrayInputStream.close();
        }
        if (TEST_FILE_NAME != null) {
            deleteFile(TEST_FILE_NAME);
        }
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry zipEntry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(zipEntry);
        assertEquals("test.txt", zipEntry.getName());
        assertEquals("Hello World!", zipEntry.getExtra().toString());
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextZipEntryWithClosedInputStream() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextZipEntry();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry archiveEntry = zipArchiveInputStream.getNextEntry();
        assertSame(zipEntry, archiveEntry);
        assertNull(zipArchiveInputStream.getNextEntry());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithClosedInputStream() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.getNextEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = zipArchiveInputStream.read(buffer);
        assertEquals(2, bytesRead);
        assertEquals("He", new String(buffer, 0, bytesRead));

        bytesRead = zipArchiveInputStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertEquals("ll", new String(buffer, 1, bytesRead));

        bytesRead = zipArchiveInputStream.read(buffer, 4, 1);
        assertEquals(0, bytesRead);

        bytesRead = zipArchiveInputStream.read(buffer);
        assertEquals(1, bytesRead);
        assertEquals("!", new String(buffer, 0, bytesRead));

        bytesRead = zipArchiveInputStream.read(buffer);
        assertEquals(-1, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testReadWithClosedInputStream() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.read(new byte[1]);
    }

    @Test
    public void testSkip() throws IOException {
        long value = 6;
        zipArchiveInputStream.skip(value);
        assertEquals(5, zipArchiveInputStream.read(new byte[5]));
        assertEquals(-1, zipArchiveInputStream.read(new byte[1]));
    }

    @Test
    public void testCloseEntry() throws IOException {
        ZipArchiveEntry zipEntry = zipArchiveInputStream.getNextZipEntry();
        zipArchiveInputStream.closeEntry();
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseEntryWithClosedInputStream() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.closeEntry();
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
        byte[] expected = ZipArchiveOutputStream.LFH_SIG;
        assertTrue(ZipArchiveInputStream.matches(signature, expected.length));
        System.arraycopy(expected, 0, signature, 0, expected.length);

        expected = ZipArchiveOutputStream.EOCD_SIG;
        assertTrue(ZipArchiveInputStream
                .matches(signature, expected.length));
    }

    @Test(expected = IOException.class)
    public void testReadFullyWithClosedInputStream() throws IOException {
        zipArchiveInputStream.close();
        zipArchiveInputStream.readFully(new byte[1]);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[10];
        zipArchiveInputStream.readFully(buffer);
        assertEquals("Hello Wor", new String(buffer, 0, 7));
    }

    @Test
    public void testFill() throws IOException {
        zipArchiveInputStream.fill();
        assertEquals(2, zipArchiveInputStream.read(new byte[2]));
        assertEquals(-1, zipArchiveInputStream.read(new byte[1]));
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }

    private static void deleteFile(String filePath) {
        if (filePath != null) {
            java.io.File file = new java.io.File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}