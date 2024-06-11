import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for X7875_NewUnix.
 */
public class X7875_NewUnixTest {

    /**
     * Test of getHeaderId method, of class X7875_NewUnix.
     */
    @Test
    public void testGetHeaderId() {
        X7875_NewUnix instance = new X7875_NewUnix();
        ZipShort expResult = new ZipShort(0x7875);
        ZipShort result = instance.getHeaderId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUID method, of class X7875_NewUnix.
     */
    @Test
    public void testGetUID() {
        X7875_NewUnix instance = new X7875_NewUnix();
        instance.setUID(12345);
        long expResult = 12345;
        long result = instance.getUID();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUID method, of class X7875_NewUnix.
     */
    @Test
    public void testSetUID() {
        X7875_NewUnix instance = new X7875_NewUnix();
        instance.setUID(12345);
        BigInteger expResult = new BigInteger("12345", 10);
        BigInteger result = instance.uid;
        assertEquals(expResult, result);
    }

    /**
     * Test of getLocalFileDataLength method, of class X7875_NewUnix.
     */
    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix instance = new X7875_NewUnix();
        byte[] uidBytes = new byte[5]; // UID is typically 32 bits, but let's test a bigger size
        byte[] gidBytes = new byte[4]; // GID is typically 32 bits, but let's test a smaller size
        instance.uid = new BigInteger(1, uidBytes);
        instance.gid = new BigInteger(1, gidBytes);
        ZipShort expResult = new ZipShort(3 + uidBytes.length + gidBytes.length);
        ZipShort result = instance.getLocalFileDataLength();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUID and setGID methods with invalid UID/GID values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidUID() {
        X7875_NewUnix instance = new X7875_NewUnix();
        instance.setUID(-1);
    }

    /**
     * Test of setUID and setGID methods with valid UID/GID values.
     */
    @Test
    public void testSetValidUID() {
        X7875_NewUnix instance = new X7875_NewUnix();
        instance.setUID(12345);
        assertNotNull(instance.uid);
    }

    /**
     * Test of parseFromLocalFileData method, of class X7875_NewUnix.
     */
    @Test
    public void testParseFromLocalFileData() {
        byte[] data = {0x01, (byte) 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                      0x01, 0x04, 0x78, 0x73, 0x74, 0x72, 0x75, 0x69, 0x00, 0x01, 0x04, 0x6f, 0x70, 0x65,
                      0x72, 0x63, 0x65, 0x00};

        X7875_NewUnix instance = new X7875_NewUnix();
        instance.parseFromLocalFileData(data, 0, data.length);
        long expResultUID = 12345;
        long expResultGID = 130701;
        long resultUID = instance.getUID();
        long resultGID = instance.getGID();
        assertEquals(expResultUID, resultUID);
        assertEquals(expResultGID, resultGID);
    }

    /**
     * Test of parseFromLocalFileData method with invalid data.
     */
    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataInvalidData() {
        byte[] data = {0x01, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                      0x01, 0x04, 0x78, 0x73, 0x74, 0x72, 0x75, 0x69, 0x00, 0x01, 0x04, 0x6f, 0x70, 0x65,
                      0x72, 0x63, 0x65, 0x00, 0x05}; // One byte more than the expected length

        X7875_NewUnix instance = new X7875_NewUnix();
        assertThrows(ZipException.class, () -> instance.parseFromLocalFileData(data, 0, data.length));
    }
}