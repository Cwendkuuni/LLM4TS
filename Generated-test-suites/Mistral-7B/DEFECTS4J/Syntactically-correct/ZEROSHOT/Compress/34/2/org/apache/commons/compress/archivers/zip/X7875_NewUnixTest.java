import static org.junit.Assert.*;
import static org.apache.commons.compress.archivers.zip.ZipUtil.*;

import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.utils.ByteStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class X7875_NewUnixTest {

    @org.junit.Test
    public void testValidNewUnix() throws IOException {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(12345L);
        newUnix.setGID(23456L);

        byte[] expectedData = createExpectedData(newUnix);

        byte[] actualData = newUnix.getLocalFileDataData();
        assertArrayEquals(expectedData, actualData);

        X7875_NewUnix newUnix2 = new X7875_NewUnix();
        newUnix2.parseFromLocalFileData(actualData, 0, actualData.length);
        assertEquals(newUnix, newUnix2);
    }

    @org.junit.Test(expected = ZipException.class)
    public void testInvalidDataLength() throws IOException {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(12345L);
        newUnix.setGID(23456L);

        byte[] data = newUnix.getLocalFileDataData();
        byte[] invalidData = new byte[data.length - 1];
        System.arraycopy(data, 0, invalidData, 0, data.length - 1);

        try {
            newUnix.parseFromLocalFileData(invalidData, 0, invalidData.length);
        } catch (ZipException ex) {
            assertEquals("Invalid length of data for NewUnix extra field", ex.getMessage());
            throw ex;
        }
    }

    @org.junit.Test(expected = ZipException.class)
    public void testInvalidUid() throws IOException {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setUID(-12345L);

        byte[] data = newUnix.getLocalFileDataData();
        try {
            newUnix.parseFromLocalFileData(data, 0, data.length);
            fail("Expected ZipException");
        } catch (ZipException ex) {
            assertEquals("Invalid UID for NewUnix extra field", ex.getMessage());
            throw ex;
        }
    }

    @org.junit.Test(expected = ZipException.class)
    public void testInvalidGid() throws IOException {
        X7875_NewUnix newUnix = new X7875_NewUnix();
        newUnix.setGID(-23456L);

        byte[] data = newUnix.getLocalFileDataData();
        try {
            newUnix.parseFromLocalFileData(data, 0, data.length);
            fail("Expected ZipException");
        } catch (ZipException ex) {
            assertEquals("Invalid GID for NewUnix extra field", ex.getMessage());
            throw ex;
        }
    }

    private byte[] createExpectedData(X7875_NewUnix newUnix) throws IOException {
        byte[] uidBytes = uidToBytes(newUnix.getUID());
        byte[] gidBytes = uidToBytes(newUnix.getGID());

        // Couldn't bring myself to just call getLocalFileDataLength() when we've
        // already got the arrays right here.  Yeah, yeah, I know, premature
        // optimization is the root of all...
        int uidSize = uidBytes.length;
        int gidSize = gidBytes.length;
        int dataLength = 3 + uidSize + gidSize;

        byte[] data = new byte[dataLength];

        // reverse() switches byte array from big-endian to little-endian.
        reverse(uidBytes);
        reverse(gidBytes);

        int pos = 0;
        data[pos++] = (byte) unsignedIntToSignedByte(newUnix.getVersion());
        data[pos++] = (byte) unsignedIntToSignedByte(uidSize);
        System.arraycopy(uidBytes, 0, data, pos, uidSize);
        pos += uidSize;
        data[pos++] = (byte) unsignedIntToSignedByte(gidSize);
        System.arraycopy(gidBytes, 0, data, pos, gidSize);

        return data;
    }

    private byte[] uidToBytes(long uid) {
        BigInteger bigUid = BigInteger.valueOf(uid);
        return bigUid.toByteArray();
    }

}