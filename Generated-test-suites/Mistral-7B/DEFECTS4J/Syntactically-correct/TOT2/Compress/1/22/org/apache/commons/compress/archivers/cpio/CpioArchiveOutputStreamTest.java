import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private static final String ENTRY_NAME = "testEntry.txt";
    private static final String ENTRY_CONTENT = "test content";

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutNextEntryAndWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.write(ENTRY_CONTENT.getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        CpioArchiveEntry actualEntry = new CpioArchiveEntry();
        actualEntry.setName(ENTRY_NAME);
        expectedEntries.put(ENTRY_NAME, actualEntry);

        Assert.assertEquals(expectedEntries, getEntriesFromOutputStream(byteArrayOutputStream.toByteArray()));
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.closeArchiveEntry();
        outputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, (short) 0xFF);
        entry.setName(ENTRY_NAME);
        outputStream.putNextEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondEntrySize() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.write("test".getBytes());
        outputStream.write(" content".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        CpioArchiveEntry actualEntry = new CpioArchiveEntry();
        actualEntry.setName(ENTRY_NAME);
        expectedEntries.put(ENTRY_NAME, actualEntry);

        Assert.assertEquals(expectedEntries, getEntriesFromOutputStream(byteArrayOutputStream.toByteArray()));

        byte[] expectedContent = "test content".getBytes();
        byte[] actualContent = byteArrayOutputStream.toByteArray();
        int index = getIndexOfEntryName(actualContent, ENTRY_NAME);
        byte[] entryContent = new byte[expectedContent.length];
        System.arraycopy(actualContent, index, entryContent, 0, expectedContent.length);
        Assert.assertArrayEquals(expectedContent, entryContent);

        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_NEW);
        entry = new CpioArchiveEntry();
        entry.setName(ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.write(ENTRY_CONTENT.getBytes());
        outputStream.write(" extra data".getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        expectedEntries = new HashMap<>();
        actualEntry = new CpioArchiveEntry();
        actualEntry.setName(ENTRY_NAME);
        expectedEntries.put(ENTRY_NAME, actualEntry);

        Assert.assertEquals(expectedEntries, getEntriesFromOutputStream(byteArrayOutputStream.toByteArray()));

        expectedContent = "test content".getBytes();
        actualContent = byteArrayOutputStream.toByteArray();
        index = getIndexOfEntryName(actualContent, ENTRY_NAME);
        entryContent = new byte[expectedContent.length];
        System.arraycopy(actualContent, index, entryContent, 0, expectedContent.length);
        Assert.assertArrayEquals(expectedContent, entryContent);

        byte[] extraData = " extra data".getBytes();
        Assert.assertArrayEquals(extraData, new byte[extraData.length]);
    }

    @Test
    public void testWriteWithSwappedHalfWords() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.write(CpioConstants.MAGIC_OLD_BINARY, 2, true);
        outputStream.finish();
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byte[] magic = new byte[2];
        inputStream.read(magic);
        Assert.assertArrayEquals(CpioConstants.MAGIC_OLD_BINARY, magic);

        byteArrayOutputStream = new ByteArrayOutputStream();
        outputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioConstants.FORMAT_OLD_BINARY);
        entry = new CpioArchiveEntry();
        entry.setName(ENTRY_NAME);
        outputStream.putNextEntry(entry);
        outputStream.write(ENTRY_CONTENT.getBytes());
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byte[] entryContent = new byte[ENTRY_CONTENT.getBytes().length];
        inputStream.read(entryContent);
        Assert.assertArrayEquals(ENTRY_CONTENT.getBytes(), entryContent);
    }

    private Map<String, CpioArchiveEntry> getEntriesFromOutputStream(byte[] bytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        CpioArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        while (archiveInputStream.hasNextEntry()) {
            CpioArchiveEntry entry = (CpioArchiveEntry) archiveInputStream.getNextEntry();
            entries.put(entry.getName(), entry);
        }
        return entries;
    }

    private int getIndexOfEntryName(byte[] bytes, String name) {
        int index = 0;
        int nameLength = name.length() + 1;
        while (index < bytes.length - nameLength) {
            if (bytes[index] == name.charAt(0)) {
                int i = 0;
                for (; i < name.length(); i++) {
                    if (bytes[index + i + 1] != name.charAt(i)) {
                        break;
                    }
                }
                if (i == name.length()) {
                    return index;
                }
            }
            index++;
        }
        throw new IllegalArgumentException("Entry not found: " + name);
    }

    @Test
    public void testWriteToFile() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("output.cpio");
        CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(fileOutputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(ENTRY_NAME);
        archiveOutputStream.putNextEntry(entry);
        archiveOutputStream.write(ENTRY_CONTENT.getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();
        archiveOutputStream.close();

        FileInputStream fileInputStream = new FileInputStream("output.cpio");
        byte[] bytes = IOUtils.toByteArray(fileInputStream);
        fileInputStream.close();

        Map<String, CpioArchiveEntry> entries = getEntriesFromOutputStream(bytes);
        Assert.assertEquals(1, entries.size());
        CpioArchiveEntry actualEntry = entries.values().iterator().next();
        Assert.assertEquals(ENTRY_NAME, actualEntry.getName());

        fileOutputStream = new FileOutputStream("output.txt");
        archiveOutputStream = new CpioArchiveOutputStream(fileOutputStream, CpioConstants.FORMAT_NEW);
        entry = new CpioArchiveEntry();
        entry.setName("output.txt");
        entry.setSize(ENTRY_CONTENT.getBytes().length);
        archiveOutputStream.putNextEntry(entry);
        archiveOutputStream.write(ENTRY_CONTENT.getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        archiveOutputStream.close();

        fileInputStream = new FileInputStream("output.cpio");
        bytes = IOUtils.toByteArray(fileInputStream);
        fileInputStream.close();

        entries = getEntriesFromOutputStream(bytes);
        Assert.assertEquals(2, entries.size());
        actualEntry = entries.values().iterator().next();
        Assert.assertEquals(ENTRY_NAME, actualEntry.getName());
        CpioArchiveEntry outputEntry = entries.iterator().next();
        Assert.assertEquals("output.txt", outputEntry.getName());
    }
}