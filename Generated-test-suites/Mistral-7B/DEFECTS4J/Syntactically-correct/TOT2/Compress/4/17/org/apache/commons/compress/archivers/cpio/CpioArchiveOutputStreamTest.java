import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private File outputFile;
    private ByteArrayOutputStream byteArrayOutputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputFile = new File("test.cpio");
        byteArrayOutputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(
                new FileOutputStream(outputFile), CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    /**
     * Test writing a single entry
     */
    @Test
    public void testWriteSingleEntry() throws IOException {
        String testFileContent = "Hello World!";

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "testfile.txt", "testfile.txt", null, null, null));
        cpioArchiveOutputStream.write(testFileContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedCpioContent = new byte[] {
                (byte) 0x61, (byte) 'C', (byte) 'P', (byte) 'I', (byte) 'O',
                (byte) '\r', (byte) '\n', (byte) 0x4e, (byte) 'I', (byte) 'n',
                (byte) 'd', (byte) 'x', (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0x07, (byte) 0x74, (byte) 't', (byte) 'e', (byte) 's',
                (byte) 't', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e',
                (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) 0,
                (byte) 32, (byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l',
                (byte) 'o', (byte) ' ', (byte) 'W', (byte) 'o', (byte) 'r',
                (byte) 'l', (byte) 'd', (byte) '!', (byte) '\r', (byte) '\n',
                (byte) '\0'
        };

        byte[] actualCpioContent = byteArrayOutputStream.toByteArray();
        assertEquals(expectedCpioContent.length, actualCpioContent.length);
        for (int i = 0; i < expectedCpioContent.length; i++) {
            assertEquals(expectedCpioContent[i], actualCpioContent[i]);
        }

        byte[] fileContent = IOUtils.toByteArray(new File("testfile.txt"));
        cpioArchiveOutputStream.putArchiveEntry(new File("testfile.txt"));
        cpioArchiveOutputStream.write(fileContent);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] expectedFileContent = fileContent;
        byte[] actualFileContent = new byte[cpioArchiveOutputStream.read(
                new byte[1024])];
        assertEquals(expectedFileContent.length, actualFileContent.length);
        for (int i = 0; i < expectedFileContent.length; i++) {
            assertEquals(expectedFileContent[i], actualFileContent[i]);
        }
    }

    /**
     * Test writing multiple entries
     */
    @Test
    public void testWriteMultipleEntries() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "entry1.txt", "entry1.txt", null, null, null));
        cpioArchiveOutputStream.write("Hello World!".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "entry2.txt", "entry2.txt", null, null, null));
        cpioArchiveOutputStream.write("Hello Java!".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        File testFile = new File("test.cpio");
        byte[] expectedCpioContent = new byte[] {
                (byte) 0x61, (byte) 'C', (byte) 'P', (byte) 'I', (byte) 'O',
                (byte) '\r', (byte) '\n', (byte) 0x4e, (byte) 'I', (byte) 'n',
                (byte) 'd', (byte) 'x', (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0x07, (byte) 0x74, (byte) 't', (byte) 'e', (byte) 's',
                (byte) 't', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e',
                (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) 0,
                (byte) 32, (byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l',
                (byte) 'o', (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0x07, (byte) 0x74, (byte) 't', (byte) 'e',
                (byte) 's', (byte) 't', (byte) 'f', (byte) 'i', (byte) 'l',
                (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't',
                (byte) 0, (byte) 32, (byte) 'H', (byte) 'e', (byte) 'l',
                (byte) 'l', (byte) 'o', (byte) ' ', (byte) 'J', (byte) 'a',
                (byte) 'v', (byte) 'a', (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0x07, (byte) 0x74, (byte) 't', (byte) 'e', (byte) 's',
                (byte) 't', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e',
                (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) 0,
                (byte) '\r', (byte) '\n', (byte) '\0'
        };

        byte[] actualCpioContent = IOUtils.toByteArray(testFile);
        assertEquals(expectedCpioContent.length, actualCpioContent.length);
        for (int i = 0; i < expectedCpioContent.length; i++) {
            assertEquals(expectedCpioContent[i], actualCpioContent[i]);
        }

        byte[] entry1Content = IOUtils.toByteArray(new File("entry1.txt"));
        byte[] entry2Content = IOUtils.toByteArray(new File("entry2.txt"));

        cpioArchiveOutputStream = new CpioArchiveOutputStream(
                new FileOutputStream("test2.cpio"), CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "entry1.txt", "entry1.txt", null, null, null));
        cpioArchiveOutputStream.write(entry1Content);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "entry2.txt", "entry2.txt", null, null, null));
        cpioArchiveOutputStream.write(entry2Content);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        File testFile2 = new File("test2.cpio");
        byte[] actualEntry1Content = new byte[cpioArchiveOutputStream.read(
                new byte[1024])];
        assertEquals(entry1Content.length, actualEntry1Content.length);
        for (int i = 0; i < entry1Content.length; i++) {
            assertEquals(entry1Content[i], actualEntry1Content[i]);
        }

        byte[] actualEntry2Content = new byte[cpioArchiveOutputStream.read(
                new byte[1024])];
        assertEquals(entry2Content.length, actualEntry2Content.length);
        for (int i = 0; i < entry2Content.length; i++) {
            assertEquals(entry2Content[i], actualEntry2Content[i]);
        }
    }

    /**
     * Test writing a single entry with incorrect data
     */
    @Test(expected = IOException.class)
    public void testWriteSingleEntryWithIncorrectData() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "testfile.txt", "testfile.txt", null, null, null));
        cpioArchiveOutputStream.write(new byte[] { 0x01, 0x02 });
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    /**
     * Test writing multiple entries with incorrect data
     */
    @Test(expected = IOException.class)
    public void testWriteMultipleEntriesWithIncorrectData() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "entry1.txt", "entry1.txt", null, null, null));
        cpioArchiveOutputStream.write(new byte[] { 0x01, 0x02 });
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "entry2.txt", "entry2.txt", null, null, null));
        cpioArchiveOutputStream.write(new byte[] { 0x01, 0x03 });
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
    }

    /**
     * Test writing multiple entries with empty names
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWriteMultipleEntriesWithEmptyNames() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "", "testfile.txt", null, null, null));
        cpioArchiveOutputStream.write("Hello World!".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "", "testfile2.txt", null, null, null));
        cpioArchiveOutputStream.write("Hello Java!".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
    }

    /**
     * Test writing multiple entries with the same name
     */
    @Test(expected = IOException.class)
    public void testWriteMultipleEntriesWithSameName() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "testfile.txt", "testfile.txt", null, null, null));
        cpioArchiveOutputStream.write("Hello World!".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "testfile.txt", "testfile.txt", null, null, null));
        cpioArchiveOutputStream.write("Hello Java!".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
    }

    /**
     * Test writing multiple entries with incorrect file sizes
     */
    @Test(expected = IOException.class)
    public void testWriteMultipleEntriesWithIncorrectFileSizes() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "entry1.txt", "entry1.txt", null, null, null));
        cpioArchiveOutputStream.write("Hello World!".getBytes());
        cpioArchiveOutputStream.writeTo(new byte[103]); // 1 byte more than the expected size
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(new CpioArchiveEntry(
                "entry2.txt", "entry2.txt", null, null, null));
        cpioArchiveOutputStream.write("Hello Java!".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
    }
}