import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private TarArchiveOutputStream tarStream;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        tarStream = new TarArchiveOutputStream(new CountingOutputStream(out));
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Test putting an entry with a short name
        TarArchiveEntry entry1 = new TarArchiveEntry("testEntry1.txt");
        tarStream.putArchiveEntry(entry1);

        // Test putting an entry with a long name
        TarArchiveEntry entry2 = new TarArchiveEntry("testEntry2.txt_with_long_name.txt");
        if (tarStream.getLongFileMode() != TarArchiveOutputStream.LONGFILE_ERROR) {
            tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        }
        tarStream.putArchiveEntry(entry2);

        // Test putting an entry with a long name with GNU extension
        TarArchiveEntry entry3 = new TarArchiveEntry("testEntry3.txt_with_long_name.txt");
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarStream.putArchiveEntry(entry3);

        // Test putting an entry with a long name and truncating it
        TarArchiveEntry entry4 = new TarArchiveEntry("testEntry4.txt_with_very_long_name.txt");
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarStream.putArchiveEntry(entry4);

        // Test putting an entry without closing it
        TarArchiveEntry entry5 = new TarArchiveEntry("testEntry5.txt");
        tarStream.putArchiveEntry(entry5);
        Assert.assertTrue(tarStream.haveUnclosedEntry);

        // Test putting an entry and closing it
        tarStream.closeArchiveEntry();
        Assert.assertFalse(tarStream.haveUnclosedEntry);

        // Test putting multiple entries
        TarArchiveEntry entry6 = new TarArchiveEntry("testEntry6.txt");
        TarArchiveEntry entry7 = new TarArchiveEntry("testEntry7.txt");
        tarStream.putArchiveEntry(entry6);
        tarStream.putArchiveEntry(entry7);
        tarStream.finish();

        // Test getting the record size
        Assert.assertEquals(512, tarStream.getRecordSize());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarStream.putArchiveEntry(entry);

        byte[] data = "Test data".getBytes(StandardCharsets.UTF_8);
        tarStream.write(data, 0, data.length);

        tarStream.finish();
        byte[] outputData = out.toByteArray();

        // Check that the header and data are written correctly
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputData);
        TarArchiveEntry headerEntry = new TarArchiveEntry(new ByteArrayInputStream(outputData));
        Assert.assertEquals(headerEntry, tarStream.getNextEntry());
        Assert.assertArrayEquals(data, new byte[inputStream.available()]);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithClosedStream() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.close();
        tarStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithFinishedStream() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();
        tarStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarStream.putArchiveEntry(entry);
        tarStream.putArchiveEntry(entry);
    }
}