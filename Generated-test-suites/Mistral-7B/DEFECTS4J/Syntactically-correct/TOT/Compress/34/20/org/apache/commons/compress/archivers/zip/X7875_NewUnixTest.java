package org.apache.commons.compress.archivers.zip.test;

import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.compress.archivers.zip.entry.ZipEntry;
import org.apache.commons.compress.archivers.zip.model.ZipExtraField;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for X7875_NewUnix extra field.
 */
public class X7875_NewUnixTest {

    @Test
    public void testCreateX7875_NewUnixObject() {
        X7875_NewUnix xf = new X7875_NewUnix();
        assertEquals(1, xf.getVersion());
        assertTrue(xf.getUID() == 0);
        assertTrue(xf.getGID() == 0);
    }

    @Test
    public void testSetUIDAndGID() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(123456789);
        xf.setGID(234567890);
        assertEquals(123456789, xf.getUID());
        assertEquals(234567890, xf.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(123456789);
        xf.setGID(234567890);
        ZipShort length = xf.getLocalFileDataLength();
        assertEquals(7, length.getValue().intValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(123456789);
        xf.setGID(234567890);
        ZipShort length = xf.getCentralDirectoryLength();
        assertEquals(0, length.getValue().intValue());
    }

    @Test
    public void testGetLocalFileDataData() throws ZipException {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(123456789);
        xf.setGID(234567890);
        byte[] data = xf.getLocalFileDataData();
        assertEquals(7, data.length);

        // check UID
        assertEquals(0x78, data[0]); // tag for this extra block type ("ux")
        assertEquals(7, signedByteToUnsignedInt(data[1])); // total data size for this block
        assertEquals(1, data[2]); // version
        assertEquals(1, data[3]); // uid size
        byte[] uidBytes = new byte[1];
        System.arraycopy(data, 4, uidBytes, 0, 1);
        assertEquals(123, uidBytes[0] & 0xFF); // check first byte
        assertEquals(123456789, ZipUtil.bigToLong(new BigInteger(1, reverse(uidBytes))));

        // check GID
        assertEquals(1, data[5]); // gid size
        byte[] gidBytes = new byte[1];
        System.arraycopy(data, 6, gidBytes, 0, 1);
        assertEquals(125, gidBytes[0] & 0xFF); // check first byte
        assertEquals(234567890, ZipUtil.bigToLong(new BigInteger(1, reverse(gidBytes))));
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{0x78, 7, 1, 1, 0x31, 0x32, 0x33, 0x34, 0x56, 0x37, 0x38, 0x39};
        xf.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, xf.getVersion());
        assertEquals(123456789, xf.getUID());
        assertEquals(234567890, xf.getGID());
    }

    @Test
    public void testCreateX7875_NewUnixObjectWithInvalidUIDOrGID() {
        try {
            X7875_NewUnix xf = new X7875_NewUnix();
            xf.setUID(-1);
            xf.setGID(-1);
            fail("Should have thrown ZipException");
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidUIDOrGID() throws ZipException {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{0x78, 7, 1, 1, (byte) 0x80, 0x32, 0x33, 0x34, 0x56, 0x37, 0x38, 0x39};
        try {
            xf.parseFromLocalFileData(data, 0, data.length);
            fail("Should have thrown ZipException");
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidUIDSize() throws ZipException {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{0x78, 7, 1, 0, 0x32, 0x33, 0x34, 0x56, 0x37, 0x38, 0x39};
        try {
            xf.parseFromLocalFileData(data, 0, data.length);
            fail("Should have thrown ZipException");
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidGIDSize() throws ZipException {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{0x78, 7, 1, 1, 0x31, 0, 0x33, 0x34, 0x56, 0x37, 0x38, 0x39};
        try {
            xf.parseFromLocalFileData(data, 0, data.length);
            fail("Should have thrown ZipException");
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidUIDOrGIDByteArray() throws ZipException {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{0x78, 7, 1, 1, (byte) 0xFF, 0x32, 0x33, 0x34, 0x56, 0x37, 0x38, 0x39};
        try {
            xf.parseFromLocalFileData(data, 0, data.length);
            fail("Should have thrown ZipException");
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidUIDOrGIDByteArrayLength() throws ZipException {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] data = new byte[]{0x78, 7, 1, 1, 0x31, 0x32, 0x33, 0x34, 0x56};
        try {
            xf.parseFromLocalFileData(data, 0, data.length);
            fail("Should have thrown ZipException");
        } catch (ZipException e) {
            // expected
        }
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix original = new X7875_NewUnix();
        original.setUID(123);
        original.setGID(456);
        X7875_NewUnix clone = (X7875_NewUnix) original.clone();
        assertEquals(original.getUID(), clone.getUID());
        assertEquals(original.getGID(), clone.getGID());
    }

    @Test
    public void testEquals() {
        X7875_NewUnix original = new X7875_NewUnix();
        X7875_NewUnix clone = new X7875_NewUnix();
        original.setUID(123);
        original.setGID(456);
        clone.setUID(123);
        clone.setGID(456);
        assertTrue(original.equals(clone));

        original.setUID(456);
        assertFalse(original.equals(clone));

        original.setGID(123);
        assertFalse(original.equals(clone));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix original = new X7875_NewUnix();
        X7875_NewUnix clone = new X7875_NewUnix();
        original.setUID(123);
        original.setGID(456);
        clone.setUID(123);
        clone.setGID(456);
        assertEquals(original.hashCode(), clone.hashCode());

        original.setUID(456);
        assertNotEquals(original.hashCode(), clone.hashCode());

        original.setGID(123);
        assertNotEquals(original.hashCode(), clone.hashCode());
    }

    @Test
    public void testToString() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(123456789);
        xf.setGID(234567890);
        String expected = "0x7875 Zip Extra Field: UID=123456789 GID=234567890";
        assertEquals(expected, xf.toString());
    }
}