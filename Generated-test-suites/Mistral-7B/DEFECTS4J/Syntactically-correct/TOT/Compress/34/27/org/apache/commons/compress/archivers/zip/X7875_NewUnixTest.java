package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;

import java.math.BigInteger;

/**
 * Test class for X7875_NewUnix class.
 */
public class X7875_NewUnixTest {

    /**
     * Test creation of the X7875_NewUnix instance with default values.
     */
    @Test
    public void testX7875_NewUnixDefaultValues() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(1000L, x7875NewUnix.getUID());
        assertEquals(1000L, x7875NewUnix.getGID());

        byte[] data = x7875NewUnix.getLocalFileDataData();
        X7875_NewUnix x7875NewUnixFromData = new X7875_NewUnix();
        x7875NewUnixFromData.parseFromLocalFileData(data, 0, data.length);
        assertEquals(x7875NewUnix, x7875NewUnixFromData);
    }

    /**
     * Test setting and getting the UID and GID values.
     */
    @Test
    public void testX7875_NewUnixSetAndGetUIDAndGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        long uid = 123456789012345L;
        long gid = 987654321098765L;

        x7875NewUnix.setUID(uid);
        x7875NewUnix.setGID(gid);

        assertEquals(uid, x7875NewUnix.getUID());
        assertEquals(gid, x7875NewUnix.getGID());

        byte[] data = x7875NewUnix.getLocalFileDataData();
        X7875_NewUnix x7875NewUnixFromData = new X7875_NewUnix();
        x7875NewUnixFromData.parseFromLocalFileData(data, 0, data.length);
        assertEquals(uid, x7875NewUnixFromData.getUID());
        assertEquals(gid, x7875NewUnixFromData.getGID());
    }

    /**
     * Test exception when setting invalid UID value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testX7875_NewUnixSetInvalidUIDValue() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        long uid = Long.MIN_VALUE;
        x7875NewUnix.setUID(uid);
    }

    /**
     * Test exception when setting invalid GID value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testX7875_NewUnixSetInvalidGIDValue() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        long gid = Long.MIN_VALUE;
        x7875NewUnix.setGID(gid);
    }

    /**
     * Test parsing of X7875_NewUnix from local file data.
     */
    @Test
    public void testX7875_NewUnixParseFromLocalFileData() throws ZipException {
        // Define test data
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x03, (byte) 0x03, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0x41, 0x00, 0x00, 0x00, 0x31, 0x35, 0x33, 0x39, 0x30, 0x38, 0x39, 0x36, 0x31, 0x37, 0x38, 0x34, 0x35, 0x36, 0x37, 0x38,
                0x39, 0x30, 0x35, 0x33, 0x31, 0x32, 0x31, 0x30, 0x39, 0x38, 0x37, 0x36, 0x32, 0x31, 0x30, 0x35, 0x36, 0x31, 0x31, 0x39,
                0x34, 0x30, 0x33, 0x34, 0x35, 0x32, 0x35, 0x33, 0x36, 0x31, 0x32, 0x34, 0x36, 0x35, 0x33, 0x36, 0x33, 0x34, 0x35, 0x36,
                0x35, 0x33, 0x36};

        // Initialize object
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        // Parse from data
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);

        // Check results
        assertEquals(1, x7875NewUnix.getVersion());
        assertEquals(new BigInteger("4100003135333129303876321035363131393430334525323533363124", 16), x7875NewUnix.getUID());
        assertEquals(new BigInteger("3400003435323533363345363533363345365336", 16), x7875NewUnix.getGID());
    }

    /**
     * Test parsing of X7875_NewUnix from empty local file data.
     */
    @Test(expected = ZipException.class)
    public void testX7875_NewUnixParseEmptyLocalFileData() throws ZipException {
        // Define test data (empty)
        byte[] data = new byte[0];

        // Initialize object
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        // Parse from data
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);

        // Check exception
        fail("Expected ZipException when parsing empty data");
    }

    /**
     * Test parsing of X7875_NewUnix with invalid length specifier.
     */
    @Test(expected = ZipException.class)
    public void testX7875_NewUnixParseInvalidLengthSpecifier() throws ZipException {
        // Define test data
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x03, (byte) 0x01, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0x41, 0x00, 0x00, 0x00, 0x31, 0x35, 0x33, 0x39, 0x30, 0x38, 0x39, 0x36, 0x31, 0x37, 0x38, 0x34, 0x35, 0x36, 0x37, 0x38,
                0x39, 0x30, 0x35, 0x33, 0x31, 0x32, 0x31, 0x30, 0x39, 0x38, 0x37, 0x36, 0x32, 0x31, 0x30, 0x35, 0x36, 0x31, 0x31, 0x39,
                0x34, 0x30, 0x33, 0x34, 0x35, 0x32, 0x35, 0x33, 0x36, 0x31, 0x32, 0x34, 0x36, 0x35, 0x33, 0x36, 0x33, 0x34, 0x35, 0x36,
                0x35, 0x33, 0x36};

        // Initialize object
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();

        // Parse from data
        try {
            x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
            fail("Expected ZipException when parsing invalid length specifier");
        } catch (ZipException e) {
            // Expected
        }
    }
}