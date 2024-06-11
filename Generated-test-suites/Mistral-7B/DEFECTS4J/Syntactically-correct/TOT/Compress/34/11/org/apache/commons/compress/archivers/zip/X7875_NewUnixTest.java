import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

    @Test
    public void testX7875_NewUnix_ValidUidGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(123456789L);
        x7875NewUnix.setGID(234567890L);

        Assert.assertEquals(new ZipShort(0x7875), x7875NewUnix.getHeaderId());
        Assert.assertEquals(new Long(123456789L), x7875NewUnix.getUID());
        Assert.assertEquals(new Long(234567890L), x7875NewUnix.getGID());
        Assert.assertEquals(new ZipShort(4), x7875NewUnix.getLocalFileDataLength());

        byte[] data = x7875NewUnix.getLocalFileDataData();
        X7875_NewUnix x7875NewUnixFromData = new X7875_NewUnix();
        x7875NewUnixFromData.parseFromLocalFileData(data, 0, data.length);

        Assert.assertEquals(x7875NewUnix, x7875NewUnixFromData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testX7875_NewUnix_NegativeUidGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(-123456789L);
        x7875NewUnix.setGID(-234567890L);
    }

    @Test
    public void testX7875_NewUnix_ZeroUidGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        x7875NewUnix.setUID(0L);
        x7875NewUnix.setGID(0L);

        Assert.assertEquals(new ZipShort(0x7875), x7875NewUnix.getHeaderId());
        Assert.assertEquals(new Long(0L), x7875NewUnix.getUID());
        Assert.assertEquals(new Long(0L), x7875NewUnix.getGID());
        Assert.assertEquals(new ZipShort(1), x7875NewUnix.getLocalFileDataLength());

        byte[] data = x7875NewUnix.getLocalFileDataData();
        X7875_NewUnix x7875NewUnixFromData = new X7875_NewUnix();
        x7875NewUnixFromData.parseFromLocalFileData(data, 0, data.length);

        Assert.assertEquals(x7875NewUnix, x7875NewUnixFromData);
    }

    @Test
    public void testX7875_NewUnix_MaxUidGid() {
        X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
        long maxLong = Long.MAX_VALUE;

        x7875NewUnix.setUID(maxLong);
        x7875NewUnix.setGID(maxLong);

        Assert.assertEquals(new ZipShort(0x7875), x7875NewUnix.getHeaderId());
        Assert.assertEquals(maxLong, x7875NewUnix.getUID());
        Assert.assertEquals(maxLong, x7875NewUnix.getGID());
        Assert.assertEquals(new ZipShort((int) (2 * Long.BYTES + 1)), x7875NewUnix.getLocalFileDataLength());

        byte[] data = x7875NewUnix.getLocalFileDataData();
        X7875_NewUnix x7875NewUnixFromData = new X7875_NewUnix();
        x7875NewUnixFromData.parseFromLocalFileData(data, 0, data.length);

        Assert.assertEquals(x7875NewUnix, x7875NewUnixFromData);
    }
}