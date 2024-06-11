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
import static org.junit.Assert.assertThrows;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setup() throws Exception {
        out = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(out, 512, 512);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String fileName = "testFile.txt";
        String entryName = "testEntry";
        byte[] fileContent = "This is a test file".getBytes(StandardCharsets.UTF_8);

        File inputFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);

        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        System.arraycopy(TarConstants.MAGIC_NUMBER, 0, expectedHeader, 0, TarConstants.MAGIC_NUMBER.length);
        expectedHeader[512] = TarConstants.STANDARD_FILE_TYPE;
        expectedHeader[513] = TarConstants.LONGNAME_FLAG;
        expectedHeader[514] = (byte) (fileName.length() & 0xFF);
        expectedHeader[515] = (byte) ((fileName.length() >> 8) & 0xFF);
        System.arraycopy(fileName.getBytes(StandardCharsets.UTF_8), 0, expectedHeader, 516, fileName.length());
        expectedHeader[516 + fileName.length()] = TarConstants.NULL;
        expectedHeader[517] = (byte) ((fileContent.length) & 0xFF);
        expectedHeader[518] = (byte) ((fileContent.length >> 8) & 0xFF);
        expectedHeader[519] = (byte) ((fileContent.length >> 16) & 0xFF);
        expectedHeader[520] = (byte) ((fileContent.length >> 24) & 0xFF);

        byte[] expectedContent = Arrays.copyOfRange(fileContent, 0, fileContent.length);

        byte[] actual = out.toByteArray();
        int headerIndex = 0;
        int contentIndex = TarConstants.MAGIC_NUMBER.length + 512;

        for (int i = 0; i < expectedHeader.length; ++i) {
            assertEquals(expectedHeader[i], actual[headerIndex++]);
        }

        for (int i = 0; i < expectedContent.length; ++i) {
            assertEquals(expectedContent[i], actual[contentIndex++]);
        }

        assertEquals(expectedContent.length + TarConstants.MAGIC_NUMBER.length + 512, actual.length);
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String fileName = "testFileWithLongName.txt";
        String entryName = "testEntryWithLongFileName";
        byte[] fileContent = "This is a test file".getBytes(StandardCharsets.UTF_8);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new File("target/test-output.tar"), 512, 512, TarArchiveOutputStream.LONGFILE_GNU);

        File inputFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);

        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();
        tarOutputStream.close();

        File outputFile = new File("target/test-output.tar");
        byte[] expectedHeader = new byte[1024];
        System.arraycopy(TarConstants.MAGIC_NUMBER, 0, expectedHeader, 0, TarConstants.MAGIC_NUMBER.length);
        expectedHeader[512] = TarConstants.STANDARD_FILE_TYPE;
        expectedHeader[513] = TarConstants.LONGNAME_FLAG;
        expectedHeader[514] = (byte) (fileName.length() & 0xFF);
        expectedHeader[515] = (byte) ((fileName.length() >> 8) & 0xFF);
        System.arraycopy(fileName.getBytes(StandardCharsets.UTF_8), 0, expectedHeader, 516, fileName.length());
        expectedHeader[516 + fileName.length()] = TarConstants.NULL;

        byte[] expectedContent = Arrays.copyOfRange(fileContent, 0, fileContent.length);

        File outputFileContent = new File("target/test-output.tar");
        byte[] actual = Files.readAllBytes(outputFileContent.toPath());
        int headerIndex = 0;
        int contentIndex = TarConstants.MAGIC_NUMBER.length + 1024;

        for (int i = 0; i < expectedHeader.length; ++i) {
            assertEquals(expectedHeader[i], actual[headerIndex++]);
        }

        for (int i = 0; i < expectedContent.length; ++i) {
            assertEquals(expectedContent[i], actual[contentIndex++]);
        }

        assertEquals(expectedContent.length + TarConstants.MAGIC_NUMBER.length + 1024, actual.length);
    }

    @Test(expected = RuntimeException.class)
    public void testLongFileNameWithErrorMode() throws IOException {
        String fileName = "testFileWithLongName.txt";
        String entryName = "testEntryWithLongFileName";
        byte[] fileContent = "This is a test file".getBytes(StandardCharsets.UTF_8);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new File("target/test-output.tar"), 512, 512, TarArchiveOutputStream.LONGFILE_ERROR);

        File inputFile = new File("src/test/resources/testFile.txt");
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);

        assertThrows(RuntimeException.class, () -> {
            tarOutputStream.write(fileContent, 0, fileContent.length);
            tarOutputStream.closeArchiveEntry();
        });

        assertEquals(0, tarOutputStream.getRecordSize());
    }

    @Test
    public void testWrite() throws IOException {
        String fileName = "testFile.txt";
        String entryName = "testEntry";
        byte[] fileContent = "This is a test file".getBytes(StandardCharsets.UTF_8);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(out, 512, 512);

        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(new File("src/test/resources/testFile.txt"), fileName);
        tarOutputStream.putArchiveEntry(archiveEntry);

        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[512];
        System.arraycopy(TarConstants.MAGIC_NUMBER, 0, expectedHeader, 0, TarConstants.MAGIC_NUMBER.length);
        expectedHeader[512] = TarConstants.STANDARD_FILE_TYPE;
        expectedHeader[513] = TarConstants.LONGNAME_FLAG;
        expectedHeader[514] = (byte) (fileName.length() & 0xFF);
        expectedHeader[515] = (byte) ((fileName.length() >> 8) & 0xFF);
        System.arraycopy(fileName.getBytes(StandardCharsets.UTF_8), 0, expectedHeader, 516, fileName.length());
        expectedHeader[516 + fileName.length()] = TarConstants.NULL;
        expectedHeader[517] = (byte) ((fileContent.length) & 0xFF);
        expectedHeader[518] = (byte) ((fileContent.length >> 8) & 0xFF);
        expectedHeader[519] = (byte) ((fileContent.length >> 16) & 0xFF);
        expectedHeader[520] = (byte) ((fileContent.length >> 24) & 0xFF);

        byte[] expectedContent = Arrays.copyOfRange(fileContent, 0, fileContent.length);

        byte[] actual = out.toByteArray();
        int headerIndex = 0;
        int contentIndex = TarConstants.MAGIC_NUMBER.length + 512;

        for (int i = 0; i < expectedHeader.length; ++i) {
            assertEquals(expectedHeader[i], actual[headerIndex++]);
        }

        for (int i = 0; i < expectedContent.length; ++i) {
            assertEquals(expectedContent[i], actual[contentIndex++]);
        }

        assertEquals(expectedContent.length + TarConstants.MAGIC_NUMBER.length + 512, actual.length);
    }
}