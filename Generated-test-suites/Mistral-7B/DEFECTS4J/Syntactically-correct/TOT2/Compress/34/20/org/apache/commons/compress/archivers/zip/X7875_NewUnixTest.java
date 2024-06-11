package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

    /**
     * Test method for {@link X7875_NewUnix#X7875_NewUnix()}.
     */
    @Test
    public void testConstructor() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        Assert.assertEquals(1, x7875NewUnix.getVersion());
        Assert.assertEquals(BigInteger.ONE, x7875NewUnix.getUID());
        Assert.assertEquals(BigInteger.ONE, x7875NewUnix.getGID());
    }

    /**
     * Test method for {@link X7875_NewUnix#getHeaderId()}.
     */
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort expectedHeaderId = new ZipShort(0x7875);
        ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
        Assert.assertEquals(expectedHeaderId.getValue(), actualHeaderId.getValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#getUID()}.
     */
    @Test
    public void testGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long expectedUID = 1;
        long actualUID = x7875NewUnix.getUID();
        Assert.assertEquals(expectedUID, actualUID);
    }

    /**
     * Test method for {@link X7875_NewUnix#setUID(long)}.
     */
    @Test
    public void testSetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long newUID = 12345;
        x7875NewUnix.setUID(newUID);
        long expectedUID = newUID;
        long actualUID = x7875NewUnix.getUID();
        Assert.assertEquals(expectedUID, actualUID);
    }

    /**
     * Test method for {@link X7875_NewUnix#getGID()}.
     */
    @Test
    public void testGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long expectedGID = 1;
        long actualGID = x7875NewUnix.getGID();
        Assert.assertEquals(expectedGID, actualGID);
    }

    /**
     * Test method for {@link X7875_NewUnix#setGID(long)}.
     */
    @Test
    public void testSetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long newGID = 54321;
        x7875NewUnix.setGID(newGID);
        long expectedGID = newGID;
        long actualGID = x7875NewUnix.getGID();
        Assert.assertEquals(expectedGID, actualGID);
    }

    /**
     * Test method for {@link X7875_NewUnix#getLocalFileDataLength()}.
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int expectedLength = 10;
        x7875NewUnix.setUID(BigInteger.valueOf(10));
        x7875NewUnix.setGID(BigInteger.valueOf(5));
        ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
        Assert.assertEquals(expectedLength, actualLength.getValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#getCentralDirectoryLength()}.
     */
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort expectedLength = new ZipShort(0);
        ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
        Assert.assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#getLocalFileDataData()}.
     */
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(BigInteger.valueOf(1));
        x7875NewUnix.setGID(BigInteger.valueOf(1));
        byte[] expectedData = {0x01, 0x00, 0x01, 0x00};
        byte[] actualData = x7875NewUnix.getLocalFileDataData();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test method for {@link X7875_NewUnix#getCentralDirectoryData()}.
     */
    @Test
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] expectedData = new byte[0];
        byte[] actualData = x7875NewUnix.getCentralDirectoryData();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test method for {@link X7875_NewUnix#parseFromLocalFileData(byte[], int, int)}.
     */
    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = {0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        int offset = 0;
        int length = data.length;
        x7875NewUnix.parseFromLocalFileData(data, offset, length);
        Assert.assertEquals(1, x7875NewUnix.getVersion());
        Assert.assertEquals(BigInteger.valueOf(1), x7875NewUnix.getUID());
        Assert.assertEquals(BigInteger.valueOf(1), x7875NewUnix.getGID());
    }

    /**
     * Test method for {@link X7875_NewUnix#parseFromCentralDirectoryData(byte[], int, int)}.
     */
    @Test
    public void testParseFromCentralDirectoryData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = new byte[0];
        int offset = 0;
        int length = data.length;
        try {
            x7875NewUnix.parseFromCentralDirectoryData(data, offset, length);
            Assert.fail();
        } catch (ZipException e) {
            // expected
        }
    }

    /**
     * Test method for {@link X7875_NewUnix#reset()}.
     */
    @Test
    public void testReset() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(BigInteger.valueOf(1));
        x7875NewUnix.setGID(BigInteger.valueOf(1));
        x7875NewUnix.reset();
        Assert.assertEquals(1, x7875NewUnix.getVersion());
        Assert.assertEquals(BigInteger.ONE, x7875NewUnix.getUID());
        Assert.assertEquals(BigInteger.ONE, x7875NewUnix.getGID());
    }

    /**
     * Test method for {@link X7875_NewUnix#toString()}.
     */
    @Test
    public void testToString() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        String expectedString = "0x7875 Zip Extra Field: UID=1 GID=1";
        String actualString = x7875NewUnix.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    /**
     * Test method for {@link X7875_NewUnix#clone()}.
     */
    @Test
    public void testClone() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        X7875_NewUnix clonedX7875NewUnix = null;
        try {
            clonedX7875NewUnix = (X7875_NewUnix) x7875NewUnix.clone();
        } catch (CloneNotSupportedException e) {
            Assert.fail();
        }
        Assert.assertEquals(x7875NewUnix.getVersion(), clonedX7875NewUnix.getVersion());
        Assert.assertEquals(x7875NewUnix.getUID(), clonedX7875NewUnix.getUID());
        Assert.assertEquals(x7875NewUnix.getGID(), clonedX7875NewUnix.getGID());
    }

    /**
     * Test method for equals().
     */
    @Test
    public void testEquals() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = x7875NewUnix1;
        Assert.assertEquals(x7875NewUnix1, x7875NewUnix3);
        Assert.assertNotEquals(x7875NewUnix1, x7875NewUnix2);
        x7875NewUnix2.setUID(x7875NewUnix1.getUID());
        x7875NewUnix2.setGID(x7875NewUnix1.getGID());
        Assert.assertEquals(x7875NewUnix1, x7875NewUnix2);
    }

    /**
     * Test method for hashCode().
     */
    @Test
    public void testHashCode() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = x7875NewUnix1;
        X7875_NewUnix x7875NewUnix4 = new X7875_NewUnix();
        x7875NewUnix4.setUID(x7875NewUnix1.getUID());
        x7875NewUnix4.setGID(x7875NewUnix1.getGID());
        Assert.assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix3.hashCode());
        Assert.assertNotEquals(x7875NewUnix1.hashCode(), x7875NewUnix2.hashCode());
        Assert.assertEquals(x7875NewUnix1.hashCode(), x7875NewUnix4.hashCode());
    }
}