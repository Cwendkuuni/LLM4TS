import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ZipArchiveInputStreamTest {

    private static final String TEST_ZIP_FILE = "TestZipFile.zip";
    private static final String TEST_ZIP_CONTENT = "TestZipContent.txt";

    private File testZipFile;
    private File testZipContent;
    private ZipArchiveInputStream testZipArchiveInputStream;
    private ZipEntry testZipEntry;

    @Before
    public void setUp() throws Exception {
        testZipFile = new File(TEST_ZIP_FILE);
        testZipContent = new File(TEST_ZIP_CONTENT);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] content = "Test content for ZipArchiveInputStreamTest".getBytes(StandardCharsets.UTF_8);
        byteArrayOutputStream.write(content, 0, content.length);
        byteArrayOutputStream.close();
        byte[] zipFileContent = byteArrayOutputStream.toByteArray();

        FileOutputStream testZipOutputStream = new FileOutputStream(testZipFile);
        testZipOutputStream.write(ZipFile.SIG.toByteArray());
        testZipOutputStream.write(ZipShort.createCentralDirectoryRecord(
                "Test Zip File",
                1,
                ZipEncodingHelper.UTF8,
                null,
                0,
                0,
                ZipEncodingHelper.UTF8
        ).toByteArray());
        testZipOutputStream.write(new ZipShort(6).toByteArray()); // number of entries
        testZipOutputStream.write(new ZipShort(1).toByteArray()); // this entry

        testZipOutputStream.write(new ZipLong(ZipEntry.STORED).toByteArray());
        testZipOutputStream.write("TestZipContent.txt".getBytes(StandardCharsets.UTF_8));
        testZipOutputStream.write(new ZipShort(ZipFile.DEFLATED).toByteArray());
        testZipOutputStream.write(new ZipShort(0).toByteArray());
        testZipOutputStream.write(new ZipShort(content.length).toByteArray());
        testZipOutputStream.write(new ZipShort(content.length).toByteArray());

        testZipOutputStream.write(content);
        testZipOutputStream.close();

        testZipArchiveInputStream = new ZipArchiveInputStream(new FileInputStream(testZipFile));
        testZipEntry = testZipArchiveInputStream.getNextZipEntry();
    }

    @After
    public void tearDown() throws Exception {
        if (testZipArchiveInputStream != null) {
            testZipArchiveInputStream.close();
        }
        if (testZipFile != null) {
            testZipFile.delete();
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(testZipArchiveInputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        assertNotNull(testZipEntry);
        assertEquals("TestZipContent.txt", testZipEntry.getName());
        assertNull(testZipArchiveInputStream.getNextZipEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[testZipEntry.getSize()];
        int bytesRead = testZipArchiveInputStream.read(buffer);
        assertEquals(testZipEntry.getSize(), bytesRead);
        assertEquals(testZipContent.getBytes(StandardCharsets.UTF_8), buffer);
    }

    @Test
    public void testReadWithClosedStream() throws IOException {
        testZipArchiveInputStream.close();
        assertThrows(IOException.class, () -> testZipArchiveInputStream.read(new byte[0]));
    }

    @Test
    public void testReadWithEmptyFile() throws IOException {
        testZipArchiveInputStream = new ZipArchiveInputStream(new FileInputStream(new File("empty.zip")));
        assertFalse(testZipArchiveInputStream.hasNextEntry());
        assertThrows(IOException.class, testZipArchiveInputStream::read);
    }

    @Test
    public void testReadWithCorruptedZipFile() throws IOException {
        testZipArchiveInputStream = new ZipArchiveInputStream(new FileInputStream(new File("corrupted.zip")));
        assertThrows(ZipException.class, testZipArchiveInputStream::getNextZipEntry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        assertEquals(testZipEntry, testZipArchiveInputStream.getNextEntry());
        assertNull(testZipArchiveInputStream.getNextEntry());
    }

    @Test
    public void testClose() throws IOException {
        testZipArchiveInputStream.close();
        assertFalse(testZipArchiveInputStream.hasNextEntry());
    }

    @Test
    public void testCloseTwice() throws IOException {
        testZipArchiveInputStream.close();
        testZipArchiveInputStream.close();
    }

    @Test
    public void testSkip() throws IOException {
        long position = testZipEntry.getSize() + 1;
        testZipArchiveInputStream.skip(position);
        assertEquals(position, testZipArchiveInputStream.getFilePointer());
    }

    @Test
    public void testCloseAndReadAgain() throws IOException {
        byte[] buffer = new byte[testZipEntry.getSize()];
        testZipArchiveInputStream.read(buffer);
        testZipArchiveInputStream.close();
        testZipArchiveInputStream = new ZipArchiveInputStream(new FileInputStream(testZipFile));
        testZipArchiveInputStream.read(buffer);
        assertEquals(testZipContent.getBytes(StandardCharsets.UTF_8), buffer);
    }
}