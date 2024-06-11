import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutNextEntryWithValidParameters() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("Test data".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] expectedHeader = {(byte) 0x19, 'C', 'P', 'I', 'O', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, '\0'};
        byte[] expectedData = "Test data".getBytes();

        byte[] actualData = outputStream.toByteArray();

        byte[] headerStartIndex = new byte[expectedHeader.length];
        System.arraycopy(actualData, 0, headerStartIndex, 0, expectedHeader.length);
        Assert.assertArrayEquals(expectedHeader, headerStartIndex);
        Assert.assertArrayEquals(expectedData, new byte[actualData.length - expectedHeader.length][]);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNullEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = null;
        cpioArchiveOutputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        cpioArchiveOutputStream.putNextEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        cpioArchiveOutputStream.putNextEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithNegativeSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(-10);

        cpioArchiveOutputStream.putNextEntry(entry);
    }

    @Test
    public void testPutNextEntryWithValidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_ASCII);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setSize(10);

        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("Test data".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] expectedHeader = {(byte) 0x1a, 'C', 'P', 'I', 'O', (byte) 0x20, 'A', 'S', 'C', 'I', 'I', 0, 10, '\0'};
        byte[] expectedData = "Test data".getBytes();

        byte[] actualData = outputStream.toByteArray();

        byte[] headerStartIndex = new byte[expectedHeader.length];
        System.arraycopy(actualData, 0, headerStartIndex, 0, expectedHeader.length);
        Assert.assertArrayEquals(expectedHeader, headerStartIndex);
        Assert.assertArrayEquals(expectedData, new byte[actualData.length - expectedHeader.length][]);
    }

    @Test
    public void testWriteWithValidParameters() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
        cpioArchiveOutputStream.write("Test data".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        byte[] expectedData = "Test data".getBytes();
        byte[] actualData = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedData, new byte[actualData.length - CpioConstants.HEADER_SIZE][]);
    }

    @Test(expected = IOException.class)
    public void testWriteWithClosedStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.finish();

        cpioArchiveOutputStream.write("Test data".getBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putNextEntry(new CpioArchiveEntry());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.write("Test data".getBytes());
    }
}