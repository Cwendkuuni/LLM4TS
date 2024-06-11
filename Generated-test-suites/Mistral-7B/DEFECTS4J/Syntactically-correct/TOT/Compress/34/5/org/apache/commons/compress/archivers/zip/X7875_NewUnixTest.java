import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        ZipShort headerId = x7875.getHeaderId();
        Assert.assertEquals(headerId.getValue(), 0x7875);
        Assert.assertNull(x7875.getUID());
        Assert.assertNull(x7875.getGID());
    }

    @Test
    public void testSetAndGetUID() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = 12345;
        x7875.setUID(uid);
        long retrievedUid = x7875.getUID();
        Assert.assertEquals(retrievedUid, uid);
    }

    @Test
    public void testSetAndGetGID() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = 67890;
        x7875.setGID(gid);
        long retrievedGid = x7875.getGID();
        Assert.assertEquals(retrievedGid, gid);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        byte[] data = new byte[]{(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x03, // version, uidSize
                (byte) 0x03, // uidSize
                (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, // uid
                (byte) 0x03, // gidSize
                (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x30 // gid
        };

        X7875_NewUnix x7875 = new X7875_NewUnix();
        x7875.parseFromLocalFileData(data, 0, data.length);

        long uid = x7875.getUID();
        long gid = x7875.getGID();

        Assert.assertEquals(uid, Long.parseLong("12345", 16));
        Assert.assertEquals(gid, Long.parseLong("67890", 16));
    }

    @Test(expected = ZipException.class)
    public void testNegativeUID() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long uid = Long.MIN_VALUE;
        x7875.setUID(uid);
    }

    @Test(expected = ZipException.class)
    public void testNegativeGID() throws ZipException {
        X7875_NewUnix x7875 = new X7875_NewUnix();
        long gid = Long.MIN_VALUE;
        x7875.setGID(gid);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        X7875_NewUnix original = new X7875_NewUnix();
        original.setUID(12345);
        original.setGID(67890);

        X7875_NewUnix clone = (X7875_NewUnix) original.clone();

        Assert.assertEquals(original.getUID(), clone.getUID());
        Assert.assertEquals(original.getGID(), clone.getGID());
    }

    @Test
    public void testEqualsAndHashcode() {
        X7875_NewUnix x78751 = new X7875_NewUnix();
        x78751.setUID(12345);
        x78751.setGID(67890);

        X7875_NewUnix x78752 = new X7875_NewUnix();
        x78752.setUID(12345);
        x78752.setGID(67890);

        Assert.assertEquals(x78751, x78752);
        Assert.assertEquals(x78751.hashCode(), x78752.hashCode());

        X7875_NewUnix x78753 = new X7875_NewUnix();
        x78753.setUID(67890);
        x78753.setGID(12345);

        Assert.assertNotEquals(x78751, x78753);
        Assert.assertNotEquals(x78751.hashCode(), x78753.hashCode());
    }
}