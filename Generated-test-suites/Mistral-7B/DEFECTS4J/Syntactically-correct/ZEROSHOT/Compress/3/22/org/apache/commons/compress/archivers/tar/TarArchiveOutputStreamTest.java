import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() throws IOException {
        testFile = File.createTempFile("testTar", ".tar");
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void teardown() throws IOException {
        testFile.delete();
    }

    @Test
    public void testConstructorWithOutputStream() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        assertNull(tarOutputStream.getFile());
    }

    @Test
    public void testConstructorWithOutputStreamBlockSizeAndRecordSize() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, 1024, 512);
        assertNull(tarOutputStream.getFile());
        assertEquals(1024, tarOutputStream.getBlockSize());
        assertEquals(512, tarOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileNameWhenLongFileModeIsError() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        String longFileName = "longFileName";
        byte[] longFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("testFile"));
        tarEntry.setName(longFileName);
        tarOutputStream.putArchiveEntry(tarEntry);
    }

    @Test
    public void testWriteToOutputStream() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);

        byte[] testData = "testData".getBytes(StandardCharsets.UTF_8);
        byte[] testDataWithLongName = "testDataWithLongName".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("testFile"), "testEntry.txt");
        tarEntry.setSize(testData.length);
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry tarEntryWithLongName = new TarArchiveEntry(new File("testFile"), "testEntryWithLongName.txt");
        tarEntryWithLongName.setSize(testDataWithLongName.length);

        if (TarArchiveOutputStream.LONGFILE_TRUNCATE == tarOutputStream.getLongFileMode()) {
            tarOutputStream.putArchiveEntry(tarEntryWithLongName);
            tarOutputStream.write(testDataWithLongName, 0, testDataWithLongName.length);
            tarOutputStream.closeArchiveEntry();
        } else if (TarArchiveOutputStream.LONGFILE_GNU == tarOutputStream.getLongFileMode()) {
            byte[] longLinkHeader = new byte[TarConstants.LF_GNULEN + testFileNameWithLongName.length + 1];
            System.arraycopy(testFileNameWithLongName.getBytes(StandardCharsets.UTF_8), 0, longLinkHeader, 0, testFileNameWithLongName.length);
            longLinkHeader[TarConstants.LF_GNULEN] = (byte) 0;
            tarOutputStream.write(longLinkHeader, 0, TarConstants.LF_GNULEN + testFileNameWithLongName.length + 1);
            tarOutputStream.write(testDataWithLongName, 0, testDataWithLongName.length);
            tarOutputStream.closeArchiveEntry();
        } else {
            throw new RuntimeException("Unexpected long file mode: " + tarOutputStream.getLongFileMode());
        }

        byte[] output = outputStream.toByteArray();

        int testEntryPosition = 0;
        int testDataPosition = 0;
        int testDataWithLongNamePosition = 0;

        if (TarArchiveOutputStream.LONGFILE_TRUNCATE == tarOutputStream.getLongFileMode()) {
            testDataPosition = TarConstants.HEADER_SIZE + testData.length;
            testDataWithLongNamePosition = TarConstants.HEADER_SIZE
                    + testFileNameWithLongName.length + 1 + testDataWithLongName.length;
        } else if (TarArchiveOutputStream.LONGFILE_GNU == tarOutputStream.getLongFileMode()) {
            testDataPosition = TarConstants.HEADER_SIZE + testFileNameWithLongName.length + 1 + testDataWithLongName.length + testData.length;
        }

        assertEquals(testData[0], output[testEntryPosition++]);
        for (int i = 1; i < testData.length; i++) {
            assertEquals(testData[i], output[testEntryPosition++]);
        }

        if (TarArchiveOutputStream.LONGFILE_TRUNCATE == tarOutputStream.getLongFileMode()) {
            assertEquals(testData[0], output[testDataPosition++]);
            for (int i = 1; i < testData.length; i++) {
                assertEquals(testData[i], output[testDataPosition++]);
            }
        } else {
            int longLinkHeaderLength = TarConstants.HEADER_SIZE + testFileNameWithLongName.length + 1;
            for (int i = 0; i < longLinkHeaderLength; i++) {
                assertEquals(testFileNameWithLongName[i], output[testEntryPosition++]);
            }
            for (int i = 0; i < testDataWithLongName.length; i++) {
                assertEquals(testDataWithLongName[i], output[testEntryPosition++]);
            }
            for (int i = 0; i < testData.length; i++) {
                assertEquals(testData[i], output[testEntryPosition++]);
            }
        }

        assertEquals(output.length, TarConstants.HEADER_SIZE + testFileNameWithLongName.length + 1 + testDataWithLongName.length + testData.length);
    }

    @Test
    public void testWriteToFile() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(testFile);

        byte[] testData = "testData".getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry tarEntry = new TarArchiveEntry(new File("testFile"), "testEntry.txt");
        tarEntry.setSize(testData.length);
        tarOutputStream.putArchiveEntry(tarEntry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();

        File outputFile = new File(testFile.getAbsolutePath() + "/testEntry.txt");
        byte[] output = Files.readAllBytes(outputFile.toPath());

        assertEquals(testData[0], output[0]);
        for (int i = 1; i < testData.length; i++) {
            assertEquals(testData[i], output[i]);
        }
    }
}