package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    @Test
    public void testCreateAndGetUidGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        // Set UID and GID with valid values
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(67890);
        // Get UID and GID and check if they are equal to the set values
        long uid = x7875NewUnix.getUID();
        long gid = x7875NewUnix.getGID();
        assertEquals("UID not set correctly", 12345, uid);
        assertEquals("GID not set correctly", 67890, gid);
    }

    @Test
    public void testSetAndGetUidGidWithMinimumLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        // Set UID and GID with minimum length values
        x7875NewUnix.setUID(0);
        x7875NewUnix.setGID(0);
        // Get UID and GID and check if they are equal to the set values and have minimum length
        long uid = x7875NewUnix.getUID();
        long gid = x7875NewUnix.getGID();
        assertEquals("UID not set correctly", 0, uid);
        assertEquals("GID not set correctly", 0, gid);
        byte[] uidBytes = x7875NewUnix.getLocalFileDataData();
        byte[] gidBytes = x7875NewUnix.getLocalFileDataData();
        int uidSize = x7875NewUnix.getLocalFileDataLength().getValue();
        int gidSize = x7875NewUnix.getLocalFileDataLength().getValue();
        assertEquals("UID size not set correctly", 1, uidSize);
        assertEquals("GID size not set correctly", 1, gidSize);
        assertEquals("UID data not set correctly", new byte[]{(byte) 0x00}, uidBytes);
        assertEquals("GID data not set correctly", new byte[]{(byte) 0x00}, gidBytes);
    }

    @Test(expected = ZipException.class)
    public void testSetNegativeUidOrGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        // Set UID and GID with negative values
        x7875NewUnix.setUID(-12345);
        x7875NewUnix.setGID(-67890);
    }

    @Test
    public void testParseFromLocalFileData() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x32, (byte) 0x34, (byte) 0x35, (byte) 0x00, (byte) 0x01, (byte) 0x06, (byte) 0x07, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x00, (byte) 0x01, (byte) 0x06, (byte) 0x67, (byte) 0x89, (byte) 0x0};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        // Parse from local file data and check if UID and GID are set correctly
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        long uid = x7875NewUnix.getUID();
        long gid = x7875NewUnix.getGID();
        assertEquals("UID not parsed correctly", 32345, uid);
        assertEquals("GID not parsed correctly", 67890, gid);
    }

    @Test
    public void testParseFromLocalFileDataWithMinimumLength() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        // Parse from local file data with minimum length
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
        // Check if UID and GID are set to default values and have minimum length
        long uid = x7875NewUnix.getUID();
        long gid = x7875NewUnix.getGID();
        assertEquals("UID not set correctly", 1, uid);
        assertEquals("GID not set correctly", 1, gid);
        byte[] uidBytes = x78785NewUnix.getLocalFileDataData();
        byte[] gidBytes = x78785NewUnix.getLocalFileDataData();
        int uidSize = x78785NewUnix.getLocalFileDataLength().getValue();
        int gidSize = x78785NewUnix.getLocalFileDataLength().getValue();
        assertEquals("UID size not set correctly", 1, uidSize);
        assertEquals("GID size not set correctly", 1, gidSize);
        assertEquals("UID data not set correctly", new byte[]{(byte) 0x00}, uidBytes);
        assertEquals("GID data not set correctly", new byte[]{(byte) 0x00}, gidBytes);
    }

    @Test
    public void testParseFromLocalFileDataWithInvalidData() {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x32, (byte) 0x34, (byte) 0x35, (byte) 0x00, (byte) 0x01, (byte) 0x06, (byte) 0x67, (byte) 0x89, (byte) 0x0, (byte) 0x02};
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        try {
            x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
            fail("Should have thrown ZipException");
        } catch (ZipException e) {
            // Expected
        }
    }

    @Test
    public void testClone() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(12345);
        x7875NewUnix.setGID(67890);
        X7875_NewUnix clonedX7875NewUnix = (X7875_NewUnix) x7875NewUnix.clone();
        // Check if UID and GID are equal in both objects
        long uid1 = x7875NewUnix.getUID();
        long gid1 = x7875NewUnix.getGID();
        long uid2 = clonedX7875NewUnix.getUID();
        long gid2 = clonedX7875NewUnix.getGID();
        assertEquals("UID not cloned correctly", uid1, uid2);
        assertEquals("GID not cloned correctly", gid1, gid2);
    }

    @Test
    public void testEquals() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix4 = x7875NewUnix1;
        // Check if objects with different UID and GID are not equal
        x7875NewUnix1.setUID(12345);
        x7875NewUnix1.setGID(67890);
        x7875NewUnix2.setUID(54321);
        x7875NewUnix2.setGID(98765);
        assertFalse("Objects with different UID and GID should not be equal", x7875NewUnix1.equals(x7875NewUnix2));
        // Check if objects with the same UID and GID are equal
        x7875NewUnix3.setUID(12345);
        x7875NewUnix3.setGID(67890);
        x7875NewUnix4.setUID(12345);
        x7875NewUnix4.setGID(67890);
        assertTrue("Objects with the same UID and GID should be equal", x7875NewUnix3.equals(x7875NewUnix4));
        // Check if this object is equal to itself
        assertTrue("This object should be equal to itself", x7875NewUnix1.equals(x7875NewUnix1));
    }

    @Test
    public void testHashCode() {
        X7875_NewUnix x7875NewUnix1 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix2 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix3 = new X7875_NewUnix();
        X7875_NewUnix x7875NewUnix4 = x7875NewUnix1;
        // Check if objects with different UID and GID have different hashcodes
        x7875NewUnix1.setUID(12345);
        x7875NewUnix1.setGID(67890);
        x7875NewUnix2.setUID(54321);
        x7875NewUnix2.setGID(98765);
        assertNotEquals("Objects with different UID and GID should have different hashcodes", x7875NewUnix1.hashCode(), x7875NewUnix2.hashCode());
        // Check if objects with the same UID and GID have the same hashcode
        x7875NewUnix3.setUID(12345);
        x7875NewUnix3.setGID(67890);
        x7875NewUnix4.setUID(12345);
        x7875NewUnix4.setGID(67890);
        assertEquals("Objects with the same UID and GID should have the same hashcode", x7875NewUnix3.hashCode(), x7875NewUnix4.hashCode());
        // Check if this object has the same hashcode as itself
        assertEquals("This object should have the same hashcode as itself", x7875NewUnix1.hashCode(), x7875NewUnix1.hashCode());
    }
}