import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TarArchiveOutputStreamTest {

    private static final byte[] TEST_DATA = "Test data".getBytes();

    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream testOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        testOutputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        outputStream.close();
        testOutputStream.close();
    }

    @Test
    public void testCreateOutputStream() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertThat(tarOutputStream, is(instanceOf(TarArchiveOutputStream.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOutputStreamWithNullOutputStream() {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertThat(tarOutputStream.getLongFileMode(), is(TarArchiveOutputStream.LONGFILE_TRUNCATE));
    }

    @Test
    public void testPutArchiveEntryWithSmallFileName() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(TEST_DATA.length);

        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(TEST_DATA, 0, TEST_DATA.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entry = new TarArchiveEntry(inputStream);
        entry.validateHeader();

        assertThat(entry.getName(), is("test.txt"));
        assertThat(entry.getSize(), is(TEST_DATA.length));
        byte[] buffer = new byte[TarConstants.HEADER_SIZE];
        inputStream.read(buffer, 0, TarConstants.HEADER_SIZE);
        TarArchiveEntry header = new TarArchiveEntry(new ByteArrayInputStream(buffer));
        header.validateHeader();
        assertThat(header.getName(), is("test.txt"));
        assertThat(header.getSize(), is(TEST_DATA.length));
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "longFileNameThatIsLongerThanTarConstants.NAMELEN.txt";
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveEntry.setSize(TEST_DATA.length);

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(longFileName.getBytes());
        tarOutputStream.write(0);
        tarOutputStream.write(TEST_DATA, 0, TEST_DATA.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entry = new TarArchiveEntry(inputStream);
        entry.validateHeader();

        assertThat(entry.getName(), is(longFileName));
        assertThat(entry.getSize(), is(TEST_DATA.length));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(TEST_DATA.length);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(TEST_DATA, 0, TEST_DATA.length);
        tarOutputStream.putArchiveEntry(tarArchiveEntry); // Should throw an exception
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(TEST_DATA.length);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(TEST_DATA, 0, TEST_DATA.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entry = new TarArchiveEntry(inputStream);
        entry.validateHeader();

        byte[] expected = new byte[TEST_DATA.length];
        System.arraycopy(TEST_DATA, 0, expected, 0, TEST_DATA.length);
        byte[] actual = new byte[TEST_DATA.length];
        inputStream.read(actual, 0, TEST_DATA.length);

        assertThat(entry.getName(), is("test.txt"));
        assertThat(entry.getSize(), is(TEST_DATA.length));
        assertThat(actual, is(expected));
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(TEST_DATA.length);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(TEST_DATA, 0, TEST_DATA.length);
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entry = new TarArchiveEntry(inputStream);
        entry.validateHeader();

        byte[] expected = new byte[TEST_DATA.length];
        System.arraycopy(TEST_DATA, 0, expected, 0, TEST_DATA.length);
        byte[] actual = new byte[TEST_DATA.length];
        inputStream.read(actual, 0, TEST_DATA.length);

        assertThat(entry.getName(), is("test.txt"));
        assertThat(entry.getSize(), is(TEST_DATA.length));
        assertThat(actual, is(expected));

        byte[] eofRecord = new byte[TarConstants.RECORD_SIZE];
        inputStream.read(eofRecord, 0, TarConstants.RECORD_SIZE);
        TarArchiveEntry eofEntry = new TarArchiveEntry(new ByteArrayInputStream(eofRecord));
        eofEntry.validateHeader();

        assertThat(eofEntry.getName(), is(TarConstants.GNU_EOF));
        assertThat(eofEntry.getSize(), is(0L));
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveEntry.setSize(TEST_DATA.length);
        tarOutputStream.putArchiveEntry(tarArchiveEntry);
        tarOutputStream.write(TEST_DATA, 0, TEST_DATA.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry entry = new TarArchiveEntry(inputStream);
        entry.validateHeader();

        byte[] expected = new byte[TEST_DATA.length];
        System.arraycopy(TEST_DATA, 0, expected, 0, TEST_DATA.length);
        byte[] actual = new byte[TEST_DATA.length];
        inputStream.read(actual, 0, TEST_DATA.length);

        assertThat(entry.getName(), is("test.txt"));
        assertThat(entry.getSize(), is(TEST_DATA.length));
        assertThat(actual, is(expected));

        byte[] eofRecord = new byte[TarConstants.RECORD_SIZE];
        inputStream.read(eofRecord, 0, TarConstants.RECORD_SIZE);
        TarArchiveEntry eofEntry = new TarArchiveEntry(new ByteArrayInputStream(eofRecord));
        eofEntry.validateHeader();

        assertThat(eofEntry.getName(), is(TarConstants.GNU_EOF));
        assertThat(eofEntry.getSize(), is(0L));
    }
}