import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.test.util.TestTools;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.compress.utils.IOUtils.toByteArray;

public class DumpArchiveInputStreamTest {

    private static final String TEST_ARCHIVE = "test.dump";

    private ArchiveInputStream input;
    private byte[] testData;

    @Before
    public void setUp() throws IOException {
        testData = createTestData();
        File testFile = new File(TEST_ARCHIVE);
        FileOutputStream output = new FileOutputStream(testFile);
        output.write(testData);
        output.close();

        input = new DumpArchiveInputStream(new FileInputStream(TEST_ARCHIVE));
    }

    @Test
    public void testBasicFunctionality() throws IOException {
        DumpArchiveEntry entry = input.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("first.txt", entry.getName());

        entry = input.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals("second.txt", entry.getName());

        input.close();
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormatException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("invalid format".getBytes(StandardCharsets.UTF_8));
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        new DumpArchiveInputStream(is);
    }

    @Test(expected = EOFException.class)
    public void testEndOfFileException() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        new DumpArchiveInputStream(is);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        InputStream is = new FileInputStream(new File("non-existent.file"));
        new DumpArchiveInputStream(is);
    }

    @Test
    public void testReadFunctionality() throws IOException {
        byte[] expected = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '\n'};
        byte[] result = new byte[expected.length];
        int read = input.read(result, 0, expected.length);
        Assert.assertEquals(expected.length, read);
        Assert.assertArrayEquals(expected, Arrays.copyOfRange(result, 0, read));

        input.close();
    }

    @Test
    public void testEndOfStreamBehavior() throws IOException {
        byte[] expected = new byte[0];
        byte[] result = new byte[expected.length];
        int read = input.read(result, 0, expected.length);
        Assert.assertEquals(-1, read);

        input.close();
    }

    @Test
    public void testSeekFunctionality() throws IOException {
        input.seek(10);
        DumpArchiveEntry entry = input.getNextEntry();
        Assert.assertEquals("second.txt", entry.getName());

        input.close();
    }

    @Test
    public void testReadCompressedData() throws IOException {
        byte[] expected = new byte[]{'C', 'o', 'm', 'p', 'r', 'e', 's', 's', 'e', 'd', '\n'};
        byte[] result = new byte[expected.length];
        int read = input.read(result, 0, expected.length);
        Assert.assertEquals(expected.length, read);
        Assert.assertArrayEquals(expected, Arrays.copyOfRange(result, 0, read));

        input.close();
    }

    @Test
    public void testReadSparseData() throws IOException {
        // read non-existent data
        byte[] result = new byte[1024];
        int read = input.read(result, 0, result.length);
        Assert.assertEquals(-1, read);

        input.close();
    }

    @Test
    public void testReadEmptyData() throws IOException {
        // read empty data
        byte[] result = new byte[1024];
        int read = input.read(result, 0, result.length);
        Assert.assertEquals(0, read);

        input.close();
    }

    @Test
    public void testReadAllData() throws IOException {
        // read all data
        byte[] buffer = new byte[input.available()];
        int read = input.read(buffer, 0, buffer.length);
        Assert.assertEquals(input.available(), read);

        input.close();

        // check that data is correct
        byte[] expected = toByteArray(input);
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test
    public void testClose() throws IOException {
        input.close();
        Assert.assertFalse(input.isOpen());
    }

    @Test
    public void testGetSummary() throws IOException {
        DumpArchiveSummary summary = input.getSummary();
        Assert.assertEquals(2, summary.getNumberOfEntries());
        Assert.assertEquals("first.txt", summary.getEntryName(0));
        Assert.assertEquals("second.txt", summary.getEntryName(1));

        input.close();
    }

    @Test
    public void testGetBytesRead() throws IOException {
        long bytesRead = input.getBytesRead();
        Assert.assertEquals(TestTools.getFileSize(new File(TEST_ARCHIVE)), bytesRead);

        input.close();
    }

    @Test
    public void testGetCount() throws IOException {
        int count = input.getCount();
        Assert.assertEquals(TestTools.getFileSize(new File(TEST_ARCHIVE)), count);

        input.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        // test reading all entries
        List<DumpArchiveEntry> entries = TestTools.getExpectedEntries(TEST_ARCHIVE);
        for (int i = 0; i < entries.size(); i++) {
            DumpArchiveEntry entry = input.getNextEntry();
            Assert.assertEquals(entries.get(i), entry);
        }

        input.close();
    }

    @Test
    public void testReadDirectoryEntry() throws IOException {
        DumpArchiveEntry entry = input.getNextEntry();
        Assert.assertEquals(".", entry.getName());
        Assert.assertEquals(".", entry.getSimpleName());
        Assert.assertEquals(DumpArchiveEntry.TYPE_DIRECTORY, entry.getType());

        entry = input.getNextEntry();
        Assert.assertEquals("..", entry.getName());
        Assert.assertEquals("..", entry.getSimpleName());
        Assert.assertEquals(DumpArchiveEntry.TYPE_DIRECTORY, entry.getType());

        input.close();
    }

    @Test
    public void testGetPath() throws IOException {
        DumpArchiveEntry entry = input.getNextEntry();
        Assert.assertEquals("/first.txt", entry.getPath());

        entry = input.getNextEntry();
        Assert.assertEquals("/second.txt", entry.getPath());

        input.close();
    }

    @Test
    public void testCloseInvalidFormat() throws IOException {
        byte[] testData = new byte[]{'H', 'e', 'l', 'l', 'o', '\n'};
        File testFile = new File("test.invalid");
        FileOutputStream output = new FileOutputStream(testFile);
        output.write(testData);
        output.close();

        try {
            new DumpArchiveInputStream(new FileInputStream("test.invalid"));
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }

        IOUtils.deleteQuietly(testFile);
    }

    @Test
    public void testCloseInvalidEntry() throws IOException {
        byte[] testData = new byte[]{'H', 'e', 'l', 'l', 'o', '\n'};
        File testFile = new File("test.invalid");
        FileOutputStream output = new FileOutputStream(testFile);
        output.write(testData);
        output.write(TestTools.createInvalidEntry(2).getData());
        output.write(TestTools.createInvalidEntry(1).getData());
        output.close();

        try {
            new DumpArchiveInputStream(new FileInputStream(TEST_ARCHIVE));
            Assert.fail();
        } catch (ArchiveException e) {
            // expected
        }

        IOUtils.deleteQuietly(testFile);
    }

    private byte[] createTestData() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DumpArchiveOutputStream out = new DumpArchiveOutputStream(baos, null);

        out.putNextEntry(new DumpArchiveEntry(".", ".", DumpArchiveEntry.TYPE_DIRECTORY));
        out.putNextEntry(new DumpArchiveEntry("first.txt", "", DumpArchiveEntry.TYPE_FILE));
        out.putNextEntry(new DumpArchiveEntry("second.txt", "", DumpArchiveEntry.TYPE_FILE));
        out.putNextEntry(new DumpArchiveEntry(".", ".", DumpArchiveEntry.TYPE_DIRECTORY));
        out.putNextEntry(new DumpArchiveEntry(" Превключване.txt", "", DumpArchiveEntry.TYPE_FILE));

        byte[] headerBytes = toByteArray(out.getSummary());
        out.writeRecord(headerBytes);

        out.finish();
        out.close();

        return baos.toByteArray();
    }
}