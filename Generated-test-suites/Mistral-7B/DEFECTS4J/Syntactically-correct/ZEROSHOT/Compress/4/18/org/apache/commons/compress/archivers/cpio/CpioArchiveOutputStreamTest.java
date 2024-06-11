import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new FileOutputStream(File.createTempFile("test", ".cpio"));
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
            outputStream.close();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Hello, World!";
        File testInputFile = new File(testFileName);
        testInputFile.createNewFile();

        CpioArchiveEntry testEntry = new CpioArchiveEntry(testInputFile, testFileName);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        cpioArchiveOutputStream.write(testFileContent.getBytes());

        cpioArchiveOutputStream.closeArchiveEntry();

        assertNotNull(testEntry);
        assertEquals(testFileName, testEntry.getName());

        byte[] expectedHeader = new byte[128];
        ArchiveUtils.seek(outputStream, 0);
        outputStream.read(expectedHeader, 0, expectedHeader.length);
        byte[] expectedContent = testFileContent.getBytes();

        // Check header
        assertEquals(CpioConstants.MAGIC_NEW, ArchiveUtils.bytes2Int(expectedHeader, 0, 8));
        assertEquals(CpioConstants.C_ISREG, ArchiveUtils.bytes2Int(expectedHeader, 16, 8));
        assertEquals(0, ArchiveUtils.bytes2Int(expectedHeader, 24, 8)); // UID = 0 for test
        assertEquals(0, ArchiveUtils.bytes2Int(expectedHeader, 32, 8)); // GID = 0 for test
        assertEquals(1, ArchiveUtils.bytes2Int(expectedHeader, 40, 8)); // Number of links = 1
        assertEquals(System.currentTimeMillis(), ArchiveUtils.bytes2Long(expectedHeader, 48, 16)); // Time
        assertEquals(testFileContent.length(), ArchiveUtils.bytes2Long(expectedHeader, 64, 16)); // Size
        assertEquals(0, ArchiveUtils.bytes2Int(expectedHeader, 72, 8)); // Device maj
        assertEquals(0, ArchiveUtils.bytes2Int(expectedHeader, 80, 8)); // Device min
        assertEquals(0, ArchiveUtils.bytes2Int(expectedHeader, 88, 8)); // Remote device maj
        assertEquals(0, ArchiveUtils.bytes2Int(expectedHeader, 96, 8)); // Remote device min
        assertEquals(testFileName.length() + 1, ArchiveUtils.bytes2Int(expectedHeader, 104, 16)); // Name length
        assertEquals(testFileContent.length() + ArchiveConstants.TRAILER_SIZE, ArchiveUtils.bytes2Long(expectedHeader, 112, 16)); // Chksum

        // Check content
        ByteArrayOutputStream contentOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = outputStream.read(buffer, 0, buffer.length)) > 0) {
            contentOutputStream.write(buffer, 0, bytesRead);
        }
        byte[] actualContent = contentOutputStream.toByteArray();
        assertEquals(testFileContent, new String(actualContent));

        outputStream.close();
        testInputFile.delete();
    }

}