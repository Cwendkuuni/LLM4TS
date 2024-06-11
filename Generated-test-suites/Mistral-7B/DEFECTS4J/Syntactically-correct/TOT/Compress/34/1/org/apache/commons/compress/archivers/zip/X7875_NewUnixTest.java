package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

    /**
     * Test method for {@link X7875_NewUnix#getHeaderId()}.
     */
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort expected = new ZipShort(0x7875);
        Assert.assertEquals(expected, x7875.getHeaderId());
    }

    /**
     * Test method for {@link X7875_NewUnix#getUID()} and {@link X7875_NewUnix#setUID(long)}.
     */
    @Test
    public void testUID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        // Test default value
        long uid = x7875.getUID();
        Assert.assertEquals(1000L, uid);

        // Test setting UID to a valid value
        long testUID = 12345678L;
        x7875.setUID(testUID);
        uid = x7875.getUID();
        Assert.assertEquals(testUID, uid);

        // Test setting UID to a negative value
        long negativeUID = -12345678L;
        x7875.setUID(negativeUID);
        uid = x7875.getUID();
        Assert.assertEquals(1000L, uid); // Should not allow negative UIDs
    }

    /**
     * Test method for {@link X7875_NewUnix#getGID()} and {@link X7875_NewUnix#setGID(long)}.
     */
    @Test
    public void testGID() {
        X7875_NewUnix x7875 = new X7875_NewUnix();

        // Test default value
        long gid = x7875.getGID();
        Assert.assertEquals(1000L, gid);

        // Test setting GID to a valid value
        long testGID = 987654321L;
        x7875.setGID(testGID);
        gid = x7875.getGID();
        Assert.assertEquals(testGID, gid);

        // Test setting GID to a negative value
        long negativeGID = -987654321L;
        x7875.setGID(negativeGID);
        gid = x7875.getGID();
        Assert.assertEquals(1000L, gid); // Should not allow negative GIDs
    }

    /**
     * Test method for {@link X7875_NewUnix#getLocalFileDataLength()} and
     * {@link X7875_NewUnix#parseFromLocalFileData(byte[], int, int)}.
     */
    @Test
    public void testParseAndCreate() {
        // Arrange
        long uid = 12345678L;
        long gid = 987654321L;
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(uid);
        x7875.setGID(gid);

        // Act
        byte[] expectedData = x7875.getLocalFileDataData();
        ZipShort expectedLength = x7875.getLocalFileDataLength();

        // Assert
        Assert.assertEquals(3 + uid.toString().length() + gid.toString().length(), expectedLength.getValue(), 0);
        Assert.assertArrayEquals(expectedData, x7875.getLocalFileDataData());

        // Act - parse
        X7875_NewUnix parsedX7875 = new X7875_NewUnix();
        parsedX7875.parseFromLocalFileData(expectedData, 0, expectedLength.getValue());

        // Assert
        Assert.assertEquals(uid, parsedX7875.getUID());
        Assert.assertEquals(gid, parsedX7875.getGID());
    }
}