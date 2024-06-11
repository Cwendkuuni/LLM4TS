import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link X7875_NewUnix}.
 */
public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertEquals(new ZipShort(0x7875), x7875.getHeaderId());
        assertNull(x7875.getUID());
        assertNull(x7875.getGID());
        assertEquals(new ZipShort(0), x7875.getLocalFileDataLength());
        assertEquals(new ZipShort(0), x7875.getCentralDirectoryLength());
        assertEquals(new byte[0], x7875.getCentralDirectoryData());
    }

    @Test
    public void testSetAndGetUidAndGid() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(12345);
        x7875.setGID(67890);
        assertEquals(12345, x7875.getUID());
        assertEquals(67890, x7875.getGID());
    }

    @Test
    public void testParseFromLocalFileData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = new byte[]{(byte) 1, (byte) 1, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8};
        x7875.parseFromLocalFileData(data, 0, data.length);
        assertEquals(1, x7875.getVersion());
        assertEquals(new BigInteger("000000000000000101010101"), x7875.getUid());
        assertEquals(new BigInteger("000000000000000101101111"), x7875.getGid());
        assertEquals(new ZipShort(10), x7875.getLocalFileDataLength());
    }

    @Test
    public void testParseFromCentralDirectoryData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        assertThrows(ZipException.class, () -> x7875.parseFromCentralDirectoryData(new byte[0], 0, 0));
    }

    @Test
    public void testTrimLeadingZeroesForceMinLength() {
        byte[] array1 = new byte[0];
        byte[] array2 = new byte[]{0};
        byte[] array3 = new byte[]{0, 0};
        byte[] array4 = new byte[]{1, 1, 1};
        byte[] array5 = new byte[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        assertEquals(new byte[0], X7875_NewUnix.trimLeadingZeroesForceMinLength(array1));
        assertEquals(new byte[]{0}, X7875_NewUnix.trimLeadingZeroesForceMinLength(array2));
        assertEquals(new byte[0], X7875_NewUnix.trimLeadingZeroesForceMinLength(array3));
        assertEquals(new byte[]{1}, X7875_NewUnix.trimLeadingZeroesForceMinLength(array4));
        assertEquals(new byte[]{1, 0}, X7875_NewUnix.trimLeadingZeroesForceMinLength(array5));
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        x7875.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.setUID(12345);
        x7875.setGID(67890);
        byte[] data = x7875.getLocalFileDataData();
        assertEquals(16, data.length);
        assertEquals((byte) 1, data[0]);
        assertEquals((byte) 1, data[1]);
        assertEquals((byte) 4, data[2]);
        assertEquals((byte) 0, data[3]);
        assertEquals(new byte[]{0, 0, 0, 0, 8, 5, 4, 3, 1, 2}, Arrays.copyOfRange(data, 4, 14));
        assertEquals((byte) 1, data[14]);
        assertEquals((byte) 4, data[15]);
    }
}