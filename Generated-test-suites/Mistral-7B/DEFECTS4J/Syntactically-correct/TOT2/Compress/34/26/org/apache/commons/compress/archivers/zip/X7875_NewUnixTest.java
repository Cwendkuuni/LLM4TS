import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;

public class X7875_NewUnixTest {

    @Test
    public void testNewX7875_NewUnixInstance() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        Assert.assertNotNull(x7875NewUnix);
    }

    @Test
    public void testGetHeaderId() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        ZipShort expectedHeaderId = new ZipShort(0x7875);
        ZipShort actualHeaderId = x7875NewUnix.getHeaderId();
        Assert.assertEquals(expectedHeaderId, actualHeaderId);
    }

    @Test
    public void testGetUid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long expectedUid = 1001;
        x7875NewUnix.setUID(expectedUid);
        long actualUid = x7875NewUnix.getUID();
        Assert.assertEquals(expectedUid, actualUid);
    }

    @Test
    public void testGetGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long expectedGid = 1002;
        x7875NewUnix.setGID(expectedGid);
        long actualGid = x7875NewUnix.getGID();
        Assert.assertEquals(expectedGid, actualGid);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidLength() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = new byte[] {0x78, 0x75, 0x01, 0x02, 0x03, 0x04, 0x05};
        x7875NewUnix.parseFromLocalFileData(data, 0, data.length);
    }

    @Test
    public void testParseFromLocalFileData() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = new byte[] {0x78, 0x75, (byte) 0x01, (byte) 0x01, 0x00, 0x00, 0x00, 0x01, 0x00, 0x02, 0x00, 0x02};
        int offset = 0;
        int length = data.length;
        x7875NewUnix.parseFromLocalFileData(data, offset, length);
        long uid = x7875NewUnix.getUID();
        long gid = x7875NewUnix.getGID();
        Assert.assertEquals(1001, uid);
        Assert.assertEquals(1002, gid);
    }

    @Test(expected = ZipException.class)
    public void testParseFromLocalFileDataWithInvalidVersion() throws ZipException {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] data = new byte[] {0x78, 0x75, (byte) 0x02, (byte) 0x01, 0x00, 0x00, 0x00, 0x01, 0x00, 0x02, 0x00, 0x02};
        int offset = 0;
        int length = data.length;
        x7875NewUnix.parseFromLocalFileData(data, offset, length);
    }

    @Test
    public void testGetLocalFileDataLength() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        int expectedLength = 5;
        int actualLength = x7875NewUnix.getLocalFileDataLength().getValue();
        Assert.assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testGetLocalFileDataData() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        byte[] expectedData = new byte[] {0x01, 0x00, 0x00, 0x01, 0x00, 0x02, 0x00, 0x02};
        byte[] actualData = x7875NewUnix.getLocalFileDataData();
        Assert.assertArrayEquals(expectedData, actualData);
    }

    
}