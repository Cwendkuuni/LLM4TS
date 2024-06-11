package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    private InputStream inputStream;
    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ArArchiveEntry.HEADER);
        outputStream.write(new byte[]{"name1".getBytes(), 0});
        outputStream.write(new byte[]{"1970-01-01".getBytes(), 0});
        outputStream.write(new byte[]{"user1".getBytes(), 0});
        outputStream.write(new byte[]{"group1".getBytes(), 0});
        outputStream.write(new byte[]{"0644".getBytes(), 0});
        outputStream.write(new byte[]{"123".getBytes(), 0});
        outputStream.write(ArArchiveEntry.TRAILER);
        outputStream.write(new byte[]{"data1".getBytes(), 0});
        outputStream.write(ArArchiveEntry.TRAILER);
        outputStream.write(new byte[]{"name2".getBytes(), 0});
        outputStream.write(new byte[]{"1970-01-02".getBytes(), 0});
        outputStream.write(new byte[]{"user2".getBytes(), 0});
        outputStream.write(new byte[]{"group2".getBytes(), 0});
        outputStream.write(new byte[]{"0644".getBytes(), 0});
        outputStream.write(new byte[]{"456".getBytes(), 0});
        outputStream.write(ArArchiveEntry.TRAILER);

        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        arArchiveInputStream = new ArArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(arArchiveInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry nextEntry = arArchiveInputStream.getNextEntry();
        Assert.assertNotNull(nextEntry);
        ArArchiveEntry arEntry = (ArArchiveEntry) nextEntry;
        Assert.assertEquals("name1", arEntry.getName());
        Assert.assertEquals(123L, arEntry.getSize());

        nextEntry = arArchiveInputStream.getNextEntry();
        Assert.assertEquals("name2", ((ArArchiveEntry) nextEntry).getName());
        Assert.assertEquals(456L, ((ArArchiveEntry) nextEntry).getSize());

        nextEntry = arArchiveInputStream.getNextEntry();
        Assert.assertNull(nextEntry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithClosedStream() throws IOException {
        arArchiveInputStream.close();
        arArchiveInputStream.getNextEntry();
    }

    @Test
    public void testMatchesSignature() {
        byte[] signature = new byte[8];
        Assert.assertFalse(ArArchiveInputStream.matches(signature, 0));

        System.arraycopy(ArArchiveEntry.HEADER, 0, signature, 0, ArArchiveEntry.HEADER.length);
        Assert.assertTrue(ArArchiveInputStream.matches(signature, ArArchiveEntry.HEADER.length));
    }
}