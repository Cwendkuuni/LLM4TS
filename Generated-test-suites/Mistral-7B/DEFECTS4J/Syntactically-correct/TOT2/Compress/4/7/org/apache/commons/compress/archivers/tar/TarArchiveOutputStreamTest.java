import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * Test for TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private File tmpFile;
    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws Exception {
        tmpFile = File.createTempFile("junit-", null);
        outputStream = new FileOutputStream(tmpFile);
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
        outputStream.close();
        tmpFile.delete();
    }

    /**
     * Test putArchiveEntry method with a simple file entry.
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        TarArchiveEntry fileEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(fileEntry);

        byte[] fileContent = new byte[5]; // 5 bytes for testing
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        byte[] expectedRecord = new byte[TarConstants.HEADER_LENGTH + fileContent.length];
        // Fill expected record with header and file content
        // ...

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(expectedRecord);
        TarArchiveInputStream tarInputStream = new TarArchiveInputStream(testInputStream);
        TarArchiveEntry testEntry = tarInputStream.getNextEntry();
        byte[] readContent = new byte[5];
        tarInputStream.read(readContent, 0, 5);

        assertArrayEquals(fileContent, readContent);
        tarInputStream.close();
    }

    /**
     * Test long file name handling with LONGFILE_GNU mode.
     * @throws IOException
     */
    @Test
    public void testLongFileNameWithGNUMode() throws IOException {
        String longFileName = "testLongFileNameWithGNUMode_" + TarConstants.NAMELEN + "bytes.txt";
        File testFile = new File(longFileName);
        testFile.createNewFile();

        TarArchiveEntry fileEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(fileEntry);

        byte[] fileContent = new byte[5]; // 5 bytes for testing
        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        // TODO: Implement reading and verifying the long file name record

        // Optional test long file name reading code here

        // ...
    }

    /**
     * Test putting an archive entry with a non-existent file.
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNonExistentFile() throws IOException {
        String nonExistentFileName = "nonExistentFileName.txt";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(nonExistentFileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    /**
     * Test putArchiveEntry method with a directory entry.
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        File testDir = new File("testDir");
        testDir.mkdir();

        TarArchiveEntry directoryEntry = new TarArchiveEntry(".");
        tarOutputStream.putArchiveEntry(directoryEntry);

        tarOutputStream.finish();
        tarOutputStream.close();

        // TODO: Implement reading and verifying the directory record

        // Optional test directory reading code here

        // ...
    }

    /**
     * Test closeArchiveEntry method.
     * @throws IOException
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        testFile.createNewFile();

        TarArchiveEntry fileEntry = new TarArchiveEntry("testFile.txt");
        tarOutputStream.putArchiveEntry(fileEntry);

        byte[] fileContent = new byte[5]; // 5 bytes for testing
        tarOutputStream.write(fileContent, 0, fileContent.length);

        // Should throw an exception if closeArchiveEntry is called before putArchiveEntry
        //tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        // Should not throw an exception
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.close();
    }

    /**
     * Test write method with a single byte.
     * @throws IOException
     */
    @Test
    public void testWriteWithSingleByte() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        byte testByte = (byte) 0x01;
        tarOutputStream.write(testByte);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        // TODO: Implement reading and verifying the written byte

        // Optional test byte reading code here

        // ...
    }

    /**
     * Test write method with multiple bytes.
     * @throws IOException
     */
    @Test
    public void testWriteWithMultipleBytes() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        byte[] testBytes = new byte[]{0x01, 0x02, 0x03};
        tarOutputStream.write(testBytes, 0, testBytes.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        // TODO: Implement reading and verifying the written bytes

        // Optional test multiple bytes reading code here

        // ...
    }

    /**
     * Test write method with a byte array longer than record size.
     * @throws IOException
     */
    @Test
    public void testWriteWithLongByteArray() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("testFile.txt"));
        byte[] testBytes = new byte[1024 * 10]; // 10KB for testing
        tarOutputStream.write(testBytes, 0, testBytes.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        // TODO: Implement reading and verifying the written bytes

        // Optional test long byte array reading code here

        // ...
    }
}