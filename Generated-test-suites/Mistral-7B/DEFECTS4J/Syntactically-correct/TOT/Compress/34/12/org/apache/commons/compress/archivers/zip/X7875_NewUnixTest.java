package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

    @Test
    public void testCreateAndGetUidAndGid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 123456789L;
        long gid = 987654321L;
        x7875.setUID(uid);
        x7875.setGID(gid);
        Assert.assertEquals(uid, x7875.getUID());
        Assert.assertEquals(gid, x7875.getGID());
    }

    @Test
    public void testSetAndGetUidAndGidWithZeroValues() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(0L);
        x7875.setGID(0L);
        Assert.assertEquals(1000L, x7875.getUID());
        Assert.assertEquals(1000L, x7875.getGID());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeUid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(-123456789L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeGid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setGID(-987654321L);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = x7875.getLocalFileDataData();
        byte[] testData = new byte[]{
                0x78, 0x75, // header ID
                0x03, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, // version=1, uid size=3, uid={0x78,0x56,0x34}
                0x01, (byte) 0x19, (byte) 0x2A, (byte) 0x4B, // gid size=4, gid={0x2A,0x4B,0x19,0x19}
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // padding
                0x00 // length specifier
        };
        x7875.parseFromLocalFileData(data, 0, data.length);
        Assert.assertEquals(123456789567L, x7875.getUID());
        Assert.assertEquals(26843009344121L, x7875.getGID());
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = x7875.getLocalFileDataData();
        data[data.length - 1] = (byte) 0xFF; // invalid length specifier
        x7875.parseFromLocalFileData(data, 0, data.length);
    }
}