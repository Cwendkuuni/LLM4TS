import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Test class for the CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        // Test construction with default format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream);

        // Test construction with specified format
        short format = CpioConstants.FORMAT_OLD_BINARY;
        ByteArrayOutputStream outputStreamBin = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStreamBin = new CpioArchiveOutputStream(outputStreamBin, format);

        // Test putNextEntry method with default format
        CpioArchiveEntry entryAscii = new CpioArchiveEntry();
        entryAscii.setName("testfileAscii.txt");
        entryAscii.setSize(10);
        archiveOutputStream.putNextEntry(entryAscii);

        // Test putNextEntry method with specified format
        CpioArchiveEntry entryBinary = new CpioArchiveEntry();
        entryBinary.setName("testfileBinary.bin");
        entryBinary.setSize(5);
        entryBinary.setFormat(format);
        archiveOutputStreamBin.putNextEntry(entryBinary);

        // Test write method
        byte[] testDataAscii = "Hello World!".getBytes();
        archiveOutputStream.write(testDataAscii, 0, testDataAscii.length);

        byte[] testDataBinary = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        archiveOutputStreamBin.write(testDataBinary, 0, testDataBinary.length);

        archiveOutputStream.finish();
        archiveOutputStreamBin.finish();

        // Test getName method of entries in the map
        HashMap<String, CpioArchiveEntry> entries = archiveOutputStream.getEntries();
        Assert.assertEquals("testfileAscii.txt", entries.get(0).getName());

        entries = archiveOutputStreamBin.getEntries();
        Assert.assertEquals("testfileBinary.bin", entries.get(0).getName());

        // Test readArchive method with default format
        ByteArrayInputStream inputStreamAscii = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveStreamFactory factory = new DefaultStreamFactory();
        ArchiveInputStream archiveInputStreamAscii = factory.createArchiveInputStream("test.cpio", inputStreamAscii);
        Assert.assertNotNull(archiveInputStreamAscii);
        Assert.assertEquals("testfileAscii.txt", archiveInputStreamAscii.getNextEntry().getName());

        // Test readArchive method with specified format
        ByteArrayInputStream inputStreamBin = new ByteArrayInputStream(outputStreamBin.toByteArray());
        ArchiveStreamFactory factoryBin = new DefaultStreamFactory();
        ArchiveInputStream archiveInputStreamBin = factoryBin.createArchiveInputStream("test.cpio", inputStreamBin);
        Assert.assertNotNull(archiveInputStreamBin);
        Assert.assertEquals("testfileBinary.bin", archiveInputStreamBin.getNextEntry().getName());

        // Test close method
        archiveOutputStream.close();
        archiveOutputStreamBin.close();
    }

    @Test(expected = IOException.class)
    public void testIllegalFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 0xFF);
    }

    @Test(expected = IOException.class)
    public void testDuplicateEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(outputStream);

        CpioArchiveEntry entryAscii = new CpioArchiveEntry();
        entryAscii.setName("testfileAscii.txt");
        entryAscii.setSize(10);
        archiveOutputStream.putNextEntry(entryAscii);

        try {
            archiveOutputStream.putNextEntry(entryAscii);
        } catch (IOException e) {
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }
}