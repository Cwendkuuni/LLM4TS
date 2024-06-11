package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        Assert.assertEquals((int) x7875.getVersion(), 1);
        Assert.assertNull(x7875.getUid());
        Assert.assertNull(x7875.getGid());
    }

    /**
     * Test getHeaderId method.
     */
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        Assert.assertEquals(x7875.getHeaderId().getValue(), 0x7875);
    }

    /**
     * Test getUID method.
     */
    @Test
    public void testGetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789);
        Assert.assertEquals(x7875.getUID(), 123456789);
    }

    /**
     * Test getGID method.
     */
    @Test
    public void testGetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(987654321);
        Assert.assertEquals(x7875.getGID(), 987654321);
    }

    /**
     * Test getLocalFileDataLength method.
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        // Set UID = "12345" and GID = "56789"
        x7875.setUID(12345);
        x7875.setGID(56789);

        int expectedLength = 5; // version: 1 byte, UID size: 2 bytes, UID: 3 bytes, GID size: 1 byte, GID: 2 bytes
        Assert.assertEquals(x7875.getLocalFileDataLength().getValue(), expectedLength);
    }

    /**
     * Test parseFromLocalFileData method.
     */
    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x03, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x02, (byte) 0x05, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39};

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        Assert.assertEquals(x7875.getVersion(), 1);
        Assert.assertEquals(x7875.getUID(), new BigInteger("12345678956789345"));
        Assert.assertEquals(x7875.getGID(), new BigInteger("3637383935342505"));
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();

        x7875_1.setUID(123456789);
        x7875_1.setGID(987654321);

        x7875_2.setUID(123456789);
        x7875_2.setGID(987654321);

        Assert.assertTrue(x7875_1.equals(x7875_2));
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode() {
        X7875_NewUnix x7875_1 = new X7875_NewUnix();
        X7875_NewUnix x7875_2 = new X7875_NewUnix();

        x7875_1.setUID(123456789);
        x7875_1.setGID(987654321);

        x7875_2.setUID(123456789);
        x7875_2.setGID(987654321);

        Assert.assertEquals(x7875_1.hashCode(), x7875_2.hashCode());
    }

    /**
     * Test parseFromLocalFileData with invalid data length.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidDataLength() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x03, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34};

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Test parseFromLocalFileData with invalid version.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidVersion() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x74, (byte) 0x01, (byte) 0x03, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39};

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    /**
     * Test parseFromLocalFileData with empty UID.
     */
    @Test
    public void testParseFromLocalFileDataEmptyUID() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01, (byte) 0x00};

        X7875_NewUnix x7875 = new X7875_NewUnix();
        try {
            x7875.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            // Expected, since UID is empty
        }

        Assert.assertNull(x7875.getUid());
    }

    /**
     * Test parseFromLocalFileData with empty GID.
     */
    @Test
    public void testParseFromLocalFileDataEmptyGID() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x03, (byte) 0x01, (byte) 0x00};

        X7875_NewUnix x7875 = new X7875_NewUnix();
        try {
            x7875.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            // Expected, since GID is empty
        }

        Assert.assertNull(x7875.getGid());
    }

    /**
     * Test parseFromLocalFileData with invalid UID size.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidUIDSize() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x02, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x3A};

        X7875_NewUnix x7875 = new X7875_NewUnix();
        try {
            x7875.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            // Expected, since UID size is incorrect
        }

        Assert.assertNull(x7875.getUid());
    }

    /**
     * Test parseFromLocalFileData with invalid GID size.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidGIDSize() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x03, (byte) 0x01, (byte) 0x03, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x3A};

        X7875_NewUnix x7875 = new X7875_NewUnix();
        try {
            x7875.parseFromLocalFileData(data, 0, data.length);
        } catch (ZipException e) {
            // Expected, since GID size is incorrect
        }

        Assert.assertNull(x7875.getGid());
    }
}