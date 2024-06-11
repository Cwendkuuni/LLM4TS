package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.CpioUtil;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream, short)}
     */
    @org.junit.Test
    public void testCpioArchiveOutputStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        assertNotNull(cpio);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)}
     */
    @org.junit.Test
    public void testPutNextEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        final CpioArchiveEntry entry = new CpioArchiveEntry();
        cpio.putNextEntry(entry);
        assertNotNull(entry);
        assertNull(cpio.getCpioEntry());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putNextEntry(CpioArchiveEntry)}
     * with a duplicate entry name
     */
    @org.junit.Test(expected = IOException.class)
    public void testPutNextEntryDuplicateEntryName() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        final CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("test.txt");
        cpio.putNextEntry(entry1);
        final CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("test.txt");
        cpio.putNextEntry(entry2);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)}
     */
    @org.junit.Test
    public void testWrite() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        final CpioArchiveEntry entry = new CpioArchiveEntry();
        cpio.putNextEntry(entry);
        final byte[] data = "Test Data".getBytes();
        cpio.write(data, 0, data.length);
        final byte[] result = out.toByteArray();
        assertEquals(data.length + 1, result.length);
        assertEquals(data, CpioUtil.subArray(result, 0, data.length));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#close()}
     */
    @org.junit.Test
    public void testClose() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        cpio.finish();
        cpio.close();
        assertNull(cpio.getCpioEntry());
        final byte[] result = out.toByteArray();
        assertTrue(result.length > 0);
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()}
     */
    @org.junit.Test
    public void testFinish() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        final CpioArchiveEntry entry = new CpioArchiveEntry();
        cpio.putNextEntry(entry);
        cpio.write("Test Data".getBytes());
        cpio.finish();
        final byte[] result = out.toByteArray();
        assertTrue(result.length > 0);
        final HashMap map = new HashMap();
        final int expectedMagicNumber = CpioConstants.MAGIC_NEW.length;
        map.put("MAGIC_NEW", CpioConstants.MAGIC_NEW);
        assertEquals(map, cpio.getMagicNumbers());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()} with a next entry
     */
    @org.junit.Test
    public void testFinishNextEntry() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        final CpioArchiveEntry entry1 = new CpioArchiveEntry();
        cpio.putNextEntry(entry1);
        cpio.write("Test Data 1".getBytes());
        final CpioArchiveEntry entry2 = new CpioArchiveEntry();
        cpio.putNextEntry(entry2);
        cpio.write("Test Data 2".getBytes());
        cpio.finish();
        final byte[] result = out.toByteArray();
        assertTrue(result.length > 0);
        final HashMap map = new HashMap();
        final int expectedMagicNumber = CpioConstants.MAGIC_NEW.length;
        map.put("MAGIC_NEW", CpioConstants.MAGIC_NEW);
        assertEquals(map, cpio.getMagicNumbers());
    }
}