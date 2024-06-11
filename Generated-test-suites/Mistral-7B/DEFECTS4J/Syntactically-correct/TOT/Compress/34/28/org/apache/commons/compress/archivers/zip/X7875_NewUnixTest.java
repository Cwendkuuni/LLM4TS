package org.apache.commons.compress.archivers.zip;

import static org.apache.commons.compress.archivers.zip.ZipUtil.reverse;
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.archivers.zip.entry.ZipEntry;
import org.apache.commons.compress.archivers.zip.extrafield.ZipShort;
import org.junit.Test;

/**
 * Test class for the X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    /**
     * Test method for {@link X7875_NewUnix#X7875_NewUnix()} .
     */
    @Test
    public void testNewX7875_NewUnix() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
        assertEquals(1, x7875.getVersion());
        assertEquals(Long.MAX_VALUE, x7875.getUID());
        assertEquals(Long.MAX_VALUE, x7875.getGID());
        assertEquals(0, x7875.getLocalFileDataLength().getValue());
        assertEquals(0, x7875.getCentralDirectoryLength().getValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#setUID(long)} .
     */
    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        assertEquals(1234567890L, x7875.getUID());
    }

    /**
     * Test method for {@link X7875_NewUnix#setGID(long)} .
     */
    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(234567890123L);
        assertEquals(234567890123L, x7875.getGID());
    }

    /**
     * Test method for {@link X7875_NewUnix#getLocalFileDataData()} .
     */
    @Test
    public void testGetLocalFileDataData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        x7875.setGID(234567890123L);

        byte[] data = x7875.getLocalFileDataData();

        ZipShort headerId = new ZipShort(0x7875);
        assertEquals(headerId.getValue(), data[0] & 0xFF, 0);
        assertEquals(headerId.getValue(), data[1] << 8 | (data[0] >> 8), 0);

        assertEquals(1, data[2]);
        assertEquals(1, data[3]);

        byte[] uidBytes = new byte[4];
        System.arraycopy(data, 4, uidBytes, 0, 4);
        reverse(uidBytes);
        BigInteger uid = new BigInteger(1, uidBytes);
        assertEquals(1234567890L, uid.longValue());

        byte[] gidBytes = new byte[4];
        System.arraycopy(data, 8, gidBytes, 0, 4);
        reverse(gidBytes);
        BigInteger gid = new BigInteger(1, gidBytes);
        assertEquals(234567890123L, gid.longValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#parseFromLocalFileData(byte[], int, int)} .
     */
    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, // header ID
                0x01, 0x04, // version, UID size
                0x00, 0x00, 0x00, 0x3C, 0x34, 0x56, 0x78, 0x90, // UID (little endian)
                0x01, 0x04, // version, GID size
                0x00, 0x00, 0x00, 0x45, 0x67, 0x89, 0x01, 0x23 // GID (little endian)
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("0x3C34567890"), x7875.getUID());
        assertEquals(new BigInteger("0x4567890123"), x7875.getGID());
        assertEquals(0, x7875.getLocalFileDataLength().getValue());
        assertEquals(0, x7875.getCentralDirectoryLength().getValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#getLocalFileDataLength()} .
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        x7875.setGID(234567890123L);

        ZipShort length = x7875.getLocalFileDataLength();
        assertEquals(11, length.getValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#getCentralDirectoryLength()} .
     */
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(1234567890L);
        x7875.setGID(234567890123L);

        ZipShort length = x7875.getCentralDirectoryLength();
        assertEquals(0, length.getValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#getLocalFileDataData()} when UID and GID are zero.
     */
    @Test
    public void testGetLocalFileDataDataWithZeroUIDAndGID() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(0L);
        x7875.setGID(0L);

        byte[] data = x7875.getLocalFileDataData();

        ZipShort headerId = new ZipShort(0x7875);
        assertEquals(headerId.getValue(), data[0] & 0xFF, 0);
        assertEquals(headerId.getValue(), data[1] << 8 | (data[0] >> 8), 0);

        // version, UID size, UID
        assertEquals(1, data[2]);
        assertEquals(0, data[3]);
        assertArrayEquals(new byte[0], Arrays.copyOfRange(data, 4, 4 + 4));

        // version, GID size, GID
        assertEquals(1, data[8]);
        assertEquals(0, data[9]);
        assertArrayEquals(new byte[0], Arrays.copyOfRange(data, 10, 10 + 4));
    }

    /**
     * Test method for {@link X7875_NewUnix#parseFromLocalFileData(byte[], int, int)} when UID and GID are zero.
     */
    @Test
    public void testParseFromLocalFileDataWithZeroUIDAndGID() throws ZipException {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, // header ID
                0x01, 0x03, // version, uid size
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // UID (little endian, all zero)
                0x01, 0x03, // version, gid size
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 // GID (little endian, all zero)
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
        assertEquals(1, x7875.getVersion());
        assertEquals(BigInteger.ZERO, x7875.getUID());
        assertEquals(BigInteger.ZERO, x7875.getGID());
        assertEquals(0, x7875.getLocalFileDataLength().getValue());
        assertEquals(0, x7875.getCentralDirectoryLength().getValue());
    }
}