import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidValue() throws IOException {
        tarOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        assertFalse(tarOutputStream.isOpen());

        byte[] expected = new byte[1024]; // 2 * 512
        byte[] actual = outputStream.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameWithoutLongFileMode() throws IOException {
        String longFileName = ArchiveUtils.generateLongFileName("test");
        tarOutputStream.putArchiveEntry(new TarArchiveEntry(longFileName));
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameWithLongFileMode() throws IOException {
        String longFileName = ArchiveUtils.generateLongFileName("test");
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);

        byte[] actual = outputStream.toByteArray();
        int longLinkIndex = -1;

        for (int i = 0; i < actual.length; i += 512) {
            byte[] record = new byte[512];
            System.arraycopy(actual, i, record, 0, record.length);
            int headerType = TarBuffer.getHeaderType(record);

            if (headerType == TarConstants.GNU_HDR_LONGNAME) {
                longLinkIndex = i;
                break;
            }
        }

        assertNotNull(longLinkIndex);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        String testData = "This is a test.";
        byte[] testDataBytes = testData.getBytes();

        tarOutputStream.write(testDataBytes, 0, testDataBytes.length);
        tarOutputStream.closeArchiveEntry();

        byte[] actual = outputStream.toByteArray();

        int index = 0;
        byte[] header = new byte[512];
        System.arraycopy(actual, index, header, 0, 512);
        int headerType = TarBuffer.getHeaderType(header);

        if (headerType != TarConstants.TAR_HDR_REGULAR) {
            fail("Unexpected header type: " + headerType);
        }

        int nameLength = TarBuffer.getFileNameLength(header);
        assertEquals("test.txt", new String(Arrays.copyOfRange(actual, index, index + nameLength)));

        index += nameLength + 512;

        byte[] data = new byte[testDataBytes.length];
        System.arraycopy(actual, index, data, 0, testDataBytes.length);
        assertArrayEquals(testDataBytes, data);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[entry.getSize() + 1];
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();
    }
}