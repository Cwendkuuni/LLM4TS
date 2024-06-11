package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.test.TestInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * JUnit 4 test file for ArArchiveInputStream class.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a,
                                        (byte) 0x74, (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x20, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x65,
                                        (byte) 0x52, (byte) 0x41, (byte) 0x53, (byte) 0x53, (byte) 0x45, (byte) 0x42, (byte) 0x52, (byte) 0x45, (byte) 0x53,
                                        (byte) 0x54, (byte) 0x20, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x65, (byte) 0x53, (byte) 0x3e, (byte) 0x0a,
                                        (byte) 0x53, (byte) 0x41, (byte) 0x4d, (byte) 0x41, (byte) 0x50, (byte) 0x45, (byte) 0x52, (byte) 0x41, (byte) 0x53,
                                        (byte) 0x48, (byte) 0x49, (byte) 0x50, (byte) 0x45, (byte) 0x53, (byte) 0x43, (byte) 0x53, (byte) 0x4d, (byte) 0x53,
                                        (byte) 0x20, (byte) 0x63, (byte) 0x61, (byte) 0x72, (byte) 0x73, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x52,
                                        (byte) 0x41, (byte) 0x53, (byte) 0x48, (byte) 0x49, (byte) 0x50, (byte) 0x45, (byte) 0x53, (byte) 0x43, (byte) 0x53,
                                        (byte) 0x4d, (byte) 0x53, (byte) 0x2e, (byte) 0x0d, (byte) 0x0a};

        ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        inputStream = new GZIPInputStream(input);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry1 = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull("Failed to get the first entry", entry1);
        Assert.assertEquals("Entry names do not match", "test.txt", entry1.getName());
        Assert.assertEquals("Entry sizes do not match", 14L, entry1.getSize());

        ArArchiveEntry entry2 = arArchiveInputStream.getNextArEntry();
        Assert.assertNotNull("Failed to get the second entry", entry2);
        Assert.assertEquals("Entry names do not match", "test2.txt", entry2.getName());
        Assert.assertEquals("Entry sizes do not match", 21L, entry2.getSize());

        ArArchiveEntry entry3 = arArchiveInputStream.getNextArEntry();
        Assert.assertNull("Expected null after the last entry", entry3);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveInputStream archiveInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry1 = archiveInputStream.getNextEntry();
        Assert.assertInstanceOf("Failed to get the first entry of type ArArchiveEntry", ArArchiveEntry.class, entry1);
        Assert.assertEquals("Entry names do not match", "test.txt", entry1.getName());
        Assert.assertEquals("Entry sizes do not match", 14L, entry1.getSize());

        ArchiveEntry entry2 = archiveInputStream.getNextEntry();
        Assert.assertInstanceOf("Failed to get the second entry of type ArArchiveEntry", ArArchiveEntry.class, entry2);
        Assert.assertEquals("Entry names do not match", "test2.txt", entry2.getName());
        Assert.assertEquals("Entry sizes do not match", 21L, entry2.getSize());

        ArchiveEntry entry3 = archiveInputStream.getNextEntry();
        Assert.assertNull("Expected null after the last entry", entry3);
    }

    @Test
    public void testMatchesSignature() throws IOException {
        InputStream inputStream = new TestInputStream(new byte[]{(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72, (byte) 0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a});
        ArArchiveInputStream arArchiveInputStream = new ArArchiveInputStream(inputStream);

        boolean result = ArArchiveInputStream.matches(new byte[0], 0);
        Assert.assertFalse("Expected false when given empty signature", result);

        result = ArArchiveInputStream.matches(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a}, 8);
        Assert.assertTrue("Expected true when given valid signature", result);

        result = ArArchiveInputStream.matches(new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x01}, 9);
        Assert.assertFalse("Expected false when given invalid signature", result);
    }
}

class TestInputStream extends InputStream {
    private final byte[] data;
    private int index;

    public TestInputStream(byte[] data) {
        this.data = data;
    }

    @Override
    public int read() throws IOException {
        return index < data.length ? (data[index++] & 0xff) : -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (len > data.length - index) {
            len = data.length - index;
        }
        System.arraycopy(data, index, b, off, len);
        index += len;
        return len;
    }
}