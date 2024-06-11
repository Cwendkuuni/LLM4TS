import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Map<String, CpioArchiveEntry> entries;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byteArrayOutputStream = new ByteArrayOutputStream();
        entries = new HashMap<>();
    }

    @After
    public void tearDown() throws Exception {
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testPutNextEntryWithEmptyName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("");
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("Test data".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] result = outputStream.toByteArray();

        byte[] expectedMagicNumber = CpioConstants.MAGIC_NEW.getBytes();
        byte[] expectedNameLength = CryptoUtils.intToByteArray(entry.getName().length() + 1, 4);
        byte[] expectedName = entry.getName().getBytes();
        byte[] expectedSize = CryptoUtils.longToByteArray((long) entry.getSize(), 4);

        assertThat(result, is(expectedMagicNumber));
        assertThat(result, is(expectedNameLength));
        assertThat(result, is(expectedName));
        assertThat(result, is(expectedSize));
        // ... test the rest of the header fields and check the data
    }

    @Test
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(entry);
        try {
            cpioArchiveOutputStream.putNextEntry(entry);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Unknown header type"));
        } finally {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putNextEntry(entry1);
        cpioArchiveOutputStream.write("Test data1".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry("test.txt");
        try {
            cpioArchiveOutputStream.putNextEntry(entry2);
        } catch (IOException e) {
            assertThat(e.getMessage(), is("duplicate entry: test.txt"));
        } finally {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntryWithInvalidNameLength() throws IOException {
        String name = new String(new char[129]);
        CpioArchiveEntry entry = new CpioArchiveEntry(name);
        cpioArchiveOutputStream.putNextEntry(entry);
        try {
            cpioArchiveOutputStream.write("Test data".getBytes());
        } catch (IOException e) {
            assertThat(e.getMessage(), is("Name too long: " + name.length()));
        } finally {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntryWithEmptyData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] result = outputStream.toByteArray();

        byte[] expectedMagicNumber = CpioConstants.MAGIC_NEW.getBytes();
        byte[] expectedNameLength = CryptoUtils.intToByteArray(entry.getName().length() + 1, 4);
        byte[] expectedName = entry.getName().getBytes();
        byte[] expectedSize = CryptoUtils.longToByteArray(0, 4);

        assertThat(result, is(expectedMagicNumber));
        assertThat(result, is(expectedNameLength));
        assertThat(result, is(expectedName));
        assertThat(result, is(expectedSize));
    }

    @Test
    public void testPutNextEntryWithData() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putNextEntry(entry);
        byte[] testData = "Test data".getBytes();
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] result = outputStream.toByteArray();

        byte[] expectedMagicNumber = CpioConstants.MAGIC_NEW.getBytes();
        byte[] expectedNameLength = CryptoUtils.intToByteArray(entry.getName().length() + 1, 4);
        byte[] expectedName = entry.getName().getBytes();
        byte[] expectedSize = CryptoUtils.longToByteArray((long) entry.getSize(), 4);

        // compare the header fields with the previously written test data
        // ...

        byte[] expectedData = testData;

        byte[] expectedDataLength = CryptoUtils.longToByteArray((long) testData.length, 4);

        assertThat(result, is(expectedMagicNumber));
        assertThat(result, is(expectedNameLength));
        assertThat(result, is(expectedName));
        assertThat(result, is(expectedSize));
        assertThat(result, is(expectedData));
        assertThat(result, is(expectedDataLength));
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("Test data".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] result = outputStream.toByteArray();

        byte[] expectedMagicNumber = CpioConstants.MAGIC_TRAILER.getBytes();

        // test the trailer header fields
        // ...

        byte[] expectedTrailerSize = CryptoUtils.longToByteArray((long) (result.length - entry.getSize()), 4);

        assertThat(result, is(expectedMagicNumber));
        assertThat(result, is(expectedTrailerSize));
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.close();
        cpioArchiveOutputStream.write("Test data".getBytes());
    }

    @Test
    public void testCloseArchiveEntryWithInvalidSize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("Test data1".getBytes());
        try {
            cpioArchiveOutputStream.closeArchiveEntry();
            cpioArchiveOutputStream.write("Test data2".getBytes());
        } catch (IOException e) {
            assertThat(e.getMessage(), is("invalid entry size (expected " + entry.getSize() + " but got " + (entry.getSize() + "Test data1".length()) + ")"));
        } finally {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntryWithNullName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry(null);
        try {
            cpioArchiveOutputStream.putNextEntry(entry);
        } catch (NullPointerException e) {
            // expected
        } finally {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntryWithEmptyEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioArchiveOutputStream.putNextEntry(entry);
        try {
            cpioArchiveOutputStream.write("Test data".getBytes());
        } catch (IOException e) {
            // expected
        } finally {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntryWithNullEntry() throws IOException {
        try {
            cpioArchiveOutputStream.putNextEntry(null);
        } catch (NullPointerException e) {
            // expected
        } finally {
            cpioArchiveOutputStream.close();
        }
    }
}