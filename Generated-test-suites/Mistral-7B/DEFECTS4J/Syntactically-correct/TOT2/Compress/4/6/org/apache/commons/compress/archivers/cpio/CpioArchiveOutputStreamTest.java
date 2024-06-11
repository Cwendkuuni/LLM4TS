import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private CpioArchiveOutputStream out;
    private ByteArrayOutputStream dataStream;

    @Before
    public void setUp() throws IOException {
        outputFile = new File("test.cpio");
        dataStream = new ByteArrayOutputStream();
        out = new CpioArchiveOutputStream(new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        out.close();
        outputFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile", "testdata".getBytes());
        out.putArchiveEntry(entry);
        out.write("testdata".getBytes(), 0, "testdata".length());
        out.closeArchiveEntry();

        InputStream input = new FileInputStream(outputFile);
        assertEquals(ArchiveUtils.readFileToByteArray(outputFile).length, outputFile.length());
        input.close();
        outputFile.delete();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        out.putArchiveEntry(entry);
        out.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        out.putArchiveEntry(entry);
        out.setEntryFormat((short) 10);
        try {
            out.putArchiveEntry(entry);
        } finally {
            out.setEntryFormat(CpioConstants.FORMAT_NEW);
        }
    }

    @Test
    public void testWriteDataAndFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        out.putArchiveEntry(entry);
        out.write(dataStream.toByteArray(), 0, dataStream.size());
        out.finish();

        InputStream input = new FileInputStream(outputFile);
        byte[] expectedBytes = CpioUtil.concatenate(
                ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW),
                CpioUtil.int2byteArray((short) 0),
                CpioUtil.int2byteArray((int) CpioConstants.FORMAT_NEW),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray((long) entry.getSize() + dataStream.size()),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray((long) entry.getCRC()),
                ArchiveUtils.toAsciiBytes(entry.getName()),
                CpioUtil.int2byteArray(5),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                ArchiveUtils.toAsciiBytes(""),
                CpioUtil.long2byteArray(0),
                ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW),
                CpioUtil.int2byteArray((short) 0),
                CpioUtil.int2byteArray((int) CpioConstants.FORMAT_NEW),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                ArchiveUtils.toAsciiBytes(""),
                CpioUtil.long2byteArray(dataStream.size()),
                dataStream.toByteArray(),
                CpioUtil.int2byteArray((int) CpioConstants.FORMAT_NEW),
                CpioUtil.int2byteArray((int) CpioConstants.C_ISREG),
                CpioUtil.int2byteArray((int) 0),
                CpioUtil.int2byteArray((int) 0),
                CpioUtil.int2byteArray((int) 0),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.int2byteArray((int) 0),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.long2byteArray(0L),
                CpioUtil.int2byteArray(0),
                CpioUtil.int2byteArray(0),
                CpioUtil.int2byteArray(0),
                CpioUtil.byte2byteArray((byte) 0x0),
                CpioUtil.byte2byteArray((byte) 0x0),
                CpioUtil.byte2byteArray((byte) 0x0),
                CpioUtil.byte2byteArray((byte) 0x0),
                CpioUtil.byte2byteArray((byte) 0x0)
        );
        byte[] actualBytes = new byte[expectedBytes.length];
        input.read(actualBytes);
        input.close();
        assertEquals(expectedBytes, actualBytes);
        outputFile.delete();
    }

    @Test
    public void testWriteDataAndWriteAnotherEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry("testfile1");
        out.putArchiveEntry(entry1);
        out.write(dataStream.toByteArray(), 0, dataStream.size());

        CpioArchiveEntry entry2 = new CpioArchiveEntry("testfile2");
        out.putArchiveEntry(entry2);
        out.write(dataStream.toByteArray(), 0, dataStream.size());
        out.finish();

        Map<String, CpioArchiveEntry> names = new HashMap<>();
        InputStream input = new FileInputStream(outputFile);
        assertNotNull(input);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            String name = new String(buffer, 0, ArchiveUtils.getLongNameLength(buffer));
            CpioArchiveEntry readEntry = new CpioArchiveEntry(new ByteArrayInputStream(buffer));
            names.put(name, readEntry);
        }
        input.close();

        assertEquals(2, names.size());
        assertEquals(entry1.getName(), names.get("testfile1").getName());
        assertEquals(entry2.getName(), names.get("testfile2").getName());

        outputFile.delete();
    }

    @Test(expected = IOException.class)
    public void testWriteDataAfterFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testfile");
        out.putArchiveEntry(entry);
        out.finish();

        out.write(dataStream.toByteArray(), 0, dataStream.size());
    }
}