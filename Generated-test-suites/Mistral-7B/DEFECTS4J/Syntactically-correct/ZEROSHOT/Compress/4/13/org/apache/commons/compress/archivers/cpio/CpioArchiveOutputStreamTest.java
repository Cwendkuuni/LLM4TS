import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "testCpioFile.cpio";
    private CpioArchiveOutputStream outputStream;
    private File testFile;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;
    private Map<String, CpioArchiveEntry> entryMap;

    @Before
    public void setUp() throws IOException {
        testFile = new File(TEST_FILE_NAME);
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
        byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        entryMap = new HashMap<>();
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.finish();
            outputStream.close();
        }
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("test1.txt", "test1.txt content");
        outputStream.putArchiveEntry(entry1);
        outputStream.write("test1.txt content".getBytes());
        outputStream.finish();
        outputStream.close();

        byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry readEntry1 = new CpioArchiveEntry(byteArrayInputStream);
        Assert.assertEquals("test1.txt", readEntry1.getName());
        Assert.assertEquals("test1.txt content", ArchiveUtils.inputStreamToString(byteArrayInputStream).trim());

        CpioArchiveEntry entry2 = new CpioArchiveEntry("test2.txt", "test2.txt content");
        outputStream = new CpioArchiveOutputStream(testFile, CpioConstants.FORMAT_NEW);
        outputStream.putArchiveEntry(entry2);
        outputStream.write("test2.txt content".getBytes());
        outputStream.finish();
        outputStream.close();

        CpioArchiveEntry readEntry2 = null;
        InputStream inputStream = new FileInputStream(testFile);
        while (readEntry2 == null) {
            readEntry2 = new CpioArchiveEntry(inputStream);
            if (readEntry2.getName().equals("test2.txt")) {
                entryMap.put(readEntry2.getName(), readEntry2);
            }
            inputStream.read(); // read until the end of the file
        }
        Assert.assertEquals("test2.txt", readEntry2.getName());
        Assert.assertEquals("test2.txt content", ArchiveUtils.inputStreamToString(new FileInputStream(testFile)).trim());

        Assert.assertEquals(2, entryMap.size());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("test1.txt", "test1.txt content");
        outputStream.putArchiveEntry(entry1);
        outputStream.write("test1.txt content".getBytes());
        outputStream.finish();
        outputStream.close();

        outputStream = new CpioArchiveOutputStream(testFile, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry2 = new CpioArchiveEntry("test1.txt", "test1.txt content");
        outputStream.putArchiveEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        outputStream = new CpioArchiveOutputStream(testFile, (short) 0xff);
        CpioArchiveEntry entry1 = new CpioArchiveEntry("test1.txt", "test1.txt content");
        outputStream.putArchiveEntry(entry1);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullName() throws IOException {
        outputStream = new CpioArchiveOutputStream(testFile, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry(null, "test1.txt content");
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidLength() throws IOException {
        outputStream = new CpioArchiveOutputStream(testFile, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry("test1.txt", "test1.txt content".getBytes().length + 1);
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullContent() throws IOException {
        outputStream = new CpioArchiveOutputStream(testFile, CpioConstants.FORMAT_NEW);
        CpioArchiveEntry entry = new CpioArchiveEntry("test1.txt", null);
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        outputStream = new CpioArchiveOutputStream(testFile, CpioConstants.FORMAT_NEW);
        outputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullFormat() throws IOException {
        outputStream = new CpioArchiveOutputStream(testFile, (short) 0);
        CpioArchiveEntry entry = new CpioArchiveEntry("test1.txt", "test1.txt content");
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullOutputStream() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test1.txt", "test1.txt content");
        outputStream = null;
        outputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithClosedStream() throws IOException {
        outputStream.close();
        outputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithNullEntry() throws IOException {
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt", "test.txt content".getBytes());
        outputStream.putArchiveEntry(entry);
        outputStream.write(entry.getContent().getBytes());
        outputStream.finish();
        outputStream.close();

        byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry readEntry = new CpioArchiveEntry(byteArrayInputStream);
        Assert.assertEquals("test.txt", readEntry.getName());
        Assert.assertArrayEquals(entry.getContent().getBytes(), ArchiveUtils.inputStreamToByteArray(byteArrayInputStream));
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        outputStream.close();
        outputStream.write(new byte[0]);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullContent() throws IOException {
        outputStream.putArchiveEntry(new CpioArchiveEntry("test.txt", null));
        outputStream.write(new byte[0]);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullOutputStream() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt", new byte[0]);
        outputStream = null;
        outputStream.write(entry.getContent().getBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullEntry() throws IOException {
        outputStream.write(new byte[0]);
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt", "test.txt content".getBytes());
        outputStream.putArchiveEntry(entry);
        outputStream.finish();
        outputStream.close();

        byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CpioArchiveEntry readEntry = new CpioArchiveEntry(byteArrayInputStream);
        Assert.assertEquals("test.txt", readEntry.getName());
        Assert.assertEquals(entry.getSize(), ArchiveUtils.inputStreamToByteArray(byteArrayInputStream).length);
    }

    @Test(expected = IOException.class)
    public void testFinishWithClosedStream() throws IOException {
        outputStream.close();
        outputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithNullOutputStream() throws IOException {
        outputStream = null;
        outputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt", "test.txt content".getBytes());
        outputStream.putArchiveEntry(entry);
        outputStream.finish();
        outputStream.close();

        Assert.assertTrue(testFile.exists());
        Assert.assertEquals(new File(testFile, entry.getName()).length(), entry.getSize());
        Assert.assertFalse(outputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseWithClosedStream() throws IOException {
        outputStream.close();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithNullOutputStream() throws IOException {
        outputStream = null;
        outputStream.close();
    }
}