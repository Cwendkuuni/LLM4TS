import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioOutputStream != null) {
            cpioOutputStream.close();
        }
    }

    @Test
    public void testPutNextEntry() throws IOException {
        String entryName = "entryName.txt";
        String entryContent = "This is a test entry.";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entryContent.length());

        cpioOutputStream.putNextEntry(entry);

        assertEquals(entryName, cpioOutputStream.getCsCurrentEntry().getName());
        assertEquals(entryContent.length(), cpioOutputStream.getCsCurrentEntry().getSize());
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryDuplicateEntry() throws IOException {
        String entryName = "entryName.txt";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);

        cpioOutputStream.putNextEntry(entry);

        try {
            cpioOutputStream.putNextEntry(entry);
        } finally {
            cpioOutputStream.close();
        }
    }

    @Test
    public void testWrite() throws IOException {
        String entryName = "entryName.txt";
        String entryContent = "This is a test entry.";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entryContent.length());

        cpioOutputStream.putNextEntry(entry);

        cpioOutputStream.write(entryContent.getBytes(StandardCharsets.UTF_8));

        assertEquals(entryContent.length(), cpioOutputStream.getCsCurrentEntry().getSize());
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        String entryName1 = "entryName1.txt";
        String entryContent1 = "This is the first test entry.";

        String entryName2 = "entryName2.txt";
        String entryContent2 = "This is the second test entry.";

        cpioOutputStream.putNextEntry(new CpioArchiveEntry().setName(entryName1));
        cpioOutputStream.write(entryContent1.getBytes(StandardCharsets.UTF_8));

        cpioOutputStream.putNextEntry(new CpioArchiveEntry().setName(entryName2));
        cpioOutputStream.write(entryContent2.getBytes(StandardCharsets.UTF_8));

        cpioOutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Map<String, ArchiveEntry> testEntries = new HashMap<>();
        ArchiveEntry testEntry1 = new CpioArchiveEntry();
        testEntry1.setName(entryName1);
        testEntries.put(entryName1, testEntry1);

        ArchiveEntry testEntry2 = new CpioArchiveEntry();
        testEntry2.setName(entryName2);
        testEntries.put(entryName2, testEntry2);

        assertEquals(testEntries.size(), getNumberOfEntries(inputStream));

        for (Map.Entry<String, ArchiveEntry> entry : testEntries.entrySet()) {
            ArchiveEntry currentEntry = cpioOutputStream.getNextEntry();
            assertEquals(entry.getKey(), currentEntry.getName());
        }
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        String entryName = "entryName.txt";
        String entryContent = "This is a test entry.";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entryContent.length());

        cpioOutputStream.putNextEntry(entry);

        cpioOutputStream.write(entryContent.getBytes(StandardCharsets.UTF_8));

        cpioOutputStream.closeArchiveEntry();

        assertFalse(cpioOutputStream.getCsCurrentEntry() instanceof CpioArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testFinishWithOpenEntry() throws IOException {
        String entryName = "entryName.txt";
        String entryContent = "This is a test entry.";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(entryName);
        entry.setSize(entryContent.length());

        cpioOutputStream.putNextEntry(entry);

        cpioOutputStream.finish();

        cpioOutputStream.putNextEntry(entry);
    }

    private int getNumberOfEntries(InputStream inputStream) throws IOException {
        byte[] magicNumber = new byte[4];
        inputStream.read(magicNumber);

        if (!CpioUtil.isMagicNumber(magicNumber)) {
            throw new IOException("Invalid magic number.");
        }

        byte[] trailerSign = new byte[4];
        inputStream.read(trailerSign);

        if (!CpioUtil.isTrailerSign(trailerSign)) {
            throw new IOException("Invalid trailer sign.");
        }

        long entrySize = CpioUtil.readUnsignedInt(inputStream);
        int numberOfEntries = 0;

        while (entrySize > 0) {
            numberOfEntries++;
            entrySize -= Long.BYTES + 1;
        }

        return numberOfEntries;
    }
}