import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testNewInstance() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        Assert.assertNotNull(x7875NewUnix);
        Assert.assertEquals(1, x7875NewUnix.getVersion());
        Assert.assertNull(x7875NewUnix.getUID());
        Assert.assertNull(x7875NewUnix.getGID());
    }

    @Test
    public void testSetGetUID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long uid = 123456;
        x7875NewUnix.setUID(uid);
        Assert.assertEquals(uid, x7875NewUnix.getUID());
    }

    @Test
    public void testSetGetGID() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long gid = 7891011;
        x7875NewUnix.setGID(gid);
        Assert.assertEquals(gid, x7875NewUnix.getGID());
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(123456);
        x7875NewUnix.setGID(7891011);
        ZipShort expectedLength = new ZipShort((byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1);
        ZipShort actualLength = x7875NewUnix.getLocalFileDataLength();
        Assert.assertArrayEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test
    public void testGetCentralDirectoryLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(123456);
        x7875NewUnix.setGID(7891011);
        ZipShort expectedLength = new ZipShort((byte) 3, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 1);
        ZipShort actualLength = x7875NewUnix.getCentralDirectoryLength();
        Assert.assertArrayEquals(expectedLength.getValue(), actualLength.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCentralDirectoryData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] actualData = x7875NewUnix.getCentralDirectoryData();
        Assert.assertArrayEquals(new byte[0], actualData);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = {(byte) 0x78, (byte) 0x75, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01};
        int offset = 0;
        int length = data.length;
        x7875NewUnix.parseFromLocalFileData(data, offset, length);
        Assert.assertEquals(1, x7875NewUnix.getVersion());
        Assert.assertEquals(123456, x7875NewUnix.getUID());
        Assert.assertEquals(7891011, x7875NewUnix.getGID());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testParseFromCentralDirectoryData() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] buffer = {0x00};
        int offset = 0;
        int length = buffer.length;
        x7875NewUnix.parseFromCentralDirectoryData(buffer, offset, length);
    }

    @Test
    public void testReset() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(123456);
        x7875NewUnix.setGID(7891011);
        x7875NewUnix.reset();
        Assert.assertEquals(1, x7875NewUnix.getVersion());
        Assert.assertNull(x7875NewUnix.getUID());
        Assert.assertNull(x7875NewUnix.getGID());
    }
}