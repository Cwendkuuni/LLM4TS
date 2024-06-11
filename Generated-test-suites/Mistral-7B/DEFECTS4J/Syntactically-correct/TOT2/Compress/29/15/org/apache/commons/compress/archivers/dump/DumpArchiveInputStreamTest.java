import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.compressors.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.GzipUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DumpArchiveInputStream.class})
public class DumpArchiveInputStreamTest {

    private InputStream testInputStream;
    private DumpArchiveInputStream testInputStreamInstance;

    @Before
    public void setUp() throws IOException {
        testInputStream = mock(InputStream.class);
        when(testInputStream.readAllBytes()).thenReturn(getTestBytes());
        testInputStreamInstance = new DumpArchiveInputStream(testInputStream);
    }

    @Test
    public void testMatches() throws IOException {
        byte[] testBytes = getTestBytes();
        assertTrue(DumpArchiveInputStream.matches(testBytes, testBytes.length));
    }

    @Test(expected = UnrecognizedFormatException.class)
    public void testUnrecognizedFormatException() throws IOException {
        byte[] testBytesWithInvalidFormat = getTestBytesWithInvalidFormat();
        new DumpArchiveInputStream(new ByteArrayInputStream(testBytesWithInvalidFormat));
    }

    @Test(expected = ArchiveException.class)
    public void testArchiveException() throws IOException {
        doReturn(-1).when(testInputStream).read(anyByteArray(), anyInt(), anyInt());
        new DumpArchiveInputStream(testInputStream);
    }

    @Test(expected = EOFException.class)
    public void testEOFException() throws IOException {
        doThrow(new EOFException()).doAnswer(invocationOnMock -> testInputStream.read(anyByteArray(), anyInt(), anyInt())).when(testInputStream)
                .read(anyByteArray(), anyInt(), anyInt());
        new DumpArchiveInputStream(testInputStream);
    }

    @Test
    public void testGetSummary() {
        DumpArchiveSummary testSummary = testInputStreamInstance.getSummary();
        assertNotNull(testSummary);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry testNextEntry = testInputStreamInstance.getNextEntry();
        assertNotNull(testNextEntry);
    }

    @Test
    public void testGetNextEntryWithNullReturn() throws IOException {
        doReturn(null).when(testInputStream).readRecord();
        DumpArchiveEntry testNextEntry = testInputStreamInstance.getNextEntry();
        assertNull(testNextEntry);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        doThrow(new IOException("Test Exception")).when(testInputStream)
                .read(anyByteArray(), anyInt(), anyInt());
        new DumpArchiveInputStream(testInputStream);
    }

    @Test
    public void testRead() throws IOException {
        byte[] readBuf = new byte[10];
        int bytesRead = testInputStreamInstance.read(readBuf, 0, readBuf.length);
        assertEquals(5, bytesRead);
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        byte[] readBuf = new byte[0];
        int bytesRead = testInputStreamInstance.read(readBuf, 0, readBuf.length);
        assertEquals(0, bytesRead);
    }

    @Test(expected = ArchiveException.class)
    public void testIncompleteRecordException() throws IOException {
        doReturn(new byte[DumpArchiveConstants.TP_SIZE - 1])
                .when(testInputStream).readRecord();
        new DumpArchiveInputStream(testInputStream);
    }

    @Test
    public void testGetPath() {
        DumpArchiveEntry testNextEntry = testInputStreamInstance.getNextEntry();
        String testPath = testInputStreamInstance.getPath(testNextEntry);
        assertNotNull(testPath);
    }

    @Test
    public void testGetPathWithNullReturn() throws IOException {
        DumpArchiveEntry testNextEntry = testInputStreamInstance.getNextEntry();
        doReturn(null).when(testInputStream).readRecord();
        String testPath = testInputStreamInstance.getPath(testNextEntry);
        assertNull(testPath);
    }

    @Test
    public void testClose() throws IOException {
        testInputStreamInstance.close();
        verify(testInputStream).close();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        new DumpArchiveInputStream(testInputStream);
        testInputStreamInstance.mark(0);
    }

    @Test(expected = InvalidMarkException.class)
    public void testInvalidMarkException() throws IOException {
        new DumpArchiveInputStream(testInputStream);
        testInputStreamInstance.mark(5);
        testInputStreamInstance.reset();
    }

    @Test
    public void testReset() throws IOException {
        new DumpArchiveInputStream(testInputStream);
        testInputStreamInstance.mark(5);
        testInputStreamInstance.reset();
        verify(testInputStream).reset();
    }

    private byte[] getTestBytes() {
        return new byte[]{(byte) 0x64, (byte) 0x65, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x20, (byte) 0x44, (byte) 0x65, (byte) 0x6D, (byte) 0x61, (byte) 0x74, (byte) 0x68, (byte) 0x20, (byte) 0x74, (byte) 0x72, (byte) 0x61, (byte) 0x63, (byte) 0x6B, (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x31, (byte) 0x31, (byte) 0x33, (byte) 0x36, (byte) 0x30, (byte) 0x2D, (byte) 0x31, (byte) 0x35, (byte) 0x32, (byte) 0x2E, (byte) 0x64, (byte) 0x65, (byte) 0x66, (byte) 0x63, (byte) 0x6F, (byte) 0x6F, (byte) 0x6D, (byte) 0x65, (byte) 0x6E, (byte) 0x74} ;
    }

    private byte[] getTestBytesWithInvalidFormat() {
        return new byte[]{(byte) 0x64, (byte) 0x65, (byte) 0x63, (byte) 0x61, (byte) 0x6D, (byte) 0x70, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x20, (byte) 0x44, (byte) 0x65, (byte) 0x6D, (byte) 0x61, (byte) 0x74, (byte) 0x68, (byte) 0x20, (byte) 0x74, (byte) 0x72, (byte) 0x61, (byte) 0x6B, (byte) 0x65, (byte) 0x74, (byte) 0x20, (byte) 0x31, (byte) 0x31, (byte) 0x33, (byte) 0x36, (byte) 0x30, (byte) 0x2D, (byte) 0x31, (byte) 0x35, (byte) 0x32, (byte) 0x2E, (byte) 0x64, (byte) 0x65, (byte) 0x66, (byte) 0x63, (byte) 0x6F, (byte) 0x6F, (byte) 0x6D, (byte) 0x65, (byte) 0x6E, (byte) 0x74, (byte) 0x00};
    }

    private static byte[] getTestBytesWithGzipCompressedData() throws IOException {
        String testData = "This is a test string.";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        byte[] testBytes = GzipUtils.compress(new ByteArrayInputStream(testDataBytes), new byte[testDataBytes.length * 2]);
        return testBytes;
    }

    @Test
    public void testReadCompressedData() throws IOException {
        byte[] testBytes = getTestBytesWithGzipCompressedData();
        InputStream testInputStreamWithCompressedData = new GzipCompressorInputStream(new ByteArrayInputStream(testBytes));
        DumpArchiveInputStream testInputStreamInstanceWithCompressedData = new DumpArchiveInputStream(testInputStreamWithCompressedData);
        byte[] readBuf = new byte[testBytes.length];
        int bytesRead = testInputStreamInstanceWithCompressedData.read(readBuf, 0, readBuf.length);
        assertArrayEquals(testBytes, readBuf);
    }
}