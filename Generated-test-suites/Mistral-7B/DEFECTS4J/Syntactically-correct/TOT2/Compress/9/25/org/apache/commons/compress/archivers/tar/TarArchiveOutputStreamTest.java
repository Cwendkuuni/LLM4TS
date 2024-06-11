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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit-test-", null);
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (tarOutputStream != null) {
            tarOutputStream.close();
        }

        if (byteArrayOutputStream != null) {
            byteArrayOutputStream.close();
        }

        if (byteArrayInputStream != null) {
            byteArrayInputStream.close();
        }

        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testEmptyConstruction() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream("/dev/null")));
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryWithFileNameLessThanNameLen() throws IOException {
        String shortFileName = "test.txt";
        TarArchiveEntry entry = new TarArchiveEntry(new File(shortFileName));
        tarOutputStream.putArchiveEntry(entry);
        byte[] header = entry.getHeader(tarOutputStream.getRecordSize());
        tarOutputStream.write(header);
        tarOutputStream.closeArchiveEntry();
        byte[] content = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(content);
        tarOutputStream.close();

        List<Byte> expectedHeader = new ArrayList<>();
        expectedHeader.addAll(TarConstants.MAGIC_NUMBER);
        expectedHeader.addAll(TarConstants.TAR_HDR_FILETYPE_REGULAR);
        expectedHeader.addAll(new Long(1L).toByteArray());
        expectedHeader.addAll(TarConstants.TAR_HDR_NAME_INDEX);
        expectedHeader.addAll(new Long(shortFileName.length().intValue()).toByteArray());
        expectedHeader.addAll(ArchiveUtils.toBytes(shortFileName));
        expectedHeader.addAll(new Long(content.length));
        expectedHeader.addAll(new Long(tarOutputStream.getPosition()));

        List<Byte> actualHeader = new ArrayList<>();
        actualHeader.addAll(tarOutputStream.buffer.getBuffer(), 0, tarOutputStream.buffer.getPosition());
        tarOutputStream.buffer.reset();

        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testPutArchiveEntryWithFileNameGreaterThanNameLen() throws IOException {
        String longFileName = "test_long_name.txt";
        TarArchiveEntry entry = new TarArchiveEntry(new File(longFileName));
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);
        byte[] longLinkName = ArchiveUtils.toAsciiBytes(entry.getName());
        byte[] longLinkHeader = new byte[longLinkName.length + 1]; // +1 for NUL
        System.arraycopy(longLinkName, 0, longLinkHeader, 0, longLinkName.length);
        tarOutputStream.write(longLinkHeader);
        tarOutputStream.write(0); // NUL terminator
        byte[] header = entry.getHeader(tarOutputStream.getRecordSize());
        tarOutputStream.write(header);
        tarOutputStream.closeArchiveEntry();
        byte[] content = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(content);
        tarOutputStream.close();

        List<Byte> expectedLongLinkHeader = new ArrayList<>();
        expectedLongLinkHeader.addAll(TarConstants.LONG_LINK_MAGIC_NUMBER);
        expectedLongLinkHeader.addAll(TarConstants.LF_GNUTYPE_LONGNAME);
        expectedLongLinkHeader.addAll(new Long(longLinkName.length).toByteArray());
        expectedLongLinkHeader.addAll(longLinkName);

        List<Byte> actualLongLinkHeader = new ArrayList<>();
        actualLongLinkHeader.addAll(tarOutputStream.buffer.getBuffer(), 0, tarOutputStream.buffer.getPosition());
        tarOutputStream.buffer.reset();

        assertEquals(expectedLongLinkHeader, actualLongLinkHeader);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithFileNameGreaterThanNameLenAndLongFileModeError() throws IOException {
        String longFileName = "test_long_name.txt";
        TarArchiveEntry entry = new TarArchiveEntry(new File(longFileName));
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        String dirName = "test_directory";
        File dir = new File(dirName);
        TarArchiveEntry entry = new TarArchiveEntry(dir, dirName);
        tarOutputStream.putArchiveEntry(entry);
        byte[] header = entry.getHeader(tarOutputStream.getRecordSize());
        tarOutputStream.write(header);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        List<Byte> expectedHeader = new ArrayList<>();
        expectedHeader.addAll(TarConstants.MAGIC_NUMBER);
        expectedHeader.addAll(TarConstants.TAR_HDR_FILETYPE_DIRECTORY);
        expectedHeader.addAll(new Long(1L).toByteArray());
        expectedHeader.addAll(TarConstants.TAR_HDR_NAME_INDEX);
        expectedHeader.addAll(new Long(dirName.length().intValue()).toByteArray());
        expectedHeader.addAll(ArchiveUtils.toBytes(dirName));

        List<Byte> actualHeader = new ArrayList<>();
        actualHeader.addAll(tarOutputStream.buffer.getBuffer(), 0, tarOutputStream.buffer.getPosition());
        tarOutputStream.buffer.reset();

        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    public void testWrite() throws IOException {
        String content = "Hello World!";
        byte[] header = new TarArchiveEntry(new File("test.txt")).getHeader(tarOutputStream.getRecordSize());
        tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File("test.txt")));
        tarOutputStream.write(header);
        tarOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        byte[] expectedContent = content.getBytes(StandardCharsets.UTF_8);
        byte[] actualContent = new byte[tarOutputStream.getCount() - tarOutputStream.getSize()];

        System.arraycopy(tarOutputStream.buffer.getBuffer(), 0, actualContent, 0, actualContent.length);
        tarOutputStream.buffer.reset();

        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testCloseArchiveEntryWhenContentLengthIsGreaterThanSpecifiedSize() throws IOException {
        String content = "Hello World!";
        byte[] header = new TarArchiveEntry(new File("test.txt")).getHeader(tarOutputStream.getRecordSize());
        tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File("test.txt")));
        tarOutputStream.write(header);
        tarOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        byte[] expectedContent = content.getBytes(StandardCharsets.UTF_8);
        byte[] actualContent = new byte[tarOutputStream.getCount() - tarOutputStream.getSize()];

        System.arraycopy(tarOutputStream.buffer.getBuffer(), 0, actualContent, 0, actualContent.length);
        tarOutputStream.buffer.reset();

        assertEquals(expectedContent, actualContent);

        assertNull(tarOutputStream.buffer.getNextBlock());
    }

    @Test
    public void testFinish() throws IOException {
        String content = "Hello World!";
        byte[] header = new TarArchiveEntry(new File("test.txt")).getHeader(tarOutputStream.getRecordSize());
        tarOutputStream.putArchiveEntry(new TarArchiveEntry(new File("test.txt")));
        tarOutputStream.write(header);
        tarOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        byte[] eofRecord = new byte[tarOutputStream.getRecordSize()];
        List<Byte> expectedEofRecord = new ArrayList<>();
        expectedEofRecord.addAll(Arrays.asList(TarConstants.MAGIC_NUMBER));
        expectedEofRecord.addAll(TarConstants.TAR_HDR_FILETYPE_END);

        tarOutputStream.buffer.writeRecord(eofRecord);

        List<Byte> actualEofRecord = new ArrayList<>();
        actualEofRecord.addAll(tarOutputStream.buffer.getBuffer(), 0, tarOutputStream.buffer.getPosition());
        tarOutputStream.buffer.reset();

        assertEquals(expectedEofRecord, actualEofRecord);
    }
}