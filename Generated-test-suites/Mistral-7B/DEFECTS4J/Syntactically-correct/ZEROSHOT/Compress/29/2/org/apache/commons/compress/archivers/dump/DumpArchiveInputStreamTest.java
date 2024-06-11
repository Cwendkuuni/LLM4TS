import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipFactory;
import org.apache.commons.compress.utils.StreamUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class DumpArchiveInputStreamTest {

    @Test
    public void testMatches() throws IOException {
        byte[] buffer = new byte[32];
        Assert.assertTrue(DumpArchiveInputStream.matches(buffer, buffer.length));
        Assert.assertFalse(DumpArchiveInputStream.matches(buffer, buffer.length - 1));
    }

    @Test
    public void testEmptyStream() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream);
        archiveInputStream.close();
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        Assert.assertNull(nextEntry);
    }

    @Test
    public void testSingleEntry() throws IOException, ArchiveException {
        String testData = "testData";

        byte[] headerBytes = DumpArchiveUtil.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.DUMPD,
                1,
                testData.getBytes().length,
                testData.getBytes().length);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(headerBytes);
        outputStream.write(testData.getBytes());
        byte[] testDataBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(testDataBytes);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream);

        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals(testData, nextEntry.getName());

        archiveInputStream.close();
    }

    @Test
    public void testMultipleEntries() throws IOException, ArchiveException {
        String testData1 = "testData1";
        String testData2 = "testData2";

        byte[] headerBytes1 = DumpArchiveUtil.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.DUMPD,
                1,
                testData1.getBytes().length,
                testData1.getBytes().length);
        byte[] headerBytes2 = DumpArchiveUtil.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.DUMPD,
                2,
                testData2.getBytes().length,
                testData2.getBytes().length);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(headerBytes1);
        outputStream.write(testData1.getBytes());
        outputStream.write(headerBytes2);
        outputStream.write(testData2.getBytes());
        byte[] testDataBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(testDataBytes);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream);

        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals(testData1, nextEntry.getName());
        nextEntry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals(testData2, nextEntry.getName());

        archiveInputStream.close();
    }

    @Test
    public void testMissingParentDirectory() throws IOException, ArchiveException {
        String testData = "testData";

        byte[] headerBytes = DumpArchiveUtil.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.DUMPD,
                1,
                testData.getBytes().length,
                testData.getBytes().length);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(headerBytes);
        outputStream.write(testData.getBytes());
        byte[] testDataBytes = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testDataBytes);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream);

        archiveInputStream.getNextEntry();
        archiveInputStream.getNextEntry(); // should throw an exception

        archiveInputStream.close();
    }

    @Test
    public void testCompressedEntry() throws IOException, ArchiveException {
        String testData = "testData";

        byte[] headerBytes = DumpArchiveUtil.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.DUMPD,
                1,
                testData.getBytes().length,
                testData.getBytes().length);

        byte[] compressedData = testData.getBytes();
        byte[] uncompressedData = testData.getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(headerBytes);
        outputStream.write(DumpArchiveUtil.createMagicNumber(
                GzipFactory.GZIP,
                0));
        CompressorInputStream compressedInputStream = new CompressorInputStream(
                new ByteArrayInputStream(testData.getBytes()));
        ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();
        StreamUtils.copy(compressedInputStream, compressedOutputStream);
        byte[] compressedBytes = compressedOutputStream.toByteArray();
        outputStream.write(compressedBytes, 0, compressedBytes.length);
        byte[] testDataBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(testDataBytes);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream);

        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        Assert.assertNotNull(nextEntry);
        Assert.assertEquals(testData, nextEntry.getName());

        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidHeader() throws IOException, ArchiveException {
        byte[] testData = "testData".getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData);
        byte[] testDataBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(testDataBytes);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream);

        archiveInputStream.getNextEntry(); // should throw an exception

        archiveInputStream.close();
    }

    @Test(expected = ArchiveException.class)
    public void testMissingEntryHeader() throws IOException, ArchiveException {
        String testData = "testData";

        byte[] headerBytes = DumpArchiveUtil.createHeader(
                DumpArchiveConstants.SEGMENT_TYPE.DUMPD,
                1,
                testData.getBytes().length,
                0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(headerBytes);
        outputStream.write(testData.getBytes());
        byte[] testDataBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(testDataBytes);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream);

        archiveInputStream.getNextEntry(); // should throw an exception

        archiveInputStream.close();
    }

    @Test(expected = EOFException.class)
    public void testEndOfStream() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DumpArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream);

        archiveInputStream.getNextEntry();
        archiveInputStream.getNextEntry();
        archiveInputStream.getNextEntry(); // should throw an exception

        archiveInputStream.close();
    }
}