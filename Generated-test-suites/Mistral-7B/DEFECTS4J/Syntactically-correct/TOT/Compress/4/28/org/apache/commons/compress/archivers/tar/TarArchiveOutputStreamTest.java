import org.apache.commons.compress.archivers.tar.*;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private OutputStream out;
    private TarOutputStream tarOut;
    private ByteArrayOutputStream byteArrayOS;

    @Before
    public void setUp() throws Exception {
        out = new ByteArrayOutputStream();
        tarOut = new TarArchiveOutputStream(out);
        byteArrayOS = new ByteArrayOutputStream();
        tarOut.setOutputStream(byteArrayOS);
    }

    @After
    public void tearDown() throws Exception {
        tarOut.close();
    }

    @Test
    public void testPutArchiveEntry_withDirectoryEntry() throws IOException {
        TarEntry directoryEntry = new TarEntry("testDir");
        directoryEntry.setSize(0);
        directoryEntry.setDirectory(true);
        tarOut.putArchiveEntry(directoryEntry);
        tarOut.closeArchiveEntry();

        byte[] expectedOutput = new byte[TarConstants.MAGIC.length + 1];
        System.arraycopy(TarConstants.MAGIC, 0, expectedOutput, 0, TarConstants.MAGIC.length);
        expectedOutput[TarConstants.MAGIC.length] = TarConstants.Type.DIRECTORY.id;
        expectedOutput[expectedOutput.length - 1] = 0; // null terminator

        assertEquals(expectedOutput, byteArrayOS.toByteArray());
    }

    @Test
    public void testPutArchiveEntry_withFileEntry_and_write_content() throws IOException {
        File testFile = new File("testFile.txt");
        TarEntry fileEntry = new TarEntry("testFile.txt");
        fileEntry.setSize(5);
        tarOut.putArchiveEntry(fileEntry);
        byte[] content = "Hello World!".getBytes();
        tarOut.write(content, 0, content.length);
        tarOut.closeArchiveEntry();

        byte[] expectedOutput = new byte[TarConstants.MAGIC.length + 1 + 5 + content.length];
        System.arraycopy(TarConstants.MAGIC, 0, expectedOutput, 0, TarConstants.MAGIC.length);
        expectedOutput[TarConstants.MAGIC.length] = TarConstants.Type.REGULAR_FILE.id;
        System.arraycopy(fileEntry.getName().getBytes(), 0, expectedOutput, TarConstants.MAGIC.length + 1, fileEntry.getName().length());
        System.arraycopy(content, 0, expectedOutput, TarConstants.MAGIC.length + 1 + fileEntry.getName().length(), content.length);

        assertEquals(expectedOutput, byteArrayOS.toByteArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntry_withNullEntry() throws IOException {
        tarOut.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_withLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 10]).replace("\0", "a");
        TarEntry entry = new TarEntry(longFileName);
        tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOut.putArchiveEntry(entry);

        tarOut.close();
        byte[] output = byteArrayOS.toByteArray();

        // Check if GNU long file extension record has been written
        int indexOfLongFileExtension = indexOfLongFileExtension(output);
        assertTrue(indexOfLongFileExtension != -1);

        // Check if file name record has been written
        int indexOfFileName = indexOfFileName(output, longFileName);
        assertTrue(indexOfFileName != -1);

        // Check if data record has been written
        int indexOfDataRecord = indexOfDataRecord(output, longFileName.length() + 1);
        assertTrue(indexOfDataRecord != -1);
    }

    @Test
    public void testFinish() throws IOException {
        // Write two different entries
        TarEntry firstEntry = new TarEntry("firstEntry.txt");
        firstEntry.setSize(5);
        tarOut.putArchiveEntry(firstEntry);
        byte[] firstContent = "First entry content".getBytes();
        tarOut.write(firstContent, 0, firstContent.length);
        tarOut.closeArchiveEntry();

        TarEntry secondEntry = new TarEntry("secondEntry.txt");
        secondEntry.setSize(10);
        tarOut.putArchiveEntry(secondEntry);
        byte[] secondContent = "Second entry content".getBytes();
        tarOut.write(secondContent, 0, secondContent.length);
        tarOut.closeArchiveEntry();

        tarOut.finish();

        byte[] expectedOutput = new byte[2 * (TarConstants.MAGIC.length + 1) + 5 + firstContent.length + 10 + secondContent.length];
        int i = 0;

        // Write first entry
        System.arraycopy(TarConstants.MAGIC, 0, expectedOutput, i, TarConstants.MAGIC.length);
        expectedOutput[i + TarConstants.MAGIC.length] = TarConstants.Type.REGULAR_FILE.id;
        System.arraycopy(firstEntry.getName().getBytes(), 0, expectedOutput, i + TarConstants.MAGIC.length + 1, firstEntry.getName().length());
        System.arraycopy(firstContent, 0, expectedOutput, i + TarConstants.MAGIC.length + 1 + firstEntry.getName().length(), firstContent.length);
        i += firstContent.length + firstEntry.getName().length() + TarConstants.MAGIC.length + 1;

        System.arraycopy(TarConstants.MAGIC, 0, expectedOutput, i, TarConstants.MAGIC.length);
        expectedOutput[i + TarConstants.MAGIC.length] = TarConstants.Type.REGULAR_FILE.id;
        System.arraycopy(secondEntry.getName().getBytes(), 0, expectedOutput, i + TarConstants.MAGIC.length + 1, secondEntry.getName().length());
        System.arraycopy(secondContent, 0, expectedOutput, i + TarConstants.MAGIC.length + 1 + secondEntry.getName().length(), secondContent.length);
        i += secondContent.length + secondEntry.getName().length() + TarConstants.MAGIC.length + 1;

        assertEquals(expectedOutput, byteArrayOS.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        TarEntry entry = new TarEntry("testFile.txt");
        entry.setSize(5);
        tarOut.putArchiveEntry(entry);
        byte[] content = "Hello World!".getBytes();
        tarOut.write(content, 0, content.length);

        tarOut.close();

        byte[] expectedOutput = new byte[TarConstants.MAGIC.length + 1 + 5 + content.length];
        System.arraycopy(TarConstants.MAGIC, 0, expectedOutput, 0, TarConstants.MAGIC.length);
        expectedOutput[TarConstants.MAGIC.length] = TarConstants.Type.REGULAR_FILE.id;
        System.arraycopy(entry.getName().getBytes(), 0, expectedOutput, TarConstants.MAGIC.length + 1, entry.getName().length());

        assertEquals(expectedOutput, byteArrayOS.toByteArray());
        assertFalse(byteArrayOS.hasRemaining());
    }

    private int indexOfLongFileExtension(byte[] output) {
        for (int i = 0; i < output.length - TarConstants.LongFileNameExtension.length; i++) {
            if (TarConstants.LongFileNameExtension.equals(new String(output, i, TarConstants.LongFileNameExtension.length()))) {
                return i;
            }
        }
        return -1;
    }

    private int indexOfFileName(byte[] output, String fileName) {
        return indexOf(output, fileName.getBytes(), 0, output.length - fileName.length());
    }

    private int indexOfDataRecord(byte[] output, int offset) {
        return indexOf(output, TarConstants.MAGIC, offset, output.length - TarConstants.MAGIC.length);
    }

    private int indexOf(byte[] output, byte[] searchString, int startIndex, int length) {
        for (int i = startIndex; i < startIndex + length - searchString.length; i++) {
            int j = 0;
            while (output[i + j] == searchString[j]) {
                j++;
                if (j == searchString.length) {
                    return i;
                }
            }
        }
        return -1;
    }
}