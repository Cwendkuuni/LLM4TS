import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private Map<String, CpioArchiveEntry> entries = new HashMap<>();

    @Before
    public void setUp() throws IOException {
        outputStream = new FileOutputStream(new File("temp.cpio"));
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        new File("temp.cpio").delete();
    }

    @Test
    public void testWriteSingleEntry() throws IOException {
        String fileName = "testFile.txt";
        String testContent = "Hello World!";
        byte[] testBytes = testContent.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setFileSize(testBytes.length);
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] resultBytes = new ByteArrayOutputStream().toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(resultBytes);
        ArchiveUtils.unpack(inputStream, new File("temp"));

        File tempFile = new File("temp", fileName);
        Assert.assertEquals(testContent, ArchiveUtils.toString(tempFile));
    }

    @Test(expected = IOException.class)
    public void testWriteOverSizeEntry() throws IOException {
        String fileName = "testFile.txt";
        String testContent = "Hello World!";
        byte[] testBytes = testContent.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setFileSize(testBytes.length + 10);
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteDuplicateEntry() throws IOException {
        String fileName = "testFile.txt";
        String testContent = "Hello World!";
        byte[] testBytes = testContent.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setFileSize(testBytes.length);
        entry.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveOutputStream.putArchiveEntry(entry); //duplicate entry
    }

    @Test(expected = IOException.class)
    public void testWriteNonRegularFile() throws IOException {
        String fileName = "testFile.txt";
        String testContent = "Hello World!";
        byte[] testBytes = testContent.getBytes();

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setFileSize(testBytes.length);
        entry.setMode(CpioConstants.C_ISDIR);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        String fileName1 = "testFile1.txt";
        String fileName2 = "testFile2.txt";
        String testContent1 = "Hello World!";
        String testContent2 = "Hi There!";
        byte[] testBytes1 = testContent1.getBytes();
        byte[] testBytes2 = testContent2.getBytes();

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(fileName1);
        entry1.setFileSize(testBytes1.length);
        entry1.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.write(testBytes1, 0, testBytes1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(fileName2);
        entry2.setFileSize(testBytes2.length);
        entry2.setMode(CpioConstants.C_ISREG);
        cpioArchiveOutputStream.putArchiveEntry(entry2);
        cpioArchiveOutputStream.write(testBytes2, 0, testBytes2.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] resultBytes = new ByteArrayOutputStream().toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(resultBytes);
        ArchiveUtils.unpack(inputStream, new File("temp"));

        File tempFile1 = new File("temp", fileName1);
        Assert.assertEquals(testContent1, ArchiveUtils.toString(tempFile1));

        File tempFile2 = new File("temp", fileName2);
        Assert.assertEquals(testContent2, ArchiveUtils.toString(tempFile2));
    }

    @Test(expected = IOException.class)
    public void testWriteInvalidFormat() throws IOException {
        CpioArchiveOutputStream invalidFormatCpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 8);
    }

    @Test
    public void testWriteTrailer() throws IOException {
        cpioArchiveOutputStream.finish();

        byte[] resultBytes = new ByteArrayOutputStream().toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(resultBytes);
        ArchiveUtils.unpack(inputStream, new File("temp"));

        File tempTrailer = new File("temp", CpioConstants.CPIO_TRAILER);
        Assert.assertNotNull(tempTrailer);
    }
}