import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntryAndClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] expectedHeader = "0000000000000000 54686973207465787465722063616e6e6574207468697320696d706c6520737461746559010000"
                + "0000000000000000 415441542020000000010000000032000000000020323130312061636361757465206d65737361676520"
                + "7463616e73736572746573206d617820636f6d70657273657221";
        byte[] expectedContent = "test content".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(expectedContent);

        tarArchiveOutputStream.closeArchiveEntry();

        byte[] actual = outputStream.toByteArray();

        byte[] header = new byte[expectedHeader.length()];
        System.arraycopy(expectedHeader.getBytes(), 0, header, 0, expectedHeader.length());
        assertArrayEquals(header, actual, 0, 512);

        // check if the rest of the output matches the expected tar format
        byte[] expectedTarFormat = new byte[actual.length - 512];
        System.arraycopy(actual, 512, expectedTarFormat, 0, expectedTarFormat.length);
        assertArrayEquals(expectedTarFormat, actual, 512, actual.length);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testLongFileNamePutArchiveEntry() throws IOException {
        String longFileName = "long_file_name_" + TarConstants.NAMELEN + "_characters.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] expectedHeader = "0000000000000000 54686973207465787465722063616e6e6574207468697320696d706c6520737461746559010000"
                + "0000000000000000 415441542020000000010000000032000000000020323130312061636361757465206d65737361676520"
                + "7463616e73736572746573206d617820636f6d70657273657221";
        tarArchiveOutputStream.write(Long.BYTES); // write the size of 2 longs, to simulate a long file size

        tarArchiveOutputStream.finish();

        byte[] actual = outputStream.toByteArray();

        byte[] header = new byte[expectedHeader.length()];
        System.arraycopy(expectedHeader.getBytes(), 0, header, 0, expectedHeader.length());
        assertArrayEquals(header, actual, 0, 512);

        // check if the rest of the output matches the expected tar format
        byte[] expectedTarFormat = new byte[actual.length - 512];
        System.arraycopy(actual, 512, expectedTarFormat, 0, expectedTarFormat.length);
        assertArrayEquals(expectedTarFormat, actual, 512, actual.length);
    }
}