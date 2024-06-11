import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewX7875_NewUnixWithDefaultValues() {
        X7875_NewUnix xf = new X7875_NewUnix();
        ZipShort headerId = xf.getHeaderId();
        Assert.assertEquals(headerId.getValue(), 0x7875);
        Assert.assertEquals(xf.getVersion(), 1);
        Assert.assertEquals(xf.getUID(), 1000L);
        Assert.assertEquals(xf.getGID(), 1000L);
        Assert.assertEquals(xf.getLocalFileDataLength().getValue(), (short) (1 + 1 + 8 + 8));
        Assert.assertArrayEquals(xf.getLocalFileDataData(), getTestDataForX7875_NewUnix(1, 1, 1000, 1000));
    }

    @Test
    public void testNewX7875_NewUnixWithCustomValues() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(2023);
        xf.setGID(3033);
        Assert.assertEquals(xf.getVersion(), 1);
        Assert.assertEquals(xf.getUID(), 2023L);
        Assert.assertEquals(xf.getGID(), 3033L);
        Assert.assertEquals(xf.getLocalFileDataLength().getValue(), (short) (1 + 1 + 8 + 8));
        Assert.assertArrayEquals(xf.getLocalFileDataData(), getTestDataForX7875_NewUnix(1, 1, 2023, 3033));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewX7875_NewUnixWithNegativeValues() {
        X7875_NewUnix xf = new X7875_NewUnix();
        xf.setUID(-1);
        xf.setGID(-1);
        xf.parseFromLocalFileData(getTestDataForX7875_NewUnix(1, 1, -1, -1), 0, getTestDataForX7875_NewUnix(1, 1, -1, -1).length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewX7875_NewUnixWithTooLongUID() {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] tooLongUID = new byte[30];
        Arrays.fill(tooLongUID, (byte) 0);
        xf.setUID(new BigInteger(new String(tooLongUID)));
        xf.parseFromLocalFileData(getTestDataForX7875_NewUnix(1, 1, tooLongUID.length + 1, 0), 0, getTestDataForX7875_NewUnix(1, 1, tooLongUID.length + 1, 0).length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewX7875_NewUnixWithTooLongGID() {
        X7875_NewUnix xf = new X7875_NewUnix();
        byte[] tooLongGID = new byte[30];
        Arrays.fill(tooLongGID, (byte) 0);
        xf.setGID(new BigInteger(new String(tooLongGID)));
        xf.parseFromLocalFileData(getTestDataForX7875_NewUnix(1, 1, 0, tooLongGID.length + 1), 0, getTestDataForX7875_NewUnix(1, 1, 0, tooLongGID.length + 1).length);
    }

    private byte[] getTestDataForX7875_NewUnix(int version, int uidSize, long uid, int gidSize, long gid) {
        byte[] uidBytes = uidToByteArray(uid, uidSize);
        byte[] gidBytes = gidToByteArray(gid, gidSize);
        byte[] data = new byte[3 + uidBytes.length + gidBytes.length];

        reverse(uidBytes);
        reverse(gidBytes);

        int pos = 0;
        data[pos++] = (byte) unsignedIntToSignedByte(version);
        data[pos++] = (byte) unsignedIntToSignedByte(uidBytes.length);
        System.arraycopy(uidBytes, 0, data, pos, uidBytes.length);
        pos += uidBytes.length;
        data[pos++] = (byte) unsignedIntToSignedByte(gidBytes.length);
        System.arraycopy(gidBytes, 0, data, pos, gidBytes.length);

        return data;
    }

    private byte[] uidToByteArray(long uid, int length) {
        byte[] uidBytes = BigInteger.valueOf(uid).toByteArray();
        if (uidBytes.length > length) {
            byte[] trimmedArray = new byte[Math.max(length, uidBytes.length - uidBytes.length + 1)];
            System.arraycopy(uidBytes, uidBytes.length - (uidBytes.length - length), trimmedArray, 0, trimmedArray.length);
            return trimmedArray;
        }
        return uidBytes;
    }

    private byte[] gidToByteArray(long gid, int length) {
        byte[] gidBytes = BigInteger.valueOf(gid).toByteArray();
        if (gidBytes.length > length) {
            byte[] trimmedArray = new byte[Math.max(length, gidBytes.length - gidBytes.length + 1)];
            System.arraycopy(gidBytes, gidBytes.length - (gidBytes.length - length), trimmedArray, 0, trimmedArray.length);
            return trimmedArray;
        }
        return gidBytes;
    }
}