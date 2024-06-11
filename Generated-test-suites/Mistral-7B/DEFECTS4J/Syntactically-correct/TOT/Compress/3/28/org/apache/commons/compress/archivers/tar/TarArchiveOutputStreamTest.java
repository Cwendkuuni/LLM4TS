import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testWriteEmptyArchive() throws IOException {
        tarOutputStream.finish();

        byte[] expectedEof = new byte[TarConstants.RECORD_SIZE];
        Arrays.fill(expectedEof, (byte) 0);

        byte[] expectedEof2 = new byte[TarConstants.RECORD_SIZE];
        Arrays.fill(expectedEof2, (byte) 0);

        byte[] expectedEmptyArchive = outputStream.toByteArray();
        byte[] eofRecord = tarOutputStream.getBuffer().getRecord();

        byte[] eofRecord2 = tarOutputStream.getBuffer().getRecord();

        assertEquals("Unexpected EOF record size", TarConstants.RECORD_SIZE, eofRecord.length);
        assertEquals("Unexpected EOF record size", TarConstants.RECORD_SIZE, eofRecord2.length);

        assertArrayEquals("Unexpected empty archive", expectedEmptyArchive, outputStream.toByteArray());
        assertArrayEquals("Unexpected EOF records", expectedEof, eofRecord);
        assertArrayEquals("Unexpected EOF records", expectedEof2, eofRecord2);
    }

    @Test
    public void testWriteSingleFile() throws IOException {
        File testFile = new File("testFile.txt");
        String fileName = "testFile.txt";

        tarOutputStream.putNextEntry(new TarArchiveEntry(testFile, fileName));
        tarOutputStream.write("Hello, World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedOutput = new byte[outputStream.size()];
        outputStream.read(expectedOutput);

        byte[] expectedEntryHeader = new byte[TarConstants.RECORD_SIZE];
        new TarArchiveEntry(new File(""), "testFile.txt").writeEntryHeader(expectedEntryHeader);

        byte[] expectedFileData = "Hello, World!".getBytes();

        assertArrayEquals("Unexpected output", expectedOutput, outputStream.toByteArray());
        assertEquals("Unexpected entry header size", TarConstants.RECORD_SIZE, expectedOutput.length);
        assertArrayEquals("Unexpected entry header", expectedEntryHeader, Arrays.copyOfRange(expectedOutput, 0, TarConstants.RECORD_SIZE));
        assertArrayEquals("Unexpected file data", expectedFileData, Arrays.copyOfRange(expectedOutput, TarConstants.RECORD_SIZE, expectedOutput.length));
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN]) + "b";

        tarOutputStream.setLongFileMode(TarOutputStream.LONGFILE_GNU);

        tarOutputStream.putNextEntry(new TarArchiveEntry(new File(""), longFileName));

        // This should not be reached
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileNameWithoutLongFileMode() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN]) + "b";

        tarOutputStream.putNextEntry(new TarArchiveEntry(new File(""), longFileName));

        // This should not be reached
    }
}