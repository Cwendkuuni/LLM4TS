import org.apache.commons.compress.archivers.zip.X7875_NewUnix;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.junit.Assert;
import org.junit.Test;

public class X7875_NewUnixTest {

 private static final byte[] UID_DATA = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04};
 private static final byte[] GID_DATA = new byte[]{0x05, 0x06, 0x07, 0x08, 0x09};

 @Test
 public void testGetHeaderId() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 ZipShort actual = x7875.getHeaderId();
 ZipShort expected = new ZipShort(0x7875);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetUID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long expected = 12345;
 x7875.setUID(expected);
 long actual = x7875.getUID();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetGID() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 long expected = 67890;
 x7875.setGID(expected);
 long actual = x7875.getGID();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalFileDataLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(UID_DATA);
 x7875.setGID(GID_DATA);
 ZipShort actual = x7875.getLocalFileDataLength();
 ZipShort expected = new ZipShort(3 + UID_DATA.length + GID_DATA.length);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetCentralDirectoryLength() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 ZipShort actual = x7875.getCentralDirectoryLength();
 ZipShort expected = new ZipShort(x7875.getLocalFileDataLength().getValue());
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetLocalFileDataData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 x7875.setUID(UID_DATA);
 x7875.setGID(GID_DATA);
 byte[] expected = new byte[]{0x01, 0x00, 0x01, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09};
 byte[] actual = x7875.getLocalFileDataData();
 Assert.assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetCentralDirectoryData() {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] actual = x7875.getCentralDirectoryData();
 Assert.assertArrayEquals(new byte[0], actual);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidLength() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[]{0x01, 0x00, 0x01, 0x00, 0x02, 0x00, 0x03};
 x7875.parseFromLocalFileData(data, 0, data.length);
 }

 @Test(expected = ZipException.class)
 public void testParseFromLocalFileDataWithInvalidVersion() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[]{0x7874, 0x01, 0x01, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09};
 x7875.parseFromLocalFileData(data, 0, data.length);
 }

 @Test
 public void testParseFromLocalFileData() throws ZipException {
 X7875_NewUnix x7875 = new X7875_NewUnix();
 byte[] data = new byte[]{0x01, 0x01, 0x01, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09};
 x7875.parseFromLocalFileData(data, 0, data.length);
 long expectedUID = 12345;
 long expectedGID = 67890;
 Assert.assertEquals(expectedUID, x7875.getUID());
 Assert.assertEquals(expectedGID, x7875.getGID());
 }
}