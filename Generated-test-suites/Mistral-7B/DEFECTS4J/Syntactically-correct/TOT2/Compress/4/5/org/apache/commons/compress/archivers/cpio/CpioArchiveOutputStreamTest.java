import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Rule
    public Exception rule = new Exception();

    @Before
    public void setUp() throws Exception {
        outputStream = new FileOutputStream(new File("test.cpio"));
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content.";
        byte[] testFileBytes = testFileContent.getBytes();

        File testFile = new File("testFile.txt");
        CpioArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        ArgumentCaptor<CpioArchiveEntry> entryCaptor = ArgumentCaptor.forClass(CpioArchiveEntry.class);
        Mockito.verify(outputStream).write(CpioUtils.toAsciiBytes(CpioConstants.MAGIC_NEW));
        Mockito.verify(outputStream).write(testFileName.getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getInode()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getMode()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getUID()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getGID()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getNumberOfLinks()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getTime()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getSize()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getDeviceMaj()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getDeviceMin()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getRemoteDeviceMaj()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getRemoteDeviceMin()).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getName().length() + 1).getBytes());
        Mockito.verify(outputStream).write(Long.toString(testEntry.getChksum()).getBytes());
        Mockito.verify(outputStream).write(testFileName.getBytes());
        Mockito.verify(outputStream).write('\0');
        Mockito.verify(outputStream).write(testFileBytes, 0, testFileBytes.length);

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        File testFileRead = new File("test.cpio");
        ByteArrayOutputStream outputStreamMock = new ByteArrayOutputStream();
        FileInputStream inputStream = new FileInputStream(testFileRead);
        byte[] bytesFromFile = IOUtils.toByteArray(inputStream);
        inputStream.close();

        CpioArchiveOutputStream cpioArchiveOutputStreamMock = new CpioArchiveOutputStream(outputStreamMock, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStreamMock.putNextEntry(Mockito.mock(CpioArchiveEntry.class));

        cpioArchiveOutputStreamMock.write(bytesFromFile, 0, bytesFromFile.length);
        cpioArchiveOutputStreamMock.closeArchiveEntry();
        cpioArchiveOutputStreamMock.finish();
        cpioArchiveOutputStreamMock.close();

        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        expectedEntries.put(testFileName, testEntry);
        assertEquals(expectedEntries, cpioArchiveOutputStreamMock.getAllEntries());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content.";
        byte[] testFileBytes = testFileContent.getBytes();

        File testFile = new File("testFile.txt");
        CpioArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);

        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(testEntry);

    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonCpioArchiveEntry() throws IOException {
        ArchiveEntry nonCpioArchiveEntry = Mockito.mock(ArchiveEntry.class);
        cpioArchiveOutputStream.putArchiveEntry(nonCpioArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testWriteWithEntryNotOpened() throws IOException {
        byte[] testFileBytes = "Test content.".getBytes();
        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
        byte[] testFileBytes = "Test content.".getBytes();
        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length);
    }

    @Test
    public void testWriteWithOpenEntry() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content.";
        byte[] testFileBytes = testFileContent.getBytes();

        File testFile = new File("testFile.txt");
        CpioArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertEquals(testFileBytes.length, cpioArchiveOutputStream.written);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithNegativeOffset() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content.";
        byte[] testFileBytes = testFileContent.getBytes();

        File testFile = new File("testFile.txt");
        CpioArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        cpioArchiveOutputStream.write(testFileBytes, -1, testFileBytes.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithNegativeLength() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content.";
        byte[] testFileBytes = testFileContent.getBytes();

        File testFile = new File("testFile.txt");
        CpioArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        cpioArchiveOutputStream.write(testFileBytes, 0, -1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithSizeMismatch() throws IOException {
        String testFileName = "testFile.txt";
        String testFileContent = "Test content.";
        byte[] testFileBytes = testFileContent.getBytes();

        File testFile = new File("testFile.txt");
        CpioArchiveEntry testEntry = new CpioArchiveEntry(testFile, testFileName);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        cpioArchiveOutputStream.write(testFileBytes, 0, testFileBytes.length + 1);
    }

    @Test
    public void testFinish() throws IOException {
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        cpioArchiveOutputStream.close();
    }
}