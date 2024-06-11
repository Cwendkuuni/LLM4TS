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
        X7875_NewUnix x7875 = new X7875_NewUnix();
        Assert.assertNotNull(x7875);
        Assert.assertEquals(1, x7875.getVersion());
        Assert.assertNull(x7875.getUID());
        Assert.assertNull(x7875.getGID());
    }

    /**
     * Test method for {@link X7875_NewUnix#setUID(long)}.
     */
    @Test
    public void testSetUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456789;
        x7875.setUID(uid);
        Assert.assertEquals(uid, x7875.getUID());
    }

    /**
     * Test method for {@link X7875_NewUnix#setGID(long)}.
     */
    @Test
    public void testSetGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 987654321;
        x7875.setGID(gid);
        Assert.assertEquals(gid, x7875.getGID());
    }

    /**
     * Test method for {@link X7875_NewUnix#getLocalFileDataLength()}.
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789);
        x7875.setGID(987654321);
        byte[] uidBytes = x7875.getUID().toByteArray();
        byte[] gidBytes = x7875.getGID().toByteArray();
        int uidSize = uidBytes.length;
        int gidSize = gidBytes.length;
        ZipShort expectedLength = new ZipShort(3 + uidSize + gidSize);
        ZipShort actualLength = x7875.getLocalFileDataLength();
        Assert.assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#getCentralDirectoryLength()}.
     */
    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789);
        x7875.setGID(987654321);
        ZipShort expectedLength = x7875.getLocalFileDataLength();
        ZipShort actualLength = x7875.getCentralDirectoryLength();
        Assert.assertEquals(expectedLength.getValue(), actualLength.getValue());
    }

    /**
     * Test method for {@link X7875_NewUnix#getLocalFileDataData()}.
     */
    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(123456789);
        x7875.setGID(987654321);
        byte[] expectedData = x7875.getLocalFileDataData();
        byte[] actualData = x7875.getLocalFileDataData();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    /**
     * Test method for {@link X7875_NewUnix#parseFromLocalFileData(byte[], int, int)}.
     */
    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix expectedX7875 = new X7875_NewUnix();
        expectedX7875.setUID(123456789);
        expectedX7875.setGID(987654321);

        byte[] data = expectedX7875.getLocalFileDataData();
        X7875_NewUnix actualX7875 = new X7875_NewUnix();
        int offset = 0;
        int length = data.length;
        actualX7875.parseFromLocalFileData(data, offset, length);
        Assert.assertEquals(expectedX7875.getVersion(), actualX7875.getVersion());
        Assert.assertEquals(expectedX7875.getUID(), actualX7875.getUID());
        Assert.assertEquals(expectedX7875.getGID(), actualX7875.getGID());
    }
}