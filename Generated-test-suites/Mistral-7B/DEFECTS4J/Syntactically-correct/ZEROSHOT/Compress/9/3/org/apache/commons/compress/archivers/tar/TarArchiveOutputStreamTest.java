import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        IOUtils.closeQuietly(byteArrayOutputStream);
        IOUtils.closeQuietly(byteArrayInputStream);
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarArchiveOutputStream.getArchiveEntry());
        tarArchiveOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() {
        tarArchiveOutputStream.setLongFileMode(3);
    }

    @Test
    public void testFinish() throws IOException {
        File testFile = new File("testFile.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(testFile.getAbsoluteFile().getParent() + File.separator + "testFile.tar");
        TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt", "Test file content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write("Test file content".getBytes(), 0, "Test file content".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        int read = 0;
        byte[] readBuffer = new byte[512];
        while ((read = byteArrayOutputStream.read(readBuffer, 0, readBuffer.length)) != -1) {
            tarArchiveOutputStream.write(readBuffer, 0, read);
        }
        byte[] expectedTarHeader = new byte[512];
        System.arraycopy(TarConstants.MAGIC_NUM, 0, expectedTarHeader, 0, TarConstants.MAGIC_NUM.length);
        TarHeader header = new TarHeader();
        header.fill(new File("testFile.txt"), "testFile.txt", (short) 0, (short) 0, null, (short) 0, (short) 0, (short) 0, 0L, 0L, (short) 0);
        header.write(expectedTarHeader);
        byte[] content = "Test file content".getBytes();
        byte[] expectedContent = new byte[content.length];
        System.arraycopy(content, 0, expectedContent, 0, content.length);
        byte[] expectedEOF = new byte[512];
        System.arraycopy(expectedContent, 0, expectedEOF, 0, content.length);
        for (int i = 0; i < 512 - content.length; i++) {
            expectedEOF[content.length + i] = 0;
        }
        byte[] expectedTarFile = new byte[expectedTarHeader.length + content.length + expectedEOF.length];
        System.arraycopy(expectedTarHeader, 0, expectedTarFile, 0, expectedTarHeader.length);
        System.arraycopy(content, 0, expectedTarFile, expectedTarHeader.length, content.length);
        System.arraycopy(expectedEOF, 0, expectedTarFile, expectedTarHeader.length + content.length, expectedEOF.length);
        byte[] actualTarFile = new byte[byteArrayOutputStream.size()];
        System.arraycopy(byteArrayOutputStream.toByteArray(), 0, actualTarFile, 0, byteArrayOutputStream.size());
        assertEquals(expectedTarFile, actualTarFile);
        tarArchiveOutputStream.close();
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        File testFile = new File("testFile.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(testFile.getAbsoluteFile().getParent() + File.separator + "testFile.tar");
        TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt", "Test file content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write("Test file content".getBytes(), 0, "Test file content".length());
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        File testFile = new File("testFile.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(testFile.getAbsoluteFile().getParent() + File.separator + "testFile.tar");
        TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt", "Test file content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        File testFile = new File("testFile.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(testFile.getAbsoluteFile().getParent() + File.separator + "testFile.tar");
        TarArchiveEntry testEntry = null;
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.close();
        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullFileName() throws IOException {
        File testFile = new File("testFile.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(testFile.getAbsoluteFile().getParent() + File.separator + "testFile.tar");
        TarArchiveEntry testEntry = new TarArchiveEntry(null, "Test file content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        File testFile = new File("testFile.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(testFile.getAbsoluteFile().getParent() + File.separator + "testFile.tar");
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName.getBytes(), "Test file content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write("Test file content".getBytes(), 0, "Test file content".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        int read = 0;
        byte[] readBuffer = new byte[512];
        while ((read = byteArrayOutputStream.read(readBuffer, 0, readBuffer.length)) != -1) {
            tarArchiveOutputStream.write(readBuffer, 0, read);
        }
        byte[] expectedTarHeader = new byte[512];
        System.arraycopy(TarConstants.MAGIC_NUM, 0, expectedTarHeader, 0, TarConstants.MAGIC_NUM.length);
        TarHeader header = new TarHeader();
        header.fill(new File("testFile.txt"), longFileName, (short) 0, (short) 0, null, (short) 0, (short) 0, (short) 0, 0L, 0L, (short) 0);
        header.write(expectedTarHeader);
        byte[] content = "Test file content".getBytes();
        byte[] expectedContent = new byte[content.length];
        System.arraycopy(content, 0, expectedContent, 0, content.length);
        byte[] expectedEOF = new byte[512];
        for (int i = 0; i < 512 - content.length; i++) {
            expectedEOF[content.length + i] = 0;
        }
        byte[] expectedTarFile = new byte[expectedTarHeader.length + content.length + expectedEOF.length];
        System.arraycopy(expectedTarHeader, 0, expectedTarFile, 0, expectedTarHeader.length);
        System.arraycopy(content, 0, expectedTarFile, expectedTarHeader.length, content.length);
        System.arraycopy(expectedEOF, 0, expectedTarFile, expectedTarHeader.length + content.length, expectedEOF.length);
        byte[] actualTarFile = new byte[byteArrayOutputStream.size()];
        System.arraycopy(byteArrayOutputStream.toByteArray(), 0, actualTarFile, 0, byteArrayOutputStream.size());
        assertEquals(expectedTarFile, actualTarFile);
        tarArchiveOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        File testFile = new File("testFile.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(testFile.getAbsoluteFile().getParent() + File.separator + "testFile.tar");
        TarArchiveEntry testEntry = new TarArchiveEntry("testDir", null);
        testEntry.isDirectory = true;
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.finish();
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        int read = 0;
        byte[] readBuffer = new byte[512];
        while ((read = byteArrayOutputStream.read(readBuffer, 0, readBuffer.length)) != -1) {
            tarArchiveOutputStream.write(readBuffer, 0, read);
        }
        byte[] expectedTarHeader = new byte[512];
        System.arraycopy(TarConstants.MAGIC_NUM, 0, expectedTarHeader, 0, TarConstants.MAGIC_NUM.length);
        TarHeader header = new TarHeader();
        header.fill(new File("testDir"), "testDir/", (short) 0, (short) 0, null, (short) 0, (short) 0, (short) 0, 0L, 0L, (short) 0);
        header.write(expectedTarHeader);
        byte[] expectedEOF = new byte[512];
        for (int i = 0; i < 512; i++) {
            expectedEOF[i] = 0;
        }
        byte[] expectedTarFile = new byte[expectedTarHeader.length + expectedEOF.length];
        System.arraycopy(expectedTarHeader, 0, expectedTarFile, 0, expectedTarHeader.length);
        System.arraycopy(expectedEOF, 0, expectedTarFile, expectedTarHeader.length, expectedEOF.length);
        byte[] actualTarFile = new byte[byteArrayOutputStream.size()];
        System.arraycopy(byteArrayOutputStream.toByteArray(), 0, actualTarFile, 0, byteArrayOutputStream.size());
        assertEquals(expectedTarFile, actualTarFile);
        tarArchiveOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testWrite() throws IOException {
        File testFile = new File("testFile.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(testFile.getAbsoluteFile().getParent() + File.separator + "testFile.tar");
        TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt", "Test file content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write("Test file content".getBytes(), 0, "Test file content".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new ByteArrayInputStream("Test file content".getBytes());
        byte[] readBuffer = new byte[512];
        int read = 0;
        while ((read = byteArrayInputStream.read(readBuffer, 0, readBuffer.length)) != -1) {
            tarArchiveOutputStream.write(readBuffer, 0, read);
        }
        tarArchiveOutputStream.write(byteArrayInputStream.readAllBytes(), 0, byteArrayInputStream.available());
        tarArchiveOutputStream.close();
        byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] actualTarFile = new byte[byteArrayOutputStream.size()];
        System.arraycopy(byteArrayOutputStream.toByteArray(), 0, actualTarFile, 0, byteArrayOutputStream.size());
        byte[] expectedTarFile = new byte[expectedTarHeader.length + content.length + expectedEOF.length];
        System.arraycopy(expectedTarHeader, 0, expectedTarFile, 0, expectedTarHeader.length);
        System.arraycopy(content, 0, expectedTarFile, expectedTarHeader.length, content.length);
        System.arraycopy(expectedEOF, 0, expectedTarFile, expectedTarHeader.length + content.length, expectedEOF.length);
        assertEquals(expectedTarFile, actualTarFile);
        testFile.delete();
    }

    @Test
    public void testClose() throws IOException {
        File testFile = new File("testFile.tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(testFile.getAbsoluteFile().getParent() + File.separator + "testFile.tar");
        TarArchiveEntry testEntry = new TarArchiveEntry("testFile.txt", "Test file content".getBytes());
        tarArchiveOutputStream.putArchiveEntry(testEntry);
        tarArchiveOutputStream.write("Test file content".getBytes(), 0, "Test file content".length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        testFile.delete();
    }
}