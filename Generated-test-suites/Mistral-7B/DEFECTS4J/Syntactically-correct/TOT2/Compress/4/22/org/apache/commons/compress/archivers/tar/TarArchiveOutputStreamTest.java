import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * TarArchiveOutputStreamTest class.
 */
public class TarArchiveOutputStreamTest {
    private OutputStream os;
    private TarArchiveOutputStream tarOutputStream;

    /**
     * Sets up test environment.
     * @throws IOException on error
     */
    @Before
    public void setUp() throws IOException {
        os = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(os);
    }

    /**
     * Tests the normal behavior of putArchiveEntry method.
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello TarArchiveOutputStream!".getBytes());
        tarOutputStream.closeArchiveEntry();

        byte[] expectedBytes = new byte[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '73', '65', '63', '72', '65', '74', '73', '74', '6F', '72', '79', '0', '0', '0', '1', '0', '0', '0', '2', '0', '0', '74', '79', '70', '65', '0', '0', '0', '0', '1', '0', '0', '0', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '0', '0', '2', '0', '0', '54', '68', '69', '73', '20', '48', '65', '6C', '6C', '6F', '20', '54', '72', '61', '6E', '61', '6D', '70', '6C', '65', '21', '0', '0', '0', '1', '0', '0', '0', '2', '0', '0', '54', '68', '69', '73', '20', '54', '72', '61', '6E', '61', '6D', '70', '6C', '65', '22', '0', '0', '0', '1', '0', '0', '0', '2', '0', '0', '54', '68', '69', '73', '20', '54', '72', '61', '6E', '61', '6D', '70', '6C', '65', '23', '0', '0', '0'};

        tarOutputStream.finish();

        byte[] actualBytes = os.toByteArray();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Tests the behavior of putArchiveEntry when the file name is too long.
     * @throws IOException on error
     */
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String fileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "test.txt";
        ArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    /**
     * Tests the behavior of putArchiveEntry when long file names are stored as GNU extensions.
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryWithLongFileNameAsGNUExtension() throws IOException {
        String fileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "test.txt";
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry entry = new TarArchiveEntry(fileName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello TarArchiveOutputStream!".getBytes());
        tarOutputStream.closeArchiveEntry();

        byte[] expectedBytes = new byte[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '73', '65', '63', '72', '65', '74', '73', '74', '6F', '72', '79', '0', '0', '0', '2', '0', '0', '1', '0', '0', '0', '2', '0', '0', '54', '68', '69', '73', '0', '0', '0', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '0', '0', '2', '0', '0', '54', '68', '69', '73', '2', '0', '0', '74', '79', '70', '65', '2', '0', '0', '0', '1', '0', '0', '0', '2', '0', '0', '54', '68', '69', '73', '2', '0', '0', '74', '79', '70', '65', '2', '0', '0'};

        tarOutputStream.finish();

        byte[] actualBytes = os.toByteArray();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Closes the output stream and the TarArchiveOutputStream.
     * @throws IOException on error
     */
    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        os.close();
    }
}