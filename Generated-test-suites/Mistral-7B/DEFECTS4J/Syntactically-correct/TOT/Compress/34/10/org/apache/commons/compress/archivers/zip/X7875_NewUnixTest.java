package org.apache.commons.compress.archivers.zip.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipEntryImpl;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ZipUtil;
import org.apache.commons.compress.archivers.zip.data.ZipByteArray;
import org.apache.commons.compress.archivers.zip.data.ZipShortBytes;
import org.apache.commons.compress.archivers.zip.extra.ZipExtraField;
import org.apache.commons.compress.archivers.zip.extra.X7875_NewUnix;

/**
 * Test case for {@link X7875_NewUnix}.
 */
public class X7875_NewUnixTest {

    // Test data
    private static final byte[] TEST_UID = ZipUtil.stringToByteArray("12345678901234567890");
    private static final byte[] TEST_GID = ZipUtil.stringToByteArray("23456789012345678901");

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        // Test default constructor
        X7875_NewUnix xf = new X7875_NewUnix();
        assertEquals(1, xf.version);
        assertNull(xf.uid);
        assertNull(xf.gid);

        // Test constructor with UID and GID
        X7875_NewUnix xf1 = new X7875_NewUnix();
        xf1.setUID(ZipUtil.bigToLong(TEST_UID));
        xf1.setGID(ZipUtil.bigToLong(TEST_GID));
        assertEquals(1, xf1.version);
        assertEquals(TEST_UID, xf1.uid.toByteArray());
        assertEquals(TEST_GID, xf1.gid.toByteArray());
    }

    /**
     * Test get and set UID.
     */
    @org.junit.Test
    public void testGetAndSetUID() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(ZipUtil.bigToLong(TEST_UID));
        assertEquals(TEST_UID, xf.getUID(), 0);

        xf.setUID(12345678901234567891L);
        assertEquals(12345678901234567891L, xf.getUID());
    }

    /**
     * Test get and set GID.
     */
    @org.junit.Test
    public void testGetAndSetGID() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setGID(ZipUtil.bigToLong(TEST_GID));
        assertEquals(TEST_GID, xf.getGID(), 0);

        xf.setGID(23456789012345678902L);
        assertEquals(23456789012345678902L, xf.getGID());
    }

    /**
     * Test getLocalFileDataLength() method.
     */
    @org.junit.Test
    public void testGetLocalFileDataLength() {
        final int expectedLength = 1 + TEST_UID.length + TEST_GID.length;
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(ZipUtil.bigToLong(TEST_UID));
        xf.setGID(ZipUtil.bigToLong(TEST_GID));

        ZipShort length = xf.getLocalFileDataLength();
        assertEquals(new ZipShort(expectedLength), length);

        // Test case for empty UID and GID
        X7875_NewUnix xf1 = new X7875_NewUnix();
        length = xf1.getLocalFileDataLength();
        assertEquals(new ZipShort(0), length);
    }

    /**
     * Test getCentralDirectoryLength() method.
     */
    @org.junit.Test
    public void testGetCentralDirectoryLength() {
        final int expectedLength = getLocalFileDataLength(new X7875_NewUnix()).getNumber().intValue();
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(ZipUtil.bigToLong(TEST_UID));
        xf.setGID(ZipUtil.bigToLong(TEST_GID));

        ZipShort length = xf.getCentralDirectoryLength();
        assertEquals(new ZipShort(expectedLength), length);
    }

    /**
     * Test getLocalFileDataData() method.
     */
    @org.junit.Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(ZipUtil.bigToLong(TEST_UID));
        xf.setGID(ZipUtil.bigToLong(TEST_GID));

        // Write test data to byte array
        byte[] testData = xf.getLocalFileDataData();

        // Check length
        int expectedLength = 1 + TEST_UID.length + TEST_GID.length;
        assertEquals(expectedLength, testData.length);

        // Check UID and GID bytes
        byte[] uidBytes = new byte[TEST_UID.length];
        byte[] gidBytes = new byte[TEST_GID.length];

        System.arraycopy(testData, 1, uidBytes, 0, TEST_UID.length);
        System.arraycopy(testData, 1 + TEST_UID.length, gidBytes, 0, TEST_GID.length);

        assertEquals(TEST_UID, new BigInteger(1, uidBytes).toByteArray());
        assertEquals(TEST_GID, new BigInteger(1, gidBytes).toByteArray());
    }

    /**
     * Test parseFromLocalFileData() method.
     */
    @org.junit.Test
    public void testParseFromLocalFileData() throws IOException {
        byte[] testData = new byte[] {
                (byte) 0x78, (byte) 0x75,
                (byte) 0x01, (byte) 0x04,
                0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30,
                0x01, 0x04,
                0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30,
        };

        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ZipEntry entry = new ZipEntryImpl("entry.txt");

        X7875_NewUnix xf = new X7875_NewUnix();
        xf.parseFromLocalFileData(input, 0, testData.length);
        assertEquals(1, xf.version);
        assertEquals(TEST_UID, xf.uid.toByteArray());
        assertEquals(TEST_GID, xf.gid.toByteArray());

        // Test parse with invalid data length
        byte[] testData2 = new byte[] {
                (byte) 0x78, (byte) 0x75,
                (byte) 0x01, (byte) 0x03, // Invalid length
                0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30,
                0x01, 0x04,
                0x00, 0x00, 0x00, 0x00, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30,
        };
        ByteArrayInputStream input2 = new ByteArrayInputStream(testData2);
        assertThrows(ZipException.class, () -> {
            xf.parseFromLocalFileData(input2, 0, testData2.length);
        });
    }

    /**
     * Test parseFromCentralDirectoryData() method.
     * (Note: This test is not applicable since this class does not store anything inside the central directory)
     */
    @org.junit.Test
    public void testParseFromCentralDirectoryData() throws IOException {
        // Not applicable since this class does not store anything inside the central directory
    }

    /**
     * Helper method to create a ZipShort from an integer.
     */
    private ZipShort getZipShort(int number) {
        return new ZipShortBytes(ZipShort.class, new byte[]{(byte) unsignedIntToSignedByte(number)});
    }

    /**
     * Helper method to create a X7875_NewUnix instance.
     */
    private X7875_NewUnix createX7875_NewUnix() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(ZipUtil.bigToLong(TEST_UID));
        xf.setGID(ZipUtil.bigToLong(TEST_GID));
        return xf;
    }

    /**
     * Helper method to create a ZipShort from a X7875_NewUnix instance.
     */
    private ZipShort getLocalFileDataLength(X7875_NewUnix xf) {
        int uidSize = trimLeadingZeroesForceMinLength(xf.uid.toByteArray()).length;
        int gidSize = trimLeadingZeroesForceMinLength(xf.gid.toByteArray()).length;
        return getZipShort(3 + uidSize + gidSize);
    }
}