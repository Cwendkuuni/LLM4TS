import org.apache.commons.compress.archivers.tar.*; // for TarConstants
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.commons.compress.utils.ArchiveUtils.toAsciiBytes;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private OutputStream outputStreamMock;

    @Before
    public void setUp() {
        outputStreamMock = Mockito.mock(OutputStream.class);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStreamMock);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidValue() throws IOException {
        tarArchiveOutputStream.setLongFileMode(1);
    }

    @Test
    public void testFinish() throws IOException {
        // This test assumes that TarArchiveOutputStream writes two EOF blocks

        // Finish the stream without errors
        tarArchiveOutputStream.finish();

        // Verify that exactly two EOF blocks have been written
        Mockito.verify(outputStreamMock, Mockito.times(2)).write(TarConstants. Records.MAGIC_NUMBER_EOF);

        // Finish the stream with an IOException
        Mockito.doThrow(new IOException()).when(outputStreamMock).write(Mockito.anyByte());
        try {
            tarArchiveOutputStream.finish();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Verify that no more bytes have been written to the output stream after the IOException
        Mockito.verify(outputStreamMock, Mockito.times(2)).write(TarConstants. Records.MAGIC_NUMBER_EOF);
        Mockito.verifyNoMoreInteractions(outputStreamMock);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = byteArrayOutputStream;
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        final byte[] inputData = "Hello, world!".getBytes();
        final int size = inputData.length;

        // Write data to the archive
        tarArchiveOutputStream.write(inputData, 0, size);

        // Verify that the data has been written to the output stream
        byte[] outputData = byteArrayOutputStream.toByteArray();
        assertArrayEquals(inputData, outputData);

        // Verify that the archive entry header has been written to the output stream
        byte[] header = new byte[TarConstants.RECORD_SIZE];
        tarArchiveOutputStream.read(header);
        assertEquals(TarConstants.HEADER_MAGIC_NUMBER, 
                header[TarConstants.HEADER_MAGIC_NUMBER_INDEX],
                header[TarConstants.HEADER_MAGIC_NUMBER_INDEX + 1]);
        assertEquals(TarConstants.FILE_TYPE_NORMAL, header[TarConstants.HEADER_FILE_TYPE_INDEX]);
        assertEquals(size + TarConstants.RECORD_SIZE,
                getUnsignedIntFromBytes(header, TarConstants.HEADER_SIZE_INDEX));
        assertEquals(size, getUnsignedIntFromBytes(header, TarConstants.HEADER_SIZE_INDEX + TarConstants.NAME_LENGTH_INDEX));
        assertEquals(size, getUnsignedLongFromBytes(header, TarConstants.HEADER_SIZE_INDEX + TarConstants.NAME_LENGTH_INDEX + TarConstants.MAGIC_NUMBER_INDEX));
        assertEquals(0, getUnsignedIntFromBytes(header, TarConstants.HEADER_CHECKSUM_INDEX));

        // Write data to the archive again
        tarArchiveOutputStream.write(inputData, 0, size);

        // Verify that the data and the archive entry header have been written to the output stream
        outputData = byteArrayOutputStream.toByteArray();
        assertArrayEquals(inputData, Arrays.copyOfRange(outputData, 0, size));
        byte[] archiveEntryHeader = Arrays.copyOfRange(outputData, size, size + TarConstants.RECORD_SIZE);
        assertArrayEquals(header, archiveEntryHeader);
    }

    private static int getUnsignedIntFromBytes(byte[] bytes, int offset) {
        return ((bytes[offset] << 24) & 0xff000000) |
               ((bytes[offset + 1] << 16) & 0xff0000) |
               ((bytes[offset + 2] << 8) & 0xff00) |
               (bytes[offset + 3] & 0xff);
    }

    private static long getUnsignedLongFromBytes(byte[] bytes, int offset) {
        long result = getUnsignedIntFromBytes(bytes, offset) & 0xffffffffL;
        result |= ((long) getUnsignedIntFromBytes(bytes, offset + 4) << 32);
        return result;
    }
}