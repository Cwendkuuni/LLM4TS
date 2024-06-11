import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 @Test
 public void testNewInstance() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 Assert.assertEquals(1, x7875NewUnix.getVersion());
 Assert.assertEquals(BigInteger.ZERO, x7875NewUnix.getUID());
 Assert.assertEquals(BigInteger.ZERO, x7875NewUnix.getGID());
 }

 @Test
 public void testSetAndGetUID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890L);
 Assert.assertEquals(1234567890L, x7875NewUnix.getUID());
 }

 @Test
 public void testSetAndGetGID() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setGID(23456789012L);
 Assert.assertEquals(23456789012L, x7875NewUnix.getGID());
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890L);
 x7875NewUnix.setGID(23456789012L);

 ZipShort expectedLength = new ZipShort((short) (1 + 1 + 1 + 8 + 8));
 Assert.assertEquals(expectedLength, x7875NewUnix.getLocalFileDataLength());
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890L);
 x7875NewUnix.setGID(23456789012L);

 Assert.assertEquals(x7875NewUnix.getLocalFileDataLength(), x7875NewUnix.getCentralDirectoryLength());
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890L);
 x7875NewUnix.setGID(23456789012L);

 byte[] expectedData = {0x01, 0x00, 0x78, 0x55, 0x01, 0x01, 0x00, 0x78, 0x75,
 (byte) 0x56, 0x37, 0x38, 0x39, 0x30, 0x30, 0x31, 0x30, 0x31, 0x32, 0x30,
 (byte) 0x31, 0x23, 0x45, 0x67, 0x89, 0x01, 0x23, 0x45, 0x67, 0x89, 0x01, 0x23};

 byte[] actualData = x7875NewUnix.getLocalFileDataData();
 Assert.assertArrayEquals(expectedData, actualData);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetCentralDirectoryData() {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 x7875NewUnix.setUID(1234567890L);
 x7875NewUnix.setGID(23456789012L);

 byte[] actualData = x7875NewUnix.getCentralDirectoryData();
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875NewUnix = new X7875_NewUnix();
 byte[] data = {0x01, 0x00, 0x78, 0x55, 0x01, 0x01, 0x00, 0x78, 0x75,
 (byte) 0x56, 0x37, 0x38, 0x39, 0x30, 0x30, 0x31, 0x30, 0x31, 0x32, 0x30,
 (byte) 0x31, 0x23, 0x45, 0x67, 0x89, 0x01, 0x23, 0x45, 0x67, 0x89, 0x01, 0x23};
 x7875NewUnix.parseFromLocalFileData(data, 0, data.length);

 Assert.assertEquals(1, x7875NewUnix.getVersion());
 Assert.assertEquals(new BigInteger("1234567890"), x7875NewUnix.getUID());
 Assert.assertEquals(new BigInteger("23456789012"), x7875NewUnix.getGID());
 }
}